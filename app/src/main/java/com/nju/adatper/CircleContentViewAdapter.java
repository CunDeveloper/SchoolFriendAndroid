package com.nju.adatper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import model.Content;

/**
 * Created by cun on 2015/12/27.
 */
public class CircleContentViewAdapter  extends FragmentStatePagerAdapter {
    private ArrayList<Content> mContents;
    public CircleContentViewAdapter(FragmentManager fm,ArrayList<Content> contents) {
        super(fm);
        mContents = contents;
    }

    @Override
    public Fragment getItem(int position) {
        return null;
    }

    @Override
    public int getCount() {
        int counnt = 0;

        return 0;
    }

}
