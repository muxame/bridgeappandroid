package net.bridgeint.app.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import net.bridgeint.app.R;
import net.bridgeint.app.activities.DashboardUpdateActivity;
import net.bridgeint.app.activities.SigninActivity;
import net.bridgeint.app.interfaces.ResponceCallback;
import net.bridgeint.app.models.PaymentType;
import net.bridgeint.app.models.UserModel;
import net.bridgeint.app.network.ServerCall;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static net.bridgeint.app.utils.Config.CARD;
import static net.bridgeint.app.utils.Config.PAYMENT_COMMENT_TEXT;

/**
 * Created by laptop on 22/05/2017.
 */

public class Utility {

    public static ProgressDialog myDialog = null;
    private static android.support.v7.app.AlertDialog myalertdialog;

    public static void changeStatusBarColor(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public static void showToast(Context context, String s) {
        Toast.makeText(context, s + "", Toast.LENGTH_SHORT).show();
    }

    public static void goToHome(Context context) {
        Intent intent = new Intent(context, DashboardUpdateActivity.class);
        context.startActivity(intent);
        ((Activity) context).finishAffinity();
    }
    public static void goToHome(Context context,String from) {
        Intent intent = new Intent(context, DashboardUpdateActivity.class);
        intent.putExtra("from",from);
        context.startActivity(intent);
        ((Activity) context).finishAffinity();
    }

    public static void hideKeyPad(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void addTokenToSharedPreference(Context context, String token) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(Config.PREF_USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(Config.DEVICE_TOKEN, token);
        editor.commit();
    }

    public static String getDeviceToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Config.PREF_USER, Context.MODE_PRIVATE);
        return prefs.getString(Config.DEVICE_TOKEN, "android");
    }

    public static void showProgressDialog(final ProgressDialog myDialog, final String message, final Activity context) {
        context.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                try {
                    myDialog.setMessage(message);
                    myDialog.setCancelable(false);
                    myDialog.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void dismissProgressDialog(ProgressDialog myDialog) {
        try {
            if (myDialog != null && myDialog.isShowing()) {
                myDialog.dismiss();
                myDialog = null;
            }
        } catch (Exception e) {

        }
    }

    public static void addUserToSharedPreference(Context context, UserModel result) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(Config.PREF_USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(Config.LOGIN_STATUS, true);
        editor.putInt(Config.USER_ID, result.getId());
        editor.putString(Config.USER_EMAIL, result.getEmail());
        //editor.putString(Config.USER_NAME, result.getName());
        //editor.putString(Config.USER_PIC, result.getImagePath());
        editor.putString(Config.AUTH_TOKEN, result.getAccessKey());

        editor.commit();
    }

    public static String getAuthToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Config.PREF_USER, Context.MODE_PRIVATE);
        return prefs.getString(Config.AUTH_TOKEN, "");
    }

    public static UserModel getUser(Context context) {
        UserModel user = new UserModel();
        SharedPreferences prefs = context.getSharedPreferences(Config.PREF_USER, Context.MODE_PRIVATE);
        user.setEmail(prefs.getString(Config.USER_EMAIL, ""));
        //user.setName(prefs.getString(Config.USER_NAME, ""));
        user.setId(prefs.getInt(Config.USER_ID, 0));
        //user.setImagePath(prefs.getString(Config.USER_PIC, ""));
        user.setAccessKey(prefs.getString(Config.AUTH_TOKEN, ""));
        return user;
    }

    public static void setUserLogin(Context context, String email, String pass) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(Config.PREF_USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(Config.LOGIN_STATUS, true);
        editor.putString(Config.USER_EMAIL, email);
        editor.putString(Config.PASSWORD, pass);
        editor.putBoolean("isFb", false);
        editor.commit();
    }

    public static void setUserLoginFb(Context context, String fbId, String firstName, String lastName) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(Config.PREF_USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(Config.LOGIN_STATUS, true);
        editor.putBoolean("isFb", true);
        editor.putString("fbId", fbId);
        editor.putString(Config.FIRST_NAME, firstName);
        editor.putString(Config.LAST_NAME, lastName);
        editor.commit();
    }

