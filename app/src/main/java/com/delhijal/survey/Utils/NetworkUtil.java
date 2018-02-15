package com.delhijal.survey.Utils;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

/**
 * Created by fit on 14-02-2018.
 */

public class NetworkUtil extends Application {

    private static NetworkUtil mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
    }

    public static synchronized NetworkUtil getInstance() {
        return mInstance;
    }

    public void setConnectionListener(NetworkChangeReceiver.ConnectionReceiverListener listener) {
        NetworkChangeReceiver.connectionReceiverListener = listener;
    }
}

