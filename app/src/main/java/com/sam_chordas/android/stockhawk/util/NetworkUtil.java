package com.sam_chordas.android.stockhawk.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.sam_chordas.android.stockhawk.application.EApplication;

/**
 * Created by bundee on 8/24/16.
 */
public final class NetworkUtil {

    /**
     * Check if network is connected
     * @return
     */
    public static final boolean IsConnected(){
        ConnectivityManager cm = (ConnectivityManager) EApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
