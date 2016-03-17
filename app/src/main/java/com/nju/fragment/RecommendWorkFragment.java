package com.nju.fragment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nju.activity.R;
import com.nju.adatper.RecommendWorkAdapter;
import com.nju.util.Divice;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class RecommendWorkFragment extends BaseFragment {

    private static final String TAG = RecommendWorkFragment.class.getSimpleName();
    private RelativeLayout mCollegeMainLayout;
    private ArrayList<TextView> mChooseLevelViews = new ArrayList<>();
    private FloatingActionButton mFloatBn;

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
        view.setPadding(view.getPaddingLeft(), Divice.getStatusBarHeight(getActivity()), view.getPaddingRight(), view.getPaddingBottom());
        mCollegeMainLayout = (RelativeLayout) view.findViewById(R.id.college_choose_dialog_relayout);
        mFloatBn =  (FloatingActionButton) view.findViewById(R.id.college_choose_dialog_actionBn);
        setUpViewPager(view);
        openChooseDialog(view);
        hideChooseDialog(view);
        addLevelChooseItem(view);
        return view;
    }


    private void setUpViewPager(View view){
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.fragment_recommed_work_viewpage);
        viewPager.setAdapter(new RecommendWorkAdapter(getFragmentManager(), getResources().getStringArray(R.array.recommdWorkType)));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeLabelColor(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void changeLabelColor(int position){
        ArrayList<TextView> textViews = getHostActivity().getRecommendLabelViews();
        for (int i = 0;i < textViews.size();i++){
            if (i == position){
                textViews.get(i).setTextColor(ContextCompat.getColor(getContext(),android.R.color.holo_red_dark));
            }else {
                textViews.get(i).setTextColor(ContextCompat.getColor(getContext(),android.R.color.white));
            }
        }
    }

    private void openChooseDialog(View view){

        mFloatBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCollegeMainLayout.setVisibility(View.VISIBLE);
                mFloatBn.setVisibility(View.GONE);
            }
        });
    }

    private void hideChooseDialog(View view){
        View mView = view.findViewById(R.id.college_choose_dialog_view);
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCollegeMainLayout.setVisibility(View.GONE);
                mFloatBn.setVisibility(View.VISIBLE);
            }
        });
    }

    private void addLevelChooseItem(View view){
         LinearLayout input = (LinearLayout) view.findViewById(R.id.college_choose_dialog_choose_layout);
        Set<String> levels = getHostActivity().getSharedPreferences().getStringSet(getString(R.string.level),new HashSet<String>());
        Set<String> collegeSet = getHostActivity().getSharedPreferences()
                .getStringSet(getString(R.string.undergraduateCollege),new HashSet<String>());
        final String[] colleges = (String[]) collegeSet.toArray(new String[collegeSet.size()]);

        for (String level:levels){
            Log.i(TAG, level);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
            TextView textView = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.bottom_choose_textview,null);
            textView.setLayoutParams(param);
            textView.setText(level);
            if (textView.getText().toString().equals(getString(R.string.undergraduate))){
                textView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));

            }
            input.addView(textView);

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView mTV = (TextView) v;
                    if (!mTV.getText().toString().equals(getString(R.string.all))) {

                    } else {
                        mCollegeMainLayout.setVisibility(View.GONE);
                        mFloatBn.setVisibility(View.VISIBLE);
                    }
                    changeLevelTVColor(mTV);
                }
            });
            mChooseLevelViews.add(textView);
        }
    }

    private void changeLevelTVColor(TextView view){
        for (TextView textView:mChooseLevelViews){
            if (view.getText().toString().equals(textView.getText().toString())){
                view.setTextColor(ContextCompat.getColor(getContext(),R.color.colorPrimaryDark));
            }else{
                textView.setTextColor(ContextCompat.getColor(getContext(), android.R.color.black));
            }
        }
    }


}
