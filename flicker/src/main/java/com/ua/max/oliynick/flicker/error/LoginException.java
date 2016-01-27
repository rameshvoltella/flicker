package com.ua.max.oliynick.flicker.error;

/**
 * Created by Максим on 26.01.2016.
 */
public class LoginException extends Throwable {

    public LoginException(String detailMessage) {
        super(detailMessage);
    }

    public LoginException() {
        super();
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    @Override
    public String getLocalizedMessage() {
        return super.getLocalizedMessage();
    }
}
