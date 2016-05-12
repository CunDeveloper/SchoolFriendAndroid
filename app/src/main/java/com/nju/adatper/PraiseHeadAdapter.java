package com.nju.adatper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nju.View.RoundedTransformation;
import com.nju.activity.R;
import com.nju.http.ImageDownloader;
import com.nju.model.AuthorInfo;
import com.nju.model.RespPraise;
import com.nju.util.PathConstant;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by cun on 2016/3/27.
 */
public class PraiseHeadAdapter extends BaseAdapter {
    private Context mContext;
    private String mBase;
    private ArrayList<RespPraise> mPraiseAuthors;


    public PraiseHeadAdapter(Context context,String base,ArrayList<RespPraise> praises) {
        mContext = context;
        mPraiseAuthors = praises;
        mBase = base;
    }

    @Override
    public int getCount() {
        return mPraiseAuthors.size();
    }

    @Override
    public Object getItem(int position) {
        return mPraiseAuthors.get(position);
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.head_img, parent, false);
            holder.headImg = (ImageView) convertView;
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        AuthorInfo praiseAuthor = mPraiseAuthors.get(position).getPraiseAuthor();
        if (praiseAuthor != null) {
            String url = PathConstant.IMAGE_PATH_SMALL + mBase + praiseAuthor.getHeadUrl();
            ImageDownloader.with(mContext).download(url,holder.headImg);
        }else {
            Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.head);
            holder.headImg.setImageBitmap(bitmap);
        }
        return convertView;
    }

    private class ViewHolder {
        private ImageView headImg;
    }
}
