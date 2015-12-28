package com.nju.adatper;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.TextView;

import com.nju.activity.R;
import com.nju.model.FriendWeibo;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by xiaojuzhang on 2015/11/20.
 */
public class UserCommentItemListAdapter extends BaseAdapter {

    private static String TAG = UserCommentItemListAdapter.class.getSimpleName();
    public static int PRAISE_OK = 0;
    public static int COMMENT_OK = 1;
    private Context mContext;
    private int mPosition;
    private List<FriendWeibo> mWeibos;
    private Handler mHandler;
    private ListPopupWindow mPopupWindow;
    private ListView mListView;
    private Dictionary<Integer, Integer> listViewItemHeights = new Hashtable<>();
    public UserCommentItemListAdapter(Context context,int position,List<FriendWeibo> weibos,Handler handler,ListPopupWindow listPopupWindow,ListView listView) {
        mContext = context;
        mPosition = position;
        mWeibos = weibos;
        mHandler = handler;
        mPopupWindow = listPopupWindow;
        mListView = listView;
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
        convertView = LayoutInflater.from(mContext).inflate(R.layout.user_comment,parent,false);
        TextView praiseTextView = (TextView) convertView.findViewById(R.id.user_comment_praise_text);
        final TextView commentTextView = (TextView) convertView.findViewById(R.id.user_comment_comment_text);
        praiseTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = new Message();
                message.what = PRAISE_OK;
                mWeibos.get(mPosition).setPraiseUserName("张三");
                mHandler.sendMessage(message);
                mPopupWindow.dismiss();
            }
        });
        commentTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = new Message();
                message.what = COMMENT_OK;
                message.obj = mPosition;
                int length = mWeibos.size()-1;
                for(int i =length;i >mPosition;i--){
                    mWeibos.remove(i);
                }
                mHandler.sendMessage(message);
                mPopupWindow.dismiss();
            }
        });
        return convertView;
    }

    private int getScroll() {
        View c = mListView.getChildAt(0); //this is the first visible row
        int scrollY = -c.getTop();
        listViewItemHeights.put(mListView.getFirstVisiblePosition(), c.getHeight());
        for (int i = 0; i < mListView.getFirstVisiblePosition(); ++i) {
            if (listViewItemHeights.get(i) != null) // (this is a sanity check)
                scrollY += listViewItemHeights.get(i); //add all heights of the views that are gone
        }
        return scrollY;
    }

}
