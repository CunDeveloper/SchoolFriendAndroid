package com.nju.fragment;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nju.activity.R;
import com.nju.adatper.EmotionPageAdapter;
import com.nju.util.Divice;
import com.nju.util.SchoolFriendLayoutParams;
import com.nju.util.SoftInput;

import java.util.ArrayList;

import model.Content;


public class PersonCircleDetailFragment extends BaseFragment {
    public static final String TAG = PersonCircleDetailFragment.class.getSimpleName();
    private Content mContent;
    private static final String CONTENT_PARA = "content_para";
    private RelativeLayout mInputLayout;
    private LinearLayout mMainLayout;
    private boolean label = true;
    private ScrollView mScrollView;
    private SchoolFriendLayoutParams mSchoolFriendLayoutParams;
    private TextView mEmotionTextView;
    private boolean isEmotionOpen = true;
    private LinearLayout mEmoLineLayout;
    private ViewPager mViewPager;
    private ArrayList<View> mSlideCircleViews;
    private int mSlidePosition = 0;
    private static final int INPUT_HEIGHT_PHONE = 60;
    private  int bottom_height;

    public static PersonCircleDetailFragment newInstance(Content content) {
        PersonCircleDetailFragment fragment = new PersonCircleDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(CONTENT_PARA,content);
        fragment.setArguments(args);
        return fragment;
    }

    public PersonCircleDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mContent = getArguments().getParcelable(CONTENT_PARA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_person_circle_detail, container, false);
        view.setPadding(view.getPaddingLeft(), Divice.getStatusBarHeight(getContext()),view.getPaddingRight(),view.getPaddingBottom());
        mInputLayout = (RelativeLayout) view.findViewById(R.id.activity_school_friend_input_layout);
        mMainLayout = (LinearLayout) view.findViewById(R.id.fragment_person_circle_detail_main_layout);
        mScrollView = (ScrollView) view.findViewById(R.id.fragment_person_circle_detail_scrollview);
        mEmotionTextView = (TextView) view.findViewById(R.id.fragment_alumni_circle_emotion);
        mViewPager = (ViewPager)view.findViewById(R.id.emotion_pager);
        mEmoLineLayout = (LinearLayout)view.findViewById(R.id.emotion_layout);
        mViewPager.setAdapter(new EmotionPageAdapter(getFragmentManager(), TAG));
        mSchoolFriendLayoutParams = new SchoolFriendLayoutParams(getContext());
        bottom_height =  (int) Divice.convertDpToPixel(INPUT_HEIGHT_PHONE,getContext())+Divice.getStatusBarHeight(getActivity());
        mInputLayout.setVisibility(View.VISIBLE);
        initView(view);
        initSlideCircleViews(view);
        initOnGlobalListener();
        initEmotionTextViewEvent();
        initViewPageScrollListener();
        return view;
    }

    private void initSlideCircleViews(View view) {
        View mView;
        mSlideCircleViews = new ArrayList<>();
        mView = view.findViewById(R.id.emotion_pager_view1);
        mSlideCircleViews.add(mView);
        mView = view.findViewById(R.id.emotion_pager_view2);
        mSlideCircleViews.add(mView);
        mView = view.findViewById(R.id.emotion_pager_view3);
        mSlideCircleViews.add(mView);
    }

    private void initView(View view) {
        TextView contentTV = (TextView) view.findViewById(R.id.school_friend_item_content);
        contentTV.setText(mContent.getContent());
        ImageView imageView = (ImageView) view.findViewById(R.id.school_friend_item_headicon_img);
        imageView.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.head2));
        TextView nameTV = (TextView) view.findViewById(R.id.school_friend_item_name_text);
        nameTV.setText("张小军");
        TextView labelTV = (TextView) view.findViewById(R.id.school_friend_item_label_text);
        labelTV.setText("南京大学软件学院 2014");

        TextView dateTV = (TextView) view.findViewById(R.id.school_friend_item_publish_date);
        dateTV.setText(mContent.getDate());
    }

    private void initViewPageScrollListener() {
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mSlideCircleViews.get(mSlidePosition).setBackground(ContextCompat.getDrawable(getContext(), R.drawable.unselect_circle_label_bg));
                mSlidePosition = position;
                mSlideCircleViews.get(mSlidePosition).setBackground(ContextCompat.getDrawable(getContext(), R.drawable.select_circle_label_bg));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initOnGlobalListener() {
        mMainLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int rootHeight = mMainLayout.getRootView().getHeight();
                int subHeight = mMainLayout.getHeight();
                if ((rootHeight - subHeight) < (rootHeight / 3) && label) {
                    mEmoLineLayout.setVisibility(View.GONE);
                    mScrollView.setLayoutParams(mSchoolFriendLayoutParams.noSoftInputParams((subHeight -bottom_height)));
                } else if ((rootHeight - subHeight) < (rootHeight / 3) && isEmotionOpen) {
                    label = true;
                } else if ((rootHeight - subHeight) > (rootHeight / 3)) {
                    if (getHostActivity().isPhone()) {
                        mScrollView.setLayoutParams(mSchoolFriendLayoutParams.softInputParams(subHeight, INPUT_HEIGHT_PHONE, Divice.getStatusBarHeight(getActivity())));
                    } else {
                        mScrollView.setLayoutParams(mSchoolFriendLayoutParams.softInputParamsFrame(subHeight, 90));
                    }
                     mEmoLineLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void initEmotionTextViewEvent() {
        mEmotionTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN && isEmotionOpen) {
                    mEmotionTextView.setText(getString(R.string.keyboard));
                    SoftInput.close(getActivity(), mEmotionTextView);
                    isEmotionOpen = false;
                    label = false;
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mEmotionTextView.setText(getString(R.string.smile));
                    SoftInput.open(getActivity());
                    isEmotionOpen = true;
                    label = false;
                }
                return true;
            }
        });
    }


}
