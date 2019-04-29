package net.bridgeint.app.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import net.bridgeint.app.BuildConfig;
import net.bridgeint.app.R;
import net.bridgeint.app.interfaces.ResponceCallback;
import net.bridgeint.app.network.ServerCall;
import net.bridgeint.app.responces.RegisterResponse;
import net.bridgeint.app.services.MyFirebaseMessagingService;
import net.bridgeint.app.utils.Constants;
import net.bridgeint.app.utils.ErrorUtils;
import net.bridgeint.app.utils.SessionManager;
import net.bridgeint.app.utils.SharedClass;
import net.bridgeint.app.utils.Utility;
import net.bridgeint.app.views.XTextView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import retrofit2.Response;

public class SplashActivity extends BaseActivity implements ResponceCallback {

    private static Integer splashDuration = 2000;
    ProgressDialog dialog;

    XTextView tvVersion;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_splash);

            sessionManager = new SessionManager(this);
            String versionName = BuildConfig.VERSION_NAME;
            tvVersion = findViewById(R.id.tv_version);
tvVersion.setText("V "+versionName);
            String token = FirebaseInstanceId.getInstance().getToken();
            Log.d("FCM", "Refreshed token: " + token);
            Utility.addTokenToSharedPreference(this, token);

           /* title = findViewById(R.id.title);
            t_title = findViewById(R.id.t_title);*/

          /*  if (Locale.getDefault().getLanguage()!=null) {
                setLocale(SessionManager.get(Constants.LANGUAGE));
            }*/
            String language = SessionManager.get(Constants.LANGUAGE);

            if (language.isEmpty()) {
                language = "en";
                SessionManager.put(Constants.LANGUAGE, language);
            }

            setLocale(language);
            Log.d("selectedLanguage", language);
            dialog = new ProgressDialog(this);
            //Utility.getFacebookHashKey(SplashActivity.this);
