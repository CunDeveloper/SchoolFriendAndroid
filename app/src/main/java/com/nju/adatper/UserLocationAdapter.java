package com.nju.adatper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.nju.activity.R;
import com.nju.model.LocationInfo;
import java.util.List;

/**
 * Created by xiaojuzhang on 2015/11/19.
 */
public class UserLocationAdapter extends BaseAdapter {

    private List<LocationInfo> mInfolist;
    private Context mContext;

    public UserLocationAdapter(List<LocationInfo> list,Context context) {
        mInfolist = list;
        mContext = context;
    }
    @Override
    public int getViewTypeCount() {

        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }
    @Override
    public int getCount() {
        return mInfolist.size();
    }

    @Override
    public Object getItem(int position) {
        return mInfolist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.user_location_item,null);
            holder = new ViewHolder();
            holder.nameView = (TextView) convertView.findViewById(R.id.user_location_item_name);
            holder.addressView = (TextView) convertView.findViewById(R.id.user_location_item_address);
            holder.imageView = (ImageView) convertView.findViewById(R.id.user_location_item_image);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.nameView.setText(mInfolist.get(position).getLocatName());
        holder.addressView.setText(mInfolist.get(position).getAddress());
        holder.imageView.setImageDrawable(mInfolist.get(position).getSelectBg());
        return convertView;
    }

    private class ViewHolder {
        public TextView nameView;
        public TextView addressView;
        public ImageView imageView;
    }
}
