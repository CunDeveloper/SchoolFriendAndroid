package com.nju.adatper;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.TextView;

import com.nju.activity.R;
import com.nju.fragment.BaseFragment;
import com.nju.fragment.CircleImageViewFragment;
import com.nju.fragment.PersonCircleFragment;
import com.nju.model.FriendWeibo;
import com.nju.model.User;
import com.nju.util.CommentPopupWindow;
import com.nju.util.SchoolFriendLayoutParams;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by xiaojuzhang on 2015/11/19.
 */
public class FriendContentAdapter extends BaseAdapter {

    private static final String TAG = FriendContentAdapter.class.getSimpleName();
    private List<FriendWeibo> mWeiBelist;
    private Context mContext;
    private Handler mHandler;
    private ListView mListView;
    private BaseFragment mFragment;
    public FriendContentAdapter(List<FriendWeibo> list, Context context,Handler handler,ListView listView,BaseFragment fragment) {
        mWeiBelist = list;
        mContext = context;
        mHandler = handler;
        mListView = listView;
        mFragment = fragment;
    }
    @Override
    public int getViewTypeCount() {

        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }
    @Override
    public int getCount() {
        return mWeiBelist.size();
    }

    @Override
    public Object getItem(int position) {
        return mWeiBelist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.school_friend_item,parent,false);
            holder = new ViewHolder();
            holder.mContentTextView = (TextView) convertView.findViewById(R.id.school_friend_item_content);
            holder.headImg = (ImageView) convertView.findViewById(R.id.school_friend_item_headicon_img);
            holder.headImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFragment.getHostActivity().open(PersonCircleFragment.newInstance(mWeiBelist.get(position).getUser().getName()));
                }
            });
            holder.mUserNameTextView = (TextView) convertView.findViewById(R.id.school_friend_item_name_text);
            holder.mUserLabel = (TextView) convertView.findViewById(R.id.school_friend_item_label_text);
            holder.mPublishDate = (TextView) convertView.findViewById(R.id.school_friend_item_publish_date);
            holder.mLocationTextView = (TextView) convertView.findViewById(R.id.school_friend_item_publish_location);
            holder.mCommentListView = (ListView) convertView.findViewById(R.id.school_friend_item_listView);
            holder.praiseView = LayoutInflater.from(mContext).inflate(R.layout.user_comment_list_header,holder.mCommentListView,false);
            holder.mPraiseUserTextView = (TextView) holder.praiseView.findViewById(R.id.user_comment_list_header_parise_username);
            holder.mPraiseUserTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFragment.getHostActivity().open(PersonCircleFragment.newInstance(mWeiBelist.get(position).getPraiseUserName()));
                }
            });
            holder.imageViewList = new ArrayList<>();

            final ImageView imageView = (ImageView) convertView.findViewById(R.id.school_friend_item_com_img);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ListPopupWindow listPopupWindow = new CommentPopupWindow(mContext, imageView);
                    listPopupWindow.setAdapter(new UserCommentItemListAdapter(mContext, position, mWeiBelist, mHandler, listPopupWindow,mListView));
                    listPopupWindow.show();
                }
            });

            SchoolFriendLayoutParams contentLayoutParams = new SchoolFriendLayoutParams(mContext);
            if(!mFragment.getHostActivity().isPhone()){
                holder.mParams = contentLayoutParams.imgParams();
            }else{
                Resources resources = mContext.getResources();
                holder.mParams = contentLayoutParams.imgPhoneParams(resources.getDimension(R.dimen.phone_head_length)
                ,3*resources.getDimension(R.dimen.phone_apadding));
            }

            holder.mLocationParams = contentLayoutParams.locationParams();

            ImageView imageView1 =(ImageView) convertView.findViewById(R.id.school_frien_item_im1);

            holder.imageViewList.add(imageView1);
            ImageView imageView2 = (ImageView) convertView.findViewById(R.id.school_frien_item_im2);

            holder.imageViewList.add(imageView2);
            ImageView imageView3 = (ImageView) convertView.findViewById(R.id.school_frien_item_im3);

            holder.imageViewList.add(imageView3);
            ImageView imageView4 = (ImageView) convertView.findViewById(R.id.school_frien_item_im4);

            holder.imageViewList.add(imageView4);

            ImageView imageView5 = (ImageView) convertView.findViewById(R.id.school_frien_item_im5);

            holder.imageViewList.add(imageView5);

            ImageView imageView6 = (ImageView) convertView.findViewById(R.id.school_frien_item_im6);

            holder.imageViewList.add(imageView6);

            ImageView imageView7 = (ImageView) convertView.findViewById(R.id.school_frien_item_im7);

            holder.imageViewList.add(imageView7);

            ImageView imageView8 = (ImageView) convertView.findViewById(R.id.school_frien_item_im8);

            holder.imageViewList.add(imageView8);

            ImageView imageView9 = (ImageView) convertView.findViewById(R.id.school_frien_item_im9);

            holder.imageViewList.add(imageView9);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        FriendWeibo friendWeibo = mWeiBelist.get(position);
        holder.mContentTextView.setText(friendWeibo.getContent());
        holder.headImg.setImageDrawable(friendWeibo.getHeadIcon());
        User user = friendWeibo.getUser();
        holder.mUserNameTextView.setText(user.getName());
        holder.mUserLabel.setText(user.getHigherSchool()+user.getXueYuan()+" "+user.getStartYear());
        holder.mPublishDate.setText(friendWeibo.getPublishDate());
        if(friendWeibo.getPraiseUserName().equals("")){
            holder.mCommentListView.removeHeaderView(holder.praiseView);
        }else{
            holder.mPraiseUserTextView.setText(friendWeibo.getPraiseUserName());
            if(holder.mCommentListView.getHeaderViewsCount()==0){
                holder.mCommentListView.addHeaderView(holder.praiseView);
            }
        }
        holder.mCommentListView.setAdapter(new UserCommentListAdapter(mContext,friendWeibo.getComments()));
        if(!friendWeibo.getLocation().equals("")){
            holder.mLocationTextView.setLayoutParams(holder.mLocationParams);
            holder.mLocationTextView.setText(friendWeibo.getLocation());
        }
        final ArrayList<Bitmap> drawables = friendWeibo.getImages();
        int length = drawables.size();
        for (int i = 0;i<length;i++) {
            ImageView imageView = holder.imageViewList.get(i);
            final int finalI = i;
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFragment.getHostActivity().open(CircleImageViewFragment.newInstance(drawables, finalI));
                }
            });
            imageView.setLayoutParams(holder.mParams);
            imageView.setImageBitmap(drawables.get(i));
        }
        return convertView;
    }

    private class ViewHolder {
        public TextView mContentTextView;
        public TextView mPublishDate;
        public TextView mUserNameTextView;
        public TextView mUserLabel;
        public TextView mLocationTextView;
        public TextView mPraiseUserTextView;
        public ImageView headImg;
        public ListView mCommentListView;
        public List<ImageView> imageViewList;
        public View praiseView;
        public LinearLayout.LayoutParams mParams;
        public LinearLayout.LayoutParams mLocationParams;
    }
}
