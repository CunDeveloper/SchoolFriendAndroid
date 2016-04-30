package com.nju.util;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.nju.View.SchoolFriendDialog;
import com.nju.activity.R;
import com.nju.fragment.BaseFragment;
import com.nju.fragment.DetailPersonInfo;
import com.nju.http.ImageDownloader;
import com.nju.http.ResponseCallback;
import com.nju.http.response.ParseResponse;
import com.nju.model.AlumniTalk;
import com.nju.model.AuthorImage;
import com.nju.model.ContentComment;
import com.nju.service.AuthorService;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by cun on 2016/4/5.
 */
public class ListViewHead {
    private static final String TAG = ListViewHead.class.getSimpleName();
    private ImageView mHeadImage;
    private ImageView mBgImage;
    private BaseFragment mFragment;

    public ListViewHead(BaseFragment fragment){
        mFragment = fragment;
    }

    private ResponseCallback callback = new ResponseCallback() {
        @Override
        public void onFail(Exception error) {
            Log.e(TAG, error.getMessage());
        }

        @Override
        public void onSuccess(String responseBody) {
            Log.i(TAG, responseBody);
            if (FragmentUtil.isAttachedToActivity(mFragment)) {
                ParseResponse parseResponse = new ParseResponse();
                try {
                    Object object = parseResponse.getInfo(responseBody,AuthorImage.class);
                    if (object != null) {
                        if (object instanceof AuthorImage){
                            AuthorImage authorImage = (AuthorImage) object;
                            String headUrl = PathConstant.IMAGE_PATH_SMALL + PathConstant.HEAD_ICON_IMG + authorImage.getHeadIconUrl();
                            Log.i(TAG,headUrl);
                            ImageDownloader.with(mFragment.getContext()).download(headUrl, mHeadImage);

                            String bgUrl = PathConstant.IMAGE_PATH + PathConstant.BG_IMAGE + authorImage.getBgUrl();
                            Log.i(TAG,bgUrl);
                            ImageDownloader.with(mFragment.getContext()).download(bgUrl,mBgImage);
                            Log.i(TAG,authorImage.getBgUrl()+"\n"+authorImage.getHeadIconUrl());
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    public  void setUp(ListView listView){
        LinearLayout head = (LinearLayout) LayoutInflater.from(mFragment.getContext()).inflate(R.layout.listview_header, listView, false);
        mHeadImage = (ImageView) head.findViewById(R.id.head_icon_img);
        mHeadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFragment.getHostActivity().open(DetailPersonInfo.newInstance());
            }
        });
        mBgImage = (ImageView) head.findViewById(R.id.bgImageView);
        mBgImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] strings = {mFragment.getString(R.string.changePhotoPage)};
                SchoolFriendDialog.listItemDialog(mFragment.getContext(),strings).show();
            }
        });
        listView.addHeaderView(head);
        AuthorService.queryAuthorImage(mFragment,callback);
    }
}
