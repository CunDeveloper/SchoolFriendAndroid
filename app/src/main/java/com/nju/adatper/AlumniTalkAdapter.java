package com.nju.adatper;

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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nju.activity.CommentOtherEvent;
import com.nju.activity.DeleteCommentEvent;
import com.nju.activity.MessageContentIdEvent;
import com.nju.activity.PersonInfoEvent;
import com.nju.activity.R;
import com.nju.fragment.BaseFragment;
import com.nju.fragment.CircleImageViewFragment;
import com.nju.http.ImageDownloader;
import com.nju.model.AlumniTalk;
import com.nju.model.AlumnicTalkPraise;
import com.nju.model.ContentComment;
import com.nju.util.CommentPopupWindow;
import com.nju.util.Constant;
import com.nju.util.DateUtil;
import com.nju.util.HeadIcon;
import com.nju.util.PathConstant;
import com.nju.util.StringBase64;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

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
            holder.listViewHead = (LinearLayout) LayoutInflater.from(mContext.getContext()).inflate(R.layout.comment_list_head, parent, false);
            holder.commentTV = (TextView) convertView.findViewById(R.id.comment_icon_tv);
            holder.deleteTV = (TextView) convertView.findViewById(R.id.delete_tv);
            holder.commentListView = (ListView) convertView.findViewById(R.id.comment_listView);
            holder.commentListView.addHeaderView(holder.listViewHead);
            holder.mPicGridView = (GridView) convertView.findViewById(R.id.pics_gridview);
            convertView.setTag(holder);

        }
        holder = (ViewHolder) convertView.getTag();
        final AlumniTalk alumniTalk = mAlumniTalks.get(position);
        final TextView tempTV = holder.commentTV;
        tempTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListPopupWindow listPopupWindow = new CommentPopupWindow(mContext.getContext(),tempTV);
                listPopupWindow.setAdapter(new UserCommentItemListAdapter(mContext.getContext(),listPopupWindow, position));
                listPopupWindow.show();
            }
        });
        HeadIcon.setUp(holder.headImg,alumniTalk.getAuthorInfo());
        try{
            holder.contentTV.setText(StringBase64.decode(alumniTalk.getContent()));
        }catch (IllegalArgumentException e){
            holder.contentTV.setText(Constant.UNKNOWN_CHARACTER);
        }
        if (alumniTalk.getLocation()!=null){
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            holder.locationTV.setLayoutParams(params);
            holder.locationTV.setVisibility(View.VISIBLE);
            holder.locationTV.setText(alumniTalk.getLocation());
        }else {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(0,0);
            holder.locationTV.setLayoutParams(params);
            holder.locationTV.setVisibility(View.GONE);
        }
        int authorId = alumniTalk.getAuthorInfo().getAuthorId();
        if (authorId == mContext.getHostActivity().userId()){
            holder.deleteTV.setText(Constant.DELETE);
        }else {
            holder.deleteTV.setText("");
        }
        holder.deleteTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new MessageContentIdEvent(alumniTalk.getId()));
            }
        });
        holder.nameTV.setText(alumniTalk.getAuthorInfo().getAuthorName());
        holder.nameTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new PersonInfoEvent(alumniTalk.getAuthorInfo()));
            }
        });
        holder.labelTV.setText(alumniTalk.getAuthorInfo().getLabel());
        holder.dateTV.setText(DateUtil.getRelativeTimeSpanString(alumniTalk.getDate()));

        if (alumniTalk.getImagePaths() == null){
            holder.mPicGridView.setAdapter(new ContentPicAdater(mContext.getContext(),PathConstant.ALUMNI_TALK_IMG_PATH,empty));
        }
        else {
            holder.mPicGridView.setAdapter(new ContentPicAdater(mContext.getContext(),PathConstant.ALUMNI_TALK_IMG_PATH,alumniTalk.getImagePaths().split(",")));
        }
        holder.mPicGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mContext.getHostActivity().open(CircleImageViewFragment.newInstance(alumniTalk.getImagePaths().split(","), position, PathConstant.ALUMNI_TALK_IMG_PATH));
            }
        });
        holder.listViewHead.removeAllViews();
        ArrayList<AlumnicTalkPraise> alumnicTalkPraises = alumniTalk.getTalkPraises();
        for (int i = 0;i<alumnicTalkPraises.size();i++){
            TextView textView = (TextView) LayoutInflater.from(mContext.getContext()).inflate(R.layout.dynamic_praise_item,parent,false);
            textView.setText(alumnicTalkPraises.get(i).getPraiseAuthor().getAuthorName());
            holder.listViewHead.addView(textView,i);
        }
        final ArrayList<ContentComment> comments = alumniTalk.getComments();
        holder.listViewHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        holder.commentListView.setAdapter(new CommentListAdapter(mContext.getContext(), comments));
        holder.commentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EventBus.getDefault().post(new CommentOtherEvent(comments.get(position - 1)));
            }
        });

        holder.commentListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                EventBus.getDefault().post(new DeleteCommentEvent(comments.get(position - 1)));
                return true;
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
        private LinearLayout listViewHead;
        private TextView praiseItemTV;
        private TextView commentTV;
        private TextView deleteTV;
    }
}
