package com.nju.adatper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import com.nju.fragment.AlumniVoiceItemFragment;

import java.util.ArrayList;

/**
 * Created by xiaojuzhang on 2016/3/14.
 */
public class AlumniVoiceViewPage extends FragmentStatePagerAdapter {

    private static String[] mTypes;
    public AlumniVoiceViewPage(FragmentManager fm, String[] types){
        super(fm);
        mTypes = types;

    }
    @Override
    public Fragment getItem(int i) {
        return AlumniVoiceItemFragment.newInstance(mTypes[i]);
    }

    @Override
    public int getCount() {
        return mTypes.length;
    }
}