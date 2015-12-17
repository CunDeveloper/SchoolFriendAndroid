package com.nju.http;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by xiaojuzhang on 2015/12/17.
 */
public class HttpManager {

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

    public void addDownLoadQueue(RequestRunnable requestRunnable) {
        mDownloadWorkQueue.add(requestRunnable);
        RequestRunnable runnable = (RequestRunnable) mDownloadWorkQueue.poll();
        if (runnable != null) {
            mDownloadThreadPool.execute(runnable);
        }

    }

    public void exeRequest(RequestRunnable requestRunnable) {
        mThreadService.execute(requestRunnable);
    }


}
