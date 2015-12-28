package com.nju.adatper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nju.activity.R;
import com.nju.model.UserInfo;

import java.util.ArrayList;

/**
 * Created by cun on 2015/12/20.
 */
public class UserInfoAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<UserInfo> mUserInfos;
    public UserInfoAdapter(Context context,ArrayList<UserInfo> userInfos){
        mContext = context;
        mUserInfos = userInfos;
    }
    @Override
    public int getCount() {
        return mUserInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return mUserInfos.get(position);
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.fragment_user_info_item,null);
            holder.labelTV = (TextView) convertView.findViewById(R.id.fragment_user_info_item_label_text);
            holder.majorTV = (TextView) convertView.findViewById(R.id.fragment_user_info_item_major_text);
            holder.xueYuanTV = (TextView) convertView.findViewById(R.id.fragment_user_info_item_xueyuan_text);
            holder.dateTV = (TextView) convertView.findViewById(R.id.fragment_user_info_item_date_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.labelTV.setText(mUserInfos.get(position).getLabel());
        holder.xueYuanTV.setText(mUserInfos.get(position).getFenYuan());
        holder.majorTV.setText(mUserInfos.get(position).getMajor());
        holder.dateTV.setText(mUserInfos.get(position).getDate());
        return convertView;
    }

    private class ViewHolder {
        private TextView labelTV;
        private TextView majorTV;
        private TextView xueYuanTV;
        private TextView dateTV;
    }
}
