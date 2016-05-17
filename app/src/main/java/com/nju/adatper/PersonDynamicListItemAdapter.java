package com.nju.adatper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.nju.activity.R;
import com.nju.model.AlumniTalk;
import com.nju.util.Constant;
import com.nju.util.PathConstant;
import com.nju.util.StringBase64;

import java.util.ArrayList;

/**
 * Created by cun on 2016/3/19.
 */
public class PersonDynamicListItemAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<AlumniTalk> mAlumniTalks;

    public PersonDynamicListItemAdapter(Context context, ArrayList<AlumniTalk> alumniTalks) {
        mContext = context;
        mAlumniTalks = alumniTalks;
    }

    @Override
    public int getCount() {
        return mAlumniTalks.size();
    }

    @Override
    public Object getItem(int position) {
        return mAlumniTalks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.person_dynamic_item_listitem, parent, false);
            holder = new ViewHolder();
            holder.contentTV = (TextView) convertView.findViewById(R.id.content_tv);

            holder.gridView = (GridView) convertView.findViewById(R.id.mPersonItemGridView);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        AlumniTalk alumniTalk = mAlumniTalks.get(position);

        try {
            holder.contentTV.setText(StringBase64.decode(alumniTalk.getContent()));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            holder.contentTV.setText(Constant.UNKNOWN_CHARACTER);
        }
        if (alumniTalk.getImagePaths() == null) {
            holder.gridView.setAdapter(new ThreePicsAdapter(mContext, PathConstant.ALUMNI_TALK_IMG_PATH, Constant.EMPTY, 120));
        } else {
            holder.gridView.setAdapter(new ThreePicsAdapter(mContext, PathConstant.ALUMNI_TALK_IMG_PATH, alumniTalk.getImagePaths().split(","), 120));
        }
        return convertView;
    }

    private class ViewHolder {
        private TextView contentTV;
        private GridView gridView;
    }
}
