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
        emotions.add(resources.getDrawable(R.mipmap.d_2_doge));
        emotions.add(resources.getDrawable(R.mipmap.d_3_shenshou));
        emotions.add(resources.getDrawable(R.mipmap.d_5_xiaoku));
        emotions.add(resources.getDrawable(R.mipmap.d_6_zuiyou));
        emotions.add(resources.getDrawable(R.mipmap.d_aini));
        emotions.add(resources.getDrawable(R.mipmap.d_aoteman));
        emotions.add(resources.getDrawable(R.mipmap.d_baibai));
        emotions.add(resources.getDrawable(R.mipmap.d_beishang));
        emotions.add(resources.getDrawable(R.mipmap.d_bishi));
        emotions.add(resources.getDrawable(R.mipmap.d_bizui));
        emotions.add(resources.getDrawable(R.mipmap.d_chanzui));
        emotions.add(resources.getDrawable(R.mipmap.d_chijing));
        emotions.add(resources.getDrawable(R.mipmap.d_dahaqi));
        emotions.add(resources.getDrawable(R.mipmap.d_ding));
        emotions.add(resources.getDrawable(R.mipmap.d_feizao));
        emotions.add(resources.getDrawable(R.mipmap.d_fennu));
        emotions.add(resources.getDrawable(R.mipmap.d_ganmao));
        emotions.add(resources.getDrawable(R.mipmap.d_guzhang));
        emotions.add(resources.getDrawable(R.mipmap.d_haha));

        emotions.add(resources.getDrawable(R.mipmap.d_haixiu));
        emotions.add(resources.getDrawable(R.mipmap.d_han));
        emotions.add(resources.getDrawable(R.mipmap.d_hehe));
        emotions.add(resources.getDrawable(R.mipmap.d_heixian));
        emotions.add(resources.getDrawable(R.mipmap.d_heng));
        emotions.add(resources.getDrawable(R.mipmap.d_huaxin));
        emotions.add(resources.getDrawable(R.mipmap.d_keai));
        emotions.add(resources.getDrawable(R.mipmap.d_kelian));
        emotions.add(resources.getDrawable(R.mipmap.d_ku));
        emotions.add(resources.getDrawable(R.mipmap.d_kun));
        emotions.add(resources.getDrawable(R.mipmap.d_landelini));
        emotions.add(resources.getDrawable(R.mipmap.d_lei));
        emotions.add(resources.getDrawable(R.mipmap.d_lvxing));
        emotions.add(resources.getDrawable(R.mipmap.d_nanhaier));

        emotions.add(resources.getDrawable(R.mipmap.d_nu));
        emotions.add(resources.getDrawable(R.mipmap.d_numa));
        emotions.add(resources.getDrawable(R.mipmap.d_nvhaier));
        emotions.add(resources.getDrawable(R.mipmap.d_qian));
        emotions.add(resources.getDrawable(R.mipmap.d_qinqin));
        emotions.add(resources.getDrawable(R.mipmap.d_shengbing));
        emotions.add(resources.getDrawable(R.mipmap.d_shiwang));

    }

    public  List<Drawable>  getEmotions() {

        return emotions;
    }
}
