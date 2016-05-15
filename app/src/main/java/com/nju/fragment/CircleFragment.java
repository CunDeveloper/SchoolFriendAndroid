package com.nju.fragment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nju.activity.R;
import com.nju.model.AuthorInfo;

import java.util.ArrayList;

public class CircleFragment extends BaseFragment {

    private static final String TAG = CircleFragment.class.getSimpleName();
    private static final String PARAM_AUTHOR = "authorInfo";
    private AuthorInfo mAuthorInfo;

    public CircleFragment() {
        // Required empty public constructor
    }

    public static CircleFragment newInstance(AuthorInfo authorInfo) {
        CircleFragment fragment = new CircleFragment();
        Bundle args = new Bundle();
        args.putParcelable(PARAM_AUTHOR, authorInfo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mAuthorInfo = getArguments().getParcelable(PARAM_AUTHOR);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(mAuthorInfo.getAuthorName());
        }
        getHostActivity().display(7);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_circle, container, false);
        // view.setPadding(view.getPaddingLeft(), Divice.getStatusBarHeight(getContext()), view.getPaddingRight(), view.getPaddingBottom());
        open(MyDynamicFragment.newInstance(mAuthorInfo));
        initView(view);
        return view;
    }

    private void open(BaseFragment fragment){
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mContainer, fragment);
        ft.commit();
    }


    private void initView(View view) {
        FloatingActionButton floatBn = (FloatingActionButton) view.findViewById(R.id.college_choose_dialog_actionBn);
        View transView = view.findViewById(R.id.transView);
        final RelativeLayout circleToolLayout = (RelativeLayout) view.findViewById(R.id.circleMain);
        floatBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circleToolLayout.setVisibility(View.VISIBLE);
            }
        });
        transView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circleToolLayout.setVisibility(View.GONE);
            }
        });
        RelativeLayout dynamicLayout = (RelativeLayout) view.findViewById(R.id.dynamci_reLayout);
        TextView dynamicIconTV = (TextView) view.findViewById(R.id.dynamicIcon);
        TextView dynamicTextTV = (TextView) view.findViewById(R.id.dynamicText);

        TextView askIconTV = (TextView) view.findViewById(R.id.askIcon);
        TextView askTextTV = (TextView) view.findViewById(R.id.askText);

        TextView voiceIconTV = (TextView) view.findViewById(R.id.voiceIcon);
        TextView voiceTextTV = (TextView) view.findViewById(R.id.voiceText);

        TextView recommendIconTV = (TextView) view.findViewById(R.id.recommendIcon);
        TextView recommendTextTV = (TextView) view.findViewById(R.id.recommendText);

        final ArrayList<TextView> toolTvs = new ArrayList<>();
        toolTvs.add(dynamicIconTV);toolTvs.add(dynamicTextTV);
        toolTvs.add(askIconTV);toolTvs.add(askTextTV);
        toolTvs.add(voiceIconTV);toolTvs.add(voiceTextTV);
        toolTvs.add(recommendIconTV);toolTvs.add(recommendTextTV);

        dynamicLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i<toolTvs.size();i++){
                    if (i == 0 || i==1){
                        toolTvs.get(i).setTextColor(ContextCompat.getColor(getContext(),R.color.colorPrimaryDark));
                    } else {
                        toolTvs.get(i).setTextColor(ContextCompat.getColor(getContext(),R.color.black80));
                    }
                }
                open(MyDynamicFragment.newInstance(mAuthorInfo));
            }
        });
        RelativeLayout askLayout = (RelativeLayout) view.findViewById(R.id.ask_reLayout);
        askLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i<toolTvs.size();i++){
                    if (i == 2 || i==3){
                        toolTvs.get(i).setTextColor(ContextCompat.getColor(getContext(),R.color.colorPrimaryDark));
                    } else {
                        toolTvs.get(i).setTextColor(ContextCompat.getColor(getContext(),R.color.black80));
                    }
                }
                open(MyAskFragment.newInstance(mAuthorInfo));
            }
        });
        RelativeLayout voiceLayout = (RelativeLayout) view.findViewById(R.id.voice_reLayout);
        voiceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i<toolTvs.size();i++){
                    if (i == 4 || i==5){
                        toolTvs.get(i).setTextColor(ContextCompat.getColor(getContext(),R.color.colorPrimaryDark));
                    } else {
                        toolTvs.get(i).setTextColor(ContextCompat.getColor(getContext(),R.color.black80));
                    }
                }
                open(MyVoiceFragment.newInstance(mAuthorInfo));
            }
        });
        final RelativeLayout recommendLayout = (RelativeLayout) view.findViewById(R.id.recommend_Layout);
        recommendLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i<toolTvs.size();i++){
                    if (i == 6 || i==7){
                        toolTvs.get(i).setTextColor(ContextCompat.getColor(getContext(),R.color.colorPrimaryDark));
                    } else {
                        toolTvs.get(i).setTextColor(ContextCompat.getColor(getContext(),R.color.black80));
                    }
                }
                open(MyRecommendFragment.newInstance(mAuthorInfo));
            }
        });
    }



}
