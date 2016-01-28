package com.ua.max.oliynick.flicker.util;

import java.util.regex.Pattern;

/**
 * Created by Максим on 28.01.2016.
 */
public class EmailUtils {

    /**
     * email regular expression to check email
     * for validity
     * */
    public static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\p{L}\\p{N}\\._%+-]+@[\\p{L}\\p{N}\\.\\-]+\\.[\\p{L}]{2,}$");

    /**
     * Indicates whether given email is valid or not
     * @param email - email to check
     * @return true if given email is valid and false
     * in another case
     * */
    public static boolean isValidEmail(final String email) {
        if(email == null) return false;

        return email.matches(EMAIL_PATTERN.toString());
    }

}
