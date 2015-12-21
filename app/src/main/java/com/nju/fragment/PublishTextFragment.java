package com.nju.fragment;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nju.activity.R;
import com.nju.adatper.EmotionPageAdater;
import com.nju.http.HttpManager;
import com.nju.http.ResponseCallback;
import com.nju.http.request.PostRequest;
import com.nju.util.Constant;
import com.nju.util.Divice;
import com.nju.util.SchoolFriendLayoutParams;
import com.nju.util.SoftInput;
import com.nju.util.StringBase64;

import java.util.ArrayList;
import java.util.HashMap;

public class PublishTextFragment extends BaseFragment {

    public static final String TAG = PublishTextFragment.class.getSimpleName();
    private Button mSendButton;
    private EditText mContentEditText;
    private TextView mLocationTextView;
    private LinearLayout mMainLayout;
    private LinearLayout mEmoLineLayout;
    private SchoolFriendLayoutParams schoolFriendLayoutParams;
    private ScrollView mScrollView;
    private TextView mEmotionView;
    private boolean isEmotionOpen = true;
    private boolean label = true;
    private ViewPager mViewPager;
    private String mLocation;
    private int mSlidePostion = 0;
    private ArrayList<View> mSlideCircleViews;

    public static PublishTextFragment newInstance() {
        return new PublishTextFragment();
    }



    public PublishTextFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_publish_text, container, false);
        view.setPadding(view.getPaddingLeft(), Divice.getStatusBarHeight(getActivity()), view.getPaddingRight(), view.getPaddingBottom());
        mContentEditText = (EditText) view.findViewById(R.id.publish_content_editText);
        mLocationTextView = (TextView) view.findViewById(R.id.publish_userloaction_text);
        mMainLayout = (LinearLayout) view.findViewById(R.id.publish_text_main_layout);
        mEmoLineLayout = (LinearLayout) view.findViewById(R.id.emotion_layout);
        schoolFriendLayoutParams = new SchoolFriendLayoutParams(getActivity());
        mScrollView = (ScrollView) view.findViewById(R.id.publish_text_scroll_layout);
        mEmotionView = (TextView) view.findViewById(R.id.emotion_icon);
        mViewPager = (ViewPager) view.findViewById(R.id.emotion_pager);
        mViewPager.setAdapter(new EmotionPageAdater(getFragmentManager(), TAG));
        initViewPagerListener();
        initOnGlobalListener();
        initFloaingBn();
        initWhoScanEvent(view);
        initSlideCircleViews(view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getHostActivity().getToolBar().setTitle(getString(R.string.publish_text));
        getHostActivity().getMenuCameraView().setVisibility(View.GONE);
        getHostActivity().getMenuDeleteView().setVisibility(View.GONE);
        mSendButton = getHostActivity().getMenuBn();
        mSendButton.setText(getString(R.string.send));
        mSendButton.setVisibility(View.VISIBLE);
        initSendEvent();
        initTextChangedEvent();
        initOpenLocationEvent();
    }

    public void inputEmotion(String text) {
        int selectionCursor = mContentEditText.getSelectionStart();
        mContentEditText.getText().insert(selectionCursor, text);
        mContentEditText.invalidate();
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

    @Override
    public void onStart() {
        super.onStart();
        if (mLocation!=null) {
            mLocationTextView.setText(mLocation);
            mLocationTextView.invalidate();
        }
    }

    public void setLocation(String location) {
        mLocation = location;
    }

    private void initOpenLocationEvent() {
        mLocationTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHostActivity().open(UserLocationFragment.newInstance());
            }
        });
    }

    private void initWhoScanEvent(View view) {
        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.fragment_publish_text_who_scan_layout);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHostActivity().open(WhoScanFragment.newInstance());
            }
        });
    }

    private void initViewPagerListener() {
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mSlideCircleViews.get(mSlidePostion).setBackground(ContextCompat.getDrawable(getContext(), R.drawable.unselect_circle_label_bg));
                mSlidePostion = position;
                mSlideCircleViews.get(mSlidePostion).setBackground(ContextCompat.getDrawable(getContext(), R.drawable.select_circle_label_bg));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    ResponseCallback callback = new ResponseCallback() {

        @Override
        public void onFail(Exception error) {

        }

        @Override
        public void onSuccess(String responseBody) {

        }
    };
    private void initSendEvent() {
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String content = mContentEditText.getText().toString();
                final String location = mLocationTextView.getText().toString();
                HashMap<String,String> params = new HashMap<>();
                params.put(Constant.USER_ID,String.valueOf(51));
                params.put(Constant.PUBLISH_TEXT, StringBase64.encode(content));
                params.put(Constant.USER_LOCATION,location);
                HttpManager.getInstance().exeRequest(new PostRequest(Constant.BASE_URL + Constant.PUBLISH_TEXT_URL, params, callback));
            }
        });
    }

    private void initFloaingBn() {
        mEmotionView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN && isEmotionOpen) {
                    mEmotionView.setText(getString(R.string.keyboard));
                    SoftInput.close(getActivity(), mEmotionView);
                    isEmotionOpen = false;
                    label = false;
                } else if (event.getAction() == MotionEvent.ACTION_DOWN && !isEmotionOpen) {
                    mEmotionView.setText(getString(R.string.smile));
                    SoftInput.open(getActivity());
                    isEmotionOpen = true;
                    label = false;
                }
                return true;
            }
        });
    }

    private void initTextChangedEvent() {
        mContentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count >= 1) {
                    mSendButton.setAlpha(1.0F);
                    mSendButton.setEnabled(true);
                } else {
                    mSendButton.setAlpha(0.5F);
                    mSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

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
                    mScrollView.setLayoutParams(schoolFriendLayoutParams.noSoftInputParams(subHeight));
                } else  if ((rootHeight - subHeight) < (rootHeight / 3) && isEmotionOpen){
                    label =true;
                } else if ((rootHeight - subHeight) > (rootHeight / 3)) {
                    if (Divice.isPhone()) {
                        mScrollView.setLayoutParams(schoolFriendLayoutParams.softInputParams(subHeight,50,Divice.getStatusBarHeight(getActivity())));
                    } else {
                        mScrollView.setLayoutParams(schoolFriendLayoutParams.softInputParamsFrame(subHeight, 90));
                    }
                    mEmoLineLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

}
