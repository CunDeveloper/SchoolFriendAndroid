package com.nju.fragment;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import com.nju.activity.R;
import com.nju.adatper.EmotionAdatper;
import com.nju.util.Emotion;
import java.util.List;

public class EmotionPagerFragment extends Fragment {

    private static final String ARG_PARAM = "param";

    private GridView mGridView;
    private List<Drawable> mEmotions;
    private OnFragmentInputEmotionListener mListener;
    public static EmotionPagerFragment newInstance(int position) {
        EmotionPagerFragment fragment = new EmotionPagerFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM, position);
        fragment.setArguments(args);
        return fragment;
    }
    public EmotionPagerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEmotions = new Emotion(getActivity()).getEmotions();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_emotion_pager, container, false);
        mGridView = (GridView) view.findViewById(R.id.fragment_emotion_pager_gridview);
        initGridViewClickListener();
        return view;
    }
    private void initGridViewClickListener(){
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mListener.onFragmentInputEmotion(mEmotions.get(position));
            }
        });
    }
    @Override
    public void onStart(){
        super.onStart();
        mGridView.setAdapter(new EmotionAdatper(mEmotions, getActivity()));
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInputEmotionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInputEmotionListener {
        void onFragmentInputEmotion(Drawable drawable);
    }

}
