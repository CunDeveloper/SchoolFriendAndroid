package com.nju.db.db.service;

import android.content.Context;

import com.nju.util.Constant;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by cun on 2016/4/16.
 */
public class DegreeUtil {
    private static HashMap<String, String> degrees = null;

    public static HashMap<String, String> degrees(Context context) {
        if (degrees == null) {
            degrees = new HashMap<>();
            if (context != null) {
                Set<String> stringSet = context.getSharedPreferences(Constant.SCHOOL_FRIEND_SHARED_PREFERENCE, Context.MODE_PRIVATE).getStringSet(Constant.DEGREES, new HashSet<String>());
                for (String str : stringSet) {
                    String[] strs = str.split(";");
                    degrees.put(strs[1], strs[0]);
                }
            }
        }
        return degrees;
    }
}
