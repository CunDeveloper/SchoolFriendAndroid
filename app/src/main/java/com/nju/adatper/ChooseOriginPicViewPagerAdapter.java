package com.nju.adatper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;

import com.nju.fragment.ChoosedOriginPicViewPageItemFragment;
import com.nju.model.ImageWrapper;

import java.util.ArrayList;

/**
 * Created by xiaojuzhang on 2015/11/27.
 */
public class ChooseOriginPicViewPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<ImageWrapper> mImgPaths;
    public ChooseOriginPicViewPagerAdapter(FragmentManager fm, ArrayList<ImageWrapper> imgPaths) {
        super(fm);
        mImgPaths = imgPaths;

    }

    @Override
    public Fragment getItem(int position) {
        return ChoosedOriginPicViewPageItemFragment.newInstance(mImgPaths.get(position).getPath());
    }

    @Override
    public Object instantiateItem(View collection, final int pos) {
        return null;
    }
    @Override
    public int getCount() {
        return mImgPaths.size();
    }

}
