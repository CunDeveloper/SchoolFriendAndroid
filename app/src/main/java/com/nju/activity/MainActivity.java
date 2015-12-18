package com.nju.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nju.db.SchoolFriendDbHelper;
import com.nju.fragment.AlumniCircleFragment;
import com.nju.fragment.BaseFragment;
import com.nju.fragment.CircleImageViewFragment;
import com.nju.fragment.EmotionPagerFragment;
import com.nju.fragment.PublishTextFragment;
import com.nju.fragment.PublishTextWithPicsFragment;
import com.nju.fragment.SeniorsVoicesFragment;
import com.nju.fragment.TuCaoFragment;
import com.nju.fragment.UserInfoFragement;
import com.nju.fragment.XueXinAuthFragmet;
import com.nju.model.UserInfo;
import com.nju.util.Divice;

import java.util.ArrayList;


public class MainActivity extends BaseActivity implements XueXinAuthFragmet.OpenFragmentListener,EmotionPagerFragment.OnFragmentInputEmotionListener {

    private static final String TAG = MainActivity.class.getSimpleName() ;
    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolBar;
    private Button mMenuBn;
    private ImageView mMenuCameraView;
    private ImageView mMenuDeleteView;
    private LinearLayout mNoActionBarLinearLayout;
    private static final String FINAL_TAG = "final_tag";
    int fragmentIndex = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mMenuBn = (Button) findViewById(R.id.main_viewpager_menu_bn);
        mMenuDeleteView = (ImageView) findViewById(R.id.main_viewpager_menu_delete_img);
        mMenuCameraView = (ImageView) findViewById(R.id.main_viewpager_camera_imageView);
        mNoActionBarLinearLayout = (LinearLayout) findViewById(R.id.main_viewpager_no_action_bar_layout);
        CoordinatorLayout mCoorDinatorLayout = (CoordinatorLayout) findViewById(R.id.main_Viewpager_content);
        if (Build.VERSION.SDK_INT>19) {
            mCoorDinatorLayout.setPadding(mCoorDinatorLayout.getPaddingLeft(), Divice.getStatusBarHeight(this)
                    , mCoorDinatorLayout.getPaddingRight(), mCoorDinatorLayout.getBottom());
        }
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        String username = getSharedPreferences(getString(R.string.shared_file_name), Context.MODE_PRIVATE).
                getString(getString(R.string.username), getString(R.string.visitor));
        View headerView = mNavigationView.inflateHeaderView(R.layout.nav_header);
        TextView textView = (TextView) headerView.findViewById(R.id.nav_header_username);
        textView.setText(username);
        initNavigationViewListener();
        open(XueXinAuthFragmet.newInstance());
        initDataBase();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getSharedPreferences().getBoolean(FINAL_TAG,false)) {
            initFinalValue();
            getSharedPreferences().edit().putBoolean(FINAL_TAG,true).commit();
        }
    }

    private void initNavigationViewListener () {
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                mDrawerLayout.closeDrawers();
                item.setChecked(true);
                switch (item.getItemId()) {
                    case R.id.nav_alumni_circle:
                        open(AlumniCircleFragment.newInstance());
                        break;
                    case R.id.nav_tucao:
                        open(TuCaoFragment.newInstance());
                        break;
                    case R.id.nav_heat_voice:
                        open(SeniorsVoicesFragment.newInstance());
                        break;

                }
                return false;
            }
        });
    }


    private void initFinalValue() {
        final int diviceWidth = Divice.getDisplayWidth(this);
        final int diviceHeight = Divice.getDisplayHeight(this);
        final int visiDiviceHeight = diviceHeight -(int)( Divice.convertDpToPixel(mToolBar.getHeight(),this)
                        +Divice.getStatusBarHeight(this));
        getSharedPreferences().edit().putInt(getString(R.string.diviceWidth),diviceWidth).commit();
        getSharedPreferences().edit().putInt(getString(R.string.diviceHeight), diviceHeight);
        getSharedPreferences().edit().putInt(getString(R.string.visiDiviceHeight),visiDiviceHeight);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void openFragment(ArrayList<UserInfo> lists) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("VALUE", lists);
        UserInfoFragement fragement = UserInfoFragement.newInstance();
        fragement.setArguments(bundle);
        FragmentManager manager = getSupportFragmentManager();
        XueXinAuthFragmet xueXinAuthFragmet= (XueXinAuthFragmet) manager.findFragmentByTag(XueXinAuthFragmet.TAG);
        if (xueXinAuthFragmet != null) {
            manager.beginTransaction().remove(xueXinAuthFragmet).commit();
        }
        open(UserInfoFragement.newInstance());
    }

    @Override
    public void open(BaseFragment fragment) {
        open(fragment,false,null);
    }

    @Override
    public void open(BaseFragment fragment, boolean clearBackStack, Fragment fragmentToRemove) {

        if (fragment.getClass().getSimpleName().equals(CircleImageViewFragment.TAG)){
            Divice.hideStatusBar(this);
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (clearBackStack) {
            clearBackStack();
        }
        ft.replace(R.id.container, fragment);
        mLocalBackstack.push(fragment);
        ft.commitAllowingStateLoss();
        fragmentIndex++;
        if (fragmentToRemove != null) {
            mLocalBackstack.remove(fragmentToRemove);
        }

    }

    @Override
    public void open(BaseFragment fragment, Fragment fragmentToRemove) {
        open(fragment,false,fragmentToRemove);
    }

    @Override
    public void hideAllMenuView() {
        mMenuBn.setVisibility(View.GONE);
        mMenuCameraView.setVisibility(View.GONE);
        mMenuDeleteView.setVisibility(View.GONE);
        mNoActionBarLinearLayout.setVisibility(View.GONE);
    }

    @Override
    public void open(BaseFragment fragment, boolean clearBackStack) {
        open(fragment, clearBackStack);
    }


    @Override
    public Toolbar getToolBar() {
        return mToolBar;
    }

    @Override
    public Button getMenuBn() {
        return mMenuBn;
    }

    @Override
    public ImageView getMenuCameraView() {
        return mMenuCameraView;
    }

    @Override
    public ImageView getMenuDeleteView() {
        return mMenuDeleteView;
    }

    @Override
    public LinearLayout geLinearLayout() {
        return mNoActionBarLinearLayout;
    }

    @Override
    public void onBackPressed() {
        if (mLocalBackstack.isEmpty() || (mLocalBackstack.size() == 1)) {
            finish();
            return;
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (mLocalBackstack.size() >=2) {
            mLocalBackstack.pop();
        }
        ft.replace(R.id.container,mLocalBackstack.peek());
        ft.commit();
    }

    private void clearBackStack() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        while (mLocalBackstack.size() > 1) {
            BaseFragment removeThis = mLocalBackstack.pop();
            ft.remove(removeThis);
            Log.d(TAG, "Removing " + removeThis.getTag() + " (" + removeThis.getClass().getSimpleName() + ")");
        }
        ft.commit();
    }

    @Override
    public void onFragmentInputEmotion(Drawable drawable) {
        PublishTextWithPicsFragment fragment = (PublishTextWithPicsFragment) mLocalBackstack.peek();
        if (fragment != null){
            fragment.inputEmotion(drawable);
        }
    }

    @Override
    public void onTextFragmentInputEmotion(String text) {
        PublishTextFragment fragment = (PublishTextFragment) mLocalBackstack.peek();
        if (fragment != null){
            fragment.inputEmotion(text);
        }
    }

    private void initDataBase(){
        SchoolFriendDbHelper.newInstance(this);
    }
}
