package com.nju.adatper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.nju.activity.R;
import com.nju.model.AlumniVoice;
import com.nju.model.RecommendWork;

import java.util.ArrayList;

/**
 * Created by cun on 2016/3/19.
 */
public class PersonRecommendAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<RecommendWork> mRecommendWorks;
    public PersonRecommendAdapter(Context context, ArrayList<RecommendWork> recommendWorks) {
        mContext = context;
        mRecommendWorks = recommendWorks;
    }

    @Override
    public int getCount() {
        return mRecommendWorks.size();
    }

    @Override
    public Object getItem(int position) {
        return mRecommendWorks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.person_item,parent,false);
            holder = new ViewHolder();
            holder.dayTV = (TextView) convertView.findViewById(R.id.date_tv);
            holder.monthTV = (TextView) convertView.findViewById(R.id.month_tv);
            holder.titleTV = (TextView) convertView.findViewById(R.id.title_tv);
            holder.contentTV = (TextView) convertView.findViewById(R.id.content_tv);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        RecommendWork recommendWork = mRecommendWorks.get(position);
        holder.titleTV.setText(recommendWork.getTitle());
        holder.contentTV.setText(recommendWork.getContent());
        return convertView;
    }

    private class ViewHolder{
        private TextView dayTV,monthTV,titleTV,contentTV;
    }
}
