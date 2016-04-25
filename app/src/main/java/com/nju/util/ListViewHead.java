package com.nju.util;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.nju.View.SchoolFriendDialog;
import com.nju.activity.R;
import com.nju.fragment.BaseFragment;
import com.nju.fragment.DetailPersonInfo;

/**
 * Created by cun on 2016/4/5.
 */
public class ListViewHead {

    public static void setUp(final BaseFragment fragment,View view,ListView listView){
        LinearLayout head = (LinearLayout) LayoutInflater.from(fragment.getContext()).inflate(R.layout.listview_header, listView, false);
        ImageView mHeadImgIcon = (ImageView) head.findViewById(R.id.head_icon_img);
        mHeadImgIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.getHostActivity().open(DetailPersonInfo.newInstance());
            }
        });
        ImageView bgImageView = (ImageView) head.findViewById(R.id.bgImageView);
        bgImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SchoolFriendDialog.changeBgDialog(fragment.getContext(),fragment.getString(R.string.changePhotoPage))
                .show();
                //fragment.getHostActivity().open(MultiChoosePicFragment.newInstance(""));
            }
        });
        listView.addHeaderView(head);
    }
}
