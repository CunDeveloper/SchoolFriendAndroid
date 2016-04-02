package com.nju.adatper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Path;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.TextView;

import com.nju.activity.R;
import com.nju.fragment.BaseFragment;
import com.nju.fragment.CircleImageViewFragment;
import com.nju.http.ImageDownloader;
import com.nju.http.ResponseCallback;
import com.nju.http.SchoolFriendHttp;
import com.nju.model.AlumniTalk;
import com.nju.util.CommentPopupWindow;
import com.nju.util.Constant;
import com.nju.util.DateUtil;
import com.nju.util.PathConstant;
import com.nju.util.StringBase64;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cun on 2016/3/30.
 */
public class AlumniTalkAdapter extends BaseAdapter {
    private static final String TAG = AlumniTalkAdapter.class.getSimpleName();
    private BaseFragment mContext;
    private static String[] empty={};
    private ArrayList<AlumniTalk> mAlumniTalks;

    public AlumniTalkAdapter(BaseFragment context,ArrayList<AlumniTalk> alumniTalks) {
        this.mContext = context;
        this.mAlumniTalks = alumniTalks;
    }
    @Override
    public int getCount() {
        return mAlumniTalks.size();
    }

    @Override
    public Object getItem(int position) {
        return mAlumniTalks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            convertView = LayoutInflater.from(mContext.getContext()).inflate(R.layout.alumni_talk_item,parent,false);
            holder = new ViewHolder();
            holder.contentTV = (TextView) convertView.findViewById(R.id.content_tv);
            holder.nameTV = (TextView) convertView.findViewById(R.id.name_tv);
            holder.labelTV = (TextView) convertView.findViewById(R.id.label_tv);
            holder.dateTV = (TextView) convertView.findViewById(R.id.date_tv);
            holder.locationTV = (TextView) convertView.findViewById(R.id.location_tv);
            holder.commentListView = (ListView) convertView.findViewById(R.id.comment_listView);
            holder.headImg = (ImageView) convertView.findViewById(R.id.head_icon_img);
            final TextView commentTV = (TextView) convertView.findViewById(R.id.comment_icon_tv);
            commentTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ListPopupWindow listPopupWindow = new CommentPopupWindow(mContext.getContext(), commentTV);
                    listPopupWindow.setAdapter(new UserCommentItemListAdapter(mContext.getContext(),listPopupWindow, position));
                    listPopupWindow.show();
                }
            });
            holder.commentListView = (ListView) convertView.findViewById(R.id.comment_listView);
            holder.mPicGridView = (GridView) convertView.findViewById(R.id.pics_gridview);

            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        final AlumniTalk alumniTalk = mAlumniTalks.get(position);
        try{
            holder.contentTV.setText(StringBase64.decode(alumniTalk.getContent()));
        }catch (IllegalArgumentException e){
            holder.contentTV.setText(Constant.UNKNOWN_CHARACTER);
        }
        holder.locationTV.setText(alumniTalk.getLocation());
        holder.nameTV.setText(alumniTalk.getAuthorInfo().getAuthorName());
        holder.labelTV.setText(alumniTalk.getAuthorInfo().getLabel());
        holder.dateTV.setText(DateUtil.getRelativeTimeSpanString(alumniTalk.getDate()));
        final String url =PathConstant.IMAGE_PATH_SMALL+PathConstant.ALUMNI_TALK_IMG_PATH+"cb54bd60f20942f9a340a027c3e018afIMG_20160330_183310.jpg";
         ImageDownloader.download(url, holder.headImg);
        if (alumniTalk.getImagePaths() == null){
            holder.mPicGridView.setAdapter(new ContentPicAdater(mContext.getContext(),empty));
        }
        else {
            holder.mPicGridView.setAdapter(new ContentPicAdater(mContext.getContext(),alumniTalk.getImagePaths().split(",")));
        }
        holder.mPicGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mContext.getHostActivity().open(CircleImageViewFragment.newInstance(alumniTalk.getImagePaths().split(","),position));
            }
        });
        return convertView;
    }

    private class ViewHolder {
        public TextView contentTV;
        public TextView dateTV;
        public TextView nameTV;
        public TextView labelTV;
        public TextView locationTV;
        public TextView praiseUserTV;
        public ImageView headImg;
        private GridView mPicGridView;
        public ListView commentListView;
        public View praiseView;
        public LinearLayout.LayoutParams mParams;
        public LinearLayout.LayoutParams mLocationParams;
    }
}
