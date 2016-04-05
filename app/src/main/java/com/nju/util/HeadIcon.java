package com.nju.util;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.nju.activity.PersonInfoEvent;
import com.nju.model.AuthorInfo;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by cun on 2016/4/5.
 */
public class HeadIcon {

    public static void setUp(ImageView imageView, final AuthorInfo authorInfo){
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new PersonInfoEvent(authorInfo));
            }
        });
    }
}
