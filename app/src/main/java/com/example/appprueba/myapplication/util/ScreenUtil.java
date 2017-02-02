package com.example.appprueba.myapplication.util;

import android.content.Context;
import android.content.res.Configuration;

/**
 * Created by Dario Mauricio Ossa Arias on 1/02/2017.
 * dario.ossa.a@gmail.com
 */

public class ScreenUtil {
    /**
     * Indica si el dispositivo es tablet o no
     * @param context
     * @return
     */
    public static boolean isDeviceATablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
}
