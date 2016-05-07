package com.nju.image;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.nju.http.HttpManager;
import com.nju.util.Divice;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutionException;

/**
 * Created by xiaojuzhang on 2016/1/5.
 */
public class BitmapDownloader {
    private static BitmapDownloader mDownloader;
    private Context mContext;
    private Bitmap mPlaceHolderBitmap;
    private String mUrl;
    private int mReqWidth;
    private int mReqHeight;
    private WeakReference<ImageView> mImageViewWeakReference;


    private BitmapDownloader(Context context) {
        mContext = context;
        mReqWidth = Divice.getDisplayWidth(context);
        mReqHeight = mReqWidth;
    }

    public static BitmapDownloader with(Context context) {
        mDownloader = new BitmapDownloader(context);
        return mDownloader;
    }

    public static boolean cancelPotentialWork(String url, ImageView imageView) {
        final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

        if (bitmapWorkerTask != null) {
            final String bitmapUrl = bitmapWorkerTask.getUrl();
            if (bitmapUrl.equals("") || !bitmapUrl.equals(url)) {
                bitmapWorkerTask.cancel(true);
            } else {
                return false;
            }
        }
        return true;
    }

    private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        return null;
    }

    public BitmapDownloader url(String url) {
        mUrl = url;
        return mDownloader;
    }

    public BitmapDownloader placeHolder(Bitmap bitmap) {
        mPlaceHolderBitmap = bitmap;
        return mDownloader;
    }

    public BitmapDownloader resize(final int targetWidth, final int targetHeight) {
        mReqWidth = targetWidth;
        mReqHeight = targetHeight;
        return mDownloader;
    }

    public BitmapDownloader into(ImageView imageView) {
        mImageViewWeakReference = new WeakReference<>(imageView);
        exeTask();
        return mDownloader;
    }

    private void exeTask() {
        if (mImageViewWeakReference != null) {
            final ImageView imageView = mImageViewWeakReference.get();
            if (cancelPotentialWork(mUrl, imageView) && imageView != null) {
                BitmapWorkerTask.LoadCallable callable = new BitmapWorkerTask.LoadCallable(mUrl, mReqWidth, mReqHeight);
                BitmapWorkerTask task = new BitmapWorkerTask(callable);
                final AsyncDrawable asyncDrawable = new AsyncDrawable(mContext.getResources(), mPlaceHolderBitmap, task);
                imageView.setImageDrawable(asyncDrawable);
                HttpManager.getInstance().addDownLoadQueue(task);
                try {
                    Bitmap bitmap = task.get();
                    final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
                    if (task == bitmapWorkerTask && bitmap != null) {
                        imageView.setImageBitmap(bitmap);
                    }

                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class AsyncDrawable extends BitmapDrawable {
        private final WeakReference<BitmapWorkerTask> mBitmapWorkerTaskReference;

        public AsyncDrawable(Resources res, Bitmap bitmap, BitmapWorkerTask bitmapWorkerTask) {
            super(res, bitmap);
            mBitmapWorkerTaskReference = new WeakReference<>(bitmapWorkerTask);
        }

        public BitmapWorkerTask getBitmapWorkerTask() {
            return mBitmapWorkerTaskReference.get();
        }
    }

}

