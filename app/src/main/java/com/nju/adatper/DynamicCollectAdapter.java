package com.nju.adatper;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.nju.activity.R;
import com.nju.fragment.BaseFragment;
import com.nju.http.ImageDownloader;
import com.nju.model.AlumniDynamicCollect;
import com.nju.model.DynamicCollect;
import com.nju.util.DateUtil;
import com.nju.util.PathConstant;
import com.nju.util.StringBase64;

import java.util.ArrayList;

/**
 * Created by cun on 2016/3/19.
 */
public class DynamicCollectAdapter extends BaseAdapter {
    private ArrayList<AlumniDynamicCollect> mDynamicCollects;
    private BaseFragment mFragment;

    public DynamicCollectAdapter(BaseFragment fragment, ArrayList<AlumniDynamicCollect> dynamicCollects) {
        mFragment = fragment;
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
        if (convertView == null) {
            convertView = LayoutInflater.from(mFragment.getContext()).inflate(R.layout.dynamic_collect_item, parent, false);
            holder = new ViewHolder();
            holder.nameTV = (TextView) convertView.findViewById(R.id.name_tv);
            holder.labelTV = (TextView) convertView.findViewById(R.id.label_tv);
            holder.dateTV = (TextView) convertView.findViewById(R.id.date_tv);
            holder.contentTV = (TextView) convertView.findViewById(R.id.content_tv);
            holder.headImg = (ImageView) convertView.findViewById(R.id.head_icon);
            holder.contentImg = (ImageView) convertView.findViewById(R.id.collect_img);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.mCheckBox);
            convertView.setTag(holder);
        }
        AlumniDynamicCollect dynamicCollect = mDynamicCollects.get(position);
        holder = (ViewHolder) convertView.getTag();
        String headName = mFragment.getHostActivity().getSharedPreferences().getString(mFragment.getString(R.string.head_url),"");
        if (!headName.equals("")) {
            String headUrl = PathConstant.IMAGE_PATH_SMALL + PathConstant.HEAD_ICON_IMG + headName;
            ImageDownloader.with(mFragment.getContext()).download(headUrl,holder.headImg);
        }

        if (dynamicCollect.getText() != null) {
            holder.contentTV.setText(StringBase64.decode(dynamicCollect.getText()));
        } else {
            String url = PathConstant.IMAGE_PATH + PathConstant.ALUMNI_TALK_IMG_PATH + dynamicCollect.getImagePath();
            ImageDownloader.with(mFragment.getContext()).download(url,holder.contentImg);
        }
        holder.dateTV.setText(DateUtil.getRelativeTimeSpanString(dynamicCollect.getDate()));

        if (dynamicCollect.getCheck() == 0) {
            holder.checkBox.setVisibility(View.GONE);
        } else if (dynamicCollect.getCheck() == 1) {
            holder.checkBox.setVisibility(View.VISIBLE);
        } else if (dynamicCollect.getCheck() == 2) {
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.checkBox.setChecked(true);
        }

        return convertView;
    }

    private class ViewHolder {
        private ImageView headImg, contentImg;
        private TextView nameTV, labelTV, dateTV, contentTV;
        private CheckBox checkBox;
    }
}
