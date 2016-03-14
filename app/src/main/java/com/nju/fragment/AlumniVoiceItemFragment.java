package com.nju.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nju.activity.R;


public class AlumniVoiceItemFragment extends BaseFragment {

    private static final String PARAM_TYPE = "param";
    private static String mType;
    public static AlumniVoiceItemFragment newInstance(String type) {
        AlumniVoiceItemFragment fragment = new AlumniVoiceItemFragment();
        Bundle args = new Bundle();
        args.putString(PARAM_TYPE,type);
        fragment.setArguments(args);
        return fragment;
    }

    public AlumniVoiceItemFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getString(PARAM_TYPE);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_alumni_voice_item, container, false);
        TextView textView = (TextView) view.findViewById(R.id.alumni_voice_item_test);
        textView.setText(mType);
        return view;
    }


}
