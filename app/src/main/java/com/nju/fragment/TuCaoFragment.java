package com.nju.fragment;
import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.nju.activity.R;
import com.nju.adatper.AlumniVoiceViewPage;
import com.nju.util.Divice;

public class TuCaoFragment extends BaseFragment {

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
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.fragment_alumni_voice_viewpage);
        viewPager.setAdapter(new AlumniVoiceViewPage(getFragmentManager(), getResources().getStringArray(R.array.voiceType)));
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getHostActivity().geLinearLayout().setVisibility(View.VISIBLE);
    }

}
