package com.nju.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nju.activity.R;
import com.nju.adatper.RecommendWorkItemAdapter;
import com.nju.model.AuthorInfo;
import com.nju.model.RecommendWork;
import com.nju.test.TestData;

import java.util.ArrayList;


public class RecommendWorkItem extends BaseFragment {

    private static final String PARAM_TYPE = "type";
    private static String mType ;
    private  ArrayList<RecommendWork>  recommendWorks;

    public static RecommendWorkItem newInstance(String type) {
        RecommendWorkItem fragment = new RecommendWorkItem();
        Bundle args = new Bundle();
        args.putString(PARAM_TYPE, type);

        fragment.setArguments(args);
        return fragment;
    }

    public RecommendWorkItem() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType= getArguments().getString(PARAM_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_recommend_work_item, container, false);
        initListView(view);
        setUpOnRefreshListener(view);
        return view;
    }

    private void setUpOnRefreshListener(View view){
        final SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateRecommendWork();
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void updateRecommendWork() {

    }

    private void  initListView (View view){
        recommendWorks = TestData.getRecommendWorks();
        ListView listView = (ListView) view.findViewById(R.id.fragment_recommend_work_item_listview);
        listView.setAdapter(new RecommendWorkItemAdapter(getContext(), recommendWorks));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getHostActivity().open(RecommendWorkItemDetailFragment.newInstance(recommendWorks.get(0)));
            }
        });
    }

}
