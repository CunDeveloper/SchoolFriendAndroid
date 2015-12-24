package com.nju.adatper;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nju.activity.R;
import com.nju.util.DateUtil;

import java.util.ArrayList;
import java.util.Calendar;

import model.Content;

/**
 * Created by cun on 2015/12/13.
 */
public class PersonCircleAdapter extends BaseAdapter {
    private static final String TAG = PersonCircleAdapter.class.getSimpleName();
    private Context mContent;
    private ArrayList<Content> mList;
    public PersonCircleAdapter(Context context,ArrayList<Content> list){
        mContent = context;
        mList = list;
    }
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
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
            convertView = LayoutInflater.from(mContent).inflate(R.layout.fragment_person_circle_item ,null);
            holder.mContentTV = (TextView) convertView.findViewById(R.id.fragment_person_circle_item_content_tv);
            holder.mImageView = (ImageView) convertView.findViewById(R.id.fragment_person_circle_item_image);
            holder.mDayTV = (TextView) convertView.findViewById(R.id.fragment_person_circle_item_day_tv);
            holder.mMonthTV = (TextView) convertView.findViewById(R.id.fragment_person_circle_item_month_tv);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        Content content = mList.get(position);
        holder.mContentTV.setText(content.getContent());
        Calendar calendar = DateUtil.getCalendar(content.getDate());
        //holder.mDayTV.setText(DateUtil.day(calendar));
        Log.e(TAG,DateUtil.month(calendar)+"");
        holder.mMonthTV.setText(DateUtil.month(calendar));
        return convertView;
    }

    private static class ViewHolder {
        public TextView mDayTV;
        public TextView mMonthTV;
        public ImageView mImageView;
        public TextView mContentTV;

    }
}
