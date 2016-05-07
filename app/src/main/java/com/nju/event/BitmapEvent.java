package com.nju.event;

import android.graphics.Bitmap;

/**
 * Created by cun on 2016/4/12.
 */
public class BitmapEvent {
    private Bitmap bitmap;

    public BitmapEvent(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
