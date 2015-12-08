package com.nju.activity;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.ImageView;

import com.nju.fragment.BaseFragment;

/**
 * Created by xiaojuzhang on 2015/12/8.
 */
public interface FragmentHostActivity {
    void open(BaseFragment fragment);
    void open(BaseFragment fragment,boolean clearBackStack);
    void open(BaseFragment fragment,boolean clearBackStack,Fragment fragmentToRemove);
    void open(BaseFragment fragment,Fragment fragmentToRemove);
    Toolbar getToolBar();
    Button getMenuBn();
    ImageView getMenuCameraView();
    ImageView getMenuDeleteView();
    BaseActivity.LocalStack<BaseFragment> getBackStack();
}
