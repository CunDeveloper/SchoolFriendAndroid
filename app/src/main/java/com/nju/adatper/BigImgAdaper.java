package com.nju.adatper;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;

import com.nju.activity.R;
import com.nju.fragment.BaseFragment;
import com.nju.http.ImageDownloader;
import com.nju.util.PathConstant;

/**
 * Created by cun on 2016/4/2.
 */
public class BigImgAdaper extends BaseAdapter {
    private static final String TAG = BigImgAdaper.class.getSimpleName();
    private Context mContext;
    private String[] mImgPaths;
    private String mBaseImg;
    public BigImgAdaper(Context context,String baseImg, String[] imgPaths) {
        mContext = context;
        mImgPaths = imgPaths;
        mBaseImg = baseImg;
    }

    @Override
    public int getCount() {
        return mImgPaths.length;
    }

    @Override
    public Object getItem(int position) {
        return mImgPaths[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.big_img,parent,false);
            viewHolder.imageView = (ImageView) convertView;
            convertView.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) convertView.getTag();
        final String url = PathConstant.IMAGE_PATH +mBaseImg+ mImgPaths[position];
        Log.i(TAG,url);
        ImageDownloader.download(url,viewHolder.imageView);
        return convertView;
    }

    private class ViewHolder{
        private ImageView imageView;
    }
}
