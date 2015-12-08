package com.nju.activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nju.fragment.AlumniCircleFragment;
import com.nju.fragment.BaseFragment;
import com.nju.fragment.UserInfoFragement;
import com.nju.fragment.XueXinAuthFragmet;
import com.nju.model.UserInfo;
import com.nju.util.Divice;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements XueXinAuthFragmet.OpenFragmentListener,FragmentHostActivity {

    private static final String TAG = MainActivity.class.getSimpleName() ;
    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolBar;
    private Button mMenuBn;
    private ImageView mMenuCameraView;
    private ImageView mMenuDeleteView;
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
        open(XueXinAuthFragmet.newInstance(),XueXinAuthFragmet.class);
    }


    private void initNavigationViewListener () {
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                mDrawerLayout.closeDrawers();
                item.setChecked(true);
                switch (item.getItemId()) {
                    case R.id.nav_discussion:
                        open(AlumniCircleFragment.newInstance(),AlumniCircleFragment.class);

                }
                return false;
            }
        });
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
        open(UserInfoFragement.newInstance(),UserInfoFragement.class);
    }

    @Override
    public void open(BaseFragment fragment,Class mClass) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Log.e(TAG,mClass.getSimpleName());
        ft.replace(R.id.container,fragment).addToBackStack(mClass.getSimpleName());
        ft.commit();
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
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
