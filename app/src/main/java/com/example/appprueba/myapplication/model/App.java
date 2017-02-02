package com.example.appprueba.myapplication.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by Dario Mauricio Ossa Arias on 30/01/2017.
 * dario.ossa.a@gmail.com
 */

public class App implements Parcelable, Comparable<App> {

    private static final String TAG = App.class.getSimpleName();
    private String nombreApp;
    private String linkImgAppResX;
    private String descripcion;
    private double precio;
    private String moneda;
    private String descripcionDerechosDeAutor;
    private String nombreSubYEmpresa;
    private String linkAppITunes;
    private String nombreEmpresa;
    private String linkEmpresaiTunes;
    private String categoriaApp;
    private String fechaLanzamientoDescripcion;
    private String fechaLanzamientoDate;

    protected App(Parcel in) {
        nombreApp = in.readString();
        linkImgAppResX = in.readString();
        descripcion = in.readString();
        precio = in.readDouble();
        moneda = in.readString();
        descripcionDerechosDeAutor = in.readString();
        nombreSubYEmpresa = in.readString();
        linkAppITunes = in.readString();
        nombreEmpresa = in.readString();
        linkEmpresaiTunes = in.readString();
        categoriaApp = in.readString();
        fechaLanzamientoDescripcion = in.readString();
        fechaLanzamientoDate = in.readString();
    }

    public App(){}

    public static final Creator<App> CREATOR = new Creator<App>() {
        @Override
        public App createFromParcel(Parcel in) {
            return new App(in);
        }

        @Override
        public App[] newArray(int size) {
            return new App[size];
        }
    };

    /**
     * Retorna la lista de apps contenidos en el json que corresponde al de itunes
     * @param json objeto string recibido de itunes
     * @return las apps que contiene
     */
    public static ArrayList<App> getAllApps(String json, Context context) {
        ArrayList<App> apps = new ArrayList<>();
        try {
            JSONArray entries = new JSONObject(json).getJSONObject("feed").getJSONArray("entry");
            if (entries != null && entries.length() > 0) {
                for (int i = 0; i < entries.length(); i++) {
                    apps.add(newApp(entries.getJSONObject(i),context));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return apps;
    }

    /**
     * Retorna una instancia de la app con todos sus datos seteados a aprtir de un objeto json
     * @param insideEntry objeto jsonobject que debe corresponder a un elemento dentro de "entry" dentro del json de itunes
     * @return la app rellenada
     */
    private static App newApp (JSONObject insideEntry, Context context) {
        App app = new App();
        String attr = "attributes";
        String href = "href";
        String label = "label";
        double density = context.getResources().getDisplayMetrics().density;
        try {
            app.nombreApp = insideEntry.getJSONObject("im:name").getString("label");
            JSONArray arraysImgs = insideEntry.getJSONArray("im:image");
            if (arraysImgs != null && arraysImgs.length()>0) {
                if(density <= 1){
                    app.linkImgAppResX = arraysImgs.getJSONObject(0).getString(label);
                } else if (density < 2){
                    app.linkImgAppResX = arraysImgs.getJSONObject(1).getString(label);
                } else {
                    app.linkImgAppResX = arraysImgs.getJSONObject(2).getString(label);
                }
            }
            app.descripcion = insideEntry.getJSONObject("summary").getString(label);
            app.precio = insideEntry.getJSONObject("im:price").getJSONObject(attr).getDouble("amount");
            app.moneda = insideEntry.getJSONObject("im:price").getJSONObject(attr).getString("currency");
            app.descripcionDerechosDeAutor = insideEntry.getJSONObject("rights").getString(label);
            app.nombreSubYEmpresa = insideEntry.getJSONObject("title").getString(label);
            app.linkAppITunes = insideEntry.getJSONObject("link").getJSONObject(attr).getString(href);
            app.nombreEmpresa = insideEntry.getJSONObject("im:artist").getString(label);
            app.linkEmpresaiTunes = insideEntry.getJSONObject("im:artist").getJSONObject(attr).getString(href);
            app.categoriaApp = insideEntry.getJSONObject("category").getJSONObject(attr).getString(label);
            app.fechaLanzamientoDescripcion = insideEntry.getJSONObject("im:releaseDate").getJSONObject(attr).getString("label");
            app.fechaLanzamientoDate = insideEntry.getJSONObject("im:releaseDate").getString(label);
        } catch (JSONException e){
            e.printStackTrace();
            return null;
        }
        Log.d(TAG, "app nueva creada : " + app.printApp());
        return app;
    }

    public String printApp() {
        return nombreApp + " / " + linkImgAppResX + " / " +
        descripcion + " / " +
        precio + " / " +
        moneda + " / " +
        descripcionDerechosDeAutor + " / " +
        nombreSubYEmpresa + " / " +
        linkAppITunes + " / " +
        nombreEmpresa + " / " +
        linkEmpresaiTunes + " / " +
        categoriaApp + " / " +
        fechaLanzamientoDescripcion + " / " +
        fechaLanzamientoDate;
    }

    //getters


    public String getNombreApp() {
        return nombreApp;
    }

    public String getLinkImgAppResX() {
        return linkImgAppResX;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public String getMoneda() {
        return moneda;
    }

    public String getDescripcionDerechosDeAutor() {
        return descripcionDerechosDeAutor;
    }

    public String getNombreSubYEmpresa() {
        return nombreSubYEmpresa;
    }

    public String getLinkAppITunes() {
        return linkAppITunes;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public String getLinkEmpresaiTunes() {
        return linkEmpresaiTunes;
    }

    public String getCategoriaApp() {
        return categoriaApp;
    }

    public String getFechaLanzamientoDescripcion() {
        return fechaLanzamientoDescripcion;
    }

    public String getFechaLanzamientoDate() {
        return fechaLanzamientoDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nombreApp);
        dest.writeString(linkImgAppResX);
        dest.writeString(descripcion);
        dest.writeDouble(precio);
        dest.writeString(moneda);
        dest.writeString(descripcionDerechosDeAutor);
        dest.writeString(nombreSubYEmpresa);
        dest.writeString(linkAppITunes);
        dest.writeString(nombreEmpresa);
        dest.writeString(linkEmpresaiTunes);
        dest.writeString(categoriaApp);
        dest.writeString(fechaLanzamientoDescripcion);
        dest.writeString(fechaLanzamientoDate);
    }

    @Override
    public int compareTo(App o) {
        return this.getCategoriaApp().compareTo(o.getCategoriaApp());
    }
}