//            if (Utility.isUsingFirstTime(SplashActivity.this)) {
//                Utility.setUsingFirstTime(SplashActivity.this);
//                startActivityForResult(new Intent(SplashActivity.this, StartLanguageSelectionActivity.class), 1001);
//            } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    goApp();
                }
            }, splashDuration);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        getHashKey();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1001) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    goApp();
                }
            }, splashDuration);
        }
    }

    @SuppressLint("PackageManagerGetSignatures")
    private void getHashKey() {
        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("hashKey", new String(Base64.encode(md.digest(), 0)));
                Log.e("hashKey", new String(Base64.encode(md.digest(), 0)));
                Log.e("hashKey", new String(Base64.encode(md.digest(), 0)));
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }

    }

    private void goApp() {

        if (Utility.isNetConnected(SplashActivity.this)) {
            if (Utility.isFbUser(this)) {
                Log.e("FbUser", new Gson().toJson(Utility.getUser(this)));
//                ServerCall.AuthenticateUser(SplashActivity.this, this);
                ServerCall.signUpFb(Utility.getFbId(this), SplashActivity.this, dialog, Utility.getUserEmail(this), Utility.getFirstName(this), Utility.getLastName(this), "Student", Utility.getDeviceToken(SplashActivity.this), rcp);

            } else if (Utility.isUserLogin(this)) {
                ServerCall.AuthenticateUser(SplashActivity.this, this);
            } else {
                if (Utility.isUsingFirstTime(SplashActivity.this)) {
                    Utility.setUsingFirstTime(SplashActivity.this);
//                    startActivityForResult(new Intent(SplashActivity.this, StartLanguageSelectionActivity.class), 1001);
                    startActivity(new Intent(this, AppTutorialActivity.class));
                    //startActivity(new Intent(this, VerificationOneActivity.class));
                    finish();
                } else {
                    //startActivity(new Intent(this, AccountChooserActivity.class));
                    startActivity(new Intent(this, SigninActivity.class));
                    //startActivity(new Intent(this, VerificationOneActivity.class));
                    finish();

                }

            }
        } else {
//            finish();
            Utility.showNetworkDailogSingle(SplashActivity.this, Constants.NET_CONNECTION_LOST, new ResponceCallback() {
                @Override
                public void onSuccessResponce(Object obj) {
                    finish();
                }

                @Override
                public void onFailureResponce(Object obj) {

                }
            });
        }
    }

    @Override
    public void onSuccessResponce(Object obj) {
        RegisterResponse register_response = ((Response<RegisterResponse>) obj).body();
        try {
            if (!register_response.getError()) {
                SharedClass.userModel = register_response.getUserModel();
                SessionManager.put(Constants.ID, register_response.getUserModel().getId());
                SessionManager.put(Constants.ACCESS_KEY, register_response.getUserModel().getAccessKey());
                SessionManager.put(Constants.NOTIFICATION, register_response.getUserModel().getNotifStatus());
                if (SharedClass.userModel.getIsPhoneVerified()) {

                    Log.d("AT@Authenticate", Utility.getDeviceToken(this));

                    if (register_response.getUserModel().getType().equalsIgnoreCase("Student")) {
                        Utility.addUserToSharedPreference(this, register_response.getUserModel());
                        if (getIntent().getStringExtra("noti_type") == null) {
                            startActivity(new Intent(SplashActivity.this, DashboardUpdateActivity.class));
                        } else {
                            Intent intent = new Intent(SplashActivity.this, DashboardUpdateActivity.class);
                            if (getIntent().getStringExtra("noti_type").equalsIgnoreCase(MyFirebaseMessagingService.TYPE_MESSAGE)) {
//                                startActivity(new Intent(SplashActivity.this, ChatActivity.class));
                                intent.putExtra("notification", true);
                            } else if (getIntent().getStringExtra("noti_type").equalsIgnoreCase("Status")) {
                                /*startActivity(new Intent(SplashActivity.this, ApplicationActivity.class));*/
                            } else {

                            }


                            startActivity(intent);
                        }
                        finish();
                    } else {
                        startActivity(new Intent(SplashActivity.this, LiveActivity.class));
                        finish();
                    }
                } else {
                    startActivity(new Intent(this, SigninActivity.class));
                    finish();
                }
            } else {
                Utility.showToast(SplashActivity.this, register_response.getMessage());
                startActivity(new Intent(this, SigninActivity.class));
                finish();
            }

        } catch (Exception ex) {
        }
    }


    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Locale.setDefault(myLocale);
        Configuration config = new Configuration();
        config.locale = myLocale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }


    @Override
    public void onFailureResponce(Object obj) {
        try {
            Response<RegisterResponse> response = (Response<RegisterResponse>) obj;
            ErrorUtils.responseError(SplashActivity.this, response);
            startActivity(new Intent(this, SigninActivity.class));
            finish();
        } catch (Exception exp) {
        }
    }

    ResponceCallback rcp = new ResponceCallback() {
        @Override
        public void onSuccessResponce(Object obj) {
            RegisterResponse registerResponse = ((Response<RegisterResponse>) obj).body();
            try {
                if (!registerResponse.getError()) {
                    SharedClass.userModel = registerResponse.getUserModel();
                    Log.e("User Model", new Gson().toJson(registerResponse.getUserModel()));
                    Utility.setUserLoginFb(SplashActivity.this, Utility.getFbId(SplashActivity.this), Utility.getFirstName(SplashActivity.this), Utility.getLastName(SplashActivity.this));

                    if (registerResponse.getUserModel().getIsPhoneVerified()) {
                        if (registerResponse.getUserModel().getType().equalsIgnoreCase("Student")) {
                            if (getIntent().getStringExtra("noti_type") == null) {
                                startActivity(new Intent(SplashActivity.this, DashboardUpdateActivity.class));
                            } else {
                                if (getIntent().getStringExtra("noti_type").equalsIgnoreCase("Message")) {
                                    startActivity(new Intent(SplashActivity.this, ChatActivity.class));
                                } else if (getIntent().getStringExtra("noti_type").equalsIgnoreCase("Status")) {
                                    /* startActivity(new Intent(SplashActivity.this, ApplicationActivity.class));*/
                                } else {
                                    startActivity(new Intent(SplashActivity.this, LiveActivity.class));
                                }
                            }
                            finish();
                        } else {
                            startActivity(new Intent(SplashActivity.this, LiveActivity.class));
                            finish();
                        }
                    } else {
                        startActivity(new Intent(SplashActivity.this, SigninActivity.class));
                        finish();
                    }
                } else {
                    Utility.showToast(SplashActivity.this, registerResponse.getMessage());
                }

                Utility.dismissProgressDialog(dialog);
            } catch (
                    Exception ex) {
                Utility.dismissProgressDialog(dialog);
            }
        }

        @Override
        public void onFailureResponce(Object obj) {
            try {
                Response<RegisterResponse> response = (Response<RegisterResponse>) obj;
                ErrorUtils.responseError(SplashActivity.this, response);
                startActivity(new Intent(SplashActivity.this, SigninActivity.class));
                finish();
            } catch (Exception e) {
            }
        }

    };
}
