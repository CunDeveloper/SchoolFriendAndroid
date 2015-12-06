package com.nju.adatper;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.nju.fragment.EmotionPagerFragment;

/**
 * Created by xiaojuzhang on 2015/11/18.
 */
public class EmotionPageAdater extends FragmentStatePagerAdapter {

    public EmotionPageAdater(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = EmotionPagerFragment.newInstance(position);
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
