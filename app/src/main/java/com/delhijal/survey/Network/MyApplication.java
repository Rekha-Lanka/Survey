package com.delhijal.survey.Network;

import android.app.Application;

/**
 * Created by dell on 2/14/2018.
 */

public class MyApplication extends Application {
    // Gloabl declaration of variable to use in whole app

    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}
