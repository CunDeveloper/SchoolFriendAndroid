package com.nju.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import com.nju.activity.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaojuzhang on 2015/11/17.
 */
public class Emotion {
    private List<Drawable> emotions;
    public Emotion(Context context) {
        emotions = new ArrayList<>();
        Resources resources = context.getResources();


    }

    public  List<Drawable>  getEmotions() {

        return emotions;
    }
}
