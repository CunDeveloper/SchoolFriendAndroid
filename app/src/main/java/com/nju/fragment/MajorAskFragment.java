package com.nju.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nju.activity.R;
import com.nju.adatper.MajorAskAdapter;
import com.nju.model.AlumniQuestion;
import com.nju.test.TestData;
import com.nju.util.Divice;

import java.util.ArrayList;


public class MajorAskFragment extends BaseFragment {

    private ArrayList<AlumniQuestion> questions;

    public static MajorAskFragment newInstance( ) {
        MajorAskFragment fragment = new MajorAskFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public MajorAskFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

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
        getHostActivity().display(2);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_major_ask, container, false);
        view.setPadding(view.getPaddingLeft(), Divice.getStatusBarHeight(getContext()), view.getPaddingRight(), view.getPaddingBottom());
        initListView(view);
        setUpOnRefreshListener(view);
        return view;
    }

    private void setUpOnRefreshListener(View view){
        final SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateMajorAsk();
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void updateMajorAsk() {

    }

    private void initListView(View view){
        questions = TestData.getQlumniQuestions();
        ListView listView = (ListView) view.findViewById(R.id.listView);
        listView.setAdapter(new MajorAskAdapter(getContext(),questions));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getHostActivity().open(MajorAskDetailFragment.newInstance(questions.get(position)));
            }
        });
    }

}
