package com.nju.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nju.activity.R;
import com.nju.adatper.CirclePager;
import com.nju.model.AuthorInfo;
import com.nju.util.Divice;

public class CircleFragment extends BaseFragment {

    private static final String TAG = CircleFragment.class.getSimpleName();
    private static final String PARAM_AUTHOR ="authorInfo";
    private AuthorInfo mAuthorInfo;
    public static CircleFragment newInstance(AuthorInfo authorInfo) {
        CircleFragment fragment = new CircleFragment();
        Bundle args = new Bundle();
        args.putParcelable(PARAM_AUTHOR,authorInfo);
        fragment.setArguments(args);
        return fragment;
    }

    public CircleFragment() {
        // Required empty public constructor
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
        if(actionBar!=null) {
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
        initViewPager(view);
        return view;
    }

    private void initViewPager(View view){
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        viewPager.setAdapter(new CirclePager(getFragmentManager()));
    }

}
