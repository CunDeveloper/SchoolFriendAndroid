package com.nju.fragment;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nju.activity.R;
import com.nju.adatper.PersonAskAdapter;
import com.nju.model.AlumniQuestion;
import com.nju.test.TestData;
import com.nju.util.Divice;

import java.util.ArrayList;

public class MyAskFragment extends BaseFragment {

    private static final String PARAM_TITLE = "paramTitle";
    private static String mTitle;

    public static MyAskFragment newInstance(String title) {
        MyAskFragment fragment = new MyAskFragment();
        Bundle args = new Bundle();
        args.putString(PARAM_TITLE,title);
        fragment.setArguments(args);
        return fragment;
    }

    public MyAskFragment() {
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
        View view = inflater.inflate(R.layout.person_refresh_listview, container, false);
        view.setPadding(view.getPaddingLeft(), Divice.getStatusBarHeight(getContext()),view.getPaddingRight(),view.getPaddingBottom());
        initListView(view);
        return view;
    }

    private void initListView(View view){
        final ArrayList<AlumniQuestion> alumniQuestions = TestData.getQlumniQuestions();
        ListView listView = (ListView) view.findViewById(R.id.listView);
        listView.setAdapter(new PersonAskAdapter(getContext(),alumniQuestions));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getHostActivity().open(PersonAskDetailFragment.newInstance(alumniQuestions.get(position)));
            }
        });
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
        getHostActivity().display(5);
    }
}
