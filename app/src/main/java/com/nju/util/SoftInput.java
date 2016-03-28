package com.nju.util;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by xiaojuzhang on 2015/11/16.
 */
public class SoftInput {

    private static InputMethodManager imm = null;

    private static void init(Context context) {
        if (imm == null) {
            imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        }
    }

    public static void open(Context context) {
        init(context);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public static void close(Context context, View view) {
        init(context);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static boolean openSoft(View view) {
        return imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }
}
