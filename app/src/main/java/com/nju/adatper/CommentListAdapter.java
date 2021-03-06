package com.nju.adatper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nju.activity.R;
import com.nju.fragment.BaseFragment;
import com.nju.fragment.CircleFragment;
import com.nju.model.ContentComment;
import com.nju.util.Constant;
import com.nju.util.StringBase64;

import java.util.ArrayList;

/**
 * Created by cun on 2016/4/13.
 */
public class CommentListAdapter extends BaseAdapter {
    private BaseFragment mFragment;
    private ArrayList<ContentComment> mComments;

    public CommentListAdapter(BaseFragment fragment, ArrayList<ContentComment> comments) {
        mFragment = fragment;
        mComments = comments;
    }

    @Override
    public int getCount() {
        return mComments.size();
    }

    @Override
    public Object getItem(int position) {
        return mComments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mFragment.getContext()).inflate(R.layout.comment_list_item, parent, false);
            holder = new ViewHolder();
            holder.commentTV = (TextView) convertView.findViewById(R.id.comment_tv);
            holder.commentedTV = (TextView) convertView.findViewById(R.id.commented_tv);
            holder.replayTV = (TextView) convertView.findViewById(R.id.replay_tv);
            holder.contentTV = (TextView) convertView.findViewById(R.id.content_tv);

            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        final ContentComment contentComment = mComments.get(position);
        holder.commentTV.setText(contentComment.getCommentAuthor().getAuthorName());
        holder.commentTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFragment.getHostActivity().open(CircleFragment.newInstance(contentComment.getCommentAuthor()));
            }
        });
        String commentedName = contentComment.getCommentedAuthor().getAuthorName();
        if (commentedName == null || commentedName.equals("")) {
            holder.commentedTV.setText("");
            holder.replayTV.setText("");
        } else {
            holder.commentedTV.setText(commentedName);
            holder.replayTV.setText(Constant.REPLAY);
        }
        try {
            holder.contentTV.setText(StringBase64.decode(contentComment.getContent()));
        } catch (IllegalArgumentException e) {

        }
        return convertView;
    }

    private class ViewHolder {
        private TextView commentTV, commentedTV, replayTV, contentTV;

    }
}
