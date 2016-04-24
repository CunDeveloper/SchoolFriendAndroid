package com.nju.adatper;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.nju.activity.MessageContentIdEvent;
import com.nju.activity.R;
import com.nju.fragment.BaseFragment;
import com.nju.fragment.CircleImageViewFragment;
import com.nju.fragment.RecommendWorkItemDetailFragment;
import com.nju.model.RecommendWork;
import com.nju.util.Constant;
import com.nju.util.DateUtil;
import com.nju.util.PathConstant;
import com.nju.util.StringBase64;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by xiaojuzhang on 2016/3/14.
 */
public class RecommendWorkItemAdapter extends BaseAdapter {

    private static final String TAG = RecommendWorkItemAdapter.class.getSimpleName();
    private BaseFragment mFragment;
    private ArrayList<RecommendWork> mRecommendWorks;

    public RecommendWorkItemAdapter(BaseFragment fragment, ArrayList<RecommendWork> recommendWorks) {
        mRecommendWorks = recommendWorks;
        mFragment = fragment;
    }

    @Override
    public int getCount() {
        return mRecommendWorks.size();
    }

    @Override
    public Object getItem(int position) {
        return mRecommendWorks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mFragment.getContext()).inflate(R.layout.recommend_work_item_list_item, parent, false);
            holder = new ViewHolder();
            holder.countTx = (TextView) convertView.findViewById(R.id.comment_number_tv);
            holder.dateTx = (TextView) convertView.findViewById(R.id.re_list_item_date);
            holder.contentTV = (TextView) convertView.findViewById(R.id.content_tv);
            holder.titleTx = (TextView) convertView.findViewById(R.id.re_list_item_title);
            holder.usernameTx = (TextView) convertView.findViewById(R.id.name_tv);
            holder.deleteTV = (TextView) convertView.findViewById(R.id.delete_tv);
            holder.gridView = (GridView) convertView.findViewById(R.id.mGridView);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        final RecommendWork recommendWork = mRecommendWorks.get(position);
        int author_id = recommendWork.getAuthorInfo().getAuthorId();
        if (author_id == mFragment.getHostActivity().userId()){
            holder.deleteTV.setText(Constant.DELETE);
        }else {
            holder.deleteTV.setText("");
        }
        holder.deleteTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new MessageContentIdEvent(recommendWork.getId()));
            }
        });
        try {
            holder.titleTx.setText(StringBase64.decode(recommendWork.getTitle()));
        } catch (IllegalArgumentException e) {
            holder.titleTx.setText(Constant.UNKNOWN_CHARACTER);
        }
        holder.titleTx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFragment.getHostActivity().open(RecommendWorkItemDetailFragment.newInstance(recommendWork));
            }
        });
        holder.usernameTx.setText(recommendWork.getAuthor().getAuthorName());
        try{
            holder.contentTV.setText(StringBase64.decode(recommendWork.getContent()));
        }catch (IllegalArgumentException e){
            holder.contentTV.setText(Constant.UNKNOWN_CHARACTER);
        }
        if (recommendWork.getImgPaths() == null){
            holder.gridView.setAdapter(new ThreePicsAdapter(mFragment.getContext(), PathConstant.ALUMNI_RECOMMEND_IMG_PATH,Constant.EMPTY));
        }
        else {
            holder.gridView.setAdapter(new ThreePicsAdapter(mFragment.getContext(),PathConstant.ALUMNI_RECOMMEND_IMG_PATH,recommendWork.getImgPaths().split(",")));
        }
        holder.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mFragment.getHostActivity().open(CircleImageViewFragment.newInstance(recommendWork.getImgPaths().split(","), position, PathConstant.ALUMNI_RECOMMEND_IMG_PATH));
            }
        });
        holder.countTx.setText(recommendWork.getCommentCount() + "");
        holder.dateTx.setText(DateUtil.getRelativeTimeSpanString(recommendWork.getDate()));
        return convertView;
    }

    private class ViewHolder {
        private TextView titleTx;
        private TextView dateTx;
        private TextView countTx;
        private TextView usernameTx;
        private TextView contentTV;
        private TextView deleteTV;
        private GridView gridView;
    }
}
