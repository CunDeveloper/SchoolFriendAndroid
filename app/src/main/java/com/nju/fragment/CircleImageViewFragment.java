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
    private static final String POSITION = "postion";
    private LinearLayout mLableLinearLayout;
    private ArrayList<Bitmap> mImages;
    private int mPostion;
    private ViewPager mViewPager;

    public static CircleImageViewFragment newInstance(ArrayList<Bitmap> images,int postion) {
        CircleImageViewFragment fragment = new CircleImageViewFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(IMAGE, images);
        bundle.putInt(POSITION,postion);
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
            mPostion = getArguments().getInt(POSITION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_circle_image_view, container, false);
        view.setPadding(view.getPaddingLeft(),Divice.getStatusBarHeight(getContext()),view.getPaddingRight(),view.getPaddingBottom());
        mLableLinearLayout = (LinearLayout) view.findViewById(R.id.fragment_circle_image_view_label_layout);
        mViewPager = (ViewPager) view.findViewById(R.id.fragment_circle_image_view_viewpager);
        mViewPager.setAdapter(new CircleImageViewAdapter(getFragmentManager(),mImages));
        initLabel(inflater);
        return view;
    }

    private void initLabel(LayoutInflater inflater) {

        int length = mImages.size();
        for (int i =0 ;i < length ; i++) {
            View view = inflater.inflate(R.layout.circle,mLableLinearLayout,false);
            if (i == mPostion) {
                view.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.select_circle_label_bg));
            }
            mLableLinearLayout.addView(view);
        }



    }

    @Override
    public void onPause() {
        super.onPause();
        Divice.showStatusBar((AppCompatActivity) getActivity());
    }
}
