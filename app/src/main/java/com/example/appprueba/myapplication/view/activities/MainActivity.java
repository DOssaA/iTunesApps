package com.example.appprueba.myapplication.view.activities;

import android.content.pm.ActivityInfo;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.appprueba.myapplication.MainMVP;
import com.example.appprueba.myapplication.R;
import com.example.appprueba.myapplication.StateMaintainer;
import com.example.appprueba.myapplication.model.App;
import com.example.appprueba.myapplication.presenter.Presenter;
import com.example.appprueba.myapplication.view.adapters.ViewPagerAdapter;
import com.example.appprueba.myapplication.view.fragments.AppDetailDialogFragment;
import com.example.appprueba.myapplication.view.fragments.AppsFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements MainMVP.ViewOps, AppsFragment.OnListFragmentInteractionListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private MainMVP.PresenterOps presenter;

    private final StateMaintainer mStateMaintainer =
            new StateMaintainer( this.getSupportFragmentManager(), TAG ); //utilizado para mantener el estado del presentador (no nulo o sin valores nulos) a lo largo de los ciclos de vida de la app

    @ViewById
    ViewPager main_viewpager;

    @ViewById
    TabLayout main_tablayout;

    @AfterViews
    void reportarListo(){
        startMVPOps();
        presenter.onCreated();
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    /**
     *  configura las vistas de la actividad
     * @param fragments
     * @param titles
     */
    @Override
    public void cargarVistas(Fragment[] fragments, String[] titles) {
        Log.d(TAG, "cargarVistas");
        final ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.setFragments(fragments);
        viewPagerAdapter.setTitles(titles);
        main_viewpager.setAdapter(viewPagerAdapter);
        main_tablayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        main_tablayout.setupWithViewPager(main_viewpager);
    }

    /**
     * Usado para alertas como de desconexion de internet
     * @param msg
     */
    @Override
    public void mostrarAlerta(String msg) {
        Log.i(TAG, "mostrarAlerta");
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
    }

    /**
     * es llamado para definir la orientacion de la pantalla
     * @param enPortrait
     */
    @Override
    public void ponerOrientacionPantalla(boolean enPortrait) {
        Log.d(TAG, "ponerOrientacionPantalla");
        if (enPortrait){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    /**
     * Crea el modal utilizando un DialogFragment custom
     * @param app
     */
    @Override
    public void mostrarModalDetalle(App app) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        AppDetailDialogFragment newFragment = AppDetailDialogFragment.newInstance(app);
        ft.setCustomAnimations(R.anim.entra_por_der,R.anim.sale_por_der);
        newFragment.show(ft, "dialog");
    }

    @Override
    public void onListFragmentInteraction(App item) {
        presenter.onListItemTouch(item);
    }

    /**
     * Initialize and restart the Presenter.
     */
    public void startMVPOps() {
        try {
            if ( mStateMaintainer.firstTimeIn() ) {
                Log.d(TAG, "onCreate() called for the first time");
                initialize(this);
            } else {
                Log.d(TAG, "onCreate() called more than once");
                reinitialize(this);
            }
        } catch ( InstantiationException | IllegalAccessException e ) {
            Log.d(TAG, "onCreate() " + e );
            throw new RuntimeException( e );
        }
    }


    /**
     * Initialize relevant MVP Objects.
     */
    private void initialize( MainMVP.ViewOps view )
            throws InstantiationException, IllegalAccessException{
        presenter = new Presenter(view, getApplicationContext());
        mStateMaintainer.put(MainMVP.PresenterOps.class.getSimpleName(), presenter);
    }

    /**
     * Recovers Presenter and informs Presenter that occurred a config change.
     * If Presenter has been lost, recreates a instance
     */
    private void reinitialize( MainMVP.ViewOps view)
            throws InstantiationException, IllegalAccessException {
        presenter = mStateMaintainer.get( MainMVP.PresenterOps.class.getSimpleName() );

        if ( presenter == null ) {
            Log.w(TAG, "recreating Presenter");
            initialize( view );
        } else {
            presenter.onConfigurationChanged( view, getApplicationContext() );
        }
    }

}
