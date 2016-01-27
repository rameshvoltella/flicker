package com.ua.max.oliynick.flicker.model;

import com.ua.max.oliynick.flicker.XMPPTCPConnectionHolder;
import com.ua.max.oliynick.flicker.error.LoginException;
import com.ua.max.oliynick.flicker.interfaces.ILoginModel;
import com.ua.max.oliynick.flicker.interfaces.ISettings;
import com.ua.max.oliynick.flicker.util.Settings;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;

import java.io.IOException;

import oliynick.max.ua.com.flicker.R;
import roboguice.inject.InjectResource;

/**
 * Created by Максим on 26.01.2016.
 */
public class LoginModel implements ILoginModel {

    @InjectResource(R.string.empty_login)
    private String emptyLogin;

    @InjectResource(R.string.empty_password)
    private String emptyPassword;

    @InjectResource(R.string.internal_error)
    private String internalError;

    public LoginModel() {}

    @Override
    public void login(String login, String password, LoginService service, boolean saveCredentials) throws LoginException {

        if(login == null || login.length() == 0)
            throw new LoginException(emptyLogin);

        if(password == null || password.length() == 0)
            throw new LoginException(emptyPassword);

        try {

            XMPPTCPConnection connection = XMPPTCPConnectionHolder.getInstance();
            ISettings settings = Settings.getInstance();

            if(!connection.isConnected()) {
                connection.connect();
            }

            connection.login(login, password);

            if(saveCredentials && !settings.isSaveAuthData()) {
                Settings.getInstance().setSaveAuthData(true);
            }

        } catch (XMPPException e) {
            e.printStackTrace();
            throw new LoginException(internalError);
        } catch (SmackException e) {
            e.printStackTrace();
            throw new LoginException(internalError);
        } catch (IOException e) {
            e.printStackTrace();
            throw new LoginException(internalError);
        }

    }

    @Override
    public void login(String login, String password, boolean saveCredentials) throws LoginException {
        login(login, password, LoginService.Xmpp, saveCredentials);
    }

    @Override
    public boolean isSaveAuthData() {
        return Settings.getInstance().isSaveAuthData();
    }

    @Override
    public void setSaveAuthData(boolean save) {
        Settings.getInstance().setSaveAuthData(save);
    }
}
