package com.nju.fragment;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.nju.View.CustomImageVIew;
import com.nju.activity.R;
import com.nju.adatper.ChoosedOriginPicViewPagerAdapter;
import com.nju.model.Image;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ChoosedImageViewFragment extends Fragment {

    private static  final String IMG_PATH = "img_path";
    private static final String POSITION = "position";
    private static final String TAG = ChoosedImageViewFragment.class.getSimpleName();
    private CustomImageVIew mImageView;
    private ArrayList<Image> mImgPaths;
    private ViewPager mViewPager;
    private int mPostion;
    private ActionBar mActionBar;
    public static ChoosedImageViewFragment newInstance(ArrayList<Image> imgPaths,int postion) {
        ChoosedImageViewFragment fragment = new ChoosedImageViewFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(IMG_PATH,imgPaths);
        args.putInt(POSITION,postion);
        fragment.setArguments(args);
        return fragment;
    }

    public ChoosedImageViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mImgPaths =getArguments().getParcelableArrayList(IMG_PATH);
            mPostion = getArguments().getInt(POSITION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        mActionBar = activity.getSupportActionBar();
        mActionBar.setTitle((mPostion + 1) + "/" + mImgPaths.size());
        mViewPager = (ViewPager) inflater.inflate(R.layout.fragment_choosed_image_view, container, false);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.publish_text_with_pics_toolbar);
        ImageView imageView = (ImageView) getActivity().findViewById(R.id.publish_text_with_pics_toolbar_delete_img);
        imageView.setVisibility(View.VISIBLE);
        Button button = (Button) toolbar.findViewById(R.id.publish_text_with_pics_toolbar_finish_button);
        button.setVisibility(View.GONE);
        mViewPager.setAdapter(new ChoosedOriginPicViewPagerAdapter(getFragmentManager(), mImgPaths));

        mViewPager.setCurrentItem(mPostion);
        initViewPagerSlideListener();
        return mViewPager;
    }

    private void initViewPagerSlideListener (){
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mActionBar.setTitle((position + 1) + "" + "/" + mImgPaths.size());
            }

            @Override
            public void onPageSelected(int position) {
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

}
