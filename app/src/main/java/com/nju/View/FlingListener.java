package com.nju.View;

import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Created by cun on 2016/4/7.
 */
public class FlingListener extends GestureDetector.SimpleOnGestureListener {

    private float velocityX;
    private float velocityY;

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        return true;
    }

    public float getVelocityX() {
        return velocityX;
    }

    public float getVelocityY() {
        return velocityY;
    }
}
