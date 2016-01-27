package com.ua.max.oliynick.flicker.interfaces;

import com.ua.max.oliynick.flicker.error.LoginException;

/**
 * Created by Максим on 21.01.2016.
 */
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
}
