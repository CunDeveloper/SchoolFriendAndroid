package com.nju.adatper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.nju.activity.R;
import com.nju.fragment.BaseFragment;
import com.nju.fragment.MyAskFragment;
import com.nju.fragment.MyRecommendFragment;
import com.nju.fragment.MyVoiceFragment;
import com.nju.fragment.PersonCircleFragment;

/**
 * Created by cun on 2016/4/2.
 */
public class CirclePager extends FragmentStatePagerAdapter {

    private BaseFragment[] baseFragments = {
            PersonCircleFragment.newInstance(""),
            MyAskFragment.newInstance(""),
            MyVoiceFragment.newInstance(""),
            MyRecommendFragment.newInstance("")
    };

    public CirclePager(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return  baseFragments[position];
    }

    @Override
    public int getCount() {
        return 4;
    }
}
