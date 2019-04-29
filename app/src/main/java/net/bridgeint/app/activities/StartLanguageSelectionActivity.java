package net.bridgeint.app.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import net.bridgeint.app.R;
import net.bridgeint.app.interfaces.ResponceCallback;
import net.bridgeint.app.responces.GenericResponce;
import net.bridgeint.app.utils.Constants;
import net.bridgeint.app.utils.SessionManager;
import net.bridgeint.app.utils.SharedClass;

import java.io.File;
import java.util.Locale;

import retrofit2.Response;

public class StartLanguageSelectionActivity extends BaseActivity implements ResponceCallback {

    LinearLayout engBtn, arabicBtn, russianBtn;
    ImageView engCheck, arabicCheck, russianCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_language_selection);

        engBtn = findViewById(R.id.engBtn);
        arabicBtn = findViewById(R.id.arabicBtn);
        russianBtn = findViewById(R.id.russianBtn);

        engCheck = findViewById(R.id.engCheck);
        arabicCheck = findViewById(R.id.arabicCheck);
        russianCheck = findViewById(R.id.russianCheck);


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
                startActivity(new Intent(StartLanguageSelectionActivity.this, SigninActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
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
                startActivity(new Intent(StartLanguageSelectionActivity.this, SigninActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
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
                startActivity(new Intent(StartLanguageSelectionActivity.this, SigninActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });

    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        deleteCache();
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
                finish();
            } else {
                if (registerResponse.getMessage().equals(Constants.AUTHENTICATION_ERROR)) {
                    SharedClass.logout(StartLanguageSelectionActivity.this);
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
                SharedClass.logout(StartLanguageSelectionActivity.this);
            } else {
                /*  ErrorUtils.responseError(LanguageSelectionActivity.this, response);*/
            }
        } catch (Exception exp) {
        }

    }

}
