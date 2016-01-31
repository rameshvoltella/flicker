package com.ua.max.oliynick.flicker.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.XMPPConnection;

import java.util.Observer;

/**
 * Created by Максим on 01.02.2016.
 */
public class ConnectionUtils extends BroadcastReceiver implements ConnectionListener {

    private final BooleanObservable connectedProp = new BooleanObservable(false);

    public ConnectionUtils(){}

    public boolean isConnected() {
        return connectedProp.getValue();
    }

    public void addConnectedPropertyListener(Observer observer) {
        connectedProp.addObserver(observer);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isConnected = !intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
        connectedProp.setValue(isConnected);
    }

    @Override
    public void connected(XMPPConnection connection) {

    }

    @Override
    public void authenticated(XMPPConnection connection, boolean resumed) {

    }

    @Override
    public void connectionClosed() {

    }

    @Override
    public void connectionClosedOnError(Exception e) {

    }

    @Override
    public void reconnectionSuccessful() {

    }

    @Override
    public void reconnectingIn(int seconds) {

    }

    @Override
    public void reconnectionFailed(Exception e) {

    }
}
