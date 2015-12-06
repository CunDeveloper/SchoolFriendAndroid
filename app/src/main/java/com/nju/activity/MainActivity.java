package com.nju.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nju.fragment.UserInfoFragement;
import com.nju.fragment.XueXinAuthFragmet;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import model.UserInfo;


public class MainActivity extends AppCompatActivity implements XueXinAuthFragmet.OpenFragmentListener {

    private static final int INTENT_REQUEST_GET_IMAGES = 1;
    private static final String TAG = MainActivity.class.getSimpleName() ;
    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (Build.VERSION.SDK_INT>19) {
            mDrawerLayout.setPadding(mDrawerLayout.getPaddingLeft(), getStatusBarHeight()
                    , mDrawerLayout.getPaddingRight(), mDrawerLayout.getBottom());
            AppBarLayout.LayoutParams params = new AppBarLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, getStatusBarHeight(), 0, 0);
            toolbar.setLayoutParams(params);
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
        initOnGlobalListener();
    }

    private void initOnGlobalListener(){
        mDrawerLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int rootHeight = mDrawerLayout.getRootView().getHeight();
                int subHeight = mDrawerLayout.getHeight();
                Log.e(TAG,"ROOTHEIGHT="+rootHeight+"=="+"subheight="+subHeight+"subWidth="+ mDrawerLayout.getRootView().getWidth());
            }
        });}

    private void initNavigationViewListener () {
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                mDrawerLayout.closeDrawers();
                item.setChecked(true);
                Intent intent = null;
                switch (item.getItemId()) {
                    case R.id.nav_messages:
                        intent = new Intent(MainActivity.this, SchoolFriendActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_home:
                        intent = new Intent(MainActivity.this,StartActivity.class);
                        startActivity(intent);
                        break;
                }
                return false;
            }
        });
    }

    private void setEmotion() throws IOException {
        InputStream inputStream = getAssets().open("d_1_miao.png");
        Drawable drawable = Drawable.createFromStream(inputStream, null);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        EditText editText1 = (EditText) findViewById(R.id.etUsername);
        int selectionCursor = editText1.getSelectionStart();
        editText1.getText().insert(selectionCursor, ".");
        selectionCursor = editText1.getSelectionStart();
        SpannableStringBuilder builder = new SpannableStringBuilder(editText1.getText());
        builder.setSpan(new ImageSpan(drawable), selectionCursor - ".".length(), selectionCursor, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        editText1.setText(builder);
        editText1.setSelection(selectionCursor);
    }

    // A method to find height of the status bar
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height","dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

//    public void capture(View view) {
//        Picasso.with(this).load("http://www.dbs.com.sg/iwov-resources/images/offers/cards/TRA00001-620x216.jpg").
//                placeholder(R.drawable.ic_dashboard)
//                .into((ImageView) findViewById(R.id.imageView2));
//        try {
//            setEmotion();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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

    private void setupFloatingLabelErrror() {
        final TextInputLayout floatingUsernameLabel = (TextInputLayout) findViewById(R.id.username_text_input_layout);
        floatingUsernameLabel.getEditText().addTextChangedListener(new TextWatcher() {
            // ...
            @Override
            public void onTextChanged(CharSequence text, int start, int count, int after) {
                if (text.length() > 0 && text.length() <= 4) {
                    floatingUsernameLabel.setError("用户名不符合勇气");
                    floatingUsernameLabel.setErrorEnabled(true);
                } else {
                    floatingUsernameLabel.setErrorEnabled(false);
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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
