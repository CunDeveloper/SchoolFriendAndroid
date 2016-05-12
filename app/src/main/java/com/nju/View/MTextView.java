package com.nju.View;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by cun on 2016/5/10.
 */
public class MTextView extends TextView {
    public MTextView(Context context) {
        super(context);
    }

    public MTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTextColor(ContextCompat.getColor(context, android.R.color.white));
    }
}
