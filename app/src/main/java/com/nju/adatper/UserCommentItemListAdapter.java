package com.nju.adatper;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListPopupWindow;
import android.widget.TextView;

import com.nju.activity.R;
import com.nju.event.CommentEvent;
import com.nju.event.PraiseEvent;
import com.nju.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.Dictionary;
import java.util.Hashtable;

/**
 * Created by xiaojuzhang on 2015/11/20.
 */
public class UserCommentItemListAdapter extends BaseAdapter {
    public static final int PRAISE_OK = 0;
    public static final int COMMENT_OK = 1;
    private static String TAG = UserCommentItemListAdapter.class.getSimpleName();
    private Context mContext;
    private int mPosition;
    private ListPopupWindow mPopupWindow;
    private boolean mIsPraise;
    private Dictionary<Integer, Integer> listViewItemHeights = new Hashtable<>();

    public UserCommentItemListAdapter(Context context, ListPopupWindow popupWindow, int position,boolean isPraise) {
        mContext = context;
        mPosition = position;
        mPopupWindow = popupWindow;
        mIsPraise = isPraise;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Object getItem(int position) {
        return "";
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder ;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.user_comment, parent, false);
            holder.praiseTextView = (TextView) convertView.findViewById(R.id.user_comment_praise_text);
            holder.commentTextView = (TextView) convertView.findViewById(R.id.user_comment_comment_text);
            convertView.setTag(holder);
        }

        holder = (ViewHolder) convertView.getTag();
        if (mIsPraise) {
            holder.praiseTextView.setTextColor(ContextCompat.getColor(mContext, android.R.color.holo_red_dark));
        }else {
            holder.praiseTextView.setTextColor(ContextCompat.getColor(mContext,android.R.color.white));

        }
        holder.praiseTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsPraise) {
                    ToastUtil.showShortText(mContext,mContext.getString(R.string.you_have_praised));
                }else {
                    EventBus.getDefault().post(new PraiseEvent(mPosition));
                }
                mPopupWindow.dismiss();
            }
        });
        holder.commentTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new CommentEvent(mPosition));
                mPopupWindow.dismiss();
            }
        });
        return convertView;
    }

    private class ViewHolder{
        private TextView praiseTextView,commentTextView;
    }

}
