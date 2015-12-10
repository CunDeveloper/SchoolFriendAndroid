package com.nju.adatper;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.nju.activity.R;
import com.nju.util.Divice;

import java.util.List;

/**
 * Created by xiaojuzhang on 2015/11/17.
 */
public class EmotionAdatper extends BaseAdapter {

    private List<Drawable> emotions;
    private Context mContext;
    public EmotionAdatper(List<Drawable> emotions,Context context) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.publish_content_image_item,null);
            int screenWidth = Divice.getDisplayWidth(mContext);
            float paddingLeft;
            int paddingWidth;
            int width;
            if (Divice.isPhone()) {
                paddingLeft = mContext.getResources().getDimension(R.dimen.phone_apadding);
                paddingWidth = (int) Divice.convertDpToPixel(8*paddingLeft,mContext);
                width = (screenWidth-paddingWidth)/8;
            }
            else{
                paddingLeft = mContext.getResources().getDimension(R.dimen.padding_right);
                paddingWidth = (int) Divice.convertDpToPixel(6*paddingLeft,mContext);
                width = (screenWidth-paddingWidth)/16;
            }

            AbsListView.LayoutParams parms = new AbsListView.LayoutParams(width,width);
            viewHolder.imageView = (ImageView) convertView;
            viewHolder.imageView.setLayoutParams(parms);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.imageView.setBackground(emotions.get(position));
        return convertView;
    }
    private class ViewHolder {
        public ImageView imageView;
    }
}
