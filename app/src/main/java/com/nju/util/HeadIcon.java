package com.nju.util;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.nju.event.PersonInfoEvent;
import com.nju.http.ImageDownloader;
import com.nju.model.AuthorInfo;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by cun on 2016/4/5.
 */
public class HeadIcon {

    private static final String TAG = HeadIcon.class.getSimpleName();

    public static void setUp(ImageView imageView, final AuthorInfo authorInfo) {
        String url = PathConstant.IMAGE_PATH_SMALL + PathConstant.HEAD_ICON_IMG + authorInfo.getHeadUrl();
        Log.i(TAG, url);
        ImageDownloader.with(imageView.getContext()).download(url, imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new PersonInfoEvent(authorInfo));
            }
        });
    }
}
