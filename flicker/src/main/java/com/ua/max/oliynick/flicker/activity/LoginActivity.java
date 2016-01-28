package com.ua.max.oliynick.flicker.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ua.max.oliynick.flicker.error.LoginException;
import com.ua.max.oliynick.flicker.interfaces.ILoginController;
import com.ua.max.oliynick.flicker.interfaces.ILoginModel;
import com.ua.max.oliynick.flicker.model.LoginModel;
import com.ua.max.oliynick.flicker.util.Settings;
import com.ua.max.oliynick.flicker.util.XMPPTCPConnectionHolder;

import oliynick.max.ua.com.flicker.R;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.login)
public class LoginActivity extends RoboActivity implements ILoginController {

    //@Inject
    private ILoginModel model = new LoginModel();

    @InjectView(R.id.loginField)
    private EditText loginField;

    @InjectView(R.id.passwordField)
    private EditText passwordField;

    @InjectView(R.id.sign_in)
    private Button loginButton;

    public LoginActivity() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // FIXME!
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //Initializing settings with app context
        Settings.initInstance(this);
        //Initializing facebook sdk with app context
        //FacebookSdk.sdkInitialize(this);
        //Initializing xmpp connection to server

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    XMPPTCPConnectionHolder.initInstance(Settings.getInstance());
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("Init", e.toString());
                    /*AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                    dialogBuilder.setTitle(R.string.error).setMessage(R.string.connection_error).
                            setPositiveButton(R.string.ok, null);

                    dialogBuilder.create().show();*/
                    // show exception dialog
                }
            }
        }).run();



    }

    @Override
    public void onLogin(View v) {

        final String authMessage = getResources().getString(R.string.auth_bar_mess);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(authMessage);

        AsyncTask<String, Void, String> loginTask = new AsyncTask<String, Void, String>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loginButton.setEnabled(false);
                progressDialog.show();
            }

            @Override
            protected void onPostExecute(String errMessage) {
                super.onPostExecute(errMessage);
                loginButton.setEnabled(true);
                progressDialog.dismiss();

                if(errMessage != null) {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LoginActivity.this);
                    dialogBuilder.setTitle(R.string.error).setMessage(errMessage).
                            setPositiveButton(R.string.ok, null);

                    dialogBuilder.create().show();
                }
            }

            @Override
            protected String doInBackground(String... params) {

                try {
                    model.login(params[0], params[1], false);
                } catch (LoginException e) {
                    e.printStackTrace();
                    return e.getMessage();
                }

                return null;
            }
        };

        loginTask.execute(loginField.getText().toString(), passwordField.getText().toString());

    }

    @Override
    public void onChangeSaveCredentials(View v) {

    }

}
