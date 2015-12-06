package com.nju.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.nju.activity.PublishTextWithPicsActivity;
import com.nju.activity.R;
import com.nju.adatper.OriginPicViewPagerAdapter;
import com.nju.model.Image;

import java.util.ArrayList;


public class CameraImageViewFragment extends Fragment {

    private static final String TAG = CameraImageViewFragment.class.getSimpleName();
    public static final String POSITION = "position";
    private static final String LABEL ="label";
    private ViewPager mViewPager;
    private ArrayList<Image> mImgs;
    private int mPosition;
    private ActionBar mActionBar;
    private Toolbar mToolbar;
    private Button mFinishButton;
    private CheckBox mCheckBox;
    private String mLabel;//
    public static CameraImageViewFragment newInstance(ArrayList<Image> imgPaths,int position,String label) {
        CameraImageViewFragment fragment = new CameraImageViewFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(TAG,imgPaths);
        args.putInt(POSITION,position);
        args.putString(LABEL,label);
        fragment.setArguments(args);
        return fragment;
    }

    public CameraImageViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mImgs = getArguments().getParcelableArrayList(TAG);
            mPosition = getArguments().getInt(POSITION);
            mLabel = getArguments().getString(LABEL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_camera_image_view, container, false);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        mActionBar = activity.getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setTitle((mPosition + 1) + "" + "/" + mImgs.size());
        mViewPager = (ViewPager) view.findViewById(R.id.fragment_camera_imgage_view_pager);
        mFinishButton = (Button) view.findViewById(R.id.fragment_camera_image_view_finish_button);
        mCheckBox = (CheckBox) view.findViewById(R.id.fragment_camera_image_view_checkBox);
        mViewPager.setAdapter(new OriginPicViewPagerAdapter(getFragmentManager(),mImgs));
        mViewPager.setCurrentItem(mPosition);
        dependLabelConditionInit();
        initViewPagerSlideListener();
        initViewPageTouchListener();
        return view;
    }

    private void dependLabelConditionInit() {
        if (mLabel.equals(getResources().getString(R.string.choosedReview))) {
            mFinishButton.setText(getResources().getString(R.string.finish)+
                    "("+mImgs.size()+"/"+getResources().getInteger(R.integer.PIC_UNM)+")");
            mCheckBox.setChecked(true);
            mFinishButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(),PublishTextWithPicsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList(getString(R.string.IMGS),mImgs);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    CameraImageViewFragment.this.onDestroy();
                    getActivity().finish();
                }
            });
        } else {

        }
    }

    private void initViewPagerSlideListener (){
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mActionBar.setTitle((position+1)+""+"/"+mImgs.size());
            }
            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initViewPageTouchListener() {
        mViewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"hello",Toast.LENGTH_LONG).show();
            }
        });
    }

}
