package com.nju.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.nju.activity.R;
import com.nju.adatper.EmotionAdapter;
import com.nju.util.Emotion;

import java.util.ArrayList;

public class EmotionPagerFragment extends BaseFragment {

    public static final String TAG = EmotionPagerFragment.class.getSimpleName();
    private static final String POSITION = "param";
    private static final String APG_PARAM1 = "param1";
    private static String mTag;
    private static ArrayList<ArrayList<String>> mEmotions;
    private GridView mGridView;
    private ArrayList<String> mSubEmotions;
    private int mPosition;

    public EmotionPagerFragment() {
    }

    public static EmotionPagerFragment newInstance(int position, String tag) {
        EmotionPagerFragment fragment = new EmotionPagerFragment();
        Bundle args = new Bundle();
        args.putInt(POSITION, position);
        args.putString(APG_PARAM1, tag);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTag = getArguments().getString(APG_PARAM1);
            mPosition = getArguments().getInt(POSITION);
        }
        if (mEmotions == null) {
            mEmotions = Emotion.emotions();
        }
        mSubEmotions = mEmotions.get(mPosition);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_emotion_pager, container, false);
        mGridView = (GridView) view.findViewById(R.id.fragment_emotion_pager_gridview);
        initGridViewClickListener();
        return view;
    }

    private void initGridViewClickListener() {
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mTag.equals(PublishTextFragment.TAG)) {
                    PublishTextFragment fragment = (PublishTextFragment) getHostActivity().getBackStack().peek();
                    if (fragment != null) {
                        fragment.inputEmotion(mSubEmotions.get(position));
                    }
                } else if (mTag.equals(AlumniCircleFragment.TAG)) {
                    AlumniCircleFragment fragment = (AlumniCircleFragment) getHostActivity().getBackStack().peek();
                    if (fragment != null) {
                        fragment.inputEmotion(mSubEmotions.get(position));
                    }
                } else if (mTag.equals(PublishTextWithPicsFragment.TAG)) {
                    PublishTextWithPicsFragment fragment = (PublishTextWithPicsFragment) getHostActivity().getBackStack().peek();
                    if (fragment != null) {
                        fragment.inputEmotion(mSubEmotions.get(position));
                    }
                } else if (mTag.equals(AlumniVoiceItemDetailFragment.TAG)) {
                    AlumniVoiceItemDetailFragment fragment = (AlumniVoiceItemDetailFragment) getHostActivity().getBackStack().peek();
                    if (fragment != null) {
                        fragment.inputEmotion(mSubEmotions.get(position));
                    }
                } else if (mTag.equals(RecommendWorkItemDetailFragment.TAG)) {
                    RecommendWorkItemDetailFragment fragment = (RecommendWorkItemDetailFragment) getHostActivity().getBackStack().peek();
                    if (fragment != null) {
                        fragment.inputEmotion(mSubEmotions.get(position));
                    }
                } else if (mTag.equals(RecommendPublishFragment.TAG)) {
                    RecommendPublishFragment fragment = (RecommendPublishFragment) getHostActivity().getBackStack().peek();
                    if (fragment != null) {
                        fragment.inputEmotion(mSubEmotions.get(position));
                    }
                } else if (mTag.equals(AskPublishFragment.TAG)) {
                    AskPublishFragment fragment = (AskPublishFragment) getHostActivity().getBackStack().peek();
                    if (fragment != null) {
                        fragment.inputEmotion(mSubEmotions.get(position));
                    }
                } else if (mTag.equals(PublishVoiceFragment.TAG)) {
                    PublishVoiceFragment fragment = (PublishVoiceFragment) getHostActivity().getBackStack().peek();
                    if (fragment != null) {
                        fragment.inputEmotion(mSubEmotions.get(position));
                    }
                } else if (mTag.equals(PublishDynamicFragment.TAG)) {
                    PublishDynamicFragment fragment = (PublishDynamicFragment) getHostActivity().getBackStack().peek();
                    if (fragment != null) {
                        fragment.inputEmotion(mSubEmotions.get(position));
                    }
                } else if (mTag.equals(MajorAskDetailFragment.TAG)) {
                    MajorAskDetailFragment fragment = (MajorAskDetailFragment) getHostActivity().getBackStack().peek();
                    if (fragment != null) {
                        fragment.inputEmotion(mSubEmotions.get(position));
                    }
                } else if (mTag.equals(PersonRecommendWorkItemDetailFragment.TAG)) {
                    PersonRecommendWorkItemDetailFragment fragment = (PersonRecommendWorkItemDetailFragment) getHostActivity().getBackStack().peek();
                    if (fragment != null) {
                        fragment.inputEmotion(mSubEmotions.get(position));
                    }
                } else if (mTag.equals(PersonAlumniVoiceItemDetail.TAG)) {
                    PersonAlumniVoiceItemDetail fragment = (PersonAlumniVoiceItemDetail) getHostActivity().getBackStack().peek();
                    if (fragment != null) {
                        fragment.inputEmotion(mSubEmotions.get(position));
                    }
                } else if (mTag.equals(PersonAskDetailFragment.TAG)) {
                    PersonAskDetailFragment fragment = (PersonAskDetailFragment) getHostActivity().getBackStack().peek();
                    if (fragment != null) {
                        fragment.inputEmotion(mSubEmotions.get(position));
                    }
                } else if (mTag.equals(AlumniDynamicFragment.TAG)) {
                    AlumniDynamicFragment fragment = (AlumniDynamicFragment) getHostActivity().getBackStack().peek();
                    if (fragment != null) {
                        fragment.inputEmotion(mSubEmotions.get(position));
                    }
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mGridView.setAdapter(new EmotionAdapter(mSubEmotions, getActivity()));
    }

}
