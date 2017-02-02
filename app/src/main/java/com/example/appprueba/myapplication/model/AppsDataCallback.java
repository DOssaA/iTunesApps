package com.example.appprueba.myapplication.model;

import com.example.appprueba.myapplication.model.App;

import java.util.ArrayList;

/**
 * Clase para el retorno de datos de applicaciones o de error si ocurre uno
 * Created by Dario Mauricio Ossa Arias on 1/02/2017.
 * dario.ossa.a@gmail.com
 */

public interface AppsDataCallback {
    void onSuccess(ArrayList<App> apps);
    void onError(String error);
}
