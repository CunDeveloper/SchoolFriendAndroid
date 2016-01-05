package com.nju.image;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.squareup.okhttp.internal.DiskLruCache;

import java.io.File;


/**
 * Created by xiaojuzhang on 2016/1/5.
 */
public class CacheUtil {
    private LruCache<String,Bitmap> mMemoryCache;
    private DiskLruCache mDiskLruCache;
    private final Object mDiskCacheLock = new Object();
    private boolean mDiskCacheStarting = true;
    private static final int DISK_CACHE_SIZE = 1024*1024*10;
    private static final String DISK_CACHE_SUB_DIR = "school_friend_img_dir";
    private static final int M_UNIT = 1024;
    private static CacheUtil mCacheUtil = null;


    private CacheUtil() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory()/M_UNIT);
        final int cacheSize = maxMemory/8;
        mMemoryCache = new LruCache<String,Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / M_UNIT;
            }
        };

    }

    public static CacheUtil getInstance() {
        if (mCacheUtil == null) {
            mCacheUtil = new CacheUtil( );
        }
        return mCacheUtil;
    }

    public  void addBitmapToMemoryCache(String key,Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key,bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

    private static File getDiskCacheDir(Context context,String uniqueName) {
        final String cachePath =
    }

}
