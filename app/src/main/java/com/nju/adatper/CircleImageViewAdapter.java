package com.nju.adatper;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.nju.fragment.CircleImageViewItemFragment;

import java.util.ArrayList;

/**
 * Created by xiaojuzhang on 2015/12/11.
 */
public class CircleImageViewAdapter extends FragmentStatePagerAdapter {
    private String[] mImages;

    public CircleImageViewAdapter(FragmentManager fm,String[] images) {
        super(fm);
        mImages = images;
    }

    @Override
    public Fragment getItem(int position) {
        return CircleImageViewItemFragment.newInstance(mImages[position]);
    }

    @Override
    public int getCount() {
        return mImages.length;
    }
}
