package com.ua.max.oliynick.flicker.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by Максим on 01.02.2016.
 */
public class NetworkUtils extends BroadcastReceiver {

    private BooleanObservable connectionProperty = new BooleanObservable(false);

    public static boolean hasInternetAccess(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        boolean isConnected = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
        connectionProperty.setValue(isConnected);

        if(isConnected){
            Toast.makeText(context, "Internet Connection Lost", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(context, "Internet Connected", Toast.LENGTH_LONG).show();
        }
    }
}
