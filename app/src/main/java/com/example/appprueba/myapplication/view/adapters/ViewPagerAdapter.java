package com.example.appprueba.myapplication.view.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Dario Mauricio Ossa Arias on 1/02/2017.
 * dario.ossa.a@gmail.com
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private Fragment fragments[];
    private String titles[];

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setFragments(Fragment[] fragments) {
        this.fragments = fragments;
    }

    public void setTitles(String[] titles) {
        this.titles = titles;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }
}
