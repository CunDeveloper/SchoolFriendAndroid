package com.nju.util;

import android.content.Context;
import android.view.View;
import android.widget.ListPopupWindow;

/**
 * Created by xiaojuzhang on 2015/11/23.
 */
public class CommentPopupWindow extends ListPopupWindow {

    public CommentPopupWindow(Context context, View view) {
        super(context);
        this.setHeight(ListPopupWindow.WRAP_CONTENT);
        this.setVerticalOffset(-90);
        this.setHorizontalOffset(-330);
        this.setWidth((int) Divice.convertDpToPixel(160, context));
        this.setAnchorView(view);
        this.setModal(true);
    }

}
