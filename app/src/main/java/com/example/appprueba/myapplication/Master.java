package com.example.appprueba.myapplication;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.HashMap;

/**
 * Created by Dario Mauricio Ossa Arias on 1/02/2017.
 * dario.ossa.a@gmail.com
 * basado en: http://www.tinmegali.com/en/model-view-presenter-mvp-in-android-part-2/
 */

public class Master extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    private HashMap<String, Object> mData = new HashMap<>();
    /**
     * Insert objects
     * @param key   reference TAG
     * @param obj   Object to save
     */
    public void put(String key, Object obj) {
        mData.put(key, obj);
    }

    /**
     * Insert obj using class name as TAG
     * @param object    obj to save
     */
    public void put(Object object) {
        put(object.getClass().getName(), object);
    }

    /**
     * Recover obj
     * @param key   reference TAG
     * @param <T>   Class
     * @return      Obj saved
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return (T) mData.get(key);
    }
}
