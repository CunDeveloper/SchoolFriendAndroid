package com.nju.adatper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nju.activity.R;
import com.nju.model.AlumniVoice;

import java.util.ArrayList;

/**
 * Created by cun on 2016/3/15.
 */
public class AlumniVoiceItemAdapter extends BaseAdapter {

    private ArrayList<AlumniVoice> mVoices;
    private Context mContext;

    public AlumniVoiceItemAdapter(Context context, ArrayList<AlumniVoice> voices) {
        mContext = context;
        mVoices = voices;
    }

    @Override
    public int getCount() {
        return mVoices.size();
    }

    @Override
    public Object getItem(int position) {
        return mVoices.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.alumni_voice_item,parent,false);
            holder.nameTV = (TextView) convertView.findViewById(R.id.alumni_vo_name);
            holder.labelTV = (TextView) convertView.findViewById(R.id.alumni_vo_label);
            holder.titleTV = (TextView) convertView.findViewById(R.id.alumni_vo_title);
            holder.dateTV = (TextView) convertView.findViewById(R.id.alumni_vo_date);
            holder.praiseCountTV = (TextView) convertView.findViewById(R.id.alumni_vo_praise_number);
            holder.commentTV = (TextView) convertView.findViewById(R.id.alumni_vo_comment_number);
            holder.simpleDescTV = (TextView) convertView.findViewById(R.id.alumni_vo_simple_desc);
            convertView.setTag(holder);
        }
        AlumniVoice voice = mVoices.get(position);
        holder = (ViewHolder) convertView.getTag();
        holder.commentTV.setText(voice.getCommentCount()+"");
        holder.praiseCountTV.setText(voice.getPraiseCount()+"");
        holder.titleTV.setText(voice.getTitle());
        holder.dateTV.setText(voice.getDate());
        holder.labelTV.setText(voice.getAuthorInfo().getLabel());
        holder.nameTV.setText(voice.getAuthorInfo().getAuthorName());
        holder.simpleDescTV.setText(voice.getSimpleDesc());
        return convertView;
    }

    private class ViewHolder{
        private ImageView headImg;
        private TextView nameTV;
        private TextView labelTV;
        private TextView titleTV;
        private TextView dateTV;
        private TextView praiseCountTV;
        private TextView commentTV;
        private TextView simpleDescTV;
    }
}
