package com.example.appprueba.myapplication.view.adapters;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.appprueba.myapplication.R;
import com.example.appprueba.myapplication.model.App;
import com.example.appprueba.myapplication.view.fragments.AppsFragment.OnListFragmentInteractionListener;

import java.util.ArrayList;

/**
 * {@link RecyclerView.Adapter} that can display a {@link App} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class AppsFragmentRecyclerViewAdapter extends RecyclerView.Adapter<AppsFragmentRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<App> mValues;
    private final OnListFragmentInteractionListener mListener;
    private Context context;

    public AppsFragmentRecyclerViewAdapter(Context context, ArrayList<App> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_apps, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.nombre.setText(mValues.get(position).getNombreApp());
        //holder.imagen.setI(mValues.get(position).content);
        Glide.with(context).load(mValues.get(position).getLinkImgAppResX()).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.imagen);
        //ObjectAnimator anim = ObjectAnimator.ofFloat(holder.mView, "alpha", 0f, 1f);
        //anim.setDuration(1000);
        //anim.start();

        ValueAnimator animator = ValueAnimator.ofFloat(0, 360);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                // 2
                holder.mView.setRotation(value);
            }
        });

        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(1000);
        animator.start();

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView imagen;
        public final TextView nombre;
        public App mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            imagen = (ImageView) view.findViewById(R.id.img_itemapp);
            nombre = (TextView) view.findViewById(R.id.name_itemapp);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + nombre.getText() + "'";
        }
    }
}
