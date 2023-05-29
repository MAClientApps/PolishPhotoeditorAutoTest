package com.autotest.photo.editor.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;



//this class will get the current network state if it's wifi , data ,roaming, or not connected
public class NetworkHelper {


    private static NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    //is connected to internet regardless wifi or data
    public static boolean isConnected(Context context) {
        NetworkInfo info = getNetworkInfo(context.getApplicationContext());
        return (info != null && info.isConnected());
    }


    //is connected via wifi
    public static boolean isConnectedWifi(Context context) {
        NetworkInfo info = getNetworkInfo(context.getApplicationContext());
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI);
    }

    //is connected via mobile data
    public static boolean isConnectedMobile(Context context) {
        NetworkInfo info = getNetworkInfo(context.getApplicationContext());
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_MOBILE);
    }

    private static boolean isRoaming(Context context) {
        NetworkInfo info = getNetworkInfo(context.getApplicationContext());
        return info.isRoaming();
    }




}
