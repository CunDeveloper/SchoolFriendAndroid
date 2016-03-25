package com.nju.adatper;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nju.activity.R;
import com.nju.model.RecommendWork;
import com.nju.util.Constant;
import com.nju.util.DateUtil;
import com.nju.util.StringBase64;

import java.util.ArrayList;

/**
 * Created by xiaojuzhang on 2016/3/14.
 */
public class RecommendWorkItemAdapter extends BaseAdapter {

    private static final String TAG = RecommendWorkItemAdapter.class.getSimpleName() ;
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
        try {
            holder.titleTx.setText(StringBase64.decode(recommendWork.getTitle()));
        }catch (IllegalArgumentException e){
            holder.titleTx.setText(Constant.UNKNOWN_CHARACTER);
        }
        holder.usernameTx.setText(recommendWork.getAuthor().getAuthorName());
        holder.labelTx.setText(recommendWork.getAuthor().getLabel());
        holder.countTx.setText(recommendWork.getCommentCount()+"");
        final long time = DateUtil.getTime(recommendWork.getDate());
        Log.e(TAG,time+"");
        if (time <= Constant.TODAY_TIME) {
            holder.dateTx.setText(Constant.TODAY);
        }
        else if (time >Constant.TODAY_TIME && time <= Constant.YESTERDAY_TIME){
            holder.dateTx.setText(Constant.YESTERDAY);
        }else{
            holder.dateTx.setText(recommendWork.getDate().split(" ")[0]);
        }

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
