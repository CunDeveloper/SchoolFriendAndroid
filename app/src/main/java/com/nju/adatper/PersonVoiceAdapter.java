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
import com.nju.util.Constant;
import com.nju.util.StringBase64;

import java.io.UnsupportedEncodingException;
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
        try {
            holder.titleTV.setText(StringBase64.decode(alumniVoice.getTitle()));
        }catch(IllegalArgumentException e){
            holder.titleTV.setText(Constant.UNKNOWN_CHARACTER);
        }

        try {
            holder.contentTV.setText(StringBase64.decode(alumniVoice.getContent()));
        }catch(IllegalArgumentException e){
            holder.contentTV.setText(Constant.UNKNOWN_CHARACTER);
        }

        return convertView;
    }

    private class ViewHolder{
        private TextView dayTV,monthTV,titleTV,contentTV;
    }
}
