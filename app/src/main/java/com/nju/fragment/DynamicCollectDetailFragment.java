package com.nju.fragment;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nju.activity.R;
import com.nju.model.DynamicCollect;
import com.nju.util.Divice;


public class DynamicCollectDetailFragment extends BaseFragment {
    private static final String PARAM_KEY = "paramKey";
    private DynamicCollect mDynamicCollect;
    public static DynamicCollectDetailFragment newInstance(DynamicCollect dynamicCollect) {
        DynamicCollectDetailFragment fragment = new DynamicCollectDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(PARAM_KEY,dynamicCollect);
        fragment.setArguments(args);
        return fragment;
    }

    public DynamicCollectDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mDynamicCollect = getArguments().getParcelable(PARAM_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dynamic_collect_detail, container, false);
        view.setPadding(view.getPaddingLeft(), Divice.getStatusBarHeight(getContext()),view.getPaddingRight(),view.getPaddingBottom());
        initView(view);
        return  view;
    }

    private void initView(View view){
        TextView nameTV = (TextView) view.findViewById(R.id.name_tv);
        nameTV.setText(mDynamicCollect.getAuthorInfo().getAuthorName());
        TextView labelTV = (TextView) view.findViewById(R.id.label_tv);
        labelTV.setText(mDynamicCollect.getAuthorInfo().getLabel());
        TextView contentTV = (TextView) view.findViewById(R.id.content_tv);
        contentTV.setText(mDynamicCollect.getContent());
        ImageView contentImg = (ImageView) view.findViewById(R.id.collect_img);
        TextView dateTV = (TextView) view.findViewById(R.id.date_tv);
        dateTV.setText(getString(R.string.collect_with)+" "+mDynamicCollect.getDate());
        if (mDynamicCollect.getContent()!=null){
             contentTV.setText(mDynamicCollect.getContent());
        }else {
            contentImg.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.cheese_2));
        }
     }
}
