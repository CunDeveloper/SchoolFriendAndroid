package com.nju.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.nju.activity.R;
import com.nju.adatper.ComplainAdapter;
import com.nju.util.Divice;


public class ComplainFragment extends BaseFragment {

    public static ComplainFragment newInstance( ) {
        ComplainFragment fragment = new ComplainFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public ComplainFragment() {
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
        View view = inflater.inflate(R.layout.fragment_complain, container, false);

        view.setPadding(view.getPaddingLeft(), Divice.getStatusBarHeight(getContext()), view.getPaddingRight(), view.getPaddingBottom());
        ListView listView = (ListView) view.findViewById(R.id.mListview);
        LinearLayout head = (LinearLayout) inflater.inflate(R.layout.complain_fragment_listhead,listView,false);
        listView.addHeaderView(head);
        listView.setAdapter(new ComplainAdapter(getContext()));
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getString(R.string.choose_reason));
        }
        getHostActivity().display(0);
        getHostActivity().display(0);
        final Button sendBn =  getHostActivity().getMenuBn();
        sendBn.setText(getString(R.string.commit));
        sendBn.setEnabled(false);sendBn.setAlpha(0.8f);
    }


}
