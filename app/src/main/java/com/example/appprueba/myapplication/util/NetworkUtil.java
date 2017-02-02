package com.example.appprueba.myapplication.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Dario Mauricio Ossa Arias on 1/02/2017.
 * dario.ossa.a@gmail.com
 */

public class NetworkUtil {

    /**
     * Indica si hay conexion a internet
     * @param context
     * @return
     */
    public static boolean isConnectedToInternet (Context context) {
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }
}
