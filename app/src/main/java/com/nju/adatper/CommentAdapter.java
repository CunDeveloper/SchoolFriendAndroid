package com.nju.adatper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nju.activity.R;
import com.nju.event.MessageEventId;
import com.nju.model.ContentComment;
import com.nju.util.Constant;
import com.nju.util.DateUtil;
import com.nju.util.StringBase64;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * Created by cun on 2016/3/27.
 */
public class CommentAdapter  extends BaseAdapter {
    private Context mContext;
    private ArrayList<ContentComment> mComments;
    public CommentAdapter(Context context, ArrayList<ContentComment> comments) {
        mContext = context;
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
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.comment_item,parent,false);
            holder.nameTV = (TextView) convertView.findViewById(R.id.name_tv);
            holder.labelTV = (TextView) convertView.findViewById(R.id.label_tv);
            holder.contentTV = (TextView) convertView.findViewById(R.id.content_tv);
            holder.dateTV = (TextView) convertView.findViewById(R.id.date_tv);
            holder.commentIconTV = (TextView) convertView.findViewById(R.id.comment_icon_tv);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        final ContentComment contentComment = mComments.get(position);
        holder.nameTV.setText(contentComment.getCommentAuthor().getAuthorName());
        holder.labelTV.setText(contentComment.getCommentAuthor().getLabel());
        try{
            holder.contentTV.setText(StringBase64.decode(contentComment.getContent()));
        }catch (IllegalArgumentException e){
            holder.contentTV.setText(Constant.UNKNOWN_CHARACTER);
        }
        holder.commentIconTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new MessageEventId(contentComment.getId()));
            }
        });
        holder.dateTV.setText(DateUtil.getRelativeTimeSpanString(contentComment.getDate()));
        return convertView;
    }

    private class ViewHolder{
        private TextView nameTV,labelTV,contentTV,dateTV,commentTV,commentIconTV;
        private ImageView headImg;
    }
}
