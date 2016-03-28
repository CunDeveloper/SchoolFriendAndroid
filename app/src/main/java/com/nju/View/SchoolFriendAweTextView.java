package com.nju.View;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by xiaojuzhang on 2015/12/16.
 */
public class SchoolFriendAweTextView extends TextView {
    private static final String FONT_PATH = "fonts/fontawesome-webfont.ttf";
    private static Typeface mTypeface;

    public SchoolFriendAweTextView(Context context) {
        super(context);
        initType();
    }

    public SchoolFriendAweTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initType();
    }

    public SchoolFriendAweTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initType();
    }

    private void initType() {
        if (mTypeface == null) {
            mTypeface = Typeface.createFromAsset(getContext().getAssets(), FONT_PATH);
        }
        this.setTypeface(mTypeface);
    }
}
