package com.nju.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.nju.activity.R;
import com.nju.adatper.ChoosedOriginPicViewPagerAdapter;
import com.nju.model.Image;

import java.util.ArrayList;

public class ChoosedImageViewFragment extends BaseFragment {

    private static  final String IMG_PATH = "img_path";
    private static final String POSITION = "position";
    public static final String TAG = ChoosedImageViewFragment.class.getSimpleName();
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
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        mActionBar = activity.getSupportActionBar();
        mActionBar.setTitle((mPostion + 1) + "/" + mImgPaths.size());
        mViewPager = (ViewPager) inflater.inflate(R.layout.fragment_choosed_image_view, container, false);
        ImageView imageView = (ImageView) getActivity().findViewById(R.id.main_viewpager_menu_delete_img);
        imageView.setVisibility(View.VISIBLE);
        Button button = (Button)getActivity().findViewById(R.id.main_viewpager_menu_bn);
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
