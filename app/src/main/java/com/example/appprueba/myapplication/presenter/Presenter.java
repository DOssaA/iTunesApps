package com.example.appprueba.myapplication.presenter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.example.appprueba.myapplication.MainMVP;
import com.example.appprueba.myapplication.R;
import com.example.appprueba.myapplication.model.App;
import com.example.appprueba.myapplication.model.MainModel;
import com.example.appprueba.myapplication.model.AppsDataCallback;
import com.example.appprueba.myapplication.util.NetworkUtil;
import com.example.appprueba.myapplication.util.ScreenUtil;
import com.example.appprueba.myapplication.view.fragments.AppsFragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Dario Mauricio Ossa Arias on 31/01/2017.
 * dario.ossa.a@gmail.com
 */

public class Presenter implements MainMVP.PresenterOps{

    public static final int NUM_COLUMNAS_TABLET = 4;
    private static final String TAG = Presenter.class.getSimpleName();
    private WeakReference<MainMVP.ViewOps> vista; // referencia a la vista
    private MainMVP.ModelOps modelo; // referencia al modelo
    private Fragment[] fragments;
    private boolean isDeviceATablet;
    private Context context;
    private String[] categorias;

    /**
     * inicia el presentador y la orientacion de la pantalla
     * @param view
     * @param context
     */
    public Presenter(MainMVP.ViewOps view, Context context) {
        vista = new WeakReference<>(view);
        isDeviceATablet = ScreenUtil.isDeviceATablet(context);
        Log.i(TAG, "isDeviceATablet: "+isDeviceATablet);
        vista.get().ponerOrientacionPantalla(!isDeviceATablet);
        modelo = new MainModel();
        this.context = context;
    }

    /**
     * Usado para eliminar instancias y liberar recursos
     */
    @Override
    public void onDestroy() {
        vista = null;
        modelo = null;
    }

    /**
     * Usado para actualizar estados iniciales
     */
    @Override
    public void onCreated() {
        if(!NetworkUtil.isConnectedToInternet(context)){
            Log.i(TAG, "onCreated, sin conexion");
            vista.get().mostrarAlerta(context.getString(R.string.sinconexion));
            ArrayList<App> apps = modelo.obtenerAppsGuardadas(context);
            if (apps != null) {
                filtrarDatosVista(apps);
                vista.get().cargarVistas(fragments,categorias);
            } else {
                Log.i(TAG, "no hay apps guardadas");
            }
        } else {
            Log.i(TAG, "onCreated, con conexion");
            modelo.obtenerAppsDescargadas(context, new AppsDataCallback() {
                @Override
                public void onSuccess(ArrayList<App> apps) {
                    if (apps != null){
                        if (vista.get() != null) {
                            modelo.guardarApps(context, apps);
                            filtrarDatosVista(apps);
                            vista.get().cargarVistas(fragments, categorias);
                        }
                    } else {
                        Log.i(TAG, "no se pudo descargar las apps");
                    }
                }

                @Override
                public void onError(String error) {
                    Log.e(TAG, "Error: "+error);
                    vista.get().mostrarAlerta(context.getString(R.string.sinconexion));
                }
            });
        }
    }

    /**
     * Llamado cuando hay un cambio que recrea una vista, por lo que se vuelve a iniciar los valores o estos serian nulos
     * @param view
     * @param context
     */
    @Override
    public void onConfigurationChanged(MainMVP.ViewOps view, Context context) {
        vista = new WeakReference<>(view);
        modelo = new MainModel();
        this.context = context;
        onCreated();
    }

    /**
     * Utiliza los datos de las apps para definir las categorias y filtrar las apps por categorias
     * @param apps
     */
    private void filtrarDatosVista(ArrayList<App> apps) {
        if(apps.size()> 0 ){
            Collections.sort(apps);
            String categoria = apps.get(0).getCategoriaApp().trim();
            ArrayList<App> filtro = new ArrayList<>();
            ArrayList<ArrayList<App>> filtrosEntrega = new ArrayList<>();
            ArrayList<String> categoriasEntrega = new ArrayList<>();
            for (App app : apps) {
                //añadir cada vez que hay un cambio de categoria
                if (!categoria.equals(app.getCategoriaApp().trim())){
                    filtrosEntrega.add(new ArrayList<>(filtro));
                    categoriasEntrega.add(categoria);
                    categoria = app.getCategoriaApp().trim();
                    filtro.clear();
                }
                filtro.add(app);
                //añadir al finalizar el ciclo
                if (app.equals(apps.get(apps.size()-1))){
                    filtrosEntrega.add(new ArrayList<>(filtro));
                    categoriasEntrega.add(categoria);
                }
            }
            fragments = new Fragment[categoriasEntrega.size()];
            categorias = categoriasEntrega.toArray(new String[categoriasEntrega.size()]);
            int numColumnas = 0;
            if (isDeviceATablet){
                numColumnas = NUM_COLUMNAS_TABLET;
            }
            for (int i = 0; i < fragments.length; i++) {
                fragments[i] = AppsFragment.newInstance(filtrosEntrega.get(i),numColumnas);
            }
        }

    }

    /**
     * utilizado para mostrar mejor una aplicacion
     * @param item
     */
    public void onListItemTouch(App item) {
        vista.get().mostrarModalDetalle(item);
    }
    /*public void activityCreated(){
        if ScreenUtil.isDeviceATablet(get)
    }*/


}
