package com.nju.adatper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.nju.fragment.RecommendWorkItem;

;

/**
 * Created by xiaojuzhang on 2016/3/14.
 */
public class RecommendWorkAdapter extends FragmentStatePagerAdapter {

    private String[] mType;

    public RecommendWorkAdapter(FragmentManager fragmentManager, String[] type) {
        super(fragmentManager);
        mType = type;
    }

    @Override
    public Fragment getItem(int i) {
        return RecommendWorkItem.newInstance(mType[i]);
    }

    @Override
    public int getCount() {
        return mType.length;
    }
}
