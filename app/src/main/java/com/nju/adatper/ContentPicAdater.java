package com.nju.adatper;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

import com.nju.activity.R;
import com.nju.http.ImageDownloader;
import com.nju.util.Divice;
import com.nju.util.PathConstant;
import com.nju.util.StringBase64;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by cun on 2016/3/31.
 */
public class ContentPicAdater extends BaseAdapter {

    private static final String TAG = ContentPicAdater.class.getSimpleName() ;
    private Context mContext;
    private String[] mImages;

    private int computeWidth(){
        int diviceWidth = Divice.getDisplayWidth(mContext);
        int spaceWidth = (int) Divice.convertDpToPixel(90,mContext);
        return (diviceWidth-spaceWidth)/3;
    }

    public ContentPicAdater(Context context, String[] images) {
        this.mContext = context;
        this.mImages = images;
    }

    @Override
    public int getCount() {
        return mImages.length;
    }

    @Override
    public Object getItem(int position) {
        return mImages[position];
    }

    @Override
    public long getItemId(int position) {
        return  position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.content_pic_img,parent,false);
            holder = new ViewHolder();
            holder.img = (ImageView) convertView;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(computeWidth(),computeWidth());
            holder.img.setLayoutParams(params);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        final String fileName = mImages[position];
        if (fileName != null){
            final String url = PathConstant.IMAGE_PATH_SMALL+PathConstant.ALUMNI_TALK_IMG_PATH+fileName;
            ImageDownloader.download(url,holder.img);
        }

        return convertView;
    }

    private class ViewHolder{
        private ImageView img;
    }
}
