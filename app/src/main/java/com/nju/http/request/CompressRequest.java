package com.nju.http.request;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import com.nju.activity.R;
import com.nju.model.BitmapWrapper;
import com.nju.util.Constant;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.concurrent.Callable;

/**
 * Created by xiaojuzhang on 2015/12/18.
 */
public class CompressRequest implements Callable<BitmapWrapper> {
    private static int mWidth = 0;
    private static int mHeight = 0;
    private static final int DEFAULT_WIDTH = 720;
    private static final int DEFAULT_HEIGHT = 1000;
    private final BitmapWrapper mBitmapWrapper;
    private final Context mContext;

    public CompressRequest(Context context,BitmapWrapper bitmapWrapper) {
        if (mWidth == 0 || mHeight == 0) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.SCHOOL_FRIEND_SHARED_PREFERENCE, Context.MODE_PRIVATE);
            mWidth = sharedPreferences.getInt(context.getString(R.string.diviceWidth),DEFAULT_WIDTH);
            mHeight = sharedPreferences.getInt(context.getString(R.string.visiDiviceHeight),DEFAULT_HEIGHT);
        }
        mBitmapWrapper = bitmapWrapper;
        mContext = context;
    }

    @Override
    public BitmapWrapper call() throws Exception {
        final Bitmap bitmap = Picasso.with(mContext).load(new File(mBitmapWrapper.getPath())).resize(mWidth, mHeight).get();
        mBitmapWrapper.setBitmap(bitmap);
        return mBitmapWrapper;
    }
}
