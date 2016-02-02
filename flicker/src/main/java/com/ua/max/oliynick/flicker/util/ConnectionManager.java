package com.ua.max.oliynick.flicker.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.XMPPConnection;

import oliynick.max.ua.com.flicker.R;

/**
 * Created by Максим on 01.02.2016.
 */
public class ConnectionManager extends BroadcastReceiver implements ConnectionListener {

    private final BooleanObservable connectedProp = new BooleanObservable(false);
    private final BooleanObservable authentificatedProp = new BooleanObservable(false);

    private static Context context = null;
    private static ConnectionManager instance = null;

    private ConnectionManager(){}

    public static void initInstance(Context context) {
        ConnectionManager.context = context;
    }

    public static synchronized ConnectionManager getInstance() {
        if(instance == null) instance = new ConnectionManager();

        return instance;
    }

    public boolean isConnected() {
        return connectedProp.getValue();
    }

    public void addConnectedPropertyListener(GenericObserver<Boolean> observer) {
        connectedProp.addObserver(observer.getObserver());
    }

    public void removeConnectedPropertyListener(GenericObserver<Boolean> observer) {
        connectedProp.deleteObserver(observer.getObserver());
    }

    public void addAuthPropertyListener(GenericObserver<Boolean> observer) {
        authentificatedProp.addObserver(observer.getObserver());
    }

    public void removeAuthPropertyListener(GenericObserver<Boolean> observer) {
        authentificatedProp.deleteObserver(observer.getObserver());
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        final boolean isConnected = !intent.
                getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);

        if(connectedProp.getValue() != isConnected) {
            connectedProp.setValue(isConnected);

            if(!isConnected) {
                runToast(context.getString(R.string.no_internet_conn));
                //Toast.makeText(context, context.getString(R.string.no_internet_conn), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void connected(XMPPConnection connection) {

        Log.d("connected", "connected");

        if(connectedProp.getValue() != true) {
            connectedProp.setValue(true);
        }
    }

    @Override
    public void authenticated(XMPPConnection connection, boolean resumed) {

        Log.d("authentificated", "authentificated " + resumed);

        if(!resumed) {
            authentificatedProp.setValue(true);
        }
    }

    @Override
    public void connectionClosed() {}

    @Override
    public void connectionClosedOnError(Exception e) {
        Log.d("reconnecting", "Connection closed on error " + e.toString());
        if(connectedProp.getValue()) {
            //Toast.makeText(context, context.getString(R.string.lost_conn), Toast.LENGTH_LONG).show();
            runToast(context.getString(R.string.lost_conn));
        }
    }

    @Override
    public void reconnectionSuccessful() {
        Log.d("reconnecting", "Successful reconnection");
        //Toast.makeText(context, context.getString(R.string.succ_rec), Toast.LENGTH_LONG).show();
        runToast(context.getString(R.string.succ_rec));
    }

    @Override
    public void reconnectingIn(int seconds) {
        Log.d("reconnecting", "Reconnecting in " + seconds + " seconds");
    }

    @Override
    public void reconnectionFailed(Exception e) {

        Log.d("reconnecting", "Reconnection failed due to " + e.toString());

        if(connectedProp.getValue()) {
            //Toast.makeText(context, context.getString(R.string.rec_err), Toast.LENGTH_SHORT).show();


            runToast(context.getString(R.string.rec_err));
        }
    }

    private static void runToast(final String messageText) {

        final Handler handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                Toast.makeText(context, (String) msg.obj, Toast.LENGTH_LONG).show();
            }
        };

        Message message = new Message();
        message.obj = messageText;
        handler.sendMessage(message);
    }
}
