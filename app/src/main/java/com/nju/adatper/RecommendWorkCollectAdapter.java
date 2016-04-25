package com.nju.adatper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nju.activity.R;
import com.nju.model.RecommendWork;
import com.nju.util.Constant;
import com.nju.util.DateUtil;
import com.nju.util.StringBase64;

import java.util.ArrayList;

/**
 * Created by cun on 2016/3/19.
 */
public class RecommendWorkCollectAdapter extends BaseAdapter {
    private ArrayList<RecommendWork> mRecommendWorks;
    private Context mContext;

    public RecommendWorkCollectAdapter(Context context, ArrayList<RecommendWork> recommendWorks) {
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
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.voice_collect_item, parent, false);
            holder = new ViewHolder();
            holder.nameTV = (TextView) convertView.findViewById(R.id.name_tv);
            holder.labelTV = (TextView) convertView.findViewById(R.id.label_tv);
            holder.dateTV = (TextView) convertView.findViewById(R.id.date_tv);
            holder.contentTV = (TextView) convertView.findViewById(R.id.content_tv);
            holder.titleTV = (TextView) convertView.findViewById(R.id.title_tv);
            holder.headImg = (ImageView) convertView.findViewById(R.id.head_icon);
            holder.chooseBox = (CheckBox) convertView.findViewById(R.id.chooseCB);
            convertView.setTag(holder);
        }
        RecommendWork recommendWork = mRecommendWorks.get(position);
        holder = (ViewHolder) convertView.getTag();
        holder.nameTV.setText(recommendWork.getAuthor().getAuthorName());
        holder.labelTV.setText(recommendWork.getAuthor().getLabel());
        holder.dateTV.setText(DateUtil.getRelativeTimeSpanString(recommendWork.getDate()));
        try{
            holder.titleTV.setText(StringBase64.decode(recommendWork.getTitle()));
        }catch (IllegalArgumentException e){
            holder.titleTV.setText(Constant.UNKNOWN_CHARACTER);
        }
        try {
            holder.contentTV.setText(StringBase64.decode(recommendWork.getContent()));
        }catch (IllegalArgumentException e){
            holder.contentTV.setText(Constant.UNKNOWN_CHARACTER);
        }
        if (recommendWork.getCheck()==1){
            holder.chooseBox.setVisibility(View.VISIBLE);
        }else if (recommendWork.getCheck() == 2){
            holder.chooseBox.setChecked(true);
            holder.chooseBox.setVisibility(View.VISIBLE);
        }else if (recommendWork.getCheck() == 0){
            holder.chooseBox.setVisibility(View.GONE);
        }
        return convertView;
    }

    private class ViewHolder {
        private ImageView headImg, contentImg;
        private TextView nameTV, labelTV, dateTV, contentTV, titleTV;
        private CheckBox chooseBox;
        private RelativeLayout collectToolLayout;
    }
}
