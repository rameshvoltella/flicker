package com.ua.max.oliynick.flicker.model;

import android.util.Log;

import com.ua.max.oliynick.flicker.error.LoginException;
import com.ua.max.oliynick.flicker.interfaces.ILoginModel;
import com.ua.max.oliynick.flicker.interfaces.ISettings;
import com.ua.max.oliynick.flicker.util.Settings;
import com.ua.max.oliynick.flicker.util.ValidationResult;
import com.ua.max.oliynick.flicker.util.XMPPTCPConnectionHolder;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import oliynick.max.ua.com.flicker.R;
import roboguice.inject.InjectResource;

/**
 * An implementation of ILoginModel interface
 * Created by Максим on 26.01.2016.
 */
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

    private final Observer sucLoginObs;

    public LoginModel() {

        sucLoginObs = new Observer() {
            @Override
            public void update(Observable observable, Object data) {
                Boolean newValue = (Boolean) data;
                Log.d("obs", "new value is " + newValue.booleanValue());
            }
        };

    }

    @Override
    public void login(String login, String password, LoginService service, boolean saveCredentials) throws LoginException {

        XMPPTCPConnectionHolder.getInstance().getLoginObservable().addObserver(sucLoginObs);

        //User's input validation
        final ValidationResult emailValidator = validateEmail(login);
        final ValidationResult passValidator = validatePassword(password);

        /*
        Throving an exception with appropriate exception message
        in case of invalid user's input
        * */
        if(!emailValidator.isValid() || !passValidator.isValid()) {

            final boolean isNullEmaiMsg = emailValidator.getMessage() == null;
            final boolean isNullPassMsg = passValidator.getMessage() == null;
            final StringBuilder msg = new StringBuilder();

            if(isNullEmaiMsg && isNullPassMsg) {
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

    @Override
    public ValidationResult validateEmail(String login) {

        /*if(login == null || login.length() == 0)
            return new ValidationResult(false, emptyEmail);

        int length = login.length();

        if(length < AppConstant.MIN_EMAIL_LEN)
            return new ValidationResult(false, shortEmail);

        if(length > AppConstant.MAX_EMAIL_LEN)
            return new ValidationResult(false, longEmail);

        if(!EmailUtils.isValidEmail(login))
            return  new ValidationResult(false, invalidEmail);*/

        return new ValidationResult();
    }

    @Override
    public ValidationResult validatePassword(String password) {

       /* if(password == null || password.length() == 0)
            return new ValidationResult(false, emptyPassword);

        int length = password.length();

        if(length < AppConstant.MIN_PASS_LEN)
            return new ValidationResult(false, shortPassword);

        if(length > AppConstant.MAX_PASS_LEN)
            return new ValidationResult(false, longPassword);*/

        return new ValidationResult();
    }
}
