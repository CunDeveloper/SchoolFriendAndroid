package com.nju.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;

import com.nju.activity.R;

import java.lang.ref.WeakReference;

/**
 * Created by xiaojuzhang on 2016/1/4.
 */
public class DownloadedDrawable extends BitmapDrawable {

    private final WeakReference<BitmapDownloadTask> mBitmapDownloadTaskReference;


    public DownloadedDrawable(Context context,Bitmap defaultBitmap,BitmapDownloadTask bitmapDownloadTask) {
        super(context.getResources(),defaultBitmap);
        mBitmapDownloadTaskReference = new WeakReference<>(bitmapDownloadTask);

    }

    public BitmapDownloadTask getBitmapDownloaderTask() {
        return mBitmapDownloadTaskReference.get();
    }


}
