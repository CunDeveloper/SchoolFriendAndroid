package com.nju.adatper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.nju.activity.R;

import java.util.Set;

/**
 * Created by xiaojuzhang on 2016/3/15.
 */
public class CollageAdapter extends BaseAdapter {

    private Context mContext;
    private String[] mColleges;

    public CollageAdapter(Context context,String[] colleges) {
        mContext = context;
        mColleges = colleges;
    }

    @Override
    public int getCount() {
        return mColleges.length;
    }

    @Override
    public Object getItem(int position) {
        return mColleges[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.college_item,parent,false);
            holder.mNameTv = (TextView) convertView;
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        holder.mNameTv.setText(mColleges[position]);
        return convertView;
    }

    private class ViewHolder{
        private TextView mNameTv;
    }
}