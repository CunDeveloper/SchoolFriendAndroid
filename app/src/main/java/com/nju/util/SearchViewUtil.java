package com.nju.util;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nju.activity.R;
import com.nju.fragment.BaseFragment;

/**
 * Created by cun on 2016/4/7.
 */
public class SearchViewUtil {

    public static void setUp(final BaseFragment fragment, View view) {
        TextView textView = (TextView) fragment.getActivity().findViewById(R.id.main_viewpager_menu_more);
        final LinearLayout searchLayout = (LinearLayout) view.findViewById(R.id.serarch_layout);
        final Button searchBn = (Button) view.findViewById(R.id.searchBn);
        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.mProgressBar);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchLayout.setVisibility(View.VISIBLE);
            }
        });
        final RelativeLayout grayView = (RelativeLayout) view.findViewById(R.id.search_gray_view);
        grayView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchLayout.setVisibility(View.GONE);
                SoftInput.close(fragment.getContext(), grayView);
                progressBar.setVisibility(View.GONE);
            }
        });
        searchBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                SoftInput.close(fragment.getContext(), searchBn);
            }
        });
    }
}
