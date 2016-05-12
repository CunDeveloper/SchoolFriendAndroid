package com.nju.adatper;

import android.app.DownloadManager;
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
import com.nju.http.ImageDownloader;
import com.nju.model.AuthorInfo;
import com.nju.model.RecommendCollect;
import com.nju.model.RecommendWork;
import com.nju.util.Constant;
import com.nju.util.DateUtil;
import com.nju.util.PathConstant;
import com.nju.util.StringBase64;

import java.util.ArrayList;

/**
 * Created by cun on 2016/3/19.
 */
public class RecommendWorkCollectAdapter extends BaseAdapter {
    private ArrayList<RecommendCollect> mRecommendCollect;
    private Context mContext;

    public RecommendWorkCollectAdapter(Context context, ArrayList<RecommendCollect> recommendWorks) {
        mContext = context;
        mRecommendCollect = recommendWorks;
    }

    @Override
    public int getCount() {
        return mRecommendCollect.size();
    }

    @Override
    public Object getItem(int position) {
        return mRecommendCollect.get(position);
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
            holder.headImg = (ImageView) convertView.findViewById(R.id.headIconImg);
            holder.chooseBox = (CheckBox) convertView.findViewById(R.id.chooseCB);
            convertView.setTag(holder);
        }
        RecommendCollect collect = mRecommendCollect.get(position);
        holder = (ViewHolder) convertView.getTag();
        RecommendWork work = collect.getRecommendWork();
        AuthorInfo info = work.getAuthor();
        if (info != null) {
            String url = PathConstant.IMAGE_PATH_SMALL + PathConstant.ALUMNI_RECOMMEND_IMG_PATH + info.getHeadUrl();
            ImageDownloader.with(mContext).download(url,holder.headImg);
            holder.nameTV.setText(info.getAuthorName());
            holder.labelTV.setText(info.getLabel());
        }


        holder.dateTV.setText(DateUtil.getRelativeTimeSpanString(collect.getDate()));
        try {
            holder.titleTV.setText(StringBase64.decode(work.getTitle()));
        } catch (IllegalArgumentException e) {
            holder.titleTV.setText(Constant.UNKNOWN_CHARACTER);
        }
        try {
            holder.contentTV.setText(StringBase64.decode(work.getContent()));
        } catch (IllegalArgumentException e) {
            holder.contentTV.setText(Constant.UNKNOWN_CHARACTER);
        }
        if (collect.getCheck() == 1) {
            holder.chooseBox.setVisibility(View.VISIBLE);
        } else if (collect.getCheck() == 2) {
            holder.chooseBox.setChecked(true);
            holder.chooseBox.setVisibility(View.VISIBLE);
        } else if (collect.getCheck() == 0) {
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
