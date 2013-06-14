package com.blackberry.howisundergroundtoday.tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class InternetHelper {

    /**
     * It checks whether the device is connected to the internet or not
     *
     * @param context
     * @return
     */
    public static boolean isConnectedToInternet(Context context) {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (mNetworkInfo == null) {
            return false;
        }
        return mNetworkInfo.isConnected();
    }

    /**
     * Checks whether the device is on a roaming network or not (It returns false if the device is not connected to the internet)
     *
     * @param context
     * @return
     */
    public static boolean isItRoaming(Context context) {
        if (!InternetHelper.isConnectedToInternet(context)) {
            return false;
        }
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return mConnectivityManager.getActiveNetworkInfo().isRoaming();

    }

}
