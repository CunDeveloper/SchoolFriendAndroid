package com.nju.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nju.activity.R;
import com.nju.adatper.EmotionPageAdapter;
import com.nju.util.Divice;
import com.nju.util.SoftInput;

public class RecommendPublishFragment extends BaseFragment {

    private static final String TAG = RecommendPublishFragment.class.getSimpleName();
    private static final String PARAM_TITLE = "paramTitle";
    private static String mTitle;
    public static RecommendPublishFragment newInstance(String title) {
        RecommendPublishFragment fragment = new RecommendPublishFragment();
        Bundle args = new Bundle();
        args.putString(PARAM_TITLE,title);
        fragment.setArguments(args);
        return fragment;
    }

    public RecommendPublishFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTitle = getArguments().getString(PARAM_TITLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recommend_publish, container, false);
        view.setPadding(view.getPaddingLeft(), Divice.getStatusBarHeight(getContext()), view.getPaddingRight(), view.getPaddingBottom());
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(mTitle);
        }
        getHostActivity().display(0);
        getHostActivity().getMenuBn().setText(getString(R.string.publish));
    }

    private void initView(View view){
        final RelativeLayout emoLayout = (RelativeLayout) view.findViewById(R.id.emotion_layout);
        final FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.comment_input_emotion_main);
        final TextView emotionIconTV = (TextView) view.findViewById(R.id.emotion_icon);
        EditText contentEditText = (EditText) view.findViewById(R.id.content_editText);
        contentEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                emoLayout.setVisibility(View.VISIBLE);
            }
        });
        contentEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emoLayout.setVisibility(View.VISIBLE);
            }
        });

        EditText titleEditText = (EditText) view.findViewById(R.id.title);
        titleEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                emoLayout.setVisibility(View.VISIBLE);
            }
        });
        titleEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emotionIconTV.setText(getString(R.string.smile));
                emoLayout.setVisibility(View.VISIBLE);
            }
        });

        final View transView = view.findViewById(R.id.emotion_transparent);
        transView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoftInput.close(getContext(),transView);
                emoLayout.setVisibility(View.GONE);
                frameLayout.setVisibility(View.GONE);
            }
        });

        emotionIconTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (frameLayout.getVisibility() == View.GONE){
                    emotionIconTV.setText(getString(R.string.keyboard));
                    frameLayout.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            frameLayout.setVisibility(View.VISIBLE);
                        }
                    }, 200);
                    SoftInput.close(getContext(),emotionIconTV);
                }else {
                    emotionIconTV.setText(getString(R.string.smile));
                    SoftInput.open(getContext());
                    frameLayout.setVisibility(View.GONE);
                }
            }
        });

        final ViewPager emoViewPage = (ViewPager) view.findViewById(R.id.viewpager);
        emoViewPage.setAdapter(new EmotionPageAdapter(getFragmentManager(),TAG));

    }

}
