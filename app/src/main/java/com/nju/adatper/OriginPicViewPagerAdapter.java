package com.nju.adatper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.nju.fragment.OriginPicsViewFragment;
import com.nju.model.Image;

import java.util.ArrayList;

/**
 * Created by xiaojuzhang on 2015/11/27.
 */
public class OriginPicViewPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<Image> mImgs;
    public OriginPicViewPagerAdapter(FragmentManager fm,ArrayList<Image> imgs) {
        super(fm);
        mImgs = imgs;

    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = OriginPicsViewFragment.newInstance(mImgs.get(position).getData());
        return fragment;
    }

    @Override
    public int getCount() {
        return mImgs.size();
    }
}
