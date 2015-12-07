package com.nju.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.nju.fragment.AlumniCircleFragment;
import com.nju.fragment.UserInfoFragement;
import com.nju.fragment.XueXinAuthFragmet;
import com.nju.model.UserInfo;
import com.nju.util.Divice;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements XueXinAuthFragmet.OpenFragmentListener {

    private static final String TAG = MainActivity.class.getSimpleName() ;
    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
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
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.container,XueXinAuthFragmet.newInstance(),XueXinAuthFragmet.TAG).addToBackStack(null).commit();
    }


    private void initNavigationViewListener () {
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                mDrawerLayout.closeDrawers();
                item.setChecked(true);
                Intent intent;
                switch (item.getItemId()) {
                    case R.id.nav_discussion:
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.container, AlumniCircleFragment.newInstance(), AlumniCircleFragment.TAG).commit();

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
        manager.beginTransaction()
                .replace(R.id.container, UserInfoFragement.newInstance()).addToBackStack(null).commit();
    }
}
