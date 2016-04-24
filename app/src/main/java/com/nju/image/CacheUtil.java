package com.nju.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.util.LruCache;

import com.jakewharton.disklrucache.DiskLruCache;
import com.nju.util.PathConstant;

import java.io.File;
import java.io.IOException;

import javax.security.auth.callback.CallbackHandler;


/**
 * Created by xiaojuzhang on 2016/1/5.
 */
public class CacheUtil {
    private static final String TAG = CacheUtil.class.getSimpleName();
    private static final int DISK_CACHE_SIZE = 1024 * 1024 * 10;
    private static final String DISK_CACHE_UNIQUE_NAME = "school_friend_img_dir";
    private static final int M_UNIT = 1024;
    private static CacheUtil mCacheUtil = null;
    private DiskLruImageCache diskLruImageCache;
    private LruCache<String, Bitmap> mMemoryCache;
    private final Object mDiskCacheLock = new Object();
    private boolean mDiskCacheStarting = true;
    private Context mContext;
    private CacheUtil(Context context) {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / M_UNIT);
        final int cacheSize = maxMemory / 8;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / M_UNIT;
            }
        };
        mContext = context;
        new InitDiskCacheTask().execute();
    }

    class InitDiskCacheTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            synchronized (mDiskCacheLock) {
                diskLruImageCache = new DiskLruImageCache(mContext,DISK_CACHE_UNIQUE_NAME,DISK_CACHE_SIZE);
                mDiskCacheStarting = false; // Finished initialization
                mDiskCacheLock.notifyAll(); // Wake any waiting threads
            }
            return null;
        }

    }


    public static CacheUtil getInstance(Context context) {
        if (mCacheUtil == null) {
            mCacheUtil = new CacheUtil(context);
        }
        return mCacheUtil;
    }

    public void addBitmapToMemoryCache(String url, Bitmap bitmap) {
        String key = convertKey(url);
        if (getBitmapFromMemCache(key) == null) {
            if (key != null && bitmap != null){
                mMemoryCache.put(key, bitmap);
            }
        }
        synchronized (mDiskCacheLock) {
            if (diskLruImageCache != null && diskLruImageCache.getBitmap(key) != null) {
                diskLruImageCache.put(key, bitmap);
            }
        }
    }

    public Bitmap getBitmapFromMemCache(String url) {
        String key = convertKey(url);
        return mMemoryCache.get(key);
    }

    public Bitmap getBitmapFromDiskCache(String url) {
        String key = convertKey(url);
        Log.i(TAG,key);
        synchronized (mDiskCacheLock) {
            // Wait while disk cache is started from background thread
            while (mDiskCacheStarting) {
                try {
                    mDiskCacheLock.wait();
                } catch (InterruptedException e) {}
            }
            if (diskLruImageCache != null) {
                return diskLruImageCache.getBitmap(key);
            }
        }
        return null;
    }

    private String convertKey(String url){
        String[] strs = url.split("/");
        CharSequence key = "";
        CharSequence smallLabel ="";
        for (String str:strs){
            if (str.equals(PathConstant.SMALL)){
                smallLabel = str;
                break;
            }
        }
        if (strs.length >1){
            String lastKey = smallLabel.toString() + strs[strs.length-1];
            if (lastKey.length()>64){
                key = lastKey.substring(0,63);
            }
            else {
                key = lastKey;
            }
            Log.i(TAG,lastKey);
        }
       return key.toString().split("\\.")[0].toLowerCase();
    }

}
