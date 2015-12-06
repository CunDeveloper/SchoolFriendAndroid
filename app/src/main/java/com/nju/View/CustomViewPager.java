package com.nju.View;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Created by xiaojuzhang on 2015/12/1.
 */
public class CustomViewPager extends ViewPager {

    private OnItemClickListener mOnItemClickListener;

    public CustomViewPager(Context context) {
        super(context);

        setup();
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);

        setup();
    }

    private void setup() {
        final GestureDetector tapGestureDetector = new   GestureDetector(getContext(), new TapGestureListener());
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                tapGestureDetector.onTouchEvent(event);
                return false;
            }
        });
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private class TapGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if(mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(getCurrentItem());
            }
            return true;
        }
    }
}
