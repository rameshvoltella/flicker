package com.ua.max.oliynick.flicker.util;

/**
 * This class represents an user's input
 * validation result.
 * Created by Максим on 28.01.2016.
 */
public class ValidationResult {

    private final boolean isValid;
    private final String message;

    public ValidationResult() {
        this(true, null);
    }

    public ValidationResult(final boolean isValid, final String message) {
        this.isValid = isValid;
        this.message = message;
    }

    public boolean isValid() {
        return isValid;
    }

    public String getMessage() {
        return message;
    }
}
