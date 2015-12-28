package com.nju.adatper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nju.activity.R;

import java.util.List;

/**
 * Created by xiaojuzhang on 2015/11/17.
 */
public class EmotionAdapter extends BaseAdapter {
    private List<String> mEmotions;
    private Context mContext;

    public EmotionAdapter(List<String> mEmotions, Context context) {
        this.mEmotions = mEmotions;
        this.mContext = context;
    }
    @Override
    public int getCount() {
        return mEmotions.size();
    }

    @Override
    public Object getItem(int position) {
        return mEmotions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.emotion_adapter_item,null);
            viewHolder.textView = (TextView) convertView;
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textView.setText(mEmotions.get(position));
         return convertView;
    }
    private class ViewHolder {
        public TextView textView;
    }
}
