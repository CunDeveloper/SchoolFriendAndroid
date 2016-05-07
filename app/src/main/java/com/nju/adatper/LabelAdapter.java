package com.nju.adatper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nju.activity.R;

import java.util.ArrayList;

/**
 * Created by cun on 2016/4/7.
 */
public class LabelAdapter extends BaseAdapter {
    private ArrayList<String> mLabels;
    private Context mContext;

    public LabelAdapter(Context context, ArrayList<String> labels) {
        this.mContext = context;
        this.mLabels = labels;
    }

    @Override
    public int getCount() {
        return mLabels.size();
    }

    @Override
    public Object getItem(int position) {
        return mLabels.get(position);
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.label_button, parent, false);
            holder.button = (TextView) convertView;
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        holder.button.setText(mLabels.get(position));
        return convertView;
    }

    private class ViewHolder {
        private TextView button;
    }
}
