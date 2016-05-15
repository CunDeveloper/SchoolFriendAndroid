package com.nju.adatper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.nju.activity.R;
import com.nju.fragment.BaseFragment;
import com.nju.fragment.PersonCircleDetailFragment;
import com.nju.fragment.PersonCircleDetailPicFragment;
import com.nju.model.AlumniTalk;
import com.nju.model.EntryDate;
import com.nju.model.MyDynamic;
import com.nju.util.DateUtil;
import com.nju.util.ToastUtil;

import java.util.ArrayList;

import model.Content;

/**
 * Created by cun on 2016/3/19.
 */
public class PersonDynamicAdapter extends BaseAdapter {
    private BaseFragment mFragment;
    private ArrayList<MyDynamic> mMyDynamics;
    private int mAuthorId;

    public PersonDynamicAdapter(BaseFragment fragment, ArrayList<MyDynamic> dynamics,int authorId) {
        mFragment = fragment;
        mMyDynamics = dynamics;
        mAuthorId = authorId;
    }

    @Override
    public int getCount() {
        return mMyDynamics.size();
    }

    @Override
    public Object getItem(int position) {
        return mMyDynamics.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mFragment.getContext()).inflate(R.layout.person_item, parent, false);
            holder = new ViewHolder();
            holder.dayTV = (TextView) convertView.findViewById(R.id.day_tv);
            holder.monthTV = (TextView) convertView.findViewById(R.id.month_tv);
            holder.listView = (ListView) convertView.findViewById(R.id.listView);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        MyDynamic dynamic = mMyDynamics.get(position);
        EntryDate entryDate = dynamic.getEntryDate();
        final ArrayList<AlumniTalk> alumniTalks = dynamic.getAlumniTalks();
        holder.listView.setAdapter(new PersonDynamicListItemAdapter(mFragment.getContext(), alumniTalks));
        holder.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String imgPaths = alumniTalks.get(position).getImagePaths();
                if (imgPaths != null && !imgPaths.trim().equals("")){
                    PersonCircleDetailPicFragment fragment = PersonCircleDetailPicFragment.newInstance(alumniTalks.get(position), 0);
                    mFragment.getHostActivity().open(fragment,fragment);
                } else {
                    mFragment.getHostActivity().open(PersonCircleDetailFragment.newInstance(alumniTalks.get(position)));
                }

            }
        });

        holder.dayTV.setText(entryDate.getDay());
        holder.monthTV.setText(DateUtil.getNoZeroMonth(entryDate.getMonth()));
        return convertView;
    }

    private class ViewHolder {
        private TextView dayTV, monthTV;
        private ListView listView;
    }
}