    public static boolean isUserLogin(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Config.PREF_USER, Context.MODE_PRIVATE);
        return prefs.getBoolean(Config.LOGIN_STATUS, false);
    }

    public static boolean isFbUser(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Config.PREF_USER, Context.MODE_PRIVATE);
        return prefs.getBoolean("isFb", false);
    }

    public static String getUserEmail(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Config.PREF_USER, Context.MODE_PRIVATE);
        return prefs.getString(Config.USER_EMAIL, "");
    }


    public static String getFirstName(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Config.PREF_USER, Context.MODE_PRIVATE);
        return prefs.getString(Config.FIRST_NAME, "");
    }

    public static String getLastName(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Config.PREF_USER, Context.MODE_PRIVATE);
        return prefs.getString(Config.LAST_NAME, "");
    }

    public static String getFbId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Config.PREF_USER, Context.MODE_PRIVATE);
        return prefs.getString("fbId", "");
    }

    public static String getUserPassword(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Config.PREF_USER, Context.MODE_PRIVATE);
        return prefs.getString(Config.PASSWORD, "");
    }

    public static void setUsingFirstTime(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(Config.PREF_USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(Config.FIRST_TIME_USE, false);
        editor.commit();
    }

    public static boolean isUsingFirstTime(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Config.PREF_USER, Context.MODE_PRIVATE);
        return prefs.getBoolean(Config.FIRST_TIME_USE, true);
    }

    public static void logout(Context context) {
        String token = getDeviceToken(context);
        SharedPreferences sharedpreferences = context.getSharedPreferences(Config.PREF_USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
//        editor.remove(Config.LOGIN_STATUS);
//        editor.remove(Config.USER_EMAIL);
//        editor.remove(Config.PASSWORD);

        editor.putBoolean(Config.FIRST_TIME_USE, false);
        editor.putString(Config.DEVICE_TOKEN, token);
        editor.commit();
    }

    public static void showLoadingDialog(final Activity activity, final String message) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (myDialog != null) {
                        myDialog.dismiss();
                    }
                    myDialog = new ProgressDialog(activity);
                    myDialog.setMessage(message);
                    myDialog.setCancelable(false);
                    myDialog.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }


    public static void showConfirmDialogSingle(final Context activity, final String message, final ResponceCallback responceCallback) {
        // AlertDialog.Builder builder = new AlertDialog.Builder(activity,R.style.CustomDialog);
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(activity, R.style.CustomDialog);
        final FrameLayout frameView = new FrameLayout(activity);
        builder.setView(frameView);
        myalertdialog = builder.create();
        LayoutInflater inflater = myalertdialog.getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.confirm_dialog, frameView);
        TextView title = dialoglayout.findViewById(R.id.text);
        TextView yesbtn = dialoglayout.findViewById(R.id.yesBtn);
        TextView noBtn = dialoglayout.findViewById(R.id.noBtn);
        noBtn.setVisibility(View.GONE);
        title.setText(message);
        yesbtn.setText(activity.getResources().getString(R.string.ok));
        yesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                responceCallback.onSuccessResponce(v);
            }
        });

        myalertdialog.setCancelable(false);
        myalertdialog.setView(dialoglayout);
        myalertdialog.show();
    }
    public static void showNetworkDailogSingle(final Context activity, final String message, final ResponceCallback responceCallback) {
        // AlertDialog.Builder builder = new AlertDialog.Builder(activity,R.style.CustomDialog);
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(activity, R.style.CustomDialog);
        final FrameLayout frameView = new FrameLayout(activity);
        builder.setView(frameView);
        myalertdialog = builder.create();
        LayoutInflater inflater = myalertdialog.getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.confirm_dialog, frameView);
        TextView title = dialoglayout.findViewById(R.id.text);
        TextView yesbtn = dialoglayout.findViewById(R.id.yesBtn);
        TextView noBtn = dialoglayout.findViewById(R.id.noBtn);
        noBtn.setVisibility(View.GONE);
        title.setText(message);
        yesbtn.setText(activity.getResources().getString(R.string.ok));
        yesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                responceCallback.onSuccessResponce(v);
            }
        });

        myalertdialog.setCancelable(false);
        myalertdialog.setView(dialoglayout);
        myalertdialog.show();
    }

    public static void showConfirmDialog(final Context activity, final String message, final ResponceCallback responceCallback) {
        try {


            // AlertDialog.Builder builder = new AlertDialog.Builder(activity,R.style.CustomDialog);
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(activity, R.style.CustomDialog);
            final FrameLayout frameView = new FrameLayout(activity);
            builder.setView(frameView);
            myalertdialog = builder.create();
            LayoutInflater inflater = myalertdialog.getLayoutInflater();
            View dialoglayout = inflater.inflate(R.layout.confirm_dialog, frameView);
            TextView title = dialoglayout.findViewById(R.id.text);
            TextView yesbtn = dialoglayout.findViewById(R.id.yesBtn);
            TextView noBtn = dialoglayout.findViewById(R.id.noBtn);
            title.setText(message);

            yesbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    responceCallback.onSuccessResponce(v);
                }
            });
            noBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    responceCallback.onFailureResponce(v);
                }
            });
            myalertdialog.setCancelable(false);
            myalertdialog.setView(dialoglayout);
            myalertdialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showConfirmwithCheckDialog(final Context activity, final String message, final ResponceCallback responceCallback) {
        try {


            // AlertDialog.Builder builder = new AlertDialog.Builder(activity,R.style.CustomDialog);
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(activity, R.style.CustomDialog);
            final FrameLayout frameView = new FrameLayout(activity);
            builder.setView(frameView);
            myalertdialog = builder.create();
            LayoutInflater inflater = myalertdialog.getLayoutInflater();
            View dialoglayout = inflater.inflate(R.layout.confirm_dialog_with_check, frameView);
            TextView title = dialoglayout.findViewById(R.id.text);
            TextView yesbtn = dialoglayout.findViewById(R.id.yesBtn);
            ImageView check = dialoglayout.findViewById(R.id.iv_check);
            TextView noBtn = dialoglayout.findViewById(R.id.noBtn);
            title.setText(message);
            check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    check.setSelected(!check.isSelected());
                }
            });

            yesbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (check.isSelected()) {
                        SessionManager.put("isShowPopup", 1);
                    } else {
                        SessionManager.put("isShowPopup", 0);
                    }
                    responceCallback.onSuccessResponce(v);
                }
            });
            noBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (check.isSelected()) {
                        SessionManager.put("isShowPopup", 1);
                    } else {
                        SessionManager.put("isShowPopup", 0);
                    }
                    responceCallback.onFailureResponce(v);
                }
            });
            myalertdialog.setCancelable(false);
            myalertdialog.setView(dialoglayout);
            myalertdialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showLanguagePopup(final Context activity, final String message, final ResponceCallback responceCallback,Activity mActivity) {

        // AlertDialog.Builder builder = new AlertDialog.Builder(activity,R.style.CustomDialog);
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(activity, R.style.CustomDialog);
        final FrameLayout frameView = new FrameLayout(activity);

        builder.setView(frameView);
        myalertdialog = builder.create();

        LayoutInflater inflater = myalertdialog.getLayoutInflater();
        LinearLayout engBtn, arabicBtn, russianBtn;
        ImageView engCheck, arabicCheck, russianCheck;
        View dialoglayout = inflater.inflate(R.layout.activity_language_selection, frameView);
        engBtn = dialoglayout.findViewById(R.id.engBtn);
        arabicBtn = dialoglayout.findViewById(R.id.arabicBtn);
        russianBtn = dialoglayout.findViewById(R.id.russianBtn);

        engCheck = dialoglayout.findViewById(R.id.engCheck);
        arabicCheck = dialoglayout.findViewById(R.id.arabicCheck);
        russianCheck = dialoglayout.findViewById(R.id.russianCheck);

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
                setLocale("en", activity);
//                changeLanguage(activity,"en",responceCallback);
                if (Utility.isUserLogin(activity)) {
                    activity.startActivity(new Intent(activity, DashboardUpdateActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                } else {
                    activity.startActivity(new Intent(activity, SigninActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                }
                hideConfirmDialog();
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
                setLocale("ar", activity);
//                 changeLanguage(activity,"ar",responceCallback);
                if (Utility.isUserLogin(activity)) {
                    activity.startActivity(new Intent(activity, DashboardUpdateActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                } else {
                    activity.startActivity(new Intent(activity, SigninActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                }
                hideConfirmDialog();
            }
        });

        russianBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                engCheck.setImageResource(R.drawable.tick_gray);
                arabicCheck.setImageResource(R.drawable.tick_gray);
                russianCheck.setImageResource(R.drawable.tick_blue);
                SessionManager.put(Constants.LANGUAGE, "ru");
                setLocale("ru", activity);
//                  changeLanguage(activity,"ru",responceCallback);
                if (Utility.isUserLogin(activity)) {
                    activity.startActivity(new Intent(activity, DashboardUpdateActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                } else {
                    activity.startActivity(new Intent(activity, SigninActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                }
                hideConfirmDialog();
            }
        });


//        TextView title = dialoglayout.findViewById(R.id.text);
//        TextView yesbtn = dialoglayout.findViewById(R.id.yesBtn);
//        TextView noBtn = dialoglayout.findViewById(R.id.noBtn);
//        title.setText(message);
//
//        yesbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                responceCallback.onSuccessResponce(v);
//            }
//        });
//        noBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                responceCallback.onFailureResponce(v);
//            }
//        });
        myalertdialog.setCancelable(false);
        myalertdialog.setView(dialoglayout);
        myalertdialog.show();

    }
    public static void changeLanguage(Activity context,String language,ResponceCallback callback) {
        ServerCall.updateLanguage(context, language, callback);
    }
    public static void setLocale(String lang, final Context activity) {
        Locale myLocale = new Locale(lang);
        Locale.setDefault(myLocale);
        Configuration config = new Configuration();
        config.locale = myLocale;
        activity.getResources().updateConfiguration(config, activity.getResources().getDisplayMetrics());
        deleteCache(activity);
    }

    public static void deleteCache(final Context activity) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            try {
                File dir = activity.getApplicationContext().getCacheDir();
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

    public static void showConfirmDynamicDialog(final Context activity, final String message, String positiveText, String nagativeText, final ResponceCallback responceCallback) {
        // AlertDialog.Builder builder = new AlertDialog.Builder(activity,R.style.CustomDialog);
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(activity, R.style.CustomDialog);
        final FrameLayout frameView = new FrameLayout(activity);
        builder.setView(frameView);
        myalertdialog = builder.create();
        LayoutInflater inflater = myalertdialog.getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.confirm_dialog, frameView);
        TextView title = dialoglayout.findViewById(R.id.text);
        TextView yesbtn = dialoglayout.findViewById(R.id.yesBtn);
        yesbtn.setText(positiveText);
        TextView noBtn = dialoglayout.findViewById(R.id.noBtn);
        noBtn.setText(nagativeText);
        title.setText(message);
        yesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                responceCallback.onSuccessResponce(v);
            }
        });
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                responceCallback.onFailureResponce(v);
            }
        });
        myalertdialog.setCancelable(false);
        myalertdialog.setView(dialoglayout);
        myalertdialog.show();
    }


    public static void showAddCommentDialog(final Context activity, final ResponceCallback responceCallback) {

        // AlertDialog.Builder builder = new AlertDialog.Builder(activity,R.style.CustomDialog);
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(activity);
        final FrameLayout frameView = new FrameLayout(activity);
        builder.setView(frameView);
        myalertdialog = builder.create();
        LayoutInflater inflater = myalertdialog.getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.add_comment_dialog, frameView);

        EditText etMessage = dialoglayout.findViewById(R.id.etMsg);
        TextView btnDone = dialoglayout.findViewById(R.id.btn_done);
        RadioGroup radioGroup = dialoglayout.findViewById(R.id.radio_group);
        RadioButton radioCOD = dialoglayout.findViewById(R.id.radio_cod);
        RadioButton radioCard = dialoglayout.findViewById(R.id.radio_card);

        radioCard.setChecked(true);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PAYMENT_COMMENT_TEXT = etMessage.getText().toString();
                PaymentType paymentType = new PaymentType();
                paymentType.setComment(etMessage.getText().toString());
               /* if (radioCOD.isChecked())
                    paymentType.setPaymentType(COD);
                else if (radioCard.isChecked())*/
                paymentType.setPaymentType(CARD);
                responceCallback.onSuccessResponce(paymentType);


            }
        });
        myalertdialog.setCancelable(true);
        myalertdialog.setView(dialoglayout);
        myalertdialog.show();
        Window window = myalertdialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawableResource(android.R.color.transparent);
    }

    public static void showFreeAddCommentDialog(final Context activity, final ResponceCallback responceCallback) {

        // AlertDialog.Builder builder = new AlertDialog.Builder(activity,R.style.CustomDialog);
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(activity, R.style.DialogTheme);
        final FrameLayout frameView = new FrameLayout(activity);
        builder.setView(frameView);
        myalertdialog = builder.create();
        LayoutInflater inflater = myalertdialog.getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.add_comment_dialog, frameView);

        EditText etMessage = dialoglayout.findViewById(R.id.etMsg);
        TextView btnDone = dialoglayout.findViewById(R.id.btn_done);
        RadioGroup radioGroup = dialoglayout.findViewById(R.id.radio_group);

        radioGroup.setVisibility(View.GONE);

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PAYMENT_COMMENT_TEXT = etMessage.getText().toString();
                responceCallback.onSuccessResponce(null);

            }
        });
        myalertdialog.setCancelable(true);
        myalertdialog.setView(dialoglayout);
        myalertdialog.show();
