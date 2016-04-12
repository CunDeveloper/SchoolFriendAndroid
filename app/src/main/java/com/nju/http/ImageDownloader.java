package com.nju.http;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.nju.activity.BitmapEvent;
import com.nju.activity.MessageEvent;
import com.nju.http.request.RequestImage;
import com.nju.image.CacheUtil;
import com.nju.image.ImageUtil;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

/**
 * Created by cun on 2016/3/31.
 */
public class ImageDownloader {
    private static final String TAG = ImageDownloader.class.getSimpleName();
    private static CacheUtil cacheUtil = CacheUtil.getInstance();

    public static  BitmapDownloaderTask download(String url,ImageView imageView) {
        if (cancelPotentialDownload(url, imageView)) {
            Bitmap bitmap;
            if ((bitmap=cacheUtil.getBitmapFromMemCache(url))!= null){
                imageView.setImageBitmap(bitmap);
            } else {
                BitmapDownloaderTask task = new BitmapDownloaderTask(imageView);
                DownloadedDrawable downloadedDrawable = new DownloadedDrawable(task);
                imageView.setImageDrawable(downloadedDrawable);
                task.execute(url);
                return task;
            }
        }
        return null;
    }

    private static BitmapDownloaderTask getBitmapDownloaderTask(ImageView imageView) {
        if (imageView != null) {
            Drawable drawable = imageView.getDrawable();
            if (drawable instanceof DownloadedDrawable) {
                DownloadedDrawable downloadedDrawable = (DownloadedDrawable)drawable;
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

   public   static class BitmapDownloaderTask extends AsyncTask<String,Void,Bitmap>{
        private String url;
        private final WeakReference<ImageView> imageViewReference;

        public BitmapDownloaderTask(ImageView imageView ) {
            imageViewReference = new WeakReference<>(imageView);
        }

//       Callback callback = new Callback() {
//           @Override
//           public void onFailure(Request request, IOException e) {
//
//           }
//
//           @Override
//           public void onResponse(Response response) throws IOException {
//               InputStream inputStream = response.body().byteStream();
//               Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//               CacheUtil.getInstance().addBitmapToMemoryCache(url,bitmap);
//               ImageView imageView = imageViewReference.get();
//               BitmapDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);
//               //Change bitmap only if this process is still associated with it
//              if (BitmapDownloaderTask.this == bitmapDownloaderTask) {
//                imageView.setImageBitmap(bitmap);
//              }
//           }
//       };
//       RequestImage requestImage = new RequestImage(mUrl,callback);
//       public void exe(){
//           HttpManager.getInstance().exeRequest(requestImage);
//       }

        @Override
        // Actual download method, run in the task thread
        protected Bitmap doInBackground(String... params) {
            try {
                InputStream stream = SchoolFriendHttp.getInstance().SynGetStream(params[0]);
                Bitmap bitmap = BitmapFactory.decodeStream(stream);
                CacheUtil.getInstance().addBitmapToMemoryCache(params[0],bitmap);
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
            BitmapDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);
            // Change bitmap only if this process is still associated with it
            if (this == bitmapDownloaderTask) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }
}
