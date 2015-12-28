package com.nju.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.nju.activity.R;
import com.nju.adatper.CircleImageViewAdapter;
import com.nju.util.Divice;

import java.util.ArrayList;


public class CircleImageViewFragment extends BaseFragment {

    public static final String TAG = CircleImageViewFragment.class.getSimpleName();
    private static final String IMAGE = "image";
    private static final String POSITION = "position";
    private LinearLayout mLabelLinearLayout;
    private ArrayList<Bitmap> mImages;
    private int mPosition;
    private ViewPager mViewPager;
    private ArrayList<View> views;
    private int mChoosePosition;

    public static CircleImageViewFragment newInstance(ArrayList<Bitmap> images,int position) {
        CircleImageViewFragment fragment = new CircleImageViewFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(IMAGE, images);
        bundle.putInt(POSITION,position);
        fragment.setArguments(bundle);
        return fragment;
    }

    public CircleImageViewFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mImages = getArguments().getParcelableArrayList(IMAGE);
            mPosition = getArguments().getInt(POSITION);
            mChoosePosition = mPosition;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_circle_image_view, container, false);
        view.setPadding(view.getPaddingLeft(),Divice.getStatusBarHeight(getContext()),view.getPaddingRight(),view.getPaddingBottom());
        mLabelLinearLayout = (LinearLayout) view.findViewById(R.id.fragment_circle_image_view_label_layout);
        mViewPager = (ViewPager) view.findViewById(R.id.fragment_circle_image_view_viewpager);
        mViewPager.setAdapter(new CircleImageViewAdapter(getFragmentManager(),mImages));
        views = new ArrayList<>(9);
        initLabel(inflater);
        initViewPagerChangeEvent();
        return view;
    }

    private void initViewPagerChangeEvent() {
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }
            @Override
            public void onPageSelected(int i) {
                views.get(mChoosePosition).setBackground(ContextCompat.getDrawable(getContext(),R.drawable.unselect_circle_label_bg));
                mChoosePosition = i ;
                views.get(mChoosePosition).setBackground(ContextCompat.getDrawable(getContext(),R.drawable.select_circle_label_bg));
            }
            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private void initLabel(LayoutInflater inflater) {

        int length = mImages.size();
        for (int i =0 ;i < length ; i++) {
            View view = inflater.inflate(R.layout.circle, mLabelLinearLayout,false);
            views.add(view);
            if (i == mPosition) {
                view.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.select_circle_label_bg));
            }
            mLabelLinearLayout.addView(view);
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        Divice.showStatusBar((AppCompatActivity) getActivity());
    }
}
