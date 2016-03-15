package com.nju.activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;

import android.support.v4.app.Fragment;
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

import com.nju.View.SchoolFriendDialog;
import com.nju.db.SchoolFriendDbHelper;
import com.nju.fragment.AlumniCircleFragment;
import com.nju.fragment.BaseFragment;
import com.nju.fragment.CircleImageViewFragment;
import com.nju.fragment.PublishTextWithPicsFragment;
import com.nju.fragment.RecommendWorkFragment;
import com.nju.fragment.SeniorsVoicesFragment;
import com.nju.fragment.TuCaoFragment;
import com.nju.fragment.WebViewFragment;
import com.nju.fragment.XueXinAuthFragment;
import com.nju.test.TestData;
import com.nju.util.Divice;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;


public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName() ;
    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolBar;
    private Button mMenuBn;
    private ImageView mMenuCameraView;
    private TextView mMenuDeleteView;
    private LinearLayout mNoActionBarLinearLayout,mNoActionBarRecommendWorkLinearLayout;
    private static boolean isPhone;
    private static final String FINAL_TAG = "final_tag";
    private ArrayList<View> actionBarViews = new ArrayList<>() ;
    int fragmentIndex = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mMenuBn = (Button) findViewById(R.id.main_viewpager_menu_bn);
        mMenuDeleteView = (TextView) findViewById(R.id.main_viewpager_menu_delete_img);
        mMenuCameraView = (ImageView) findViewById(R.id.main_viewpager_camera_imageView);
        mNoActionBarLinearLayout = (LinearLayout) findViewById(R.id.main_viewpager_no_action_bar_layout);
        mNoActionBarRecommendWorkLinearLayout = (LinearLayout) findViewById(R.id.main_viewpager_no_action_recommend_work_layout);
        actionBarViews.add(mMenuBn);actionBarViews.add(mMenuDeleteView);actionBarViews.add(mMenuCameraView);
        actionBarViews.add(mNoActionBarLinearLayout);actionBarViews.add(mNoActionBarRecommendWorkLinearLayout);
        CoordinatorLayout mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_Viewpager_content);
        if (Build.VERSION.SDK_INT>19) {
            mCoordinatorLayout.setPadding(mCoordinatorLayout.getPaddingLeft(), Divice.getStatusBarHeight(this)
                    , mCoordinatorLayout.getPaddingRight(), mCoordinatorLayout.getBottom());
        }
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        isPhone = getResources().getBoolean(R.bool.large_layout);
        String username = getSharedPreferences(getString(R.string.shared_file_name), Context.MODE_PRIVATE).
                getString(getString(R.string.username), getString(R.string.visitor));
        View headerView = mNavigationView.inflateHeaderView(R.layout.nav_header);
        TextView textView = (TextView) headerView.findViewById(R.id.nav_header_username);
        textView.setText(username);
        initNavigationViewListener();

        XueXinAuthFragment fragment = XueXinAuthFragment.newInstance();
        open(fragment,true,fragment);
        initDataBase();
    }

    @Override
    public void onStart() {
        super.onStart();
        initFinalValue();
//        if (getSharedPreferences().getBoolean(FINAL_TAG,false)) {
//
//            getSharedPreferences().edit().putBoolean(FINAL_TAG,true).commit();
//        }
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
                    case R.id.nav_recrict:
                        open(RecommendWorkFragment.newInstance());
                        break;
                }
                return false;
            }
        });
    }


    private void initFinalValue() {
        final int deviceWidth = Divice.getDisplayWidth(this);
        final int deviceHeight = Divice.getDisplayHeight(this);
        final int visibleDeviceHeight = deviceHeight -(int)( Divice.convertDpToPixel(mToolBar.getHeight(),this)
                        +Divice.getStatusBarHeight(this));
        getSharedPreferences().edit().putInt(getString(R.string.diviceWidth), deviceWidth).commit();
        getSharedPreferences().edit().putInt(getString(R.string.diviceHeight), deviceHeight).commit();
        getSharedPreferences().edit().putInt(getString(R.string.visiDiviceHeight), visibleDeviceHeight).commit();
        //for only test
        Set<String> levels = new HashSet<>();levels.add("本科");levels.add("所有");levels.add("硕士");
        getSharedPreferences().edit().putStringSet(getString(R.string.level),levels).commit();
        getSharedPreferences().edit().putStringSet(getString(R.string.undergraduateCollege), TestData.getUndergraduateCollege()).commit();

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
        mLocalBackStack.push(fragment);
        ft.commitAllowingStateLoss();
        fragmentIndex++;
        if (fragmentToRemove != null) {
            mLocalBackStack.remove(fragmentToRemove);
        }
    }

    @Override
    public void open(BaseFragment fragment, Fragment fragmentToRemove) {
        open(fragment, false, fragmentToRemove);
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
        open(fragment, clearBackStack,null);
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
    public TextView getMenuDeleteView() {
        return mMenuDeleteView;
    }

    @Override
    public LinearLayout geLinearLayout() {
        return mNoActionBarLinearLayout;
    }

    @Override
    public LinearLayout getRecommendLayout() {
        return (LinearLayout)findViewById(R.id.main_viewpager_no_action_recommend_work_layout);
    }

    @Override
    public void display(int target) {
        for (int i=0;i<actionBarViews.size();i++){
            if(target == i){
                actionBarViews.get(i).setVisibility(View.VISIBLE);
            }else {
                actionBarViews.get(i).setVisibility(View.GONE);
            }
        }
    }

    @Override
    public boolean isPhone() {
        return isPhone;
    }

    @Override
    public void onBackPressed() {
        if (mLocalBackStack.isEmpty() || (mLocalBackStack.size() == 1)) {
            finish();
            return;
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (mLocalBackStack.size() >=2) {
            BaseFragment fragment = mLocalBackStack.pop();
            if (fragment.getClass().getSimpleName() .equals(PublishTextWithPicsFragment.TAG)) {
                SchoolFriendDialog dialog = SchoolFriendDialog.exitReminderDialog(this,getString(R.string.are_you_sure_exit_this_eidt));
                dialog.show();
            }
            else {
                ft.replace(R.id.container,mLocalBackStack.peek());
                ft.commit();
            }
            }
        }


    private void clearBackStack() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        while (mLocalBackStack.size() > 1) {
            BaseFragment removeThis = mLocalBackStack.pop();
            ft.remove(removeThis).commit();
            Log.d(TAG, "Removing " + removeThis.getTag() + " (" + removeThis.getClass().getSimpleName() + ")");
        }
        ft.commit();
    }
    private void initDataBase(){
        SchoolFriendDbHelper.newInstance(this);
    }
}
