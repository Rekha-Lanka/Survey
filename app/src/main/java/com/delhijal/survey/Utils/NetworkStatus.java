package com.delhijal.survey.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by fit on 09-02-2018.
 */

public class NetworkStatus {
    private boolean isConnected = false;
    public boolean isNetworkAvailable(final Context context) {
        ConnectivityManager connectivity = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        if(!isConnected){
                            Toast.makeText(context,"Connected to Internet",Toast.LENGTH_LONG).show();
                            //Log.v(LOG_TAG, "Now you are connected to Internet!");
                            //networkStatus.setText("Now you are connected to Internet!");
                            isConnected = true;
                            //do your processing here ---
                            //if you need to post any data to the server or get status
                            //update from the server
                        }
                        return true;
                    }
                }
            }
        }
        Toast.makeText(context,"No network connection",Toast.LENGTH_LONG).show();
//        Log.v(LOG_TAG, "You are not connected to Internet!");
//        networkStatus.setText("You are not connected to Internet!");
        isConnected = false;
        return false;
    }

}
