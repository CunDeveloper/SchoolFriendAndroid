package com.nju.View;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by xiaojuzhang on 2015/12/28.
 */
public class SchoolFriendGridView extends GridView {
    public SchoolFriendGridView(Context context) {
        super(context);
    }

    public SchoolFriendGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SchoolFriendGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(MEASURED_SIZE_MASK, MeasureSpec.AT_MOST));
        getLayoutParams().height = getMeasuredHeight();
    }
}
