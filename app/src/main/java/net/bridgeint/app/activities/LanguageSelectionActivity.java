package net.bridgeint.app.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import net.bridgeint.app.R;
import net.bridgeint.app.interfaces.ResponceCallback;
import net.bridgeint.app.network.ServerCall;
import net.bridgeint.app.responces.GenericResponce;
import net.bridgeint.app.utils.Constants;
import net.bridgeint.app.utils.SessionManager;
import net.bridgeint.app.utils.SharedClass;
import net.bridgeint.app.utils.Utility;

import java.io.File;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Response;

public class LanguageSelectionActivity extends BaseActivity implements ResponceCallback {

    LinearLayout engBtn, arabicBtn, russianBtn;
    ImageView engCheck, arabicCheck, russianCheck;
    @BindView(R.id.backBtn)
    ImageView backBtn;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_selection);
        ButterKnife.bind(this);
        logEvent(Constants.EVENT_CHANGE_LANGUAGE_SCREEN,getUserDetails());
        logFirebaseEvent(Constants.EVENT_CHANGE_LANGUAGE_SCREEN.replace(" ","_").toLowerCase());
        sessionManager = new SessionManager(this);

        engBtn = findViewById(R.id.engBtn);
        arabicBtn = findViewById(R.id.arabicBtn);
        russianBtn = findViewById(R.id.russianBtn);

        engCheck = findViewById(R.id.engCheck);
        arabicCheck = findViewById(R.id.arabicCheck);
        russianCheck = findViewById(R.id.russianCheck);
        if (SessionManager.get(Constants.LANGUAGE).equalsIgnoreCase("ar")) {
            backBtn.setRotation(180);
        }else {
            backBtn.setRotation(360);
        }
        if (SessionManager.get(Constants.LANGUAGE).equalsIgnoreCase("en")) {
            engCheck.setImageResource(R.drawable.tick_blue);
        } else if (SessionManager.get(Constants.LANGUAGE).equalsIgnoreCase("ar")) {
            arabicCheck.setImageResource(R.drawable.tick_blue);
        } else if (SessionManager.get(Constants.LANGUAGE).equalsIgnoreCase("ru")) {
            russianCheck.setImageResource(R.drawable.tick_blue);
        }

        engBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                engCheck.setImageResource(R.drawable.tick_blue);
                arabicCheck.setImageResource(R.drawable.tick_gray);
                russianCheck.setImageResource(R.drawable.tick_gray);
                SessionManager.put(Constants.LANGUAGE, "en");
                setLocale("en");
                changeLanguage("en");
//                if (Utility.isUserLogin(LanguageSelectionActivity.this)) {
//                    startActivity(new Intent(LanguageSelectionActivity.this, DashboardUpdateActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
//                } else {
//                    startActivity(new Intent(LanguageSelectionActivity.this, SigninActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
//                }
//                finish();
            }
        });

        arabicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                engCheck.setImageResource(R.drawable.tick_gray);
                arabicCheck.setImageResource(R.drawable.tick_blue);
                russianCheck.setImageResource(R.drawable.tick_gray);
                SessionManager.put(Constants.LANGUAGE, "ar");
                setLocale("ar");
                 changeLanguage("ar");

            }
        });

        russianBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                engCheck.setImageResource(R.drawable.tick_gray);
                arabicCheck.setImageResource(R.drawable.tick_gray);
                russianCheck.setImageResource(R.drawable.tick_blue);
                SessionManager.put(Constants.LANGUAGE, "ru");
                setLocale("ru");
                  changeLanguage("ru");
//                if (Utility.isUserLogin(LanguageSelectionActivity.this)) {
//                    startActivity(new Intent(LanguageSelectionActivity.this, DashboardUpdateActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
//                } else {
//                    startActivity(new Intent(LanguageSelectionActivity.this, SigninActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
//                }
//                finish();
            }
        });
    }

    public void changeLanguage(String language) {
        ServerCall.updateLanguage(LanguageSelectionActivity.this, language, LanguageSelectionActivity.this);
    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Locale.setDefault(myLocale);
        Configuration config = new Configuration();
        config.locale = myLocale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        deleteCache();
    }

    private void navigateToBack() {
        /* startActivity(new Intent(LanguageSelectionActivity.this,DashboardActivity.class));*/
        LanguageSelectionActivity.this.recreate();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setResult(RESULT_OK);
                finish();
                startActivity(new Intent(LanguageSelectionActivity.this, SplashActivity.class));
            }
        }, 200);
    }

    public void deleteCache() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            try {
                File dir = getApplicationContext().getCacheDir();
                deleteDir(dir);
            } catch (Exception e) {
            }
        }
    }

    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    @Override
    public void onSuccessResponce(Object obj) {
        GenericResponce registerResponse = ((Response<GenericResponce>) obj).body();
        try {
            /*    Utility.showToast(LanguageSelectionActivity.this, registerResponse.getMessage());*/
            if (!registerResponse.getError()) {
                if (Utility.isUserLogin(LanguageSelectionActivity.this)) {
                    startActivity(new Intent(LanguageSelectionActivity.this, DashboardUpdateActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                } else {
                    startActivity(new Intent(LanguageSelectionActivity.this, SigninActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                }
                finish();
            } else {
                if (registerResponse.getMessage().equals(Constants.AUTHENTICATION_ERROR)) {
                    SharedClass.logout(LanguageSelectionActivity.this);
                } else {
                    /*Utility.showToast(LanguageSelectionActivity.this, registerResponse.getMessage());*/
                }
            }
        } catch (Exception ex) {
        }
    }

    @Override
    public void onFailureResponce(Object obj) {
        try {
            Response<GenericResponce> response = (Response<GenericResponce>) obj;
            if (response.message().equals(Constants.AUTHENTICATION_ERROR)) {
                SharedClass.logout(LanguageSelectionActivity.this);
            } else {
                /*  ErrorUtils.responseError(LanguageSelectionActivity.this, response);*/
            }
        } catch (Exception exp) {
        }

    }

    @OnClick(R.id.backBtn)
    public void onViewClicked() {
        supportFinishAfterTransition();
    }
}
