package com.nju.adatper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.nju.activity.R;
import com.nju.http.ImageDownloader;
import com.nju.model.AlumniVoice;
import com.nju.model.VoiceCollect;
import com.nju.util.Constant;
import com.nju.util.DateUtil;
import com.nju.util.PathConstant;
import com.nju.util.StringBase64;

import java.util.ArrayList;

/**
 * Created by cun on 2016/3/19.
 */
public class VoiceCollectAdapter extends BaseAdapter {
    private ArrayList<VoiceCollect> mVoiceCollects;
    private Context mContext;

    public VoiceCollectAdapter(Context context, ArrayList<VoiceCollect> voiceCollects) {
        mContext = context;
        mVoiceCollects = voiceCollects;
    }

    @Override
    public int getCount() {
        return mVoiceCollects.size();
    }

    @Override
    public Object getItem(int position) {
        return mVoiceCollects.get(position);
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
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.chooseCB);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        VoiceCollect collect = mVoiceCollects.get(position);
        AlumniVoice voice = collect.getAlumnusVoice();
        String url = PathConstant.IMAGE_PATH_SMALL + PathConstant.ALUMNI_VOICE_IMG_PATH + voice.getAuthorInfo().getHeadUrl();
        ImageDownloader.with(mContext).download(url,holder.headImg);
        holder.nameTV.setText(voice.getAuthorInfo().getAuthorName());
        holder.labelTV.setText(voice.getAuthorInfo().getLabel());
        holder.dateTV.setText(DateUtil.getRelativeTimeSpanString(collect.getDate()));
        try {
            holder.titleTV.setText(StringBase64.decode(voice.getTitle()));
        } catch (IllegalArgumentException e) {
            holder.titleTV.setText(Constant.UNKNOWN_CHARACTER);
        }
        try {
            holder.contentTV.setText(StringBase64.decode(voice.getContent()));
        } catch (IllegalArgumentException e) {
            holder.contentTV.setText(Constant.UNKNOWN_CHARACTER);
        }

        if (collect.getCheck() == 0) {
            holder.checkBox.setVisibility(View.GONE);
        } else if (collect.getCheck() == 1) {
            holder.checkBox.setVisibility(View.VISIBLE);
        } else if (collect.getCheck() == 2) {
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.checkBox.setChecked(true);
        }

        return convertView;
    }

    private class ViewHolder {
        private ImageView headImg, contentImg;
        private TextView nameTV, labelTV, dateTV, contentTV, titleTV;
        private CheckBox checkBox;
    }
}
