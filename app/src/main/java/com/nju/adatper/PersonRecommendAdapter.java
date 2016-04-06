package com.nju.adatper;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.nju.activity.R;
import com.nju.fragment.BaseFragment;
import com.nju.fragment.PersonRecommendWorkItemDetailFragment;
import com.nju.model.EntryDate;
import com.nju.model.RecommendWork;
import com.nju.util.Constant;
import com.nju.util.DateUtil;
import com.nju.util.StringBase64;
import com.nju.util.ToastUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by cun on 2016/3/19.
 */
public class PersonRecommendAdapter extends BaseAdapter {
    private BaseFragment mFragment;
    private HashMap<EntryDate,ArrayList<RecommendWork>> mRecommendWorkMap;
    private EntryDate[] arrayKeys;

    public PersonRecommendAdapter(BaseFragment fragment, HashMap<EntryDate,ArrayList<RecommendWork>> arrayListHashMap) {
        mFragment = fragment;
        mRecommendWorkMap = arrayListHashMap;
        Set<EntryDate> keys = mRecommendWorkMap.keySet();
         arrayKeys=  keys.toArray(new EntryDate[keys.size()]);
        Arrays.sort(arrayKeys, Collections.reverseOrder());
    }

    @Override
    public int getCount() {
        return arrayKeys.length;
    }

    @Override
    public Object getItem(int position) {
        return mRecommendWorkMap.get(arrayKeys[position]);
       // return mRecommendWorks.get(position);
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
        EntryDate entryDate= arrayKeys[position];
        final ArrayList<RecommendWork> recommendWorks =  mRecommendWorkMap.get(entryDate);
        holder.listView.setAdapter(new PersonRecommendListItemAdapter(mFragment.getContext(), recommendWorks));
        holder.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mFragment.getHostActivity().open(PersonRecommendWorkItemDetailFragment.newInstance(recommendWorks.get(position)));
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
