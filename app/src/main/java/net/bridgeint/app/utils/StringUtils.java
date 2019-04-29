package net.bridgeint.app.utils;

import android.util.Patterns;

import java.util.regex.Pattern;

public class StringUtils {

    public static final String EMPTY_STRING = "";
    public static final String SPACE_STRING = " ";
    public static final String COLON = " : ";
    public static final String DATE_SEPARATOR = "/";
    public static final String TIME_SEPARATOR = ":";
    public static final String COMMA = ",";
    public static final String PERCENTAGE = "%";
    public static final String NULL_STRING = "null";
    public static final String COUNTRY_CODE_INDIA = "+91";
    private static final int MAX_VALID_DIGITS_PHONE = 14;
    private static final int MIN_VALID_DIGITS_PHONE = 8;
    private static int MAX_VALID_AGE = 90;
    private static int MIN_VALID_AGE = 18;

    /**
     * Checks if string is empty or not
     *
     * @param value string to check
     * @return true if empty or null, false otherwise
     */
    public static boolean isEmpty(String value) {
        try {
            value = value.trim();
        } catch (Exception e) {
        }
        return value == null || value.length() == 0;
    }

    /**
     * Checks if string is valid and having at least minimum number of characters or not
     *
     * @param value string to check
     * @param min   minimum length of validity
     * @return true if valid, false otherwise
     */
    public static boolean isValid(String value, int min) {
        try {
            value = value.trim();
        } catch (Exception e) {
        }
        return value != null && value.length() >= min;
    }

    /**
     * Checks if string is valid or not
     *
     * @param value string to check
     * @return true if valid, false otherwise
     */
    public static boolean isValid(String value) {
        return !isEmpty(value) && !value.equalsIgnoreCase(EMPTY_STRING) && !value.equalsIgnoreCase(NULL_STRING);
    }

    /**
     * Checks if email id is in valid format or not
     *
     * @param emailId email id to check
     * @return true if valid, false otherwise
     */
    public static boolean isValidEmail(String emailId) {
        if (!isValid(emailId)) {
            return false;
        }
/*
        String emailPattern = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")";
*/
        Pattern pattern = Patterns.EMAIL_ADDRESS;

        return pattern.matcher(emailId).matches();
    }

    /**
     * Checks if phone number is in valid format and not having max digits
     *
     * @param phone phone number to check
     * @return true if valid, false otherwise
     */
    public static boolean isValidPhone(String phone) {
        try {
            phone = phone.trim();
        } catch (Exception e) {
        }
        return phone != null && isValid(phone) && phone.length() <= MAX_VALID_DIGITS_PHONE && phone.length() >= MIN_VALID_DIGITS_PHONE;
    }

    public static boolean equals(String s1, String s2) {
        return isValid(s1) && isValid(s2) && s1.equals(s2);
    }

    public static String formatDate(int day, int month, int year) {
        return String.format("%02d" + DATE_SEPARATOR + "%02d" + DATE_SEPARATOR + "%02d",
                month, day, year);
/*
        return
                String.valueOf(month) + DATE_SEPARATOR
                        + String.valueOf(day) + DATE_SEPARATOR
                        + String.valueOf(year)
                ;
*/
    }

    public static String formatTime(int hour, int minute, int second) {
        return String.format("%02d" + TIME_SEPARATOR + "%02d", hour, minute);
    }

    public static String trimLastComma(String str) {
        if (str != null && str.length() >= 2 && str.charAt(str.length() - 1) == ',') {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    public static boolean isValidAge(String age) {
        try {
            age = age.trim();
        } catch (Exception e) {
        }
        return age != null && isValid(age) && Integer.parseInt(age) <= MAX_VALID_AGE && Integer.parseInt(age) >= MIN_VALID_AGE;
    }
}