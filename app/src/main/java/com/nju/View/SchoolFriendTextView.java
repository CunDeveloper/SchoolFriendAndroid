package com.nju.View;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by cun on 2015/12/3.
 */
public class SchoolFriendTextView extends TextView {
    private static final String FONT_PATH = "fonts/AppleColorEmoji.ttf";
    private static Typeface mTypeface;

    public SchoolFriendTextView(Context context) {
        super(context);
        initType();
    }

    public SchoolFriendTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initType();
    }

    public SchoolFriendTextView(Context context, AttributeSet attrs) {
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
