package com.nju.fragment;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

import com.nju.View.SchoolFriendDialog;
import com.nju.activity.R;
import com.nju.adatper.EmotionPageAdapter;
import com.nju.http.HttpManager;
import com.nju.http.ResponseCallback;
import com.nju.http.request.PostRequest;
import com.nju.util.Constant;
import com.nju.util.Divice;
import com.nju.util.SchoolFriendLayoutParams;
import com.nju.util.SoftInput;
import com.nju.util.StringBase64;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class PublishTextFragment extends BaseFragment {
    public static final String TAG = PublishTextFragment.class.getSimpleName();
    private Button mSendButton;
    private EditText mContentEditText;
    private TextView mLocationTextView;
    private SchoolFriendLayoutParams schoolFriendLayoutParams;
    private boolean isEmotionOpen = true;
    private boolean label = true;
    private String mLocation;
    private int mSlidePosition = 0;
    private ArrayList<View> mSlideCircleViews;
    private SchoolFriendDialog mDialog;
    Callback call = new Callback() {
        @Override
        public void onFailure(Request request, IOException e) {
            e.printStackTrace();
            // Log.e(TAG,e.getMessage());
            mDialog.dismiss();
        }

        @Override
        public void onResponse(Response response) throws IOException {
            Log.e(TAG, response.body().string());
            mDialog.dismiss();
        }
    };
    ResponseCallback callback = new ResponseCallback() {

        @Override
        public void onFail(Exception error) {
            mDialog.dismiss();
        }

        @Override
        public void onSuccess(String responseBody) {
            mDialog.dismiss();
        }
    };

    public PublishTextFragment() {
    }

    public static PublishTextFragment newInstance() {
        return new PublishTextFragment();
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
        schoolFriendLayoutParams = new SchoolFriendLayoutParams(getActivity());
        initViewPager(view);
        initOnGlobalListener(view);
        initFloatingBn(view);
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
        if (mLocation != null) {
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

    private void initViewPager(View view) {
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.emotion_pager);
        viewPager.setAdapter(new EmotionPageAdapter(getFragmentManager(), TAG));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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

    private void initSendEvent() {
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog = SchoolFriendDialog.showProgressDialogNoTitle(getContext(), getString(R.string.uploading));
                mDialog.show();
                SoftInput.close(getContext(), mSendButton);
                final String content = mContentEditText.getText().toString();
                final String location = mLocationTextView.getText().toString();
                HashMap<String, String> params = new HashMap<>();
                params.put(Constant.USER_ID, String.valueOf(51));
                params.put(Constant.PUBLISH_TEXT, StringBase64.encode(content));
                params.put(Constant.USER_LOCATION, location);
                HttpManager.getInstance().exeRequest(new PostRequest(Constant.BASE_URL + Constant.PUBLISH_TEXT_URL, params, call, TAG));
            }
        });
    }

    private void initFloatingBn(View view) {
        final TextView emotionView = (TextView) view.findViewById(R.id.emotion_icon);
        emotionView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN && isEmotionOpen) {
                    emotionView.setText(getString(R.string.keyboard));
                    SoftInput.close(getActivity(), emotionView);
                    isEmotionOpen = false;
                    label = false;
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    emotionView.setText(getString(R.string.smile));
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

    private void initOnGlobalListener(View view) {
        final LinearLayout mainLayout = (LinearLayout) view.findViewById(R.id.publish_text_main_layout);
        final ScrollView scrollView = (ScrollView) view.findViewById(R.id.publish_text_scroll_layout);
        final LinearLayout emoLineLayout = (LinearLayout) view.findViewById(R.id.emotion_layout);
        mainLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int rootHeight = mainLayout.getRootView().getHeight();
                int subHeight = mainLayout.getHeight();
                if ((rootHeight - subHeight) < (rootHeight / 3) && label) {
                    emoLineLayout.setVisibility(View.GONE);
                    scrollView.setLayoutParams(schoolFriendLayoutParams.noSoftInputParams(subHeight));
                } else if ((rootHeight - subHeight) < (rootHeight / 3) && isEmotionOpen) {
                    label = true;
                } else if ((rootHeight - subHeight) > (rootHeight / 3)) {
                    if (getHostActivity().isPhone()) {
                        scrollView.setLayoutParams(schoolFriendLayoutParams.softInputParams(subHeight, 50, Divice.getStatusBarHeight(getActivity())));
                    } else {
                        scrollView.setLayoutParams(schoolFriendLayoutParams.softInputParamsFrame(subHeight, 90));
                    }
                    emoLineLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

}
