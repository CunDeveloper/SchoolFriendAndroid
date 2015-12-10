package com.nju.adatper;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.nju.fragment.EmotionPagerFragment;

/**
 * Created by xiaojuzhang on 2015/11/18.
 */
public class EmotionPageAdater extends FragmentStatePagerAdapter {

    private static String TAG;
    public EmotionPageAdater(FragmentManager fm,String tag) {
        super(fm);
        TAG = tag;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = EmotionPagerFragment.newInstance(position,TAG);
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
