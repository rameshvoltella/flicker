package com.ua.max.oliynick.flicker.interfaces;

import com.ua.max.oliynick.flicker.error.LoginException;
import com.ua.max.oliynick.flicker.util.ValidationResult;

/**
 * This interface represents login
 * model which provides login controller
 * with data and set of methods to valid
 * user input
 * Created by Максим on 21.01.2016.
 */
public interface ILoginModel {

    /**Add other social networks*/
    public enum LoginService {Xmpp, Facebook, Vk};

    /**
     * This method should be invoked in order to login user
     * in XMPP server
     * @param login user's login
     * @param password user's password
     * @param service service to use. For example it can be an
     *                Xmpp(native service), Facebook, Vkontakte
     * */
    public void login(final String login, final String password, LoginService service) throws LoginException;

    /**
     * This method should be invoked in order to login user
     * in XMPP server. In this case user will be signed via
     * xmpp service
     * @param login user's login
     * @param password user's password
     * */
    public void login(final String login, final String password) throws  LoginException;

    /**
     * Returns true if user wants to save his credentials
     * in order do not input them each time he starts app
     * */
    public boolean isSavedAuthData();

    /**
     * Returns saved user's app login. If it wasn't set, then
     * null will be returned
     * */
    public String getSavedLogin();

    /**
     * Returns saved user's app password. If it wasn't set, then
     * null will be returned
     * */
    public String getSavedPassword();

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
