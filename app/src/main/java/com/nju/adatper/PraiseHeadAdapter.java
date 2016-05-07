package com.nju.adatper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nju.View.RoundedTransformation;
import com.nju.activity.R;
import com.squareup.picasso.Picasso;

/**
 * Created by cun on 2016/3/27.
 */
public class PraiseHeadAdapter extends BaseAdapter {
    private Context mContext;

    public PraiseHeadAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return 6;
    }

    @Override
    public Object getItem(int position) {
        return null;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.head_img, parent, false);
            holder.headImg = (ImageView) convertView;
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        Picasso.with(mContext).load(R.drawable.cheese_3)
                .transform(new RoundedTransformation(R.dimen.small_circle, 4))
                .resizeDimen(R.dimen.small_circle, R.dimen.small_circle).centerCrop()
                .into(holder.headImg);
        return convertView;
    }

    private class ViewHolder {
        private ImageView headImg;
    }
}
