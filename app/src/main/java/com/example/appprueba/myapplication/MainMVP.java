package com.example.appprueba.myapplication;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.example.appprueba.myapplication.model.App;
import com.example.appprueba.myapplication.model.AppsDataCallback;

import java.util.ArrayList;

/** Interfaces principales para implementar el patrón MVP
 * Created by Dario Mauricio Ossa Arias on 1/02/2017.
 * dario.ossa.a@gmail.com
 */

public interface MainMVP {

    /**
     * Operaciones ofrecidas desde el modelo al presentador
     */
    interface ModelOps {
        void obtenerAppsDescargadas(Context context, AppsDataCallback appsDataCallback);
        void guardarApps(Context context, ArrayList<App> apps);
        ArrayList<App> obtenerAppsGuardadas(Context context);
    }

    /**
     * Operaciones obligatorias para la vista, usadas por el presentador
     */
    interface ViewOps {
        void cargarVistas(Fragment[] fragments, String[] titles);
        void mostrarAlerta(String msg);
        void ponerOrientacionPantalla(boolean enPortrait);
        void mostrarModalDetalle(App app);
    }

    /**
     * Operaciones ofrecidas desde el Presentador a la vista
     */
    interface PresenterOps{
        void onDestroy();
        void onCreated();
        void onConfigurationChanged(MainMVP.ViewOps view, Context context);
        void onListItemTouch(App item);
    }
}
