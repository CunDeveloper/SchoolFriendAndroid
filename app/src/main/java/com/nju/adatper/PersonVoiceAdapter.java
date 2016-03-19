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

import java.util.ArrayList;

/**
 * Created by cun on 2016/3/19.
 */
public class PersonVoiceAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<AlumniVoice> mAlumniVoices;
    public PersonVoiceAdapter(Context context, ArrayList<AlumniVoice> alumniVoices) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.person_item,parent,false);
            holder = new ViewHolder();
            holder.dayTV = (TextView) convertView.findViewById(R.id.date_tv);
            holder.monthTV = (TextView) convertView.findViewById(R.id.month_tv);
            holder.titleTV = (TextView) convertView.findViewById(R.id.title_tv);
            holder.contentTV = (TextView) convertView.findViewById(R.id.content_tv);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        AlumniVoice alumniVoice = mAlumniVoices.get(position);
        holder.titleTV.setText(alumniVoice.getTitle());
        holder.contentTV.setText(alumniVoice.getContent());
        return convertView;
    }

    private class ViewHolder{
        private TextView dayTV,monthTV,titleTV,contentTV;
    }
}