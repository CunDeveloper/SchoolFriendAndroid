package com.nju.image;

import android.graphics.Bitmap;

import com.nju.http.SchoolFriendHttp;

import java.io.InputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * Created by xiaojuzhang on 2016/1/5.
 */
public class BitmapWorkerTask extends FutureTask<Bitmap> {

    private static String mUrl ="";
    public BitmapWorkerTask(Callable<Bitmap> callable) {
        super(callable);
    }

    public static class LoadCallable implements Callable<Bitmap> {
        private final int mTargetWidth;
        private final int mTargetHeight;
        public LoadCallable(String url,int targetWidth,int targetHeight) {
            mUrl = url;
            mTargetWidth = targetWidth;
            mTargetHeight = targetHeight;
        }
        @Override
        public Bitmap call() throws Exception {
            InputStream inputStream = SchoolFriendHttp.getInstance().SynGetStream(mUrl);
            return ImageUtil.decodeSampledBitmapFromStream(inputStream,mTargetWidth,mTargetHeight);
        }
    }

    public String getUrl() {
        return mUrl;
    }

}
