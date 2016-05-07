package com.nju.fragment;


import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nju.activity.R;
import com.nju.util.Divice;


public class SeniorsVoicesFragment extends BaseFragment {

    private static final String TAG = SeniorsVoicesFragment.class.getSimpleName();

    public SeniorsVoicesFragment() {
        // Required empty public constructor
    }

    public static SeniorsVoicesFragment newInstance() {
        SeniorsVoicesFragment fragment = new SeniorsVoicesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_seniors_voices, container, false);
        view.setPadding(view.getPaddingLeft(), Divice.getStatusBarHeight(getContext()), view.getPaddingRight(), view.getPaddingBottom());
        TextView textView = (TextView) view.findViewById(R.id.fragment_seniors_voice_tv);
        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.fragment_web_view_progressBar);
        progressBar.getProgressDrawable().setColorFilter(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark), PorterDuff.Mode.SRC_IN);
        return view;
    }

}
