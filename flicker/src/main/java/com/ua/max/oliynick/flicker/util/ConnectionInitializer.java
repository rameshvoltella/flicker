package com.ua.max.oliynick.flicker.util;

import android.content.Context;
import android.os.AsyncTask;

import com.ua.max.oliynick.flicker.interfaces.IInitializible;
import com.ua.max.oliynick.flicker.singleton.ConnectionListener;
import com.ua.max.oliynick.flicker.singleton.MainApp;

import org.jivesoftware.smack.ReconnectionManager;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;

import java.util.Observable;

/**
 * Initializes and appends all connection listeners
 * Created by Максим on 01.02.2016.
 */
public class ConnectionInitializer implements IInitializible {

    private boolean isInitialized = false;

    public ConnectionInitializer() {}

    public boolean isInitialized() {
        return isInitialized;
    }

    @Override
    public void initialize(final Context context) {

        //StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
       // StrictMode.setThreadPolicy(policy);

        final ConnectionListener connectionManager = MainApp.getConnectionListener();

        final XMPPTCPConnection connection = MainApp.getConnection();
        final ReconnectionManager reconnectionManager = ReconnectionManager.getInstanceFor(connection);
        reconnectionManager.enableAutomaticReconnection();

        /*Setups reconnection manager based on saved user's settings*/
        if(MainApp.getSettings().getReconnetionPolicy() ==
                ReconnectionManager.ReconnectionPolicy.FIXED_DELAY) {

            reconnectionManager.setReconnectionPolicy(ReconnectionManager.ReconnectionPolicy.FIXED_DELAY);
            reconnectionManager.setFixedDelay(MainApp.getSettings().getReconnectionInterval());

        } else {
            reconnectionManager.setReconnectionPolicy(ReconnectionManager.ReconnectionPolicy.RANDOM_INCREASING_DELAY);
        }

        final GenericObserver<Boolean> connectionListener = new GenericObserver<Boolean>(false) {

            public GenericObserver<Boolean> selfRef() {
                return this;
            }

            @Override
            /**Will be fired each time application internet connectivity changes*/
            public void onValueChanged(Observable observable, Boolean oldValue, Boolean newValue) {

                if (newValue && !connection.isConnected()) {

                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            try {
                                // this operation may be time-consuming
                                connection.connect();
                                /*
                                after successful connection.
                                This listener should be removed
                                * */
                                MainApp.getConnectionListener().
                                        removeConnectedPropertyListener(selfRef());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return null;
                        }
                    }.execute();
                }
            }
        };

        /*Adding connection listener*/
        connection.addConnectionListener(MainApp.getConnectionListener());
        /**/
        MainApp.getConnectionListener().addConnectedPropertyListener(connectionListener);

        isInitialized = true;
    }
}
