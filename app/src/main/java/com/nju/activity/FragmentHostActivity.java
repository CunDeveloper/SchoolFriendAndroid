package com.nju.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.ImageView;

import com.nju.fragment.BaseFragment;

/**
 * Created by xiaojuzhang on 2015/12/8.
 */
public interface FragmentHostActivity {
    void open(BaseFragment fragment,Class mClass);
    Toolbar getToolBar();
    Button getMenuBn();
    ImageView getMenuCameraView();
    ImageView getMenuDeleteView();
}
