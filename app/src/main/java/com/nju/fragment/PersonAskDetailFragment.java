package com.nju.fragment;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nju.activity.R;
import com.nju.model.AlumniQuestion;
import com.nju.util.Divice;


public class PersonAskDetailFragment extends BaseFragment {
    private static final String PARAM_KEY = "questionKey";
    private AlumniQuestion mAlumniQuestion;
    public static PersonAskDetailFragment newInstance(AlumniQuestion alumniQuestion) {
        PersonAskDetailFragment fragment = new PersonAskDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(PARAM_KEY,alumniQuestion);
        fragment.setArguments(args);
        return fragment;
    }

    public PersonAskDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mAlumniQuestion = getArguments().getParcelable(PARAM_KEY);
        }
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.own_major_ask);
        }
        getHostActivity().display(5);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_person_ask_detail, container, false);
        view.setPadding(view.getPaddingLeft(), Divice.getStatusBarHeight(getContext()),view.getPaddingRight(),view.getPaddingBottom());
        initView(view);
        return view;
    }

    private void initView(View view){
        TextView problemTV = (TextView) view.findViewById(R.id.problem_tv);
        problemTV.setText(mAlumniQuestion.getProblem());
        TextView descTV = (TextView) view.findViewById(R.id.description_tv);
        descTV.setText(mAlumniQuestion.getDescription());
        TextView nameTV = (TextView) view.findViewById(R.id.name_tv);
        nameTV.setText(mAlumniQuestion.getAuthorInfo().getAuthorName());
        TextView dateTV = (TextView) view.findViewById(R.id.date_tv);
        dateTV.setText(mAlumniQuestion.getDate());
    }

}
