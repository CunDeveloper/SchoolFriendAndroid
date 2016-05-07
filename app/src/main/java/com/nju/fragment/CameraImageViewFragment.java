package com.nju.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

import com.nju.activity.R;
import com.nju.adatper.OriginPicViewPagerAdapter;
import com.nju.model.Image;
import com.nju.util.AsyncCompress;
import com.nju.util.Divice;
import com.nju.util.ToastUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;


public class CameraImageViewFragment extends BaseFragment {

    private static final String TAG = CameraImageViewFragment.class.getSimpleName();
    private static final String POSITION = "position";
    private static final String LABEL = "label";
    private static final String SUB_LABEL = "subLabel";
    private static final String LEFT_BRACKET = "(";
    private static final String RIGHT_BRACKET = ")";
    private static final String SLASH = "/";
    private static final int ADD_PIC_IN_ALL = 205;
    private static final int SUB_PIC_IN_ALL = 206;
    private static final float HALF_ALPHA = 0.5F;
    private static final int MOST_PICS = 9;
    private static final int ADD_PIC = 203;
    private static final int SUB_PIC = 204;
    private final ArrayList<Image> mChoosePic = new ArrayList<>();
    private final ArrayList<Image> mChoosePicInAll = new ArrayList<>();
    private final HashSet<Integer> mChooseIndex = new HashSet<>();
    private final HashSet<Integer> mChooseIndexInAll = new HashSet<>();
    private final Handler mHandler = new MyHandler(this);
    private ViewPager mViewPager;
    private ArrayList<Image> mImages;
    private int mPosition;
    private Button mFinishButton;
    private CheckBox mCheckBox;
    private String mLabel;//
    private String mSubLabel;
    private RelativeLayout mBottomLayout;

    public CameraImageViewFragment() {
    }

