package com.nju.adatper;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nju.activity.R;
import com.nju.util.Divice;

import java.util.List;

/**
 * Created by xiaojuzhang on 2015/11/17.
 */
public class EmotionAdatper1 extends BaseAdapter {

    private List<String> emotions;
    private Context mContext;
    public EmotionAdatper1(List<String> emotions,Context context) {
        this.emotions = emotions;
        this.mContext = context;
    }
    @Override
    public int getCount() {
        return emotions.size();
    }

    @Override
    public Object getItem(int position) {
        return emotions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.emotion_adapter_item,null);
            viewHolder.textView = (TextView) convertView;
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textView.setText(emotions.get(position));
         return convertView;
    }
    private class ViewHolder {
        public TextView textView;
    }
}
