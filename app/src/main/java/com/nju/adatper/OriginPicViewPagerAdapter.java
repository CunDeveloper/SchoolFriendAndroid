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

    private ArrayList<Image> mImages;

    public OriginPicViewPagerAdapter(FragmentManager fm, ArrayList<Image> images) {
        super(fm);
        mImages = images;
    }

    @Override
    public Fragment getItem(int position) {
        return OriginPicsViewFragment.newInstance(mImages.get(position).getData());
    }

    @Override
    public int getCount() {
        return mImages.size();
    }
}
