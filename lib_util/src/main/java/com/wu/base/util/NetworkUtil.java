package com.wu.base.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtil {


    public static boolean isConnectInternet(Context context) {
        if (context == null) return false;
        ConnectivityManager connectManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectManager != null) {
            NetworkInfo networkinfo = connectManager.getActiveNetworkInfo();
            return networkinfo != null && networkinfo.isAvailable();
        }
        return false;
    }

}