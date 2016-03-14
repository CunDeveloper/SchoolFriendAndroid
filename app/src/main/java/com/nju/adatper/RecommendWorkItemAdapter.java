package com.nju.adatper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.nju.activity.R;
import com.nju.model.RecommendWork;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by xiaojuzhang on 2016/3/14.
 */
public class RecommendWorkItemAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<RecommendWork> mRecommendWorks;

    public RecommendWorkItemAdapter(Context context,ArrayList<RecommendWork> recommendWorks) {
        mRecommendWorks = recommendWorks;
        mContext = context;
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
        if (convertView==null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.recommend_work_item_list_item,parent,false);
            holder = new ViewHolder();
            holder.countTx = (TextView) convertView.findViewById(R.id.fragment_re_list_item_count);
            holder.dateTx = (TextView) convertView.findViewById(R.id.re_list_item_date);
            holder.labelTx = (TextView) convertView.findViewById(R.id.re_list_item_label);
            holder.titleTx = (TextView) convertView.findViewById(R.id.re_list_item_title);
            holder.usernameTx = (TextView) convertView.findViewById(R.id.re_list_item_name);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        RecommendWork recommendWork = mRecommendWorks.get(position);
        holder.titleTx.setText(recommendWork.getTitle());
        holder.usernameTx.setText(recommendWork.getAuthor().getAuthorName());
        holder.labelTx.setText(recommendWork.getAuthor().getLabel());
        holder.countTx.setText(recommendWork.getCommentCount()+"");
        holder.dateTx.setText(recommendWork.getDate());
        return convertView;
    }

    private class ViewHolder{
        private TextView titleTx;
        private TextView dateTx;
        private TextView countTx;
        private TextView usernameTx;
        private TextView labelTx;
    }
}
