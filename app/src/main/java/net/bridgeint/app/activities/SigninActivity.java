package net.bridgeint.app.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;

import net.bridgeint.app.R;
import net.bridgeint.app.interfaces.ResponceCallback;
import net.bridgeint.app.network.ServerCall;
import net.bridgeint.app.responces.RegisterResponse;
import net.bridgeint.app.utils.Constants;
import net.bridgeint.app.utils.ErrorUtils;
import net.bridgeint.app.utils.SessionManager;
import net.bridgeint.app.utils.SharedClass;
import net.bridgeint.app.utils.TextValidation;
import net.bridgeint.app.utils.Utility;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;

import static net.bridgeint.app.utils.Constants.EMAIL;
import static net.bridgeint.app.utils.Constants.EVENT_LOGIN;
import static net.bridgeint.app.utils.Constants.FB_ID;
import static net.bridgeint.app.utils.Constants.FIRST_NAME;
import static net.bridgeint.app.utils.Constants.LAST_NAME;
import static net.bridgeint.app.utils.Constants.PARAM_EMAIL;
import static net.bridgeint.app.utils.Constants.PARAM_USER;

public class SigninActivity extends BaseActivity implements View.OnClickListener, ResponceCallback {

    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.forgetPassBtn)
    TextView forgetPassBtn;
    @BindView(R.id.btnGetStarted)
    AppCompatButton btnGetStarted;
    @BindView(R.id.iv_create_account)
    ImageView ivCreateAccount;
    @BindView(R.id.txt_create_account)
    AppCompatButton txtCreateAccount;
    @BindView(R.id.txt_change_language)
    TextView txtChangeLanguage;
    @BindView(R.id.btnFb)
    LoginButton btnFb;
    @BindView(R.id.btnFblogin)
    LinearLayout btnFblogin;
    private String email;
    private String password;
    private ProgressDialog dialog;
    private CallbackManager callbackManager;
    private SessionManager sessionManager;
    String fbId, firstName, lastName;

    private static final int INTENT_FORGOT_PASS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        ButterKnife.bind(this);
        initialize();
        etEmail.requestFocus();
    }

    private void initialize() {
        btnGetStarted.setOnClickListener(this);
        forgetPassBtn.setOnClickListener(this);
        ivCreateAccount.setOnClickListener(this);
        txtCreateAccount.setOnClickListener(this);
        txtChangeLanguage.setOnClickListener(this);
        dialog = new ProgressDialog(this);
        sessionManager = new SessionManager(this);

        callbackManager = CallbackManager.Factory.create();
        logFirebaseEvent(EVENT_LOGIN.replace(" ", "_").toLowerCase());
        ResponceCallback rcp = new ResponceCallback() {
            @Override
            public void onSuccessResponce(Object obj) {
                RegisterResponse registerResponse = ((Response<RegisterResponse>) obj).body();
                try {
                    if (!registerResponse.getError()) {

                        if (registerResponse.getUserModel().isNew()) {
                            Intent intent = new Intent(SigninActivity.this, SignupActivity.class);
                            intent.putExtra(FIRST_NAME, firstName);
                            intent.putExtra(LAST_NAME, lastName);
                            intent.putExtra(EMAIL, email);
                            intent.putExtra(FB_ID, fbId);
                            startActivity(intent);
                        } else {
                            SharedClass.userModel = registerResponse.getUserModel();
                            Log.e("User Model", new Gson().toJson(registerResponse.getUserModel()));
                            Utility.setUserLoginFb(SigninActivity.this, fbId, firstName, lastName);
                            //context.startActivity(new Intent(context, DashboardActivity.class));
                            if (registerResponse.getUserModel().getIsPhoneVerified()) {
                                startActivity(new Intent(SigninActivity.this, DashboardUpdateActivity.class));
                                finish();
                            } else {
                                startActivity(new Intent(SigninActivity.this, VerificationOneActivity.class));
                                finish();
                            }
                        }
                    } else {
                        Utility.showToast(SigninActivity.this, registerResponse.getMessage());
                    }

                    Utility.dismissProgressDialog(dialog);
                } catch (Exception ex) {
                    Utility.dismissProgressDialog(dialog);

                }
            }

            @Override
            public void onFailureResponce(Object obj) {
                try {
                    Utility.dismissProgressDialog(dialog);
                    Response<RegisterResponse> response = (Response<RegisterResponse>) obj;
                    if (response.message().equals(Constants.AUTHENTICATION_ERROR)) {
                        SharedClass.logout(SigninActivity.this);
                    } else {
                        ErrorUtils.responseError(SigninActivity.this, response);
                    }
                } catch (Exception exp) {
                }
            }
        };


      /*  btnFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FbLogin fbLogin = new FbLogin();
                fbLogin.fbLogin(SigninActivity.this, btnFb, callbackManager);

                fbLogin.setOnItemClickListener(new FbLogin.MyClickListener() {
                    @Override
                    public void onItemClick(JSONObject response) {
                        try {
                            String id = response.getString("id");
                            String first_name = response.getString("first_name");
                            String last_name = response.getString("last_name");
                            Log.e("names", first_name + "-" + last_name);
                            String email = response.getString("email");
                            URL profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?type=large");
                            fbId = id;
                            firstName = first_name;
                            lastName = last_name;
                            ServerCall.signUpFb(id, SigninActivity.this, dialog, email, first_name, last_name, "Student", Utility.getDeviceToken(SigninActivity.this), rcp);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });*/

        btnFblogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                facebookAccess(rcp);
            }
        });


    }

    private void facebookAccess(ResponceCallback rcp) {
        final AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null && !accessToken.isExpired()) {
            btnFb.performClick();
        } else {
            LoginManager.getInstance()
                    .setLoginBehavior(LoginBehavior.NATIVE_WITH_FALLBACK)
                    .logInWithReadPermissions(SigninActivity.this, Arrays.asList("public_profile","email"));
        }
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                GraphRequest graphRequest = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        (object, response) -> {

                            try {
                                if (object.has("id"))
                                    fbId = object.getString("id");
                                if (object.has("first_name"))
                                    firstName = object.getString("first_name");
                                if (object.has("last_name"))
                                    lastName = object.getString("last_name");

                                if (object.has("email"))
                                    email = object.getString("email");
                                // URL profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?type=large");
                                if (email == null)
                                    email = "";
                                ServerCall.signUpFb(fbId, SigninActivity.this, dialog, email, firstName, lastName, "Student", Utility.getDeviceToken(SigninActivity.this), rcp);
                                LoginManager.getInstance().logOut();

                            } catch (Exception e) {
                                Log.e("TAG", e.getMessage());
                                e.printStackTrace();
                            }

                        });
                Bundle bundle = new Bundle();
                bundle.putString("fields", "name,email,picture.type(large),gender,first_name,last_name");
                graphRequest.setParameters(bundle);
                graphRequest.executeAsync();

            }

            @Override
            public void onCancel() {
                // Toast.makeText(SigninActivity.this, "OnCancel", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                Log.e("TAG", exception.getMessage());
                exception.printStackTrace();
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnGetStarted:
                email = etEmail.getText().toString().trim();
                password = etPassword.getText().toString().trim();
                if (email.isEmpty()) {
                    Utility.showToast(this, getString(R.string.validation_empty_email_address));
                } else if (!TextValidation.isValidEmail(email)) {
                    Utility.showToast(this, getString(R.string.validation_valid_email_address));
                } else if (password.isEmpty()) {
                    Utility.showToast(this, getString(R.string.validation_enter_password));
                } else {
                    if (Utility.isNetConnected(this)) {
                        ServerCall.signIn(this, dialog, email, password, this);
                    } else {
                        Utility.showNetworkDailogSingle(this, Constants.NET_CONNECTION_LOST, new ResponceCallback() {
                            @Override
                            public void onSuccessResponce(Object obj) {
                                android.os.Process.killProcess(android.os.Process.myPid());
                                System.exit(1);
                            }

                            @Override
                            public void onFailureResponce(Object obj) {

                            }
                        });
                    }
                }
                break;
            case R.id.forgetPassBtn:
                if (Utility.isNetConnected(this)) {
                    startActivityForResult(new Intent(this, ForgetPasswordActivity.class), INTENT_FORGOT_PASS);
                } else {
                    Utility.showNetworkDailogSingle(this, Constants.NET_CONNECTION_LOST, new ResponceCallback() {
                        @Override
                        public void onSuccessResponce(Object obj) {
                            android.os.Process.killProcess(android.os.Process.myPid());
                            System.exit(1);
                        }

                        @Override
                        public void onFailureResponce(Object obj) {

                        }
                    });
                }
                break;
            case R.id.txt_create_account:
                startActivity(new Intent(this, SignupActivity.class));
                break;
            case R.id.txt_change_language:
                Utility.showLanguagePopup(this, "test", new ResponceCallback() {
                    @Override
                    public void onSuccessResponce(Object obj) {
                        Utility.hideConfirmDialog();
                    }

                    @Override
                    public void onFailureResponce(Object obj) {
                        Utility.hideConfirmDialog();
                    }
                },SigninActivity.this);
//                startActivity(new Intent(this, StartLanguageSelectionActivity.class));
                break;

            default:
                break;
        }
    }

    @Override
    public void onSuccessResponce(Object obj) {
        RegisterResponse registerResponse = ((Response<RegisterResponse>) obj).body();
        try {
            if (!registerResponse.getError()) {
                SharedClass.userModel = registerResponse.getUserModel();
                Bundle bundle = new Bundle();
                bundle.putString(PARAM_USER, SharedClass.userModel.getFirstName() + " " + SharedClass.userModel.getLastName());
                bundle.putString(PARAM_EMAIL, SharedClass.userModel.getEmail());
                logEvent(EVENT_LOGIN, bundle);
//                logFirebaseEvent(EVENT_LOGIN.replace(" ", "_").toLowerCase());
                SessionManager.put(Constants.ID, registerResponse.getUserModel().getId());
                SessionManager.put(Constants.ACCESS_KEY, registerResponse.getUserModel().getAccessKey());
                SessionManager.put(Constants.NOTIFICATION, registerResponse.getUserModel().getNotifStatus());
                if (SharedClass.userModel.getIsPhoneVerified()) {
                    ServerCall.updateDeviceToken(Utility.getDeviceToken(this));
                    etEmail.setText("");
                    etPassword.setText("");
                    Utility.setUserLogin(this, email, password);
                    if (registerResponse.getUserModel().getType().equalsIgnoreCase("Student")) {
                        Utility.addUserToSharedPreference(this, registerResponse.getUserModel());
                        startActivity(new Intent(this, DashboardUpdateActivity.class));
                        finishAffinity();
                    } else {
                        startActivity(new Intent(this, LiveActivity.class));
                        this.finishAffinity();
                    }
                    //Utility.showToast(context, registerResponse.getMessage());
                } else {
                    startActivity(new Intent(this, VerificationOneActivity.class));

                }
            } else {
                Utility.showToast(this, registerResponse.getMessage());
            }
            Utility.dismissProgressDialog(dialog);
        } catch (Exception ex) {
            Utility.dismissProgressDialog(dialog);
        }
    }

    @Override
    public void onFailureResponce(Object obj) {
        try {
            Utility.dismissProgressDialog(dialog);
            Response<RegisterResponse> response = (Response<RegisterResponse>) obj;
            if (response.message().equals(Constants.AUTHENTICATION_ERROR)) {
                SharedClass.logout(this);
            } else {
                ErrorUtils.responseError(this, response);
            }
        } catch (Exception exp) {
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if(requestCode == INTENT_FORGOT_PASS && resultCode == Activity.RESULT_OK){
            String email = data.getStringExtra(EMAIL);
            etEmail.setText(email);
        }
    }
}
