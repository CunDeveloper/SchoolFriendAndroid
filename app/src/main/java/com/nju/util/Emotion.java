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
    private static ArrayList<String> mEmotions;
    public Emotion(Context context) {
        emotions = new ArrayList<>();
        Resources resources = context.getResources();

    }

    public  List<Drawable>  getEmotions() {

        return emotions;
    }

    public static ArrayList<String> emotions(){
        if (mEmotions == null) {
            mEmotions = new ArrayList<>();
            mEmotions.add("\uD83D\uDE00");
            mEmotions.add("\uD83D\uDE01");
            mEmotions.add("\uD83D\uDE02");
            mEmotions.add("\uD83D\uDE03");
            mEmotions.add("\uD83D\uDE04");
            mEmotions.add("\uD83D\uDE05");
            mEmotions.add("\uD83D\uDE06");
            mEmotions.add("\uD83D\uDE07");
            mEmotions.add("\uD83D\uDE08");
            mEmotions.add("\uD83D\uDE09");
            mEmotions.add("\uD83D\uDE0A");
            mEmotions.add("\uD83D\uDE0B");
            mEmotions.add("\uD83D\uDE0E");
            mEmotions.add("\uD83D\uDE0D");
            mEmotions.add("\uD83D\uDE18");

            mEmotions.add("\uD83D\uDE19");
            mEmotions.add("\uD83D\uDE1A");
            mEmotions.add("\u263A");
           // mEmotions.add("\uD83E\uDD17");
            mEmotions.add("\uD83D\uDE07");
           // mEmotions.add("\uD83E\uDD14");
            mEmotions.add("\uD83D\uDE10");
            mEmotions.add("\uD83D\uDE11");
            mEmotions.add("\uD83D\uDE36");
            //mEmotions.add("\uD83D\uDE44");
            mEmotions.add("\uD83D\uDE0F");
            mEmotions.add("\uD83D\uDE23");
            mEmotions.add("\uD83D\uDE25");
            mEmotions.add("\uD83D\uDE2E");
            //mEmotions.add("\uD83E\uDD10");
            mEmotions.add("\uD83D\uDE2F");
            mEmotions.add("\uD83D\uDE2A");
            mEmotions.add("\uD83D\uDE2B");
            mEmotions.add("\uD83D\uDE34");
            mEmotions.add("\uD83D\uDE0C");


        }
        return mEmotions;
    }
}
