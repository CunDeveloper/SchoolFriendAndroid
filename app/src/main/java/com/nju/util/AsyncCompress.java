package com.nju.util;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.nju.activity.R;
import com.nju.fragment.AskPublishFragment;
import com.nju.fragment.BaseFragment;
import com.nju.fragment.PublishDynamicFragment;
import com.nju.fragment.PublishTextWithPicsFragment;
import com.nju.fragment.PublishVoiceFragment;
import com.nju.fragment.RecommendPublishFragment;
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
    private static int  mWidth =0;
    private static final int ROW_NUMBERS = 4;
    private static final int SPACE_NUMBERS = ROW_NUMBERS+1;
    private String mLabel;

    public AsyncCompress(BaseFragment fragment,String label) {
        mFragment = fragment;
        mLabel = label;
        if (mWidth ==0) {
            float space= mFragment.getResources().getDimension(R.dimen.phone_apadding);
            int spacePX = (int) Divice.convertDpToPixel(space,mFragment.getContext());
            mWidth = (Divice.getDisplayWidth(mFragment.getContext())-SPACE_NUMBERS*spacePX)/ROW_NUMBERS;
        }
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
        ChoosePicUtil.openFragment(mFragment,mLabel,imageWrappers);
    }
}
