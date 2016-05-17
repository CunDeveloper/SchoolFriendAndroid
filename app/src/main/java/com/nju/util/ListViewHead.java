package com.nju.util;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.nju.View.SchoolFriendDialog;
import com.nju.activity.BaseActivity;
import com.nju.activity.R;
import com.nju.event.MessageAuthorImageEvent;
import com.nju.fragment.BaseFragment;
import com.nju.fragment.DetailPersonInfoFragment;
import com.nju.fragment.SingleChoosePicFragment;
import com.nju.http.ImageDownloader;
import com.nju.http.ResponseCallback;
import com.nju.http.response.ParseResponse;
import com.nju.model.Author;
import com.nju.model.AuthorImage;
import com.nju.model.AuthorInfo;
import com.nju.service.AuthorService;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

/**
 * Created by cun on 2016/4/5.
 */
public class ListViewHead {
    private static final String TAG = ListViewHead.class.getSimpleName();
    private BaseActivity mFragment;
    private ResponseCallback callback = new ResponseCallback() {
        @Override
        public void onFail(Exception error) {
            Log.e(TAG, error.getMessage());
        }

        @Override
        public void onSuccess(String responseBody) {
            Log.i(TAG, responseBody);
                ParseResponse parseResponse = new ParseResponse();
                try {
                    Object object = parseResponse.getInfo(responseBody, AuthorImage.class);
                    if (object != null) {
                        if (object instanceof AuthorImage) {
                            AuthorImage authorImage = (AuthorImage) object;
                            mFragment.getSharedPreferences().edit()
                                    .putString(mFragment.getString(R.string.head_url), authorImage.getHeadIconUrl()).commit();
                            mFragment.getSharedPreferences().edit().
                                    putString(mFragment.getString(R.string.bg_url), authorImage.getBgUrl()).commit();
                            EventBus.getDefault().post(new MessageAuthorImageEvent(Constant.OK));
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

    };

    public ListViewHead(BaseActivity fragment) {
        mFragment = fragment;
    }

    public void setUp(ListView listView) {
        LinearLayout head = (LinearLayout) LayoutInflater.from(mFragment).inflate(R.layout.listview_header, listView, false);
        listView.addHeaderView(head);
        String bgUrl = mFragment.getSharedPreferences()
                .getString(mFragment.getString(R.string.bg_url),"");
        if (bgUrl.equals("")){
            AuthorService.queryAuthorImage(mFragment, callback);
        }
    }

    public static void initView(View view, final AuthorInfo authorInfo, final BaseFragment fragment) {
        final int userId = fragment.getHostActivity().userId();
        ImageView mBgImage = (ImageView) view.findViewById(R.id.bgImageView);
        ImageView mHeadImage = (ImageView) view.findViewById(R.id.head_icon_img);
        mHeadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.getHostActivity().open(DetailPersonInfoFragment.newInstance(authorInfo));
            }
        });
        mBgImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userId == authorInfo.getAuthorId()) {
                    String[] strings = {fragment.getString(R.string.changePhotoPage)};
                    final String updateBg = fragment.getString(R.string.update_bg);
                    SchoolFriendDialog.listItemDialog(fragment.getContext(), strings, new SchoolFriendDialog.ListItemCallback() {
                        @Override
                        public void onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
                            SingleChoosePicFragment singleChooseFragment = SingleChoosePicFragment.newInstance(updateBg);
                            fragment.getHostActivity().open(singleChooseFragment, singleChooseFragment);
                        }
                    }).show();
                }
            }
        });
        TextView userNameTV = (TextView) view.findViewById(R.id.userNameTV);
        String bgFileName = authorInfo.getBgUrl();
        if (bgFileName != null && ! bgFileName.equals("")) {
            String bgUrl = PathConstant.IMAGE_PATH + PathConstant.BG_IMAGE + bgFileName;
            String headUrl = PathConstant.IMAGE_PATH_SMALL + PathConstant.HEAD_ICON_IMG + authorInfo.getHeadUrl();
            ImageDownloader.with(view.getContext()).download(headUrl,mHeadImage);
            ImageDownloader.with(view.getContext()).download(bgUrl,mBgImage);
            userNameTV.setText(authorInfo.getAuthorName());
        }
    }

    public static void initView(View view, final BaseFragment fragment) {
        final AuthorInfo authorInfo = new AuthorInfo();
        final int userId = fragment.getHostActivity().userId();
        authorInfo.setAuthorId(fragment.getHostActivity().userId());
        String username = fragment.getHostActivity().getSharedPreferences().getString(fragment.getString(R.string.username),"");
        authorInfo.setAuthorName(username);
        String headUrl = fragment.getHostActivity().getSharedPreferences().getString(fragment.getString(R.string.head_url),"");
        authorInfo.setHeadUrl(headUrl);
        String bgUrl = fragment.getHostActivity().getSharedPreferences().getString(fragment.getString(R.string.bg_url), "");
        authorInfo.setBgUrl(bgUrl);
        ImageView mBgImage = (ImageView) view.findViewById(R.id.bgImageView);
        ImageView mHeadImage = (ImageView) view.findViewById(R.id.head_icon_img);
        mHeadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.getHostActivity().open(DetailPersonInfoFragment.newInstance(authorInfo));
            }
        });
        mBgImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userId == authorInfo.getAuthorId()) {
                    String[] strings = {fragment.getString(R.string.changePhotoPage)};
                    final String updateBg = fragment.getString(R.string.update_bg);
                    SchoolFriendDialog.listItemDialog(fragment.getContext(), strings, new SchoolFriendDialog.ListItemCallback() {
                        @Override
                        public void onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
                            SingleChoosePicFragment singleChooseFragment = SingleChoosePicFragment.newInstance(updateBg);
                            fragment.getHostActivity().open(singleChooseFragment, singleChooseFragment);
                        }
                    }).show();
                }
            }
        });

        TextView userNameTV = (TextView) view.findViewById(R.id.userNameTV);
        String bgFileName = authorInfo.getBgUrl();
        if (bgFileName != null && ! bgFileName.equals("")) {
            String aBgUrl = PathConstant.IMAGE_PATH + PathConstant.BG_IMAGE + bgFileName;
            String aHeadUrl = PathConstant.IMAGE_PATH_SMALL + PathConstant.HEAD_ICON_IMG + authorInfo.getHeadUrl();
            ImageDownloader.with(view.getContext()).download(aHeadUrl,mHeadImage);
            ImageDownloader.with(view.getContext()).download(aBgUrl,mBgImage);
            userNameTV.setText(authorInfo.getAuthorName());
        }
    }

    public void queryAuthorImage(){
        AuthorService.queryAuthorImage(mFragment, callback);
    }
}
