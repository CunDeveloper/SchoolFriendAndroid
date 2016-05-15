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
import com.nju.fragment.PersonRecommendWorkItemDetailFragment;
import com.nju.fragment.RecommendWorkItemDetailFragment;
import com.nju.model.EntryDate;
import com.nju.model.MyRecommend;
import com.nju.model.RecommendWork;
import com.nju.util.DateUtil;

import java.util.ArrayList;

/**
 * Created by cun on 2016/3/19.
 */
public class PersonRecommendAdapter extends BaseAdapter {
    private BaseFragment mFragment;
    private ArrayList<MyRecommend> mMyRecommends;
    private int mAuthorId;

    public PersonRecommendAdapter(BaseFragment fragment, ArrayList<MyRecommend> recommends,int authorId) {
        mFragment = fragment;
        mMyRecommends = recommends;
        mAuthorId = authorId;
    }

    @Override
    public int getCount() {
        return mMyRecommends.size();
    }

    @Override
    public Object getItem(int position) {
        // return mRecommendWorkMap.get(arrayKeys[position]);
        // return mRecommendWorks.get(position);
        return mMyRecommends.get(position);
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
        MyRecommend myRecommend = mMyRecommends.get(position);
        EntryDate entryDate = myRecommend.getEntryDate();
        final ArrayList<RecommendWork> recommendWorks = myRecommend.getRecommendWorks();

        holder.listView.setAdapter(new PersonRecommendListItemAdapter(mFragment.getContext(), recommendWorks));
        holder.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int userId = mFragment.getHostActivity().userId();
                if (userId == mAuthorId) {
                    mFragment.getHostActivity().open(PersonRecommendWorkItemDetailFragment.newInstance(recommendWorks.get(position)));
                } else {
                    mFragment.getHostActivity().open(RecommendWorkItemDetailFragment.newInstance(recommendWorks.get(position)));
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
