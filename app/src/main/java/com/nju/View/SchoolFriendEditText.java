package com.nju.View;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by xiaojuzhang on 2015/12/16.
 */
public class SchoolFriendEditText extends EditText {
    private static Typeface mTypeface;
    private static final String FONT_PATH = "fonts/AppleColorEmoji.ttf";

    public SchoolFriendEditText(Context context) {
        super(context);
        initType();
    }

    public SchoolFriendEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initType();
    }

    public SchoolFriendEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initType();
    }

    private void initType(){
        if (mTypeface ==null) {
            mTypeface = Typeface.createFromAsset(getContext().getAssets(),FONT_PATH);
        }
        this.setTypeface(mTypeface);
        this.setTextColor(ContextCompat.getColor(getContext(), android.R.color.black));
    }

}
