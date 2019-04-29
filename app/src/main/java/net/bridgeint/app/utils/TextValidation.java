package net.bridgeint.app.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ufraj on 11/22/2016.
 */
public class TextValidation {

     public static boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public static boolean isValidtText(String name) {
        return !name.equals(null) && name.length() >= 1;
    }
}