//        Window window = myalertdialog.getWindow();
//        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
//        window.setBackgroundDrawableResource(android.R.color.transparent);
    }

    public static void showChatDialog(final Context activity, String message) {

        if (((Activity) activity).isFinishing()) {
            Log.e("Notification", "Return chat dialog");
            return;
        }
        Log.e("Notification", "chat dialog");

        if(myalertdialog != null && myalertdialog.isShowing()){
            myalertdialog.dismiss();
        }

        // AlertDialog.Builder builder = new AlertDialog.Builder(activity,R.style.CustomDialog);
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(activity, R.style.MyAlertDialogTheme);
        final FrameLayout frameView = new FrameLayout(activity);
        builder.setView(frameView);
        myalertdialog = builder.create();
        LayoutInflater inflater = myalertdialog.getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.chat_dialog, frameView);

        TextView etMessage = dialoglayout.findViewById(R.id.message);
        TextView btnCancel = dialoglayout.findViewById(R.id.btn_cancel);
        TextView btnView = dialoglayout.findViewById(R.id.btn_view);

        etMessage.setText(message);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myalertdialog.dismiss();
            }
        });

        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // activity.startActivity(new Intent(activity, ChatActivity.class));
                myalertdialog.dismiss();
                Intent intent = new Intent(activity, DashboardUpdateActivity.class);
                intent.putExtra("notification", true);
              //  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                activity.startActivity(intent);

            }
        });
        myalertdialog.setCancelable(true);
        myalertdialog.setView(dialoglayout);
        myalertdialog.show();
        Window window = myalertdialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawableResource(android.R.color.transparent);
    }


    public static void hideConfirmDialog() {
        try {
            if (myalertdialog != null) {
                myalertdialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }


    public static void hideLoadingDialog() {
        try {
            if (myDialog != null && myDialog.isShowing()) {
                myDialog.dismiss();

            }
        } catch (Exception e) {

        }
    }

    public static boolean isNetConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static String convertDate(String time) {
        String str = "";
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date d = format.parse(time);
            str = new SimpleDateFormat("dd-MMM-yyyy").format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String getFormattedDate(Context context, String time) {
        String str = "";
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date d = format.parse(time);

            long smsTimeInMilis = d.getTime();

            Calendar smsTime = Calendar.getInstance();
            smsTime.setTimeInMillis(smsTimeInMilis);

            Calendar now = Calendar.getInstance();

            final String timeFormatString = "dd-MMM-yyyy";
            final String dateTimeFormatString = "dd-MMM-yyyy";
            if (now.get(Calendar.DATE) == smsTime.get(Calendar.DATE)) {
                str = "Today";// + DateFormat.format(timeFormatString, smsTime);
            } else if (now.get(Calendar.DATE) - smsTime.get(Calendar.DATE) == 1) {
                str = "Yesterday";// + DateFormat.format(timeFormatString, smsTime);
            } else if (now.get(Calendar.YEAR) == smsTime.get(Calendar.YEAR)) {
                str = DateFormat.format(dateTimeFormatString, smsTime).toString();
            } else {
                str = DateFormat.format("dd-MMM-yyyy", smsTime).toString();
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String getDateWithMonthName(String time) {
        String str = "";
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date d = format.parse(time);

            long smsTimeInMilis = d.getTime();

            Calendar smsTime = Calendar.getInstance();
            smsTime.setTimeInMillis(smsTimeInMilis);
            str = DateFormat.format("dd-MMM-yyyy", smsTime).toString();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String getMessageTime(String dateFormat) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        //SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Date date = null;
        try {

            dt.setTimeZone(TimeZone.getTimeZone("Asia/Dubai"));

            date = dt.parse(dateFormat);

            System.out.println(date);
            System.out.println(dt.format(date));

        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat formatted = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formatted.setTimeZone(TimeZone.getDefault());
        String s = "";
        if (date != null) {
            s = formatted.format(date);
        }

        return s;


    }


    public static int getDeviceWidthInPercentage(final Context context, int percentage) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float toMultiply = percentage / 100f;
        float pixels = displayMetrics.widthPixels * toMultiply;
        return (int) pixels;
    }

    public static String getPathFromUri(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static String createCameraImageFileName() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return timeStamp + ".jpg";
    }

    public static boolean checkDir(String path) {
        boolean result = true;
        File f = new File(path);
        if (!f.exists()) {
            result = f.mkdirs();
        } else if (f.isFile()) {
            f.delete();
            result = f.mkdirs();
        }
        return result;
    }

    public static Bitmap loadBitmap(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap b = null;
        try {
            options.inSampleSize = 1;
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);
            if (options.mCancel || options.outWidth == -1
                    || options.outHeight == -1) {
                return null;
            }
            options.inSampleSize = 2;
            options.inJustDecodeBounds = false;
            options.inDither = false;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            b = BitmapFactory.decodeFile(path, options);
            if (b == null)
                return null;

            int orientation = getOrientation(path);
            if (orientation != 1) {
                Matrix m = new Matrix();
                m.postRotate(getRotation(orientation));
                b = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), m, false);
            }

        } catch (OutOfMemoryError ex) {
            ex.printStackTrace();
            System.gc();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }

    private static int getRotation(int orientation) {
        switch (orientation) {
            case 1:
                return 0;
            case 8:
                return 270;
            case 3:
                return 180;
            case 6:
                return 90;
            default:
                return 0;
        }
    }

    private static int getOrientation(String file) {
        int orientation = 1;
        ExifInterface exif;
        if (!TextUtils.isEmpty(file)) {
            try {
                exif = new ExifInterface(file);
                orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ExceptionInInitializerError e) {
                e.printStackTrace();
            }
        }
        return orientation;
    }

    public static void getFacebookHashKey(Context context) {
        PackageInfo info;
        try {
            info = context.getPackageManager().getPackageInfo("com.devicebee.tryapply", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("hash key", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
    }
}
