package com.example.appprueba.myapplication.view.fragments;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.appprueba.myapplication.R;
import com.example.appprueba.myapplication.model.App;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

/**
 * Modal para mostrar la información de una app
 * Created by Dario Mauricio Ossa Arias on 1/02/2017.
 * dario.ossa.a@gmail.com
 */

public class AppDetailDialogFragment extends DialogFragment {

    private static final String ARG_APP = "app_detalle";
    private static final String TAG = AppDetailDialogFragment.class.getSimpleName();
    private App app;

    public static AppDetailDialogFragment newInstance(App apps) {
        AppDetailDialogFragment fragment = new AppDetailDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_APP, apps);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            app = getArguments().getParcelable(ARG_APP);
        }
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog d = new Dialog(getActivity(), R.style.MyCustomTheme);
        d.requestWindowFeature(STYLE_NO_TITLE);
        d.setContentView(R.layout.dialog_appdetail);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        Glide.with(this).load(app.getLinkImgAppResX()).into(((ImageView)d.findViewById(R.id.dialog_imagen)));
        ((TextView)d.findViewById(R.id.dialog_nombre)).setText(app.getNombreApp());
        ((TextView)d.findViewById(R.id.dialog_empresa)).setText(app.getNombreEmpresa());
        ((TextView)d.findViewById(R.id.dialog_descripcion)).setText(app.getDescripcion());
        ((TextView)d.findViewById(R.id.dialog_copyright)).setText(app.getDescripcionDerechosDeAutor());
        final Button cerrar = (Button)d.findViewById(R.id.dialog_close);
        ValueAnimator animator = ValueAnimator.ofFloat(0.5f, 1);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                // 2
                cerrar.setScaleX(value);
                cerrar.setScaleY(value);
                cerrar.setRotation(value);
            }
        });
        ObjectAnimator a = ObjectAnimator.ofInt(cerrar,"alpha",0,1);
        a.setDuration(1000);
        a.setInterpolator(new AccelerateDecelerateInterpolator());

        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(1000);
        //animator.start();
        AnimatorSet s = new AnimatorSet();
        s.playTogether(a,animator);
        s.start();

        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
                Log.i(TAG, "click a cierre");
                d.onBackPressed();
            }
        });
        return d;
    }
}
