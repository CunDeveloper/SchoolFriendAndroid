package com.nju.activity;

import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nju.fragment.BaseFragment;
import com.vincentbrison.openlibraries.android.dualcache.lib.DualCache;

import java.util.ArrayList;

/**
 * Created by xiaojuzhang on 2015/12/8.
 */
public interface FragmentHostActivity {
    void open(BaseFragment fragment);
    void open(BaseFragment fragment,boolean clearBackStack);
    void open(BaseFragment fragment,boolean clearBackStack,Fragment fragmentToRemove);
    void open(BaseFragment fragment,Fragment fragmentToRemove);
    void hideAllMenuView();
    ApplicationHandler getAppHandler();
    SharedPreferences getSharedPreferences();
    Toolbar getToolBar();
    Button getMenuBn();
    ImageView getMenuCameraView();
    TextView getMenuDeleteView();
    LinearLayout geLinearLayout();
    View getRecommendLayout();
    void display(int i);
    BaseActivity.LocalStack<BaseFragment> getBackStack();
    boolean isPhone();
    String token();
    int userId();
}
