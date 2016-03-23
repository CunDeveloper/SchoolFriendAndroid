package com.nju.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.nju.View.SchoolFriendDialog;
import com.nju.activity.BaseActivity;
import com.nju.activity.R;
import com.nju.adatper.ChooseOriginPicViewPagerAdapter;
import com.nju.model.ImageWrapper;

import java.util.ArrayList;

public class ChooseImageViewFragment extends BaseFragment {

    private static  final String IMG_PATH = "img_path";
    private static final String POSITION = "position";
    public static final String TAG = ChooseImageViewFragment.class.getSimpleName();
    private static final String SLASH = "/";
    private ArrayList<ImageWrapper> mImgPaths;
    private int mPosition;
    private ViewPager mViewPager;
    private ChooseOriginPicViewPagerAdapter mPagerAdapter;
    private int mDelPosition = 0;

    public static ChooseImageViewFragment newInstance(ArrayList<ImageWrapper> imgPaths,int position) {
        ChooseImageViewFragment fragment = new ChooseImageViewFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(IMG_PATH,imgPaths);
        args.putInt(POSITION,position);
        fragment.setArguments(args);
        return fragment;
    }

    public ChooseImageViewFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mImgPaths =getArguments().getParcelableArrayList(IMG_PATH);
            mPosition = getArguments().getInt(POSITION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mViewPager = (ViewPager) inflater.inflate(R.layout.fragment_choosed_image_view, container, false);
        mPagerAdapter = new ChooseOriginPicViewPagerAdapter(getFragmentManager(), mImgPaths);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(mPosition);
        initViewPagerSlideListener(mViewPager);
        return mViewPager;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getHostActivity().getToolBar().setTitle((mPosition + 1) + SLASH + mImgPaths.size());
        getHostActivity().getMenuCameraView().setVisibility(View.GONE);
        TextView deleteView = getHostActivity().getMenuDeleteView();
        deleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayDialog();
            }
        });
        deleteView.setVisibility(View.VISIBLE);
        getHostActivity().getMenuBn().setVisibility(View.GONE);
    }

    private  void displayDialog() {
        SchoolFriendDialog dialog = SchoolFriendDialog.remindDialog(getContext(),getString(R.string.reminder),getString(R.string.are_you_sure_delete_this_pic));

        dialog.getBuilder().onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                mImgPaths.remove(mDelPosition);
                if (mImgPaths.size()>0){
                    mViewPager.setAdapter(mPagerAdapter);
                } else {
                    BaseActivity.LocalStack<BaseFragment> stack = getHostActivity().getBackStack();
                    if (stack.size()>1){
                        stack.pop();
                        BaseFragment fragment = stack.peek();
                        getHostActivity().open(fragment);
                    }

                }
            }
        });
        dialog.getBuilder().onNegative(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {

            }
        });
        dialog.show();
    }

    private void initViewPagerSlideListener (ViewPager viewPager){
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                getHostActivity().getToolBar().setTitle((position + 1) + "" + SLASH + mImgPaths.size());
                mDelPosition = position;
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
