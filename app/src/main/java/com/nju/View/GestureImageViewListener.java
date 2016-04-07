package com.nju.View;

/**
 * Created by cun on 2016/4/7.
 */
public interface GestureImageViewListener {
    public void onTouch(float x, float y);

    public void onScale(float scale);

    public void onPosition(float x, float y);
}
