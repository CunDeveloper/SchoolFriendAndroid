package com.nju.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by xiaojuzhang on 2015/12/28.
 */
public class ToastUtil {
    public static void ShowText(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

    public static void showShortText(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
