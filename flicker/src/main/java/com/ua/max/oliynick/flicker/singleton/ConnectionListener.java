package com.ua.max.oliynick.flicker.singleton;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.ua.max.oliynick.flicker.util.GenericObservable;
import com.ua.max.oliynick.flicker.util.GenericObserver;

import org.jivesoftware.smack.XMPPConnection;

import oliynick.max.ua.com.flicker.R;

/**
 * Created by Максим on 01.02.2016.
 */
public final class ConnectionListener extends BroadcastReceiver implements org.jivesoftware.smack.ConnectionListener {

    private final GenericObservable<Boolean> connectedProp = new GenericObservable<>(false);
    private final GenericObservable<Boolean> authentificatedProp = new GenericObservable<>(false);

    private final Context context;

    public ConnectionListener(Context context){
        this.context = context;
    }

    public boolean isConnected() {
        return connectedProp.getValue();
    }

    public void addConnectedPropertyListener(GenericObserver<Boolean> observer) {
        connectedProp.addObserver(observer);
    }

    public void removeConnectedPropertyListener(GenericObserver<Boolean> observer) {
        connectedProp.deleteObserver(observer);
    }

    public void addAuthPropertyListener(GenericObserver<Boolean> observer) {
        authentificatedProp.addObserver(observer);
    }

    public void removeAuthPropertyListener(GenericObserver<Boolean> observer) {
        authentificatedProp.deleteObserver(observer);
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

    private void runToast(final String messageText) {

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
