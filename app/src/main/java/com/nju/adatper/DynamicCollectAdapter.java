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
import com.nju.model.DynamicCollect;

import java.util.ArrayList;

/**
 * Created by cun on 2016/3/19.
 */
public class DynamicCollectAdapter extends BaseAdapter {
    private ArrayList<DynamicCollect> mDynamicCollects;
    private Context mContext;
    public DynamicCollectAdapter(Context context, ArrayList<DynamicCollect> dynamicCollects) {
        mContext = context;
        mDynamicCollects = dynamicCollects;
    }

    @Override
    public int getCount() {
        return mDynamicCollects.size();
    }

    @Override
    public Object getItem(int position) {
        return mDynamicCollects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.dynamic_collect_item,parent,false);
            holder = new ViewHolder();
            holder.nameTV = (TextView) convertView.findViewById(R.id.name_tv);
            holder.labelTV = (TextView) convertView.findViewById(R.id.label_tv);
            holder.dateTV = (TextView) convertView.findViewById(R.id.date_tv);
            holder.contentTV = (TextView) convertView.findViewById(R.id.content_tv);
            holder.headImg = (ImageView) convertView.findViewById(R.id.head_icon);
            holder.contentImg = (ImageView) convertView.findViewById(R.id.collect_img);
            convertView.setTag(holder);
        }
        DynamicCollect dynamicCollect = mDynamicCollects.get(position);
        holder = (ViewHolder) convertView.getTag();
        holder.nameTV.setText(dynamicCollect.getAuthorInfo().getAuthorName());
        holder.labelTV.setText(dynamicCollect.getAuthorInfo().getLabel());
        if (dynamicCollect.getContent()!=null){
            holder.contentTV.setText(dynamicCollect.getContent());
        }else {
            holder.contentImg.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(),R.drawable.cheese_2));
        }

        holder.dateTV.setText(dynamicCollect.getDate());
        return convertView;
    }

    private class ViewHolder{
        private ImageView headImg,contentImg;
        private TextView nameTV,labelTV,dateTV,contentTV;
    }
}
