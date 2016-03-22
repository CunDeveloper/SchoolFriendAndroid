package com.nju.fragment;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nju.activity.R;
import com.nju.util.Divice;
import com.nju.util.ToastUtil;


public class SettingFragment extends BaseFragment  {

    public static SettingFragment newInstance( ) {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public SettingFragment() {
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
            actionBar.setHomeAsUpIndicator(R.drawable.arrows);
            actionBar.setTitle(R.string.setting);
        }
        getHostActivity().display(6);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        view.setPadding(view.getPaddingLeft(), Divice.getStatusBarHeight(getContext()), view.getPaddingRight(), view.getPaddingBottom());
        addViewEvent(view);
        return view;
    }

    private void addViewEvent(View view){
        TextView functionTV = (TextView) view.findViewById(R.id.function_desc);
        functionTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHostActivity().open(WebViewFragment.newInstance("file:///android_asset/html/function_desc.html"));
            }
        });
        TextView sysNoticeTV = (TextView) view.findViewById(R.id.sys_notice);
        sysNoticeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHostActivity().open(WebViewFragment.newInstance("file:///android_asset/html/sys_notice.html"));
            }
        });
        TextView helpTV = (TextView) view.findViewById(R.id.help_fellback);
        helpTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHostActivity().open(WebViewFragment.newInstance("file:///android_asset/html/help.html"));
            }
        });
        TextView jubaoTV = (TextView) view.findViewById(R.id.jubao_tousu);
        jubaoTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHostActivity().open(WebViewFragment.newInstance("file:///android_asset/html/jubao.html"));
            }
        });
        TextView checkVersionTV = (TextView) view.findViewById(R.id.check_new_version);
        checkVersionTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        TextView cleanStorageTV = (TextView) view.findViewById(R.id.clean_storage);
        cleanStorageTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        TextView exitTV = (TextView) view.findViewById(R.id.exit_alumni);
        exitTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


}
