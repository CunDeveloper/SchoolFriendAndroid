package com.nju.adatper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nju.activity.R;
import com.nju.model.Comment;

import java.util.List;

/**
 * Created by xiaojuzhang on 2015/11/20.
 */
public class UserCommentListAdapter extends BaseAdapter {
    private Context mContext;
    private List<Comment> mList;

    public UserCommentListAdapter(Context context,List<Comment> list) {
        mContext = context;
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
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.user_comment_list,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.mContentTV = (TextView) convertView.findViewById(R.id.user_comment_list_text);
            viewHolder.mUserNameTV = (TextView) convertView.findViewById(R.id.user_comment_list_user);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Comment comment = mList.get(position);
        viewHolder.mUserNameTV.setText(comment.getcUserName()+":");
        viewHolder.mContentTV.setText(comment.getContent());
        return convertView;
    }

    private class ViewHolder{
        public TextView mUserNameTV;
        public TextView mContentTV;
    }

}
