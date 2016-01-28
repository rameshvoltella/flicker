package com.ua.max.oliynick.flicker.interfaces;

import com.ua.max.oliynick.flicker.error.LoginException;
import com.ua.max.oliynick.flicker.util.ValidationResult;

import roboguice.inject.ContextSingleton;

/**
 * This interface represents login
 * model which provides login controller
 * with data and set of methods to valid
 * user input
 * Created by Максим on 21.01.2016.
 */
@ContextSingleton
public interface ILoginModel {

    /**Add other social networks*/
    public enum LoginService {Xmpp, Facebook, Vk};

    /**
     * This method should be invoked in order to login user
     * in XMPP server
     * @param login - user's login
     * @param password - user's password
     * */
    public void login(final String login, final String password, LoginService service, boolean saveCredentials) throws LoginException;

    public void login(final String login, final String password, boolean saveCredentials) throws  LoginException;

    public boolean isSaveAuthData();

    public void setSaveAuthData(final boolean save);

    /**
     * Validates given login
     * @param login - specified login
     * @return ValidationResult
     * */
    public ValidationResult validateEmail(final String login);

    /**
     * Validates given password
     * @param password - specified password
     * @return ValidationResult
     * */
    public ValidationResult validatePassword(final String password);
}
