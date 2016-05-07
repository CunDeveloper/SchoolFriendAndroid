package com.nju.util;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.nju.View.SchoolFriendDialog;
import com.nju.activity.R;
import com.nju.fragment.BaseFragment;
import com.nju.fragment.DetailPersonInfo;
import com.nju.fragment.SingleChoosePicFragment;
import com.nju.http.ImageDownloader;
import com.nju.http.ResponseCallback;
import com.nju.http.response.ParseResponse;
import com.nju.model.AuthorImage;
import com.nju.service.AuthorService;

import java.io.IOException;

/**
 * Created by cun on 2016/4/5.
 */
public class ListViewHead {
    private static final String TAG = ListViewHead.class.getSimpleName();
    private ImageView mHeadImage;
    private ImageView mBgImage;
    private BaseFragment mFragment;
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
                    Object object = parseResponse.getInfo(responseBody, AuthorImage.class);
                    if (object != null) {
                        if (object instanceof AuthorImage) {
                            AuthorImage authorImage = (AuthorImage) object;
                            String headUrl = PathConstant.IMAGE_PATH_SMALL + PathConstant.HEAD_ICON_IMG + authorImage.getHeadIconUrl();
                            mFragment.getHostActivity().getSharedPreferences().edit()
                                    .putString(mFragment.getString(R.string.head_url), headUrl).commit();
                            mFragment.getHostActivity().getSharedPreferences().edit()
                                    .putString(mFragment.getString(R.string.head_url_filename), authorImage.getHeadIconUrl()).commit();
                            Log.i(TAG, headUrl);
                            ImageDownloader.with(mFragment.getContext()).download(headUrl, mHeadImage);
                            Bitmap bitmap = ImageDownloader.with(mFragment.getContext()).download(headUrl).bitmap();
                            BitmapUtil.saveToFile(mFragment, bitmap, authorImage.getHeadIconUrl());
                            String bgUrl = PathConstant.IMAGE_PATH + PathConstant.BG_IMAGE + authorImage.getBgUrl();
                            Log.i(TAG, bgUrl);
                            ImageDownloader.with(mFragment.getContext()).download(bgUrl, mBgImage);
                            Log.i(TAG, authorImage.getBgUrl() + "\n" + authorImage.getHeadIconUrl());
                            Log.i(TAG, "readBitmpa" + BitmapUtil.get(authorImage.getHeadIconUrl()));
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    public ListViewHead(BaseFragment fragment) {
        mFragment = fragment;
    }

    public void setUp(ListView listView) {
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
                SchoolFriendDialog.listItemDialog(mFragment.getContext(), strings, new SchoolFriendDialog.ListItemCallback() {
                    @Override
                    public void onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
                        SingleChoosePicFragment fragment = SingleChoosePicFragment.newInstance(mFragment.getString(R.string.update_bg));
                        mFragment.getHostActivity().open(fragment, fragment);
                    }
                }).show();
            }
        });
        listView.addHeaderView(head);
        AuthorService.queryAuthorImage(mFragment, callback);
    }
}
