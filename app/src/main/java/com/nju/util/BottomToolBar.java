package com.nju.util;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.nju.activity.R;
import com.nju.adatper.CollageAdapter;
import com.nju.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by cun on 2016/4/5.
 */
public class BottomToolBar {

    public static void show(final BaseFragment fragment,View view ){
        final ArrayList<TextView> mChooseLevelViews = new ArrayList<>();
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.college_choose_dialog_choose_layout);
        final ListView listView = (ListView) view.findViewById(R.id.college_choose_dialog_listview);
        final RelativeLayout mCollegeMainLayout = (RelativeLayout) view.findViewById(R.id.college_choose_dialog_relayout);
        openChooseDialog(mCollegeMainLayout,listView,view);
        hideChooseDialog(mCollegeMainLayout,listView,view);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCollegeMainLayout.setVisibility(View.GONE);
            }
        });
        Set<String> levels = fragment.getHostActivity().getSharedPreferences().getStringSet(fragment.getString(R.string.level),new HashSet<String>());
        Set<String> collegeSet = fragment.getHostActivity().getSharedPreferences()
                .getStringSet(fragment.getString(R.string.undergraduateCollege),new HashSet<String>());
        final String[] colleges = collegeSet.toArray(new String[collegeSet.size()]);

        for (String level:levels) {
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
            TextView textView = (TextView) LayoutInflater.from(fragment.getContext()).inflate(R.layout.bottom_choose_textview, null);
            textView.setLayoutParams(param);
            textView.setText(level);
            if (textView.getText().toString().equals(fragment.getString(R.string.undergraduate))) {
                textView.setTextColor(ContextCompat.getColor(fragment.getContext(), R.color.primayDark));
                listView.setAdapter(new CollageAdapter(fragment.getContext(), colleges));
                listView.setVisibility(View.VISIBLE);
            }
            layout.addView(textView);
            mChooseLevelViews.add(textView);
            textView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    TextView tv = (TextView) v;
                    if (!tv.getText().toString().equals(Constant.ALL)) {
                        listView.setAdapter(new CollageAdapter(fragment.getContext(), colleges));
                        listView.setVisibility(View.VISIBLE);
                    }
                    changeLevelTVColor(mChooseLevelViews, fragment.getContext(), tv);
                    return true;
                }
            });

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView mTV = (TextView) v;
                    mCollegeMainLayout.setVisibility(View.GONE);
                    changeLevelTVColor(mChooseLevelViews,fragment.getContext(),mTV);
                }
            });
        }
    }

    private static void changeLevelTVColor(ArrayList<TextView> mChooseLevelViews,Context context,TextView view){
        for (TextView textView:mChooseLevelViews){
            if (view.getText().toString().equals(textView.getText().toString())){
                view.setTextColor(ContextCompat.getColor(context,R.color.primayDark));
            }else{
                textView.setTextColor(ContextCompat.getColor(context, android.R.color.black));
            }
        }
    }

    private static void openChooseDialog(final RelativeLayout mCollegeMainLayout, final ListView listView, final View view){
        FloatingActionButton floatBn = (FloatingActionButton) view.findViewById(R.id.college_choose_dialog_actionBn);
        floatBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCollegeMainLayout.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
            }
        });
    }

    private static void hideChooseDialog(final RelativeLayout mCollegeMainLayout, final ListView listView,View view){
        View mView = view.findViewById(R.id.college_choose_dialog_view);
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCollegeMainLayout.setVisibility(View.GONE);
                listView.setVisibility(View.GONE);
            }
        });
    }
}
