package com.ua.max.oliynick.flicker.model;

import android.util.Log;

import com.ua.max.oliynick.flicker.error.LoginException;
import com.ua.max.oliynick.flicker.interfaces.ILoginModel;
import com.ua.max.oliynick.flicker.interfaces.ISettings;
import com.ua.max.oliynick.flicker.singleton.MainApp;
import com.ua.max.oliynick.flicker.util.AppConstant;
import com.ua.max.oliynick.flicker.util.ValidationResult;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;

import java.io.IOException;

import oliynick.max.ua.com.flicker.R;
import roboguice.inject.ContextSingleton;
import roboguice.inject.InjectResource;

/**
 * An implementation of ILoginModel interface
 * Created by Максим on 26.01.2016.
 */
@ContextSingleton
public class LoginModel implements ILoginModel {

    @InjectResource(R.string.invalid_input)
    private String invalidInput;

    @InjectResource(R.string.empty_email)
    private String emptyEmail;

    @InjectResource(R.string.short_email)
    private String shortEmail;

    @InjectResource(R.string.long_email)
    private String longEmail;

    @InjectResource(R.string.invalid_email)
    private String invalidEmail;

    @InjectResource(R.string.empty_password)
    private String emptyPassword;

    @InjectResource(R.string.short_pass)
    private String shortPassword;

    @InjectResource(R.string.long_pass)
    private String longPassword;

    @InjectResource(R.string.internal_error)
    private String internalError;

    @InjectResource(R.string.login_xmpp_err)
    private String xmppErr;

    public LoginModel() {}

    @Override
    public void login(String login, String password, LoginService service) throws LoginException {

        if(!MainApp.getConnectionListener().isConnected() ||
                !MainApp.getConnection().isConnected()) {
            throw new LoginException("not connected");
        }

        //User's input validation
        final ValidationResult emailValidator = validateEmail(login);
        final ValidationResult passValidator = validatePassword(password);

        /*
        Throving an exception with appropriate exception message
        in case of invalid user's input
        * */
        if(!emailValidator.isValid() || !passValidator.isValid()) {

            final boolean isNullEmailMsg = emailValidator.getMessage() == null;
            final boolean isNullPassMsg = passValidator.getMessage() == null;
            final StringBuilder msg = new StringBuilder();

            if(isNullEmailMsg && isNullPassMsg) {
                msg.append(invalidInput);
            } else {
                if (emailValidator.getMessage() != null) {
                    msg.append(emailValidator.getMessage()).append('\n');
                }
                if (passValidator.getMessage() != null) {
                    msg.append(passValidator.getMessage()).append('\n');
                }
                msg.setLength(msg.length() - 1);
            }
            throw new LoginException(msg.toString());
        }

        XMPPTCPConnection connection = MainApp.getConnection();

        try {

            ISettings settings = MainApp.getSettings();

            if(!connection.isConnected()) {
                connection.connect();
            }

            connection.login(login, password);

            if(settings.isSaveAuthData()) {
                settings.setSaveAuthData(true);
            } else {
                settings.setSaveAuthData(false);
            }

            // reloads settings for current user
            settings.loadFor(login.toLowerCase());

        } catch (XMPPException e) {
            e.printStackTrace();
            Log.d("exc", "xmppexc " + e.getMessage());
            connection.disconnect();
            throw new LoginException(xmppErr);
        } catch (SmackException e) {
            e.printStackTrace();
            Log.d("exc", "smackexc " + e.getMessage());
            throw new LoginException(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("exc", "ioexc " + e.getMessage());
            throw new LoginException(internalError);
        }

    }

    @Override
    public void login(String login, String password) throws LoginException {
        login(login, password, LoginService.Xmpp);
    }

    @Override
    public boolean isSavedAuthData() {
        return MainApp.getSettings().isSaveAuthData();
    }

    @Override
    public String getSavedLogin() {
        return MainApp.getSettings().getSavedLogin();
    }

    @Override
    public String getSavedPassword() {
        return MainApp.getSettings().getSavedPassword();
    }

    @Override
    public ValidationResult validateEmail(String login) {

        if(login == null || login.length() == 0)
            return new ValidationResult(false, emptyEmail);

        int length = login.length();

        if(length < AppConstant.MIN_EMAIL_LEN)
            return new ValidationResult(false, shortEmail);

        if(length > AppConstant.MAX_EMAIL_LEN)
            return new ValidationResult(false, longEmail);

        return new ValidationResult();
    }

    @Override
    public ValidationResult validatePassword(String password) {

        if(password == null || password.length() == 0)
            return new ValidationResult(false, emptyPassword);

        int length = password.length();

        if(length < AppConstant.MIN_PASS_LEN)
            return new ValidationResult(false, shortPassword);

        if(length > AppConstant.MAX_PASS_LEN)
            return new ValidationResult(false, longPassword);

        return new ValidationResult();
    }
}
