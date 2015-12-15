package com.nju.fragment;


import android.graphics.Typeface;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nju.activity.R;
import com.nju.util.AndroidEmoji;
import com.nju.util.Divice;


public class SeniorsVoicesFragment extends BaseFragment {

    public static SeniorsVoicesFragment newInstance() {
        SeniorsVoicesFragment fragment = new SeniorsVoicesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public SeniorsVoicesFragment() {
        // Required empty public constructor
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


        int unicode = 0x1F602;


        textView.setText(getEmijoByUnicode(unicode)+"==="+"\uD83D\uDE00");
//        Typeface font = AndroidEmoji.FontProvider.getInstance(getContext()).getFontEmoji();
//        textView.setTypeface(font);
        return view;
    }

    public String getEmijoByUnicode(int unicode){
        return new String(Character.toChars(unicode));
    }


}
