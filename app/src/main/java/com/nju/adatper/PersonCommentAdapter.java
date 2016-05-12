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
import com.nju.http.ImageDownloader;
import com.nju.model.AuthorInfo;
import com.nju.model.ContentComment;
import com.nju.util.Constant;
import com.nju.util.DateUtil;
import com.nju.util.PathConstant;
import com.nju.util.StringBase64;

import java.util.ArrayList;

/**
 * Created by cun on 2016/5/10.
 */
public class PersonCommentAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<ContentComment> mComments;

    public PersonCommentAdapter(Context context,ArrayList<ContentComment> comments){
        this.mContext = context;
        this.mComments = comments;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.dynamic_comment_item,parent,false);
            convertView.setTag(holder);
            holder.headImg = (ImageView) convertView.findViewById(R.id.headIMG);
            holder.commentAuthorNameTV = (TextView) convertView.findViewById(R.id.commentTv);
            holder.commentedAuthorNameTV = (TextView) convertView.findViewById(R.id.commentedTv);
            holder.contentTV = (TextView) convertView.findViewById(R.id.contentTv);
            holder.dateTV = (TextView) convertView.findViewById(R.id.dateTv);
            holder.replayStrTV = (TextView) convertView.findViewById(R.id.replayStrTv);
        }
        holder = (ViewHolder) convertView.getTag();
        ContentComment comment = mComments.get(position);
        AuthorInfo commentAuthor = comment.getCommentAuthor();
        if (commentAuthor != null) {
            holder.commentAuthorNameTV .setText(commentAuthor.getAuthorName());
            String picName = commentAuthor.getHeadUrl();
            if (picName != null){
                String url = PathConstant.IMAGE_PATH_SMALL + PathConstant.ALUMNI_TALK_IMG_PATH + picName;
                ImageDownloader.with(mContext).download(url,holder.headImg);
            }
        }else {
            holder.commentAuthorNameTV .setText(Constant.EMPTY_STR);
            Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.head);
            holder.headImg.setImageBitmap(bitmap);
        }

        AuthorInfo commentedAuthor = comment.getCommentedAuthor();
        if (commentedAuthor != null){
            String commentedAuthorName = commentedAuthor.getAuthorName();
            if (commentedAuthorName != null && ! commentedAuthorName.equals("")){
                holder.replayStrTV.setText(Constant.REPLAY);
                holder.commentedAuthorNameTV.setText(commentedAuthorName);
            } else {
                holder.replayStrTV.setText(Constant.EMPTY_STR);
                holder.commentedAuthorNameTV.setText(Constant.EMPTY_STR);
            }
        } else {
            holder.replayStrTV.setText(Constant.EMPTY_STR);
            holder.commentedAuthorNameTV.setText(Constant.EMPTY_STR);
        }
        try{
            holder.contentTV.setText(StringBase64.decode(comment.getContent()));
        }catch (IllegalArgumentException e){
            holder.contentTV.setText(Constant.UNKNOWN_CHARACTER);
        }
        holder.dateTV.setText(DateUtil.getTimeString(comment.getDate()));
        return convertView;
    }

    private class ViewHolder{
        private ImageView headImg;
        private TextView commentAuthorNameTV,commentedAuthorNameTV,dateTV,contentTV,replayStrTV;
    }
}
