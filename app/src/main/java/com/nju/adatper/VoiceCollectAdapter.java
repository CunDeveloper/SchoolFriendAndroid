package com.nju.adatper;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.nju.activity.R;
import com.nju.model.AlumniVoice;
import com.nju.model.DynamicCollect;

import java.util.ArrayList;

/**
 * Created by cun on 2016/3/19.
 */
public class VoiceCollectAdapter extends BaseAdapter {
    private ArrayList<AlumniVoice> mAlumniVoices;
    private Context mContext;
    public VoiceCollectAdapter(Context context, ArrayList<AlumniVoice> alumniVoices) {
        mContext = context;
        mAlumniVoices = alumniVoices;
    }

    @Override
    public int getCount() {
        return mAlumniVoices.size();
    }

    @Override
    public Object getItem(int position) {
        return mAlumniVoices.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.voice_collect_item,parent,false);
            holder = new ViewHolder();
            holder.nameTV = (TextView) convertView.findViewById(R.id.name_tv);
            holder.labelTV = (TextView) convertView.findViewById(R.id.label_tv);
            holder.dateTV = (TextView) convertView.findViewById(R.id.date_tv);
            holder.contentTV = (TextView) convertView.findViewById(R.id.content_tv);
            holder.titleTV = (TextView) convertView.findViewById(R.id.title_tv);
            holder.headImg = (ImageView) convertView.findViewById(R.id.head_icon);
            convertView.setTag(holder);
        }
        AlumniVoice alumniVoice = mAlumniVoices.get(position);
        holder = (ViewHolder) convertView.getTag();
        holder.nameTV.setText(alumniVoice.getAuthorInfo().getAuthorName());
        holder.labelTV.setText(alumniVoice.getAuthorInfo().getLabel());
        holder.dateTV.setText(alumniVoice.getDate());
        holder.titleTV.setText(alumniVoice.getTitle());
        holder.contentTV.setText(alumniVoice.getContent());
        return convertView;
    }

    private class ViewHolder{
        private ImageView headImg,contentImg;
        private TextView nameTV,labelTV,dateTV,contentTV,titleTV;
    }
}