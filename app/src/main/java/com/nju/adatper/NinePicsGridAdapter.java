package com.nju.adatper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nju.activity.R;
import com.nju.model.ImageWrapper;

import java.util.ArrayList;

/**
 * Created by xiaojuzhang on 2015/12/28.
 */
public class NinePicsGridAdapter extends BaseAdapter {
    private ArrayList<ImageWrapper> mUploadImages;
    private Context mContext;

    public NinePicsGridAdapter(Context context, ArrayList<ImageWrapper> uploadImages) {
        mUploadImages = uploadImages;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mUploadImages.size();
    }

    @Override
    public Object getItem(int position) {
        return mUploadImages.get(position);
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.nine_pics_grid_item, null);
            holder.mImageView = (ImageView) convertView;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mImageView.setImageBitmap(mUploadImages.get(position).getBitmap());
        return convertView;
    }

    private static class ViewHolder {
        public ImageView mImageView;
    }
}
