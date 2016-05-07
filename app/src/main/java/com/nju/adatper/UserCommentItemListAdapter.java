package com.nju.adatper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListPopupWindow;
import android.widget.TextView;

import com.nju.activity.R;
import com.nju.event.CommentEvent;
import com.nju.event.PraiseEvent;

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
    private Dictionary<Integer, Integer> listViewItemHeights = new Hashtable<>();

    public UserCommentItemListAdapter(Context context, ListPopupWindow popupWindow, int position) {
        mContext = context;
        mPosition = position;
        mPopupWindow = popupWindow;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Object getItem(int position) {
        return "hello";
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.user_comment, parent, false);
        TextView praiseTextView = (TextView) convertView.findViewById(R.id.user_comment_praise_text);
        final TextView commentTextView = (TextView) convertView.findViewById(R.id.user_comment_comment_text);
        praiseTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new PraiseEvent(mPosition));
                mPopupWindow.dismiss();
            }
        });
        commentTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new CommentEvent(mPosition));
                mPopupWindow.dismiss();
            }
        });
        return convertView;
    }

}
