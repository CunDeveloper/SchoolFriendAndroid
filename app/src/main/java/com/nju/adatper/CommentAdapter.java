package com.nju.adatper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nju.activity.R;
import com.nju.event.MessageEventId;
import com.nju.event.PersonInfoEvent;
import com.nju.http.ImageDownloader;
import com.nju.model.AuthorInfo;
import com.nju.model.ContentComment;
import com.nju.util.Constant;
import com.nju.util.DateUtil;
import com.nju.util.PathConstant;
import com.nju.util.StringBase64;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * Created by cun on 2016/3/27.
 */
public class CommentAdapter extends BaseAdapter {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.comment_item, parent, false);
            holder.nameTV = (TextView) convertView.findViewById(R.id.name_tv);
            holder.contentTV = (TextView) convertView.findViewById(R.id.content_tv);
            holder.dateTV = (TextView) convertView.findViewById(R.id.date_tv);
            holder.headImg = (ImageView) convertView.findViewById(R.id.head_icon_img);
            holder.commentIconTV = (TextView) convertView.findViewById(R.id.comment_icon_tv);
            holder.replayTV = (TextView) convertView.findViewById(R.id.replay_tv);
            holder.commentedAuthorNameTV = (TextView) convertView.findViewById(R.id.commentedAuthorNameTV);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        final ContentComment contentComment = mComments.get(position);
        AuthorInfo commentedAuthor = contentComment.getCommentedAuthor();
        if (commentedAuthor != null) {
            String commentedAuthorName = commentedAuthor.getAuthorName();
            if (commentedAuthorName == null || commentedAuthorName.equals("")) {
                holder.commentedAuthorNameTV.setText("");
                holder.replayTV.setText("");
            } else {
                holder.commentedAuthorNameTV.setText(commentedAuthorName);
                holder.replayTV.setText(Constant.REPLAY);
            }
        }
        AuthorInfo commentAuthor = contentComment.getCommentAuthor();
        if (commentAuthor != null){
            String headPath = commentAuthor.getHeadUrl();
            if (headPath != null){
                String headUrl = PathConstant.IMAGE_PATH_SMALL + PathConstant.HEAD_ICON_IMG + headPath;
                ImageDownloader.with(mContext).download(headUrl,holder.headImg);
            }
        }else {
            Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.head);
            holder.headImg.setImageBitmap(bitmap);
        }


        holder.nameTV.setText(contentComment.getCommentAuthor().getAuthorName());

        try {
            holder.contentTV.setText(StringBase64.decode(contentComment.getContent()));
        } catch (IllegalArgumentException e) {
            holder.contentTV.setText(Constant.UNKNOWN_CHARACTER);
        }
        holder.commentIconTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new MessageEventId(contentComment.getId()));
            }
        });
        holder.nameTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = (TextView) v;
                EventBus.getDefault().post(new PersonInfoEvent(contentComment.getCommentAuthor()));
            }
        });
        holder.headImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new PersonInfoEvent(contentComment.getCommentAuthor()));
            }
        });
        holder.dateTV.setText(DateUtil.getRelativeTimeSpanString(contentComment.getDate()));
        return convertView;
    }

    private class ViewHolder {
        private TextView nameTV, contentTV, dateTV,
                replayTV, commentedAuthorNameTV, commentTV, commentIconTV;
        private ImageView headImg;
    }
}
