package com.nju.util;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.nju.fragment.BaseFragment;
import com.nju.fragment.PublishTextWithPicsFragment;
import com.nju.model.Image;
import com.nju.model.ImageWrapper;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by xiaojuzhang on 2015/12/28.
 */
public class AsyncCompress extends AsyncTask<ArrayList<Image>,Void,ArrayList<ImageWrapper>> {
    private BaseFragment mFragment;
    private int  mWidth;

    public AsyncCompress(BaseFragment fragment) {
        mFragment = fragment;
        mWidth = Divice.getDisplayWidth(mFragment.getContext())/3;
    }

    @Override
    protected ArrayList<ImageWrapper> doInBackground(ArrayList<Image>... params) {
        ArrayList<Image> images = params[0];
        ArrayList<ImageWrapper> imageWrappers = new ArrayList<>();
        ImageWrapper imageWrapper;
        for (Image image:images) {
            imageWrapper = new ImageWrapper();
            try {
                Bitmap bitmap = Picasso.with(mFragment.getContext()).load(new File(image.getData())).resize(mWidth,mWidth).get();
                imageWrapper.setBitmap(bitmap);
                imageWrapper.setPath(image.getData());
                imageWrappers.add(imageWrapper);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return imageWrappers;
    }

    @Override
    protected void onPostExecute(ArrayList<ImageWrapper> imageWrappers) {
        super.onPostExecute(imageWrappers);
        mFragment.getHostActivity().open(PublishTextWithPicsFragment.newInstance(imageWrappers));
    }
}
