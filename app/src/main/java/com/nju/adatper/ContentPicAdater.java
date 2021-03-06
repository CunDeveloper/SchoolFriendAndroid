package com.nju.adatper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nju.activity.R;
import com.nju.http.ImageDownloader;
import com.nju.util.Divice;
import com.nju.util.PathConstant;

/**
 * Created by cun on 2016/3/31.
 */
public class ContentPicAdater extends BaseAdapter {

    private static final String TAG = ContentPicAdater.class.getSimpleName();
    private Context mContext;
    private String[] mImages;
    private String mBaseImg;

    public ContentPicAdater(Context context, String imgPath, String[] images) {
        this.mContext = context;
        this.mImages = images;
        mBaseImg = imgPath;
    }

    private int computeWidth() {
        int diviceWidth = Divice.getDisplayWidth(mContext);
        int spaceWidth = (int) Divice.convertDpToPixel(90, mContext);
        return (diviceWidth - spaceWidth) / 3;
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
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.content_pic_img, parent, false);
            holder = new ViewHolder();
            holder.img = (ImageView) convertView;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(computeWidth(), computeWidth());
            holder.img.setLayoutParams(params);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        final String fileName = mImages[position];
        if (fileName != null) {
            final String url = PathConstant.IMAGE_PATH_SMALL + mBaseImg + fileName;
            ImageDownloader.with(mContext).download(url, holder.img);
        }

        return convertView;
    }

    private class ViewHolder {
        private ImageView img;
    }
}
