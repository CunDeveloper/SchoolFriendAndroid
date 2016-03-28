package com.nju.fragment;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.nju.activity.R;
import com.nju.adatper.CommentAdapter;
import com.nju.model.AlumniQuestion;
import com.nju.test.TestData;
import com.nju.util.Constant;
import com.nju.util.DateUtil;
import com.nju.util.Divice;
import com.nju.util.StringBase64;


public class MajorAskDetailFragment extends BaseFragment {
    private static final String PARAM_KEY = "questionKey";
    private AlumniQuestion mAlumniQuestion;
    public static MajorAskDetailFragment newInstance(AlumniQuestion alumniQuestion) {
        MajorAskDetailFragment fragment = new MajorAskDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(PARAM_KEY,alumniQuestion);
        fragment.setArguments(args);
        return fragment;
    }

    public MajorAskDetailFragment() {
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
        }
        getHostActivity().display(6);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_major_ask_detail, container, false);
        view.setPadding(view.getPaddingLeft(), Divice.getStatusBarHeight(getContext()),view.getPaddingRight(),view.getPaddingBottom());
        initView(view);
        return view;
    }

    private void initView(View view){
        TextView problemTV = (TextView) view.findViewById(R.id.problem_tv);
        try{
            problemTV.setText(StringBase64.decode(mAlumniQuestion.getProblem()));
        }catch (IllegalArgumentException e){
            problemTV.setText(Constant.UNKNOWN_CHARACTER);
        }
        TextView descTV = (TextView) view.findViewById(R.id.description_tv);

        try{
            descTV.setText(StringBase64.decode(mAlumniQuestion.getDescription()));
        }catch (IllegalArgumentException e){
            descTV.setText(Constant.UNKNOWN_CHARACTER);
        }
        TextView nameTV = (TextView) view.findViewById(R.id.name_tv);
        nameTV.setText(mAlumniQuestion.getAuthor().getAuthorName());
        TextView dateTV = (TextView) view.findViewById(R.id.date_tv);
        dateTV.setText(DateUtil.getRelativeTimeSpanString(mAlumniQuestion.getDate()));
        ListView commentListView = (ListView) view.findViewById(R.id.new_comment_listview);
        commentListView.setAdapter(new CommentAdapter(getContext(), TestData.getComments()));
    }
}
