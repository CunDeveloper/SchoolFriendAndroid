package com.nju.adatper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.nju.activity.R;
import com.nju.fragment.BaseFragment;
import com.nju.fragment.SingleChoosePicFragment;
import com.nju.model.Image;
import com.nju.util.Divice;
import com.nju.util.SchoolFriendLayoutParams;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by cun on 2016/5/2.
 */
public class SingleChoosePicAdapter extends BaseAdapter {
    private static final int TYPE_CAPTURE = 0;
    private static final int TYPE_IMAGE = 1;
    private static final int TYPE_MAX_COUNT = TYPE_IMAGE + 1;
    private BaseFragment mFragment;
    private ArrayList<Image> mImagePaths;

    public SingleChoosePicAdapter(BaseFragment fragment,ArrayList<Image> images){
        this.mFragment = fragment;
        this.mImagePaths = images;
    }

    @Override
    public int getCount() {
        return mImagePaths.size();
    }

    @Override
    public Object getItem(int position) {
        return mImagePaths.get(position);
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
        if (position == 0)
            return TYPE_CAPTURE;
        else
            return TYPE_IMAGE;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        int type = getItemViewType(position);
        if (convertView == null){
            holder = new ViewHolder();
            switch (type){
                case TYPE_IMAGE:{
                    convertView = LayoutInflater.from(mFragment.getContext()).
                            inflate(R.layout.single_choose_image_item, parent, false);
                    holder.imageView = (ImageView) convertView.findViewById(R.id.mImageView);
                    holder.imgWidth = Divice.dividerScreen(mFragment.getContext(), 3);
                    convertView.setTag(holder);
                    break;
                }
                case TYPE_CAPTURE:{
                    convertView = LayoutInflater.from(mFragment.getContext()).inflate(R.layout.choose_image_camera, parent,false);
                    convertView.setLayoutParams(new SchoolFriendLayoutParams(mFragment.getContext()).chooseImgParams());

                    break;
                }
            }
        }
        holder = (ViewHolder) convertView.getTag();
        if (type == TYPE_IMAGE) {
            Picasso.with(mFragment.getContext()).load(new File(mImagePaths.get(position).getData())).resize(holder.imgWidth, holder.imgWidth).centerCrop()
                    .into(holder.imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onError() {

                        }
                    });
        }
        return convertView;
    }

    private class ViewHolder {
        public ImageView imageView;
        public int imgWidth;
    }
}
