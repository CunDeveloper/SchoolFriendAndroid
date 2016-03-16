package com.nju.util;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaojuzhang on 2015/11/17.
 */
public class Emotion {
    private List<Drawable> emotions;
    private static ArrayList<ArrayList<String>> mEmotions;
    private static ArrayList<String> mEmotionPage1,mEmotionPage2,mEmotionPage3;

    public static ArrayList<ArrayList<String>> emotions() {
        if (mEmotions == null) {
            mEmotions = new ArrayList<>();
            mEmotions.add(emotionsPhonePage1());
            mEmotions.add(emotionsPhonePage2());
            mEmotions.add(emotionsPhonePage3());
        }
        return mEmotions;
    }

    private static ArrayList<String> emotionsPhonePage1(){
        if (mEmotionPage1 == null) {
            mEmotionPage1 = new ArrayList<>();
            mEmotionPage1.add("\uD83D\uDE00");
            mEmotionPage1.add("\uD83D\uDE01");
            mEmotionPage1.add("\uD83D\uDE02");
            mEmotionPage1.add("\uD83D\uDE03");
            mEmotionPage1.add("\uD83D\uDE04");
            mEmotionPage1.add("\uD83D\uDE05");
            mEmotionPage1.add("\uD83D\uDE06");
            mEmotionPage1.add("\uD83D\uDE07");
            mEmotionPage1.add("\uD83D\uDE08");
            mEmotionPage1.add("\uD83D\uDE09");
            mEmotionPage1.add("\uD83D\uDE0A");
            mEmotionPage1.add("\uD83D\uDE0B");
            mEmotionPage1.add("\uD83D\uDE0E");
            mEmotionPage1.add("\uD83D\uDE0D");
            mEmotionPage1.add("\uD83D\uDE18");
            mEmotionPage1.add("\uD83D\uDE19");
            mEmotionPage1.add("\uD83D\uDE1A");
            mEmotionPage1.add("\u263A");
            mEmotionPage1.add("\uD83D\uDE07");
            mEmotionPage1.add("\uD83D\uDE10");
            mEmotionPage1.add("\uD83D\uDE11");
            mEmotionPage1.add("\uD83D\uDE36");
            mEmotionPage1.add("\uD83D\uDE0F");
            mEmotionPage1.add("\uD83D\uDE23");
            mEmotionPage1.add("\uD83D\uDE25");
            mEmotionPage1.add("\uD83D\uDE2E");
            mEmotionPage1.add("\uD83D\uDE2F");
            mEmotionPage1.add("\uD83D\uDE2A");
            mEmotionPage1.add("\uD83D\uDE2B");
            mEmotionPage1.add("\uD83D\uDE34");
            mEmotionPage1.add("\uD83D\uDE0C");
            mEmotionPage1.add("\uD83D\uDE1B");
        }

        return mEmotionPage1;
    }

    private static ArrayList<String> emotionsPhonePage2(){
        if (mEmotionPage2 == null) {
            mEmotionPage2 = new ArrayList<>();
            mEmotionPage2.add("\uD83D\uDE1C");
            mEmotionPage2.add("\uD83D\uDE1D");
            mEmotionPage2.add("\uD83D\uDE12");
            mEmotionPage2.add("\uD83D\uDE13");
            mEmotionPage2.add("\uD83D\uDE14");
            mEmotionPage2.add("\uD83D\uDE15");
            mEmotionPage2.add("\uD83D\uDE16");
            mEmotionPage2.add("\uD83D\uDE37");
            mEmotionPage2.add("\uD83D\uDE32");
            mEmotionPage2.add("\uD83D\uDE1E");
            mEmotionPage2.add("\uD83D\uDE1F");
            mEmotionPage2.add("\uD83D\uDE24");
            mEmotionPage2.add("\uD83D\uDE22");
            mEmotionPage2.add("\uD83D\uDE2D");
            mEmotionPage2.add("\uD83D\uDE26");
            mEmotionPage2.add("\uD83D\uDE27");
            mEmotionPage2.add("\uD83D\uDE28");
            mEmotionPage2.add("\uD83D\uDE29");
            mEmotionPage2.add("\uD83D\uDE2C");
            mEmotionPage2.add("\uD83D\uDE30");
            mEmotionPage2.add("\uD83D\uDE31");
            mEmotionPage2.add("\uD83D\uDE33");
            mEmotionPage2.add("\uD83D\uDE35");
            mEmotionPage2.add("\uD83D\uDE21");
            mEmotionPage2.add("\uD83D\uDE20");
            mEmotionPage2.add("\uD83D\uDE08");
            mEmotionPage2.add("\uD83D\uDC7F");
            mEmotionPage2.add("\uD83D\uDC79");
            mEmotionPage2.add("\uD83D\uDC7A");
            mEmotionPage2.add("\uD83D\uDC80");
            mEmotionPage2.add("\uD83D\uDC7B");
            mEmotionPage2.add("\uD83D\uDC7D");
        }
        return mEmotionPage2;
    }

    private static ArrayList<String> emotionsPhonePage3(){
        if (mEmotionPage3 == null) {
            mEmotionPage3 = new ArrayList<>();
            mEmotionPage3.add("\uD83D\uDC7E");
            mEmotionPage3.add("\uD83D\uDCA9");
            mEmotionPage3.add("\uD83D\uDE3A");
            mEmotionPage3.add("\uD83D\uDE38");
            mEmotionPage3.add("\uD83D\uDE39");
            mEmotionPage3.add("\uD83D\uDE3B");
            mEmotionPage3.add("\uD83D\uDE3C");
            mEmotionPage3.add("\uD83D\uDE3D");
            mEmotionPage3.add("\uD83D\uDE40");
            mEmotionPage3.add("\uD83D\uDE3F");
            mEmotionPage3.add("\uD83D\uDE3E");
            mEmotionPage3.add("\uD83D\uDE48");
            mEmotionPage3.add("\uD83D\uDE49");
            mEmotionPage3.add("\uD83D\uDE4A");
            mEmotionPage3.add("\uD83D\uDC66");
            mEmotionPage3.add("\uD83D\uDC67");
            mEmotionPage3.add("\uD83D\uDC68");
            mEmotionPage3.add("\uD83D\uDC69");
            mEmotionPage3.add("\uD83D\uDC74");
            mEmotionPage3.add("\uD83D\uDC75");
            mEmotionPage3.add("\uD83D\uDC76");
            mEmotionPage3.add("\uD83D\uDC71");
            mEmotionPage3.add("\uD83D\uDC6E");
            mEmotionPage3.add("\uD83D\uDC72");
            mEmotionPage3.add("\uD83D\uDC73");
            mEmotionPage3.add("\uD83D\uDC77");
            mEmotionPage3.add("\uD83D\uDC78");
            mEmotionPage3.add("\uD83D\uDC82");
            mEmotionPage3.add("\uD83D\uDC7C");
            mEmotionPage3.add("\uD83D\uDC6F");
            mEmotionPage3.add("\uD83D\uDC86");
            mEmotionPage3.add("\uD83D\uDC87");
        }
        return mEmotionPage3;
    }
}
