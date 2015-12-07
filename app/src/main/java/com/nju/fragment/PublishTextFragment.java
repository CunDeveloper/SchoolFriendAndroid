package com.nju.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.nju.activity.R;
import com.nju.util.Divice;

public class PublishTextFragment extends Fragment {

    public static final String TAG = PublishTextFragment.class.getSimpleName();
    private Button mSendButton;

    public static PublishTextFragment newInstance() {
        return new PublishTextFragment();
    }

    public PublishTextFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_publish_text, container, false);
        view.setPadding(view.getPaddingLeft(), Divice.getStatusBarHeight(getActivity()),view.getPaddingRight(),view.getPaddingBottom());
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null){
            actionBar.setTitle(getString(R.string.publish_text));
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        getActivity().findViewById(R.id.main_viewpager_camera_imageView).setVisibility(View.GONE);
        mSendButton = (Button) activity.findViewById(R.id.main_viewpager_menu_bn);
        mSendButton.setText(getString(R.string.send));
        mSendButton.setVisibility(View.VISIBLE);
        initSendEvent();
        return view;
    }

    private void initSendEvent() {
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

}