    public static CameraImageViewFragment newInstance(ArrayList<Image> imgPaths, int position, String label, String subLabel) {
        CameraImageViewFragment fragment = new CameraImageViewFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(TAG, imgPaths);
        args.putInt(POSITION, position);
        args.putString(LABEL, label);
        args.putString(SUB_LABEL, subLabel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mImages = getArguments().getParcelableArrayList(TAG);
            mPosition = getArguments().getInt(POSITION);
            mLabel = getArguments().getString(LABEL);
            mSubLabel = getArguments().getString(SUB_LABEL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera_image_view, container, false);
        view.setPadding(view.getPaddingLeft(), Divice.getStatusBarHeight(getActivity()), view.getPaddingRight(), view.getPaddingBottom());
        mCheckBox = (CheckBox) view.findViewById(R.id.fragment_camera_image_view_checkBox);
        mViewPager = (ViewPager) view.findViewById(R.id.fragment_camera_imgage_view_pager);
        mBottomLayout = (RelativeLayout) view.findViewById(R.id.fragment_camera_image_view_bottom_layout);
        mViewPager.setAdapter(new OriginPicViewPagerAdapter(getFragmentManager(), mImages));
        mViewPager.setCurrentItem(mPosition);
        initViewPagerSlideListener();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getHostActivity().getToolBar().setTitle((mPosition + 1) + "" + SLASH + mImages.size());
        getHostActivity().getMenuCameraView().setVisibility(View.GONE);
        getHostActivity().getMenuDeleteView().setVisibility(View.GONE);
        mFinishButton = getHostActivity().getMenuBn();
        mFinishButton.setVisibility(View.VISIBLE);
        dependLabelConditionInit();
        initFinishEvent();
        initCheckBoxEvent();
    }

    private void dependLabelConditionInit() {
        if (mLabel.equals(getString(R.string.choosedReview))) {
            mCheckBox.setChecked(true);
            setFinishBn(getString(R.string.finish) + LEFT_BRACKET + mImages.size() + SLASH + MOST_PICS + RIGHT_BRACKET, true, 1);
            mChoosePic.addAll(mImages);
            for (int i = 0; i < mImages.size(); i++) {
                mChooseIndex.add(i);
            }
        } else if (mLabel.equals(getString(R.string.capture_image))) {
            setFinishBn(getString(R.string.finish), true, 1);
            hideBottomLayout();
        } else if (mLabel.equals(getString(R.string.allPicsReview))) {
            setFinishBn(getString(R.string.finish), false, HALF_ALPHA);
            mCheckBox.setChecked(false);
            for (int i = 0; i < mImages.size(); i++) {
                mChooseIndexInAll.add(i);
            }
        }
    }

    private void setFinishBn(final String text, final boolean enabled, final float alpha) {
        mFinishButton.setText(text);
        mFinishButton.setEnabled(enabled);
        mFinishButton.setAlpha(alpha);
    }

    @SuppressWarnings("unchecked")
    private void initFinishEvent() {
        mFinishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mLabel.equals(getString(R.string.capture_image))) {
                    new AsyncCompress(CameraImageViewFragment.this, mSubLabel).execute(mImages);
                } else if (mLabel.equals(getString(R.string.choosedReview))) {
                    new AsyncCompress(CameraImageViewFragment.this, mSubLabel).execute(mChoosePic);
                } else if (mLabel.equals(getString(R.string.allPicsReview))) {
                    new AsyncCompress(CameraImageViewFragment.this, mSubLabel).execute(mChoosePicInAll);
                }
            }
        });
    }

    private void initCheckBoxEvent() {

        mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dependLabelCheck();
            }
        });
    }

    private void dependLabelCheck() {
        if (mLabel.equals(getString(R.string.choosedReview))) {
            Message message = new Message();
            if (mCheckBox.isChecked()) {
                message.what = ADD_PIC;
                message.obj = mViewPager.getCurrentItem();
                mChooseIndex.add(mViewPager.getCurrentItem());
                mHandler.sendMessage(message);
            } else {
                message.what = SUB_PIC;
                message.obj = mViewPager.getCurrentItem();
                mChooseIndex.remove(mViewPager.getCurrentItem());
                mHandler.sendMessage(message);
            }
        } else if (mLabel.equals(getString(R.string.allPicsReview))) {
            Message message = new Message();
            if (mCheckBox.isChecked()) {
                message.what = ADD_PIC_IN_ALL;
                message.obj = mViewPager.getCurrentItem();
                mChooseIndexInAll.remove(mViewPager.getCurrentItem());
                mHandler.sendMessage(message);
            } else {
                message.what = SUB_PIC_IN_ALL;
                message.obj = mViewPager.getCurrentItem();
                int size = mChoosePicInAll.size();
                if (size != MOST_PICS) {
                    mChooseIndexInAll.add(mViewPager.getCurrentItem());
                }
                mHandler.sendMessage(message);
            }
        }
    }

    private void initViewPagerSlideListener() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                dependLabelSlide(position);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void dependLabelSlide(int position) {
        if (mLabel.equals(getString(R.string.choosedReview))) {
            getHostActivity().getToolBar().setTitle((position + 1) + "" + SLASH + mImages.size());
            if (mChooseIndex.contains(position)) {
                mCheckBox.setChecked(true);
            } else {
                mCheckBox.setChecked(false);
            }
        } else if (mLabel.equals(getString(R.string.allPicsReview))) {
            if (mChooseIndexInAll.contains(position)) {
                mCheckBox.setChecked(false);
            } else {
                mCheckBox.setChecked(true);
            }
        }
    }

    public void hideBottomLayout() {
        mBottomLayout.setVisibility(View.GONE);
    }

    public void showBottomLayout() {
        mBottomLayout.setVisibility(View.VISIBLE);
    }

    private static class MyHandler extends Handler {

        private final WeakReference<CameraImageViewFragment> mCameraImageViewFragment;

        private MyHandler(CameraImageViewFragment cameraImageViewFragment) {
            this.mCameraImageViewFragment = new WeakReference<>(cameraImageViewFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            CameraImageViewFragment fragment = mCameraImageViewFragment.get();
            if (fragment != null) {
                super.handleMessage(msg);
                if (fragment.mLabel.equals(fragment.getString(R.string.choosedReview))) {
                    reviewImage(fragment, msg);
                } else if (fragment.mLabel.equals(fragment.getString(R.string.allPicsReview))) {
                    reviewAllImage(fragment, msg);
                }

            }
        }

        private void reviewImage(CameraImageViewFragment fragment, Message msg) {
            if (msg.what == ADD_PIC) {
                fragment.mChoosePic.add(fragment.mImages.get((Integer) msg.obj));
                fragment.setFinishBn(fragment.getString(R.string.finish) + LEFT_BRACKET + (fragment.mChoosePic.size()) + SLASH + MOST_PICS + RIGHT_BRACKET,
                        true, 1);
            } else if (msg.what == SUB_PIC) {
                fragment.mChoosePic.remove(fragment.mImages.get((Integer) msg.obj));
                int size = fragment.mChoosePic.size();
                if (size == 0) {
                    fragment.setFinishBn(fragment.getString(R.string.finish), false, HALF_ALPHA);
                } else {
                    fragment.setFinishBn(fragment.getString(R.string.finish) + LEFT_BRACKET + fragment.mChoosePic.size() + SLASH + MOST_PICS + RIGHT_BRACKET, true, 1);
                }

            }
        }

        private void reviewAllImage(CameraImageViewFragment fragment, Message msg) {
            if (msg.what == ADD_PIC_IN_ALL) {
                int size = fragment.mChoosePicInAll.size();
                if (size == MOST_PICS) {
                    ToastUtil.showShortText(fragment.getContext(), fragment.getString(R.string.most_choose_nine_pics));
                } else {
                    fragment.mChoosePicInAll.add(fragment.mImages.get((Integer) msg.obj));
                    fragment.setFinishBn(fragment.getString(R.string.finish) + LEFT_BRACKET + fragment.mChoosePicInAll.size() + SLASH + MOST_PICS + RIGHT_BRACKET,
                            true, 1);
                }
            } else if (msg.what == SUB_PIC_IN_ALL) {
                fragment.mChoosePicInAll.remove(fragment.mImages.get((Integer) msg.obj));
                int size = fragment.mChoosePicInAll.size();
                if (size == 0) {
                    fragment.setFinishBn(fragment.getString(R.string.finish), false, HALF_ALPHA);
                } else {
                    fragment.setFinishBn(fragment.getString(R.string.finish) + LEFT_BRACKET + size + SLASH + MOST_PICS + RIGHT_BRACKET, true, 1);
                }

            }
        }

    }
}
