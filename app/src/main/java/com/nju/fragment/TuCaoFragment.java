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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nju.activity.R;
import com.nju.adatper.AlumniVoiceViewPage;
import com.nju.adatper.CollageAdapter;
import com.nju.util.Divice;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class TuCaoFragment extends BaseFragment {

    private static final String TAG = TuCaoFragment.class.getSimpleName();
    private RelativeLayout mCollegeMainLayout;
    private LinearLayout mChooseLayout;
    private ArrayList<TextView> mChooseLevelViews = new ArrayList<>();


    public static TuCaoFragment newInstance() {
        return  new TuCaoFragment();
    }

    public TuCaoFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tu_cao, container, false);
        view.setPadding(view.getPaddingLeft(), Divice.getStatusBarHeight(getActivity()), view.getPaddingRight(), view.getPaddingBottom());
        addAdapterChangeEvent(view);
        mCollegeMainLayout = (RelativeLayout) view.findViewById(R.id.college_choose_dialog_relayout);
        openChooseDialog(view);
        hideChooseDialog(view);
        addLevelChooseItem(view);
        return view;
    }

    private void addAdapterChangeEvent(View view){
        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.fragment_alumni_voice_viewpage);
        viewPager.setAdapter(new AlumniVoiceViewPage(getFragmentManager(), getResources().getStringArray(R.array.voiceType)));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            getHostActivity().display(3);
        }
        getHostActivity().geLinearLayout().setVisibility(View.VISIBLE);
    }

    private void openChooseDialog(View view){
        FloatingActionButton floatBn = (FloatingActionButton) view.findViewById(R.id.college_choose_dialog_actionBn);
        floatBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCollegeMainLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    private void hideChooseDialog(View view){
        View mView = view.findViewById(R.id.college_choose_dialog_view);
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCollegeMainLayout.setVisibility(View.GONE);
            }
        });
    }

    private void addLevelChooseItem(View view){
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.college_choose_dialog_choose_layout);
        final ListView listView = (ListView) view.findViewById(R.id.college_choose_dialog_listview);
        Set<String> levels = getHostActivity().getSharedPreferences().getStringSet(getString(R.string.level),new HashSet<String>());
        Set<String> collegeSet = getHostActivity().getSharedPreferences()
                .getStringSet(getString(R.string.undergraduateCollege),new HashSet<String>());
        final String[] colleges = (String[]) collegeSet.toArray(new String[collegeSet.size()]);

        for (String level:levels){
            Log.i(TAG,level);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
            TextView textView = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.bottom_choose_textview,null);
            textView.setLayoutParams(param);
            textView.setText(level);
            if (textView.getText().toString().equals(getString(R.string.undergraduate))){
                textView.setTextColor(ContextCompat.getColor(getContext(),R.color.primayDark));
                listView.setAdapter(new CollageAdapter(getContext(), colleges));
                listView.setVisibility(View.VISIBLE);
            }
            layout.addView(textView);

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView mTV = (TextView) v;
                    if (!mTV.getText().toString().equals(getString(R.string.all))) {
                        listView.setAdapter(new CollageAdapter(getContext(),colleges));
                        listView.setVisibility(View.VISIBLE);
                    } else {
                        mCollegeMainLayout.setVisibility(View.GONE);
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
                view.setTextColor(ContextCompat.getColor(getContext(),R.color.primayDark));
            }else{
                 textView.setTextColor(ContextCompat.getColor(getContext(), android.R.color.black));
            }
        }
    }

}
