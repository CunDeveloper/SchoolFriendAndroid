package com.nju.image;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.nju.http.SchoolFriendHttp;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.concurrent.Callable;

/**
 * Created by xiaojuzhang on 2016/1/4.
 */
public class BitmapDownloadTask implements Runnable {
    private String mUrl;
    private final int mReqWidth;
    private final int mReqHeight;
    private final WeakReference<ImageView> mImageViewReference;

    public BitmapDownloadTask (ImageView imageView,String url,int reqWidth,int reqHeight) {
        mUrl = url;
        mReqWidth = reqWidth;
        mReqHeight = reqHeight;
        mImageViewReference = new WeakReference<>(imageView);
    }


    @Override
    public void run() {
        InputStream inputStream = null;
        try {
            inputStream = SchoolFriendHttp.getInstance().SynGetStream(mUrl);
            ImageUtil.decodeSampledBitmapFromStream(inputStream,mReqWidth,mReqHeight);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
