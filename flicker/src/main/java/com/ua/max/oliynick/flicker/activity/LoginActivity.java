package com.ua.max.oliynick.flicker.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.inject.Inject;
import com.ua.max.oliynick.flicker.XMPPTCPConnectionHolder;
import com.ua.max.oliynick.flicker.interfaces.ILoginController;
import com.ua.max.oliynick.flicker.model.LoginModel;
import com.ua.max.oliynick.flicker.util.Settings;

import oliynick.max.ua.com.flicker.R;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.login)
public class LoginActivity extends RoboActivity implements ILoginController {

    @Inject
    private LoginModel model;

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
        try {
            XMPPTCPConnectionHolder.initInstance(Settings.getInstance());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Init", e.toString());
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder.setTitle(R.string.error).setMessage(R.string.connection_error).
                    setPositiveButton(R.string.ok, null);

            dialogBuilder.create().show();
            // show exception dialog
        }

    }

    @Override
    public void onLogin(View v) {

        /*Handler h = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0) {
                    updateUI();
                } else if(msg){
                    showErrorDialog();
                }
            }
        };*/

        loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        //progressDialog.getWindow().setGravity(Gravity.CENTER_VERTICAL);
        progressDialog.show();

        /*new android.os.Handler().post(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(9000);
                    model.login(loginField.getText().toString(), passwordField.getText().toString(), false);
                    Log.i("LoginActivity", "Successful login");
                } catch (LoginException e) {
                    e.printStackTrace();
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getApplicationContext());
                    dialogBuilder.setTitle(R.string.error).setMessage(e.getMessage()).
                            setPositiveButton(R.string.ok, null);

                    dialogBuilder.create().show();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    progressDialog.dismiss();
                    loginButton.setEnabled(true);
                }
            }
        });*/


    }

    @Override
    public void onChangeSaveCredentials(View v) {

    }

    private void invalidate() {

    }
}
