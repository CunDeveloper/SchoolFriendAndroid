package com.nju.adatper;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nju.activity.R;
import com.nju.http.ImageDownloader;
import com.nju.util.Divice;
import com.nju.util.PathConstant;

/**
 * Created by cun on 2016/4/1.
 */

public class ThreePicsAdapter extends BaseAdapter {

    private static final String TAG = ThreePicsAdapter.class.getSimpleName();
    private static final int TYPE_LESS_THREE_PIC = 0;
    private static final int TYPE_MORE_THREE_PIC = 1;
    private static final int TYPE_MAX_COUNT = TYPE_MORE_THREE_PIC + 1;
    private static final int MAX_PIC = 3;
    private Context mContext;
    private String[] mImages;
    private String mBaseImgPath;
    private int mExtraDp;
    private int picNumber = 0;

    public ThreePicsAdapter(Context context, String imgPath, String[] images, int extraDp) {
        this.mContext = context;
        setImages(images);
        mBaseImgPath = imgPath;
        this.mExtraDp = extraDp;
    }

    public ThreePicsAdapter(Context context, String imgPath, String[] images) {
        this.mContext = context;
        setImages(images);
        mBaseImgPath = imgPath;
        mExtraDp = 30;
    }

    private int computeWidth() {
        int diviceWidth = Divice.getDisplayWidth(mContext);
        int spaceWidth = (int) Divice.convertDpToPixel(mExtraDp, mContext);
        return (diviceWidth - spaceWidth) / MAX_PIC;
    }

    private void setImages(String[] images) {
        if (images.length > MAX_PIC) {
            picNumber = images.length;
            mImages = new String[MAX_PIC];
            for (int i = 0; i < MAX_PIC; i++) {
                mImages[i] = images[i];
            }
        } else {
            mImages = images;
        }
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
    public int getViewTypeCount() {
        return TYPE_MAX_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        if ((position + 1) % MAX_PIC == 0 && picNumber > MAX_PIC) {
            return TYPE_MORE_THREE_PIC;
        } else {
            return TYPE_LESS_THREE_PIC;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            int type = getItemViewType(position);
            switch (type) {
                case TYPE_MORE_THREE_PIC: {
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.more_three_pic_item, parent, false);
                    holder.img = (ImageView) convertView.findViewById(R.id.mImageView);
                    holder.relativeLayout = (RelativeLayout) convertView.findViewById(R.id.mRelayout);
                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(computeWidth(), computeWidth());
                    holder.img.setLayoutParams(params);
                    holder.relativeLayout.setLayoutParams(params);
                    holder.picNumberTV = (TextView) convertView.findViewById(R.id.picNumberTV);
                    convertView.setTag(holder);
                    break;
                }
                case TYPE_LESS_THREE_PIC: {
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.content_pic_img, parent, false);
                    holder.img = (ImageView) convertView;
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(computeWidth(), computeWidth());
                    holder.img.setLayoutParams(params);
                    convertView.setTag(holder);
                    break;
                }
            }

        }
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
            final String fileName = mImages[position];
            if (fileName != null) {
                final String url = PathConstant.IMAGE_PATH_SMALL + mBaseImgPath + fileName;
                Log.i(TAG, url);
                ImageDownloader.with(mContext).download(url, holder.img);
            }
            if (holder.picNumberTV != null) {
                holder.picNumberTV.setText("共" + picNumber + "张图");
            }
        }

        return convertView;
    }

    private class ViewHolder {
        private ImageView img;
        private RelativeLayout relativeLayout;
        private TextView picNumberTV;
    }
}

