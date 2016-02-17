package com.ua.max.oliynick.flicker.util;

import android.content.Context;

import com.ua.max.oliynick.flicker.interfaces.IInitializible;

import org.jivesoftware.smack.ReconnectionManager;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;

import java.util.Observable;

/**
 * Created by Максим on 01.02.2016.
 */
public class AppInitializer implements IInitializible {

    private boolean isInitialized = false;

    public AppInitializer() {}

    public boolean isInitialized() {
        return isInitialized;
    }

    @Override
    public void initialize(Context context) {

        if(isInitialized) return;

        //StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
       // StrictMode.setThreadPolicy(policy);

        //Initializing settings with app context
        Settings.initInstance(context);
        ConnectionManager.initInstance(context);

        final ConnectionManager connectionManager = ConnectionManager.getInstance();
        final GenericObserver<Boolean> connectionListener = new GenericObserver<Boolean>(false) {

            @Override
            public void onValueChanged(Observable observable, Boolean oldValue, Boolean newValue) {

                if (newValue && !XMPPTCPConnectionHolder.isInit()) {

                    final Thread initThread = new Thread(new Runnable() {
                        @Override
                        public void run() {

                            try {

                                XMPPTCPConnectionHolder.initInstance(Settings.getInstance());
                                final XMPPTCPConnection connection = XMPPTCPConnectionHolder.getInstance();
                                final ReconnectionManager reconnectionManager = ReconnectionManager.getInstanceFor(connection);
                                reconnectionManager.enableAutomaticReconnection();

                                if(Settings.getInstance().getReconnetionPolicy() ==
                                        ReconnectionManager.ReconnectionPolicy.FIXED_DELAY) {

                                    reconnectionManager.setReconnectionPolicy(ReconnectionManager.ReconnectionPolicy.FIXED_DELAY);
                                    reconnectionManager.setFixedDelay(Settings.getInstance().getReconnectionInterval());

                                } else {
                                    reconnectionManager.setReconnectionPolicy(ReconnectionManager.ReconnectionPolicy.RANDOM_INCREASING_DELAY);
                                }

                                connection.addConnectionListener(ConnectionManager.getInstance());
                                connection.connect();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    initThread.setDaemon(true);
                    initThread.run();
                }
            }
        };

        //Initializing xmpp connection to server
        ConnectionManager.getInstance().addConnectedPropertyListener(connectionListener);
        isInitialized = true;

    }
}
