package com.nju.adatper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.nju.fragment.BaseFragment;
import com.nju.fragment.MyAskFragment;
import com.nju.fragment.MyDynamicFragment;
import com.nju.fragment.MyRecommendFragment;
import com.nju.fragment.MyVoiceFragment;

/**
 * Created by cun on 2016/4/2.
 */
public class CirclePager extends FragmentStatePagerAdapter {

    private BaseFragment[] baseFragments = {
            MyDynamicFragment.newInstance(""),
            MyAskFragment.newInstance(""),
            MyVoiceFragment.newInstance(""),
            MyRecommendFragment.newInstance("")
    };

    public CirclePager(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return baseFragments[position];
    }

    @Override
    public int getCount() {
        return 4;
    }

//    @Override
//    public int getItemPosition(Object object) {
//        return POSITION_NONE;
//    }
}
