package com.example.appprueba.myapplication.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.example.appprueba.myapplication.MainMVP;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Dario Mauricio Ossa Arias on 1/02/2017.
 * dario.ossa.a@gmail.com
 */

public class MainModel implements MainMVP.ModelOps {

    public final static String PREFS_NAME = "com.example.appprueba.myapplication.prefs";
    public final static String APPS_KEY = "com.example.appprueba.myapplication.prefs";
    private static final String TAG = MainModel.class.getSimpleName();

    /**
     * Descarga json con la descripcion de las apps desde itunes y devuelve las apps
     * @return ArrayList de App con las recientemente descargadas o null si no fue posible
     */
    public void obtenerAppsDescargadas(Context context, AppsDataCallback appsDataCallback){
        downloadApps(context, appsDataCallback);
    }

    /**
     * guarda en la BD interna el json con las apps
     * @param context Context android
     * @param apps todas las apps ArrayList<App>
     */
    public void guardarApps(Context context, ArrayList<App> apps){
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = gson.toJson(apps, new TypeToken<ArrayList<App>>(){}.getType());
        prefs.edit().putString(APPS_KEY, json).apply();
    }

    /**
     * Consulta la BD interna por el json y devuelve todas las apps guardadas
     * @param context Context de android
     * @return ArrayList de App con las ultimas que se hayan guardado o null si no hay
     */
    public ArrayList<App> obtenerAppsGuardadas(Context context){
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(APPS_KEY,"");
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<App>>() {}.getType();
        return gson.fromJson(json,type);
    }

    /**
     * Descarga las apps y las devuelve
     * @param context
     * @param callback
     */
    private void downloadApps(final Context context, final AppsDataCallback callback) {
        new AsyncTask<Void, Void, String>(){

            int responseCode;

            @Override
            protected String doInBackground(Void... params) {
                //Log.d(TAG, restClient.downloadApps().toString());
                Log.d(TAG, "updateApps1");
                try {
                    URL url = new URL("https://itunes.apple.com/us/rss/topfreeapplications/limit=20/json");
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setReadTimeout(15*1000);

                    Log.d(TAG, "updateApps2");
                    responseCode = urlConnection.getResponseCode();
                    Log.d(TAG, "rc: "+responseCode);
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        return readStream(urlConnection.getInputStream());
                    } else {
                        return "HttpURLConnection error code: " + responseCode;
                    }
                }catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    callback.onSuccess(App.getAllApps( s, context));
                } else {
                    callback.onError(s);
                }
            }
        }.execute();
    }

    /**
     * Lee un input stream para devolverlo como String
     * @param in
     * @return
     */
    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }
}
