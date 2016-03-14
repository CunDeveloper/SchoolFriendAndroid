package com.nju.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nju.activity.R;
import com.nju.adatper.RecommendWorkAdapter;
import com.nju.util.Divice;


public class RecommendWorkFragment extends BaseFragment {

    public static RecommendWorkFragment newInstance( ) {
        RecommendWorkFragment fragment = new RecommendWorkFragment();

        return fragment;
    }

    public RecommendWorkFragment() {
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
            actionBar.setTitle(R.string.recommend_work);
        }
        getHostActivity().display(4);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recommend_work, container, false);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.fragment_recommed_work_viewpage);
        viewPager.setAdapter(new RecommendWorkAdapter(getFragmentManager(),getResources().getStringArray(R.array.recommdWorkType)));
        view.setPadding(view.getPaddingLeft(), Divice.getStatusBarHeight(getActivity()), view.getPaddingRight(), view.getPaddingBottom());
        return view;
    }

}
