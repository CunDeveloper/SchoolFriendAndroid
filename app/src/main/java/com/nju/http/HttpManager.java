package com.nju.http;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.nju.http.request.CompressRequest;
import com.nju.model.BitmapWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by xiaojuzhang on 2015/12/17.
 */
public class HttpManager {
    private static final String TAG = HttpManager.class.getSimpleName();
    private static final int KEEP_ALIVE_TIME = 1;
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT;
    private final BlockingDeque<Runnable> mDownloadWorkQueue;
    private final ThreadPoolExecutor mDownloadThreadPool;
    private final ExecutorService mThreadService;
    private static HttpManager sInstance = null;
    private static final int CORE_POOL_SIZE = 8;
    private static final int MAXIMUM_POOL_SIZE = 8;
    private static int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();

    static {
        KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;
        sInstance = new HttpManager();
    }

    private HttpManager(){
        mDownloadWorkQueue = new LinkedBlockingDeque<>();
        mDownloadThreadPool = new ThreadPoolExecutor(CORE_POOL_SIZE,MAXIMUM_POOL_SIZE,KEEP_ALIVE_TIME,KEEP_ALIVE_TIME_UNIT,mDownloadWorkQueue);
        mThreadService =   Executors.newSingleThreadExecutor();
    }

    public static HttpManager getInstance(){
        return sInstance;
    }

    public void addDownLoadQueue(Runnable runnable) {
        mDownloadWorkQueue.add(runnable);
        Runnable reqRunnable = mDownloadWorkQueue.poll();
        if (reqRunnable != null) {
            mDownloadThreadPool.execute(reqRunnable);
        }

    }

    public ArrayList<BitmapWrapper> compressBitmap(Context context,ArrayList<BitmapWrapper> bitmapWrappers) {
        ArrayList<BitmapWrapper> bitmaps = new ArrayList<>();
        ArrayList<CompressRequest> compressRequests = new ArrayList<>();
        for (BitmapWrapper bitmapWrapper : bitmapWrappers) {
            compressRequests.add(new CompressRequest(context, bitmapWrapper));
        }
        try {
            List<Future<BitmapWrapper>> futures =mDownloadThreadPool.invokeAll(compressRequests);
            for (Future<BitmapWrapper> future:futures){
                bitmaps.add(future.get());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG,e.getMessage());
        }
        return bitmaps;
    }

    public void exeRequest(RequestRunnable requestRunnable) {
        mThreadService.execute(requestRunnable);
    }

}
