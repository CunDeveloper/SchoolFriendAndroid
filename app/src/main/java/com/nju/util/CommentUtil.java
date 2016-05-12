package com.nju.util;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nju.activity.R;
import com.nju.adatper.EmotionPageAdapter;
import com.nju.fragment.BaseFragment;

import java.util.ArrayList;

/**
 * Created by xiaojuzhang on 2016/3/16.
 */
public class CommentUtil {

    public static void hideSoft(final Context context, View view) {
        View mView = view.findViewById(R.id.re_work_item_detail_hide_layout);
        final TextView mEmotionTv = (TextView) view.findViewById(R.id.comment_emotion);
        final FrameLayout mEmotionLineLayout = (FrameLayout) view.findViewById(R.id.comment_input_emotion_main);
        final RelativeLayout hideReLayout = (RelativeLayout) view.findViewById(R.id.re_work_item_detail_hide_main_layout);
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoftInput.close(context, hideReLayout);
                hideReLayout.setVisibility(View.GONE);
                mEmotionLineLayout.setVisibility(View.GONE);
            }
        });

        mEmotionTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEmotionLineLayout.getVisibility() == View.GONE) {
                    SoftInput.close(context, mEmotionTv);
                    mEmotionLineLayout.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mEmotionLineLayout.setVisibility(View.VISIBLE);
                        }
                    }, 200);
                } else {
                    SoftInput.open(context);
                    mEmotionLineLayout.setVisibility(View.GONE);
                }
            }
        });
    }

    public static void hideEmotionAndSoft(final Context context, View view) {
        final View mView = view.findViewById(R.id.re_work_item_detail_hide_layout);
        final TextView mEmotionTv = (TextView) view.findViewById(R.id.comment_emotion);
        final FrameLayout mEmotionLineLayout = (FrameLayout) view.findViewById(R.id.comment_input_emotion_main);
        final RelativeLayout hideReLayout = (RelativeLayout) view.findViewById(R.id.re_work_item_detail_hide_main_layout);
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoftInput.close(context, hideReLayout);
                mEmotionLineLayout.setVisibility(View.GONE);
                mView.setVisibility(View.GONE);
            }
        });

        mEmotionTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEmotionLineLayout.getVisibility() == View.GONE) {
                    SoftInput.close(context, mEmotionTv);
                    mEmotionLineLayout.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mEmotionLineLayout.setVisibility(View.VISIBLE);
                        }
                    }, 200);
                } else {
                    SoftInput.open(context);
                    mEmotionLineLayout.setVisibility(View.GONE);
                }
            }
        });
    }

    public static RelativeLayout getHideLayout(View view) {
        return (RelativeLayout) view.findViewById(R.id.re_work_item_detail_hide_main_layout);
    }

    public static void initViewPager(Fragment fragment, View view) {
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        viewPager.setAdapter(new EmotionPageAdapter(fragment.getChildFragmentManager(), fragment.getClass().getSimpleName()));
    }


    public static EditText getCommentEdit(BaseFragment fragment, final View view) {
        hideSoft(fragment.getContext(), view);
        initViewPager(fragment, view);
        addViewPageEvent(fragment.getContext(), view);
        final FrameLayout mEmotionLineLayout = (FrameLayout) view.findViewById(R.id.comment_input_emotion_main);
        EditText editText = (EditText) view.findViewById(R.id.comment_edittext);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEmotionLineLayout.setVisibility(View.GONE);
            }
        });
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mEmotionLineLayout.setVisibility(View.GONE);
            }
        });
        return editText;
    }

    public static EditText getCommentEditWithEmotion(BaseFragment fragment, final View view) {
        hideEmotionAndSoft(fragment.getContext(), view);
        initViewPager(fragment, view);
        addViewPageEvent(fragment.getContext(), view);
        final View mView = view.findViewById(R.id.re_work_item_detail_hide_layout);
        final FrameLayout mEmotionLineLayout = (FrameLayout) view.findViewById(R.id.comment_input_emotion_main);
        EditText editText = (EditText) view.findViewById(R.id.comment_edittext);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEmotionLineLayout.setVisibility(View.GONE);
                mView.setVisibility(View.VISIBLE);
            }
        });
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mEmotionLineLayout.setVisibility(View.GONE);
                mView.setVisibility(View.VISIBLE);
            }
        });
        return editText;
    }

    public static void closeSoftKey(Context context, View view) {
        final FrameLayout mEmotionLineLayout = (FrameLayout) view.findViewById(R.id.comment_input_emotion_main);
        final RelativeLayout hideReLayout = (RelativeLayout) view.findViewById(R.id.re_work_item_detail_hide_main_layout);
        SoftInput.close(context, hideReLayout);
        hideReLayout.setVisibility(View.GONE);
        mEmotionLineLayout.setVisibility(View.GONE);
    }

    public static void closeSoftKeyWithComment(Context context, View view) {
        final FrameLayout mEmotionLineLayout = (FrameLayout) view.findViewById(R.id.comment_input_emotion_main);
        final RelativeLayout hideReLayout = (RelativeLayout) view.findViewById(R.id.re_work_item_detail_hide_main_layout);
        SoftInput.close(context, hideReLayout);
        mEmotionLineLayout.setVisibility(View.GONE);
    }

    public static void addViewPageEvent(final Context context, View view) {
        final int[] mSlidePosition = {0};
        ArrayList<View> mSlideCircleViews = new ArrayList<>();
        View mView;
        mView = view.findViewById(R.id.emotion_pager_view1);
        mSlideCircleViews.add(mView);
        mView = view.findViewById(R.id.emotion_pager_view2);
        mSlideCircleViews.add(mView);
        mView = view.findViewById(R.id.emotion_pager_view3);
        mSlideCircleViews.add(mView);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        final ArrayList<View> finalMSlideCircleViews = mSlideCircleViews;
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                finalMSlideCircleViews.get(mSlidePosition[0]).setBackground(ContextCompat.getDrawable(context, R.drawable.unselect_circle_label_bg));
                mSlidePosition[0] = position;
                finalMSlideCircleViews.get(mSlidePosition[0]).setBackground(ContextCompat.getDrawable(context, R.drawable.select_circle_label_bg));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

}
