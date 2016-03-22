package com.nju.util;

import android.app.Activity;

import com.nju.fragment.BaseFragment;

/**
 * Created by xiaojuzhang on 2016/3/22.
 */
public class FragmentUtil {

    public static boolean isAttachedToActivity(BaseFragment fragment){
        Activity activity = fragment.getActivity();
        if (activity != null && fragment.isAdded()){
            return true;
        }else {
            return false;
        }
    }
}
