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

import java.util.ArrayList;

/**
 * Created by cun on 2016/3/18.
 */
public class InputEmotionUtil {

     private static String label;
     public static void initView(final Fragment fragment,View view,String TAG){
        final RelativeLayout emoLayout = (RelativeLayout) view.findViewById(R.id.emotion_layout);
        final FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.comment_input_emotion_main);
        final TextView emotionIconTV = (TextView) view.findViewById(R.id.emotion_icon);
        EditText contentEditText = (EditText) view.findViewById(R.id.content_editText);
        contentEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                emoLayout.setVisibility(View.VISIBLE);
                emotionIconTV.setText(fragment.getString(R.string.smile));
                label = fragment.getString(R.string.content);
            }
        });
        contentEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emoLayout.setVisibility(View.VISIBLE);
                emotionIconTV.setText(fragment.getString(R.string.smile));
                label = fragment.getString(R.string.content);
            }
        });

        EditText titleEditText = (EditText) view.findViewById(R.id.title);
        titleEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                emoLayout.setVisibility(View.VISIBLE);
                emotionIconTV.setText(fragment.getString(R.string.smile));
                label = fragment.getString(R.string.title);
            }
        });
        titleEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emotionIconTV.setText(fragment.getString(R.string.smile));
                emoLayout.setVisibility(View.VISIBLE);
                label = fragment.getString(R.string.title);
            }
        });

        final View transView = view.findViewById(R.id.emotion_transparent);
        transView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoftInput.close(fragment.getContext(),transView);
                emoLayout.setVisibility(View.GONE);
                frameLayout.setVisibility(View.GONE);
            }
        });

        emotionIconTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (frameLayout.getVisibility() == View.GONE){
                    emotionIconTV.setText(fragment.getString(R.string.keyboard));
                    frameLayout.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            frameLayout.setVisibility(View.VISIBLE);
                        }
                    }, 200);
                    SoftInput.close(fragment.getContext(),emotionIconTV);
                }else {
                    emotionIconTV.setText(fragment.getString(R.string.smile));
                    SoftInput.open(fragment.getContext());
                    frameLayout.setVisibility(View.GONE);
                }
            }
        });
        final ViewPager emoViewPage = (ViewPager) view.findViewById(R.id.viewpager);
        emoViewPage.setAdapter(new EmotionPageAdapter(fragment.getFragmentManager(),TAG));
    }

    public static void addViewPageEvent(final Context context,View view){
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

    public static String getLabel(){
        return label;
    }
}
