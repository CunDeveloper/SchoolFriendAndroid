package com.nju.http;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.nju.event.BitmapEvent;
import com.nju.image.CacheUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

/**
 * Created by cun on 2016/3/31.
 */
public class ImageDownloader {
    private static final String TAG = ImageDownloader.class.getSimpleName();
    private static ImageDownloader imageDownloader;
    private static CacheUtil cacheUtil;
    private Object lock = new Object();
    private Bitmap mBitmap;

    private ImageDownloader(Context context) {
        cacheUtil = CacheUtil.getInstance(context);
    }

    public static ImageDownloader with(Context context) {
        if (imageDownloader == null) {
            imageDownloader = new ImageDownloader(context);
        }
        return imageDownloader;
    }

    private static BitmapDownloaderTask getBitmapDownloaderTask(ImageView imageView) {
        if (imageView != null) {
            Drawable drawable = imageView.getDrawable();
            if (drawable instanceof DownloadedDrawable) {
                DownloadedDrawable downloadedDrawable = (DownloadedDrawable) drawable;
                return downloadedDrawable.getBitmapDownloaderTask();
            }
        }
        return null;
    }

    private static boolean cancelPotentialDownload(String url, ImageView imageView) {
        BitmapDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);
        if (bitmapDownloaderTask != null) {
            String bitmapUrl = bitmapDownloaderTask.url;
            if ((bitmapUrl == null) || (!bitmapUrl.equals(url))) {
                bitmapDownloaderTask.cancel(true);
            } else {
                // The same URL is already being downloaded.
                return false;
            }
        }
        return true;
    }

    public synchronized BitmapDownloaderTask download(String url, ImageView imageView) {
        if (cancelPotentialDownload(url, imageView)) {
            Bitmap bitmap;
            if ((bitmap = cacheUtil.getBitmapFromMemCache(url)) != null) {
                imageView.setImageBitmap(bitmap);
            } else {
                if ((bitmap = cacheUtil.getBitmapFromDiskCache(url)) != null) {
                    imageView.setImageBitmap(bitmap);
                } else {
                    BitmapDownloaderTask task = new BitmapDownloaderTask(imageView);
                    DownloadedDrawable downloadedDrawable = new DownloadedDrawable(task);
                    imageView.setImageDrawable(downloadedDrawable);
                    task.execute(url);
                    return task;
                }
            }
        }
        return null;
    }

    public synchronized ImageDownloader download(String url) {
        Bitmap bitmap;
        if ((bitmap = cacheUtil.getBitmapFromMemCache(url)) != null) {
            mBitmap = bitmap;
            notify();
            return this;
        } else {
            if ((bitmap = cacheUtil.getBitmapFromDiskCache(url)) != null) {
                mBitmap = bitmap;
                notify();
                return this;
            } else {
                BitmapDownloaderTask task = new BitmapDownloaderTask(null);
                DownloadedDrawable downloadedDrawable = new DownloadedDrawable(task);
                task.execute(url);
            }
        }
        return this;
    }

    public synchronized Bitmap bitmap() {
        while (mBitmap == null) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return mBitmap;
    }

    static class DownloadedDrawable extends ColorDrawable {
        private final WeakReference<BitmapDownloaderTask> bitmapDownloaderTaskReference;

        public DownloadedDrawable(BitmapDownloaderTask bitmapDownloaderTask) {
            super(Color.GRAY);
            bitmapDownloaderTaskReference =
                    new WeakReference<>(bitmapDownloaderTask);
        }

        public BitmapDownloaderTask getBitmapDownloaderTask() {
            return bitmapDownloaderTaskReference.get();
        }
    }

    public class BitmapDownloaderTask extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;
        private String url;

        public BitmapDownloaderTask(ImageView imageView) {
            imageViewReference = new WeakReference<>(imageView);

        }

        @Override
        // Actual download method, run in the task thread
        protected Bitmap doInBackground(String... params) {
            try {
                InputStream stream = SchoolFriendHttp.getInstance().SynGetStream(params[0]);
                Bitmap bitmap = BitmapFactory.decodeStream(stream);

                //may be leak
                cacheUtil.addBitmapToMemoryCache(params[0], bitmap);
                return bitmap;

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        // Once the image is downloaded, associates it to the imageView
        protected void onPostExecute(Bitmap bitmap) {
            ImageView imageView = imageViewReference.get();
            mBitmap = bitmap;

            if (imageView != null) {
                BitmapDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);
                // Change bitmap only if this process is still associated with it
                if (this == bitmapDownloaderTask) {
                    imageView.setImageBitmap(bitmap);
                    EventBus.getDefault().post(new BitmapEvent(bitmap));
                }
            }
        }
    }
}
