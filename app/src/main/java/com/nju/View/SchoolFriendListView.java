package com.nju.View;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by xiaojuzhang on 2015/12/4.
 */
public class SchoolFriendListView extends ListView {

    private static final String TAG = SchoolFriendListView.class.getSimpleName();
    private int mTouchSlop;
    private ViewConfiguration mViewConfiguration;
    private  boolean mIsScrolling;
    private int startP;
    private int endP;

    public SchoolFriendListView(Context context) {
        super(context);
    }

    public SchoolFriendListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mViewConfiguration = ViewConfiguration.get(context);
        mTouchSlop = mViewConfiguration.getScaledTouchSlop();
    }

    public SchoolFriendListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        final int action = MotionEventCompat.getActionMasked(ev);

        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            mIsScrolling = false;
            return false;
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN:{
                startP = (int) ev.getY();
                return true;
            }
            case MotionEvent.ACTION_MOVE:{
                if (mIsScrolling) {
                    return true;
                }
                endP = (int) ev.getY();
                Toast.makeText(getContext(), "starp=" + startP + "==" + "endP=" + endP,Toast.LENGTH_SHORT).show();
                final int yDiff = endP - startP;
                if (yDiff > mTouchSlop && getFirstVisiblePosition()!=0) {
                    // Start scrolling!
                    mIsScrolling = true;
                    return true;
                }
                break;
            }
        }
        return false;
    }


}
