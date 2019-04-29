package net.bridgeint.app.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by zeeshan on 5/31/17.
 */

public class SessionManager {
    private static SharedPreferences.Editor editor;
    private static SharedPreferences pref;
    private static Context context;
    public static final String WRONG_PAIR = "Key-Value pair cannot be blank or null";

    private static final String isLogin = "isLogin";
    private static final String keyName = "userName";
    private static final String keyEmail = "userEmail";
    private static final String key = "key";

    private static final String keyImageUrl = "img_url";
    private static int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "sessionPreference_bridge";

    public SessionManager(Context _contxt) {
        context = _contxt;
        pref = _contxt.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession() {

        editor.putString(isLogin, "true");
        editor.commit();
    }

    public void saveUserName(String name) {
        editor.putString(keyName, name);
        editor.commit();
    }

    public void remove(String key) {
        editor.remove(key);
        editor.commit();
    }


    public void saveUserImageURL(String url) {
        editor.putString(keyImageUrl, url);
        editor.commit();
    }

    public String getUserImageURL() {
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        String imgUrl = pref.getString(keyImageUrl, "");
        return imgUrl;
    }


    public boolean checkLogin() {
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        String login = pref.getString(isLogin, "");

        return login.equalsIgnoreCase("true");
    }

    public void clearSession() {
        editor.clear();
        editor.commit();
    }

    public static boolean put(final String key, final String value) {
        if (key == null || key.equals("")) {
            throw new IllegalArgumentException(WRONG_PAIR);
        }
        pref = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        editor.putString(key, value);
        return editor.commit();
    }

    public static boolean put(final String key, final Integer value) {
        if (key == null || key.equals("")) {
            throw new IllegalArgumentException(WRONG_PAIR);
        }
        pref = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        editor.putInt(key, value);
        return editor.commit();
    }

    public static boolean put(final String key, final boolean value) {
        if (key == null || key.equals("")) {
            throw new IllegalArgumentException(WRONG_PAIR);
        }
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor.putBoolean(key, value);
        return editor.commit();
    }

    public static String get(String accessToken) {

        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        String number = pref.getString(accessToken, "");
        return number;
    }


    public static int getInt(final String key) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return pref.getInt(key, 0);
    }

}
