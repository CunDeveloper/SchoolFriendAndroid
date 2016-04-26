package com.nju.adatper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.nju.activity.R;
import com.nju.fragment.BaseFragment;
import com.nju.fragment.PersonAskDetailFragment;
import com.nju.model.AlumniQuestion;
import com.nju.model.AlumniVoice;
import com.nju.model.EntryDate;
import com.nju.model.MyAsk;
import com.nju.util.Constant;
import com.nju.util.DateUtil;
import com.nju.util.StringBase64;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by cun on 2016/3/19.
 */
public class PersonAskAdapter extends BaseAdapter {
    private BaseFragment mFragment;
    private ArrayList<MyAsk> mMyAsks;
    public PersonAskAdapter(BaseFragment fragment, ArrayList<MyAsk> asks) {
        mFragment = fragment;
        mMyAsks = asks;
    }

    @Override
    public int getCount() {
        return mMyAsks.size();
    }

    @Override
    public Object getItem(int position) {
        return mMyAsks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mFragment.getContext()).inflate(R.layout.person_item, parent, false);
            holder = new ViewHolder();
            holder.dayTV = (TextView) convertView.findViewById(R.id.day_tv);
            holder.monthTV = (TextView) convertView.findViewById(R.id.month_tv);
            holder.listView = (ListView) convertView.findViewById(R.id.listView);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        MyAsk myAsk = mMyAsks.get(position);
        EntryDate entryDate = myAsk.getEntryDate();
        final ArrayList<AlumniQuestion> alumniQuestions = myAsk.getAlumniQuestions();
        holder.listView.setAdapter(new PersonAskListItemAdapter(mFragment.getContext(),alumniQuestions ));
        holder.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mFragment.getHostActivity().open(PersonAskDetailFragment.newInstance(alumniQuestions.get(position)));
            }
        });

        holder.dayTV.setText(entryDate.getDay());
        holder.monthTV.setText(DateUtil.getNoZeroMonth(entryDate.getMonth()));
        return convertView;
    }

    private class ViewHolder {
        private TextView dayTV, monthTV;
        private ListView listView;
    }
}
