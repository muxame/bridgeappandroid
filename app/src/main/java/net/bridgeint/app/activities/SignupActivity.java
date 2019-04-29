package net.bridgeint.app.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;

public class SignupActivity extends BaseActivity implements View.OnClickListener, ResponceCallback {

    @BindView(R.id.firstName)
    EditText firstName;
    @BindView(R.id.lastName)
    EditText lastName;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.btnGetStarted)
    TextView btnGetStarted;
    @BindView(R.id.termBtn)
    LinearLayout termBtn;
    @BindView(R.id.iv_back_to_login)
    ImageView ivBackToLogin;
    @BindView(R.id.lay_password)
    LinearLayout layPassword;
    @BindView(R.id.cb_terms_and_condition)
    ImageView cbTermsAndCondition;
    private String userType = Constants.STUDENT_CONST;
    private String email;
    private String uniName, firstname, lastname;
    private String password;
    private ProgressDialog dialog;
    private Context context;
    private SessionManager sessionManager;

    private String fbId;
    private boolean isFbUser = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        initialize();

    }

    private void initialize() {
        btnGetStarted.setOnClickListener(this);
        termBtn.setOnClickListener(this);
        ivBackToLogin.setOnClickListener(this);
        dialog = new ProgressDialog(this);
        context = this;
        if (SessionManager.get(Constants.LANGUAGE).equalsIgnoreCase("ar")) {
            ivBackToLogin.setRotation(180);
        }
        cbTermsAndCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbTermsAndCondition.setSelected(!cbTermsAndCondition.isSelected());
            }
        });
        logFirebaseEvent(Constants.EVENT_SIGNUP.replace(" ", "_").toLowerCase());
        if (getIntent() != null) {
            if (getIntent().hasExtra(Constants.FB_ID)) {
                fbId = getIntent().getStringExtra(Constants.FB_ID);
                firstname = getIntent().getStringExtra(Constants.FIRST_NAME);
                lastname = getIntent().getStringExtra(Constants.LAST_NAME);
                email = getIntent().getStringExtra(Constants.EMAIL);
                firstName.setText(firstname);
                lastName.setText(lastname);
                etEmail.setText(email);
                etPassword.setVisibility(View.GONE);
                layPassword.setVisibility(View.GONE);
                isFbUser = true;
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnGetStarted:
                email = etEmail.getText().toString().trim();
                password = etPassword.getText().toString().trim();
                firstname = firstName.getText().toString().trim();
                lastname = lastName.getText().toString().trim();
             /*   if (userType.equals(Constants.STUDENT_CONST)) {


                } *//*else {
                    firstname = etUniName.getText().toString().trim();
                    lastname = etUniName.getText().toString().trim();
                }*/
                if (firstname.isEmpty()) {
                    Utility.showToast(context, getString(R.string.validation_first_name));
                    return;
                    /*if (userType.equals(Constants.STUDENT_CONST)) {
                        Utility.showToast(context, "Please Enter " + userType + " First Name");
                    } else {
                        Utility.showToast(context, "Please Enter " + userType + " Name");
                    }*/
                } else if (lastname.isEmpty()) {
                    Utility.showToast(context, getString(R.string.validation_last_name));
                    return;
                    /*if (userType.equals(Constants.STUDENT_CONST)) {
                        Utility.showToast(context, "Please Enter " + userType + " Last Name");
                    } else {
                        Utility.showToast(context, "Please Enter " + userType + " Name");
                    }*/
                } else if (email.isEmpty()) {
                    Utility.showToast(context, getString(R.string.empty_email_valid));
                    return;
                } else if (!TextValidation.isValidEmail(email)) {
                    Utility.showToast(context, getString(R.string.validation_email));
                    return;
                } else if (password.isEmpty()) {
                    if (!isFbUser) {
                        Utility.showToast(context, getString(R.string.validation_password));
                        return;
                    }
                } else if (!cbTermsAndCondition.isSelected()) {
                    Utility.showToast(context, getString(R.string.validation_accept_terms_condition));
                    return;
                }


                if (Utility.isNetConnected(context)) {
                    if (isFbUser) {
                        ServerCall.signUpFb(fbId, SignupActivity.this, dialog, email, firstname, lastname, "Student", Utility.getDeviceToken(SignupActivity.this), this);
                    } else {
                        ServerCall.signUp(this, dialog, firstname, lastname, email, password, firstname, userType, Utility.getDeviceToken(context), this);
                    }

                } else {
                    Utility.showNetworkDailogSingle(SignupActivity.this, Constants.NET_CONNECTION_LOST, new ResponceCallback() {
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
            case R.id.termBtn:
                if (SessionManager.get(Constants.LANGUAGE).equalsIgnoreCase("ar")) {
                    startActivity(new Intent(context, WebViewActivity.class).putExtra(Constants.WEB_LINK, Constants.TERM_CONDITION_URL_AR));
                } else if (SessionManager.get(Constants.LANGUAGE).equalsIgnoreCase("ru")) {
                    startActivity(new Intent(context, WebViewActivity.class).putExtra(Constants.WEB_LINK, Constants.TERM_CONDITION_URL_RU));
                } else {
                    startActivity(new Intent(context, WebViewActivity.class).putExtra(Constants.WEB_LINK, Constants.TERM_CONDITION_URL));
                }

                break;
            case R.id.iv_back_to_login:
                startActivity(new Intent(context, SigninActivity.class));
                finish();
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
                bundle.putString(Constants.PARAM_USER, SharedClass.userModel.getFirstName() + " " + SharedClass.userModel.getLastName());
                logEvent(Constants.EVENT_SIGNUP, bundle);
                logFirebaseEvent(Constants.EVENT_SIGNUP.replace(" ", "_").toLowerCase());
                SessionManager.put(Constants.ID, registerResponse.getUserModel().getId());
                SessionManager.put(Constants.ACCESS_KEY, registerResponse.getUserModel().getAccessKey());
                SessionManager.put(Constants.NOTIFICATION, registerResponse.getUserModel().getNotifStatus());

                Utility.setUserLogin(context, email, password);
                context.startActivity(new Intent(context, VerificationOneActivity.class));
                //Utility.showToast(context, registerResponse.getMessage());


            } else {
                Utility.showToast(context, registerResponse.getMessage());
                if (registerResponse.getUserModel().getAccStatus() == 0 && !registerResponse.getUserModel().getIsPhoneVerified()) {
                    SharedClass.userModel = registerResponse.getUserModel();
                    Utility.setUserLogin(context, email, password);
                    context.startActivity(new Intent(context, VerificationOneActivity.class));
                }
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
}
