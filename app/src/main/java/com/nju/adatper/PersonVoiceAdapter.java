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
import com.nju.fragment.PersonAlumniVoiceItemDetail;
import com.nju.model.AlumniVoice;
import com.nju.model.EntryDate;
import com.nju.util.DateUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by cun on 2016/3/19.
 */
public class PersonVoiceAdapter extends BaseAdapter {
    private BaseFragment mFragment;
    private HashMap<EntryDate,ArrayList<AlumniVoice>> mAluminVoiceMap;
    private EntryDate[] arrayKeys;

    public PersonVoiceAdapter(BaseFragment fragment, HashMap<EntryDate,ArrayList<AlumniVoice>> arrayListHashMap) {
        mFragment = fragment;
        mAluminVoiceMap = arrayListHashMap;
        Set<EntryDate> keys = mAluminVoiceMap.keySet();
        arrayKeys=  keys.toArray(new EntryDate[keys.size()]);
        Arrays.sort(arrayKeys, Collections.reverseOrder());
    }

    @Override
    public int getCount() {
        return arrayKeys.length;
    }

    @Override
    public Object getItem(int position) {
        return mAluminVoiceMap.get(arrayKeys[position]);
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
        EntryDate entryDate = arrayKeys[position];
        final ArrayList<AlumniVoice> alumniVoices = mAluminVoiceMap.get(entryDate);
        holder.listView.setAdapter(new PersonVoiceListItemAdapter(mFragment.getContext(), alumniVoices));
        holder.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mFragment.getHostActivity().open(PersonAlumniVoiceItemDetail.newInstance(alumniVoices.get(position), "学习"));
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
