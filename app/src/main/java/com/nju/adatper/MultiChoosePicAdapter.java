package com.nju.adatper;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.nju.activity.R;
import com.nju.fragment.BaseFragment;
import com.nju.fragment.CameraImageViewFragment;
import com.nju.model.Image;
import com.nju.util.Divice;
import com.nju.util.SchoolFriendLayoutParams;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by xiaojuzhang on 2015/11/25.
 */
public class MultiChoosePicAdapter extends BaseAdapter {

    public static final int CHOOSE_OK = 0;
    public static final int ADD_PIC_OK = 10;
    public static final int REMOVE_PIC_OK = 11;
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int IMAGE_SUM = 9;
    private static final int TYPE_CAPTURE = 0;
    private static final  int TYPE_IMAGE = 1;
    private static final int TYPE_MAX_COUNT = TYPE_IMAGE + 1;
    private static final String TAG = MultiChoosePicAdapter.class.getSimpleName();
    private ArrayList<Image> mImages;
    private AppCompatActivity mContext;
    private int choosePicNumber = 0;
    private Handler mHandler;
    private BaseFragment mFragment;
    public MultiChoosePicAdapter(AppCompatActivity context, ArrayList<Image> imgs, Handler handler, BaseFragment fragment){
        mContext = context;
        mImages = imgs;
        mHandler = handler;
        mFragment = fragment;
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_MAX_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
         if(position==0)
             return TYPE_CAPTURE;
         else
             return TYPE_IMAGE;
    }

    @Override
    public int getCount() {
        return mImages.size();
    }

    @Override
    public Object getItem(int position) {
        return mImages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        int type = getItemViewType(position);
        if (convertView == null) {
            holder = new ViewHolder();
            switch (type) {
                case TYPE_IMAGE:
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.choose_image_item, null);
                    holder.imageView = (ImageView) convertView.findViewById(R.id.choose_image_item_imageView);
                    final ViewHolder finalHolder1 = holder;
                    holder.imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                           mFragment.getHostActivity().open(CameraImageViewFragment.newInstance(mImages, position, mContext.getString(R.string.allPicsReview)));
                         }
                    });
                    holder.checkBox = (CheckBox) convertView.findViewById(R.id.choose_image_item_checkBox);
                    holder.checkBox.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (choosePicNumber < IMAGE_SUM) {
                                if(finalHolder1.checkBox.isChecked()){
                                    finalHolder1.imageView.setAlpha(0.5f);
                                    finalHolder1.checkBox.setChecked(true);
                                    choosePicNumber = choosePicNumber +1;
                                    mHandler.sendMessage(addImgMessage(position));
                                }
                                else {
                                    finalHolder1.imageView.setAlpha(1.0f);
                                    finalHolder1.checkBox.setChecked(false);
                                    choosePicNumber = choosePicNumber -1;
                                    mHandler.sendMessage(removeImgMessage(position));
                                }
                                mHandler.sendMessage(sendChoiceMessage());
                            }
                            else{
                                if(!finalHolder1.checkBox.isChecked()){
                                    finalHolder1.imageView.setAlpha(1.0f);
                                    finalHolder1.checkBox.setChecked(false);
                                    choosePicNumber = choosePicNumber -1;
                                    mHandler.sendMessage(sendChoiceMessage());
                                    mHandler.sendMessage(removeImgMessage(position));
                                }
                                else {
                                    Toast.makeText(mContext,mContext.getResources().getString(R.string.picture_choose_reminder),Toast.LENGTH_LONG).show();
                                }

                            }
                        }
                    });
                    holder.imgWidth = Divice.dividerScreen(mContext, 3);
                    convertView.setTag(holder);
                    break;
                case TYPE_CAPTURE:
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.choose_image_camera, null);
                    convertView.setLayoutParams(new SchoolFriendLayoutParams(mContext).chooseImgParams());
                    break;
            }

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(type == TYPE_IMAGE){
            final ViewHolder finalHolder = holder;
            Picasso.with(mContext).load(new File(mImages.get(position).getData())).resize(holder.imgWidth,holder.imgWidth).centerCrop()
                    .into(holder.imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            finalHolder.checkBox.setVisibility(View.VISIBLE);
                        }
                        @Override
                        public void onError() {

                        }
                    });
        }
        return convertView;
    }

    private Message sendChoiceMessage() {
        Message message = new Message();
        message.what = CHOOSE_OK;
        message.obj = choosePicNumber;
        return  message;
    }

    private Message addImgMessage(int position) {
        Message message = new Message();
        message.what = ADD_PIC_OK;
        message.obj = position;
        return  message;
    }

    private Message removeImgMessage(int position) {
        Message message = new Message();
        message.what = REMOVE_PIC_OK;
        message.obj = position;
        return  message;
    }

    private class ViewHolder{
        public ImageView imageView;
        public CheckBox checkBox;
        public int imgWidth;
    }
}
