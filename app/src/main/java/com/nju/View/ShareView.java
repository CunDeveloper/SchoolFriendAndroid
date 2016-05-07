package com.nju.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nju.activity.R;
import com.nju.event.MessageShareEventId;
import com.nju.fragment.BaseFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * Created by cun on 2016/4/27.
 */
public class ShareView {
    public static void init(final BaseFragment fragment, View view) {
        final RelativeLayout mainLayout = (RelativeLayout) view.findViewById(R.id.shareLayout);
        mainLayout.setVisibility(View.VISIBLE);
        GridView gridView = (GridView) view.findViewById(R.id.mGridView);
        gridView.setAdapter(new ShareAdapter(view.getContext()));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Bitmap bitmap = BitmapFactory.decodeResource(fragment.getResources(), R.drawable.head1);
//                //WeiXinShare.pic(bitmap, SendMessageToWX.Req.WXSceneSession,fragment.getHostActivity().wxApi());
//                String url = "http://115.159.186.158:8080/school-friend-service-webapp/SchoolFriendHtml/html/recommedShare.html";
//                //String url = "file:///android_asset/SchoolFriendHtml/html/recommedShare.html";
//                String title = "上海SAP云计算招聘";
//                String description = "要求会用java";
//                if (position == 0) {
//                    WeiXinShare.webPage(url, title, description, bitmap, SendMessageToWX.Req.WXSceneSession, fragment.getHostActivity().wxApi());
//                } else if (position == 1) {
//                    WeiXinShare.webPage(url, title, description, bitmap, SendMessageToWX.Req.WXSceneTimeline, fragment.getHostActivity().wxApi());
//                }

                EventBus.getDefault().post(new MessageShareEventId(position));
            }
        });
        View topTrans = view.findViewById(R.id.top_transparent);
        View bottomTrans = view.findViewById(R.id.bottom_transparent);
        topTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainLayout.setVisibility(View.GONE);
            }
        });
        bottomTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainLayout.setVisibility(View.GONE);
            }
        });
    }

    private static class ShareAdapter extends BaseAdapter {
        private ArrayList<Bitmap> mBitmaps = new ArrayList<>();
        private Context mContent;

        public ShareAdapter(Context context) {
            mContent = context;
            mBitmaps.add(BitmapFactory.decodeResource(mContent.getResources(), R.drawable.wechat_f));
            mBitmaps.add(BitmapFactory.decodeResource(mContent.getResources(), R.drawable.wechat_moment_f));
        }

        @Override
        public int getCount() {
            return mBitmaps.size();
        }

        @Override
        public Object getItem(int position) {
            return mBitmaps.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(mContent).inflate(R.layout.share_item, parent, false);
                holder.imageView = (ImageView) convertView;
                convertView.setTag(holder);
            }
            holder = (ViewHolder) convertView.getTag();
            holder.imageView.setImageBitmap(mBitmaps.get(position));
            return convertView;
        }

        private class ViewHolder {
            public ImageView imageView;
        }
    }
}
