package com.ua.max.oliynick.flicker.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.inject.Inject;
import com.ua.max.oliynick.flicker.error.LoginException;
import com.ua.max.oliynick.flicker.interfaces.ILoginController;
import com.ua.max.oliynick.flicker.interfaces.ILoginModel;
import com.ua.max.oliynick.flicker.util.Settings;
import com.ua.max.oliynick.flicker.util.ValidationResult;
import com.ua.max.oliynick.flicker.util.XMPPTCPConnectionHolder;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import java.io.IOException;

import oliynick.max.ua.com.flicker.R;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;

@ContentView(R.layout.login)
public class LoginActivity extends RoboActivity implements ILoginController {

    @Inject
    private ILoginModel model;

    @InjectView(R.id.loginField)
    private EditText loginField;

    @InjectView(R.id.passwordField)
    private EditText passwordField;

    @InjectView(R.id.sign_in)
    private Button loginButton;

    @InjectResource(R.string.auth_bar_mess)
    private String authProgrText;

    private final ProgressDialog authDialog = new ProgressDialog(this);

    // TODO move
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            boolean isConnected = !intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);

            if(!XMPPTCPConnectionHolder.isInit()) {
                if(isConnected) {
                    //remove toast
                    Toast.makeText(context, "Trying to connect", Toast.LENGTH_LONG).show();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                XMPPTCPConnectionHolder.initInstance(Settings.getInstance());
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (XMPPException e) {
                                e.printStackTrace();
                            } catch (SmackException e) {
                                e.printStackTrace();
                            }
                        }
                    }).run();
                } else {
                    Toast.makeText(context, "There is no internet connection", Toast.LENGTH_LONG).show();
                }
            }

        }
    };

    public LoginActivity() {}

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // FIXME!
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        authDialog.setIndeterminate(true);
        authDialog.setCancelable(false);
        authDialog.setMessage(authProgrText);

        //Initializing settings with app context
        Settings.initInstance(getApplicationContext());
        //Initializing facebook sdk with app context
       // FacebookSdk.sdkInitialize(this);
        //Initializing xmpp connection to server

        if(model.getSavedLogin() != null
                && model.getSavedPassword() != null) {

            loginField.setText(model.getSavedLogin());
            passwordField.setText(model.getSavedPassword());
        } else {
            //TODO remove else clause
            Settings.getInstance().setCredentials("maxxx","qwerty");
        }

        Log.d("credentials", Settings.getInstance().getSavedLogin());
        Log.d("credentials", Settings.getInstance().getSavedPassword());

    }

    @Override
    public void onLogin(View v) {

        final String login = loginField.getText().toString();
        final String password = passwordField.getText().toString();
        final ValidationResult loginValid = model.validateEmail(login);
        final ValidationResult passValid = model.validatePassword(password);
        String errMess = null;

        if(!loginValid.isValid()) {
            errMess = loginValid.getMessage();
            loginField.requestFocus();
        } else if(!passValid.isValid()) {
            errMess = passValid.getMessage();
            passwordField.requestFocus();
        }

        if(errMess != null) {
            Toast.makeText(LoginActivity.this, errMess, Toast.LENGTH_LONG).show();
            return;
        }

        AsyncTask<String, Void, String> loginTask = new AsyncTask<String, Void, String>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loginButton.setEnabled(false);
                authDialog.show();
            }

            @Override
            protected void onPostExecute(String errMessage) {
                super.onPostExecute(errMessage);
                loginButton.setEnabled(true);
                authDialog.dismiss();

                if(errMessage != null) {
                    Toast.makeText(LoginActivity.this, errMessage, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(LoginActivity.this, "OK", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(String... params) {

                try {
                    model.login(params[0], params[1]);
                } catch (LoginException e) {
                    e.printStackTrace();
                    return e.getMessage();
                }

                return null;
            }
        };

        loginTask.execute(login, password);
    }

}
