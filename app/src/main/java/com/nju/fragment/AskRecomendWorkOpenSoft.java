package com.nju.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nju.activity.R;


public class AskRecomendWorkOpenSoft extends BaseFragment {


    public AskRecomendWorkOpenSoft() {
        // Required empty public constructor
    }

    public static AskRecomendWorkOpenSoft newInstance() {
        AskRecomendWorkOpenSoft fragment = new AskRecomendWorkOpenSoft();
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
        return inflater.inflate(R.layout.fragment_ask_recomend_work_open_soft, container, false);
    }

}
