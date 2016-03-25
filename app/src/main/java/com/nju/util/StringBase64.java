package com.nju.util;

import android.util.Base64;

import java.io.UnsupportedEncodingException;

/**
 * Created by xiaojuzhang on 2015/12/17.
 */
public class StringBase64 {

    public static String encode(String str) {
       return Base64.encodeToString(str.getBytes(), Base64.DEFAULT);
    }

    public static String decode(String str){
        String result ="";
        try {
            result = new String(Base64.decode(str, Base64.DEFAULT),Constant.UTF_8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

}
