package com.nju.adatper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nju.activity.R;
import com.nju.model.AlumniVoice;
import com.nju.util.Constant;
import com.nju.util.StringBase64;

import java.util.ArrayList;

/**
 * Created by cun on 2016/4/6.
 */
public class PersonVoiceListItemAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<AlumniVoice> mAlumniVoices;

    public PersonVoiceListItemAdapter(Context context, ArrayList<AlumniVoice> alumniVoices) {
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
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.person_item_listitem, parent, false);
            holder = new ViewHolder();
            holder.titleTV = (TextView) convertView.findViewById(R.id.title_tv);
            holder.contentTV = (TextView) convertView.findViewById(R.id.content_tv);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        AlumniVoice alumniVoice = mAlumniVoices.get(position);
        try {
            holder.titleTV.setText(StringBase64.decode(alumniVoice.getTitle()));
        } catch (IllegalArgumentException e) {
            holder.titleTV.setText(Constant.UNKNOWN_CHARACTER);
        }

        try {
            holder.contentTV.setText(StringBase64.decode(alumniVoice.getContent()));
        } catch (IllegalArgumentException e) {
            holder.contentTV.setText(Constant.UNKNOWN_CHARACTER);
        }

        return convertView;
    }

    private class ViewHolder {
        private TextView titleTV, contentTV;
    }
}
