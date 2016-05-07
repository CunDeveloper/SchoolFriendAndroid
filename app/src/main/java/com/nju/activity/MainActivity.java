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

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.nju.View.RoundedTransformation;
import com.nju.View.SchoolFriendDialog;
import com.nju.db.SchoolFriendDbHelper;
import com.nju.event.MessageEventMore;
import com.nju.event.NetworkStateChanged;
import com.nju.fragment.AlumniDynamicFragment;
import com.nju.fragment.AlumniVoiceFragment;
import com.nju.fragment.AskCollectFragment;
import com.nju.fragment.AskPublishFragment;
import com.nju.fragment.BaseFragment;
import com.nju.fragment.CircleImageViewFragment;
import com.nju.fragment.DynamicCollectFragment;
import com.nju.fragment.MajorAskFragment;
import com.nju.fragment.MyCircleFragment;
import com.nju.fragment.PublishDynamicFragment;
import com.nju.fragment.PublishVoiceFragment;
import com.nju.fragment.RecommendCollectFragment;
import com.nju.fragment.RecommendPublishFragment;
import com.nju.fragment.RecommendWorkFragment;
import com.nju.fragment.SettingFragment;
import com.nju.fragment.VoiceCollectFragment;
import com.nju.fragment.XueXinAuthFragment;
import com.nju.util.Constant;
import com.nju.util.Divice;
import com.nju.util.LoadData;
import com.nju.util.SchoolFriendGson;
import com.nju.util.ToastUtil;
import com.splunk.mint.Mint;
import com.squareup.picasso.Picasso;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class MainActivity extends BaseActivity {
    private static final String WX_APPID = "wx0e7d0e1f21f36288";
    private static final String WX_APP_SECRET = "0fb14de9a1832a7741f2caee321e89a1";
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String FINAL_TAG = "final_tag";
    private static final SchoolFriendGson gson = SchoolFriendGson.newInstance();
    private static boolean isPhone;
    private static String token = null;
    int fragmentIndex = 0;
    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolBar;
    private Button mMenuBn;
    private ImageView mMenuCameraView, mNavHeadImg;
    private TextView mMenuDeleteView, mNavNameTV;
    private LinearLayout mNoActionBarLinearLayout, mNoActionBarRecommendWorkLinearLayout;
    private ArrayList<View> actionBarViews = new ArrayList<>();
    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mint.setApplicationEnvironment(Mint.appEnvironmentStaging);
        Mint.initAndStartSession(MainActivity.this, "378226b0");
        api = WXAPIFactory.createWXAPI(this, WX_APPID, true);
        api.registerApp(WX_APPID);
        setContentView(R.layout.activity_main);
        initView();
        initNavigationViewListener();
        initDataBase();
        int isAuthorization = getSharedPreferences().getInt(getString(R.string.is_authorization), 0);
        if (token().equals("")) {
            XueXinAuthFragment fragment = XueXinAuthFragment.newInstance(getString(R.string.noToken));
            open(fragment, true, fragment);
        } else if (isAuthorization == 0) {
            XueXinAuthFragment fragment = XueXinAuthFragment.newInstance(getString(R.string.hasToken));
            open(fragment, true, fragment);
        } else if (isAuthorization == 1) {
            open(AlumniDynamicFragment.newInstance());
        }
    }

    private void initView() {
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mMenuBn = (Button) findViewById(R.id.main_viewpager_menu_bn);
        mMenuDeleteView = (TextView) findViewById(R.id.main_viewpager_menu_delete_img);
        mMenuCameraView = (ImageView) findViewById(R.id.main_viewpager_camera_imageView);
        mNoActionBarLinearLayout = (LinearLayout) findViewById(R.id.main_viewpager_no_action_bar_layout);
        mNoActionBarRecommendWorkLinearLayout = (LinearLayout) findViewById(R.id.main_viewpager_no_action_recommend_work_layout);
        actionBarViews.add(mMenuBn);
        actionBarViews.add(mMenuDeleteView);
        actionBarViews.add(mMenuCameraView);
        actionBarViews.add(mNoActionBarLinearLayout);
        actionBarViews.add(mNoActionBarRecommendWorkLinearLayout);
        actionBarViews.add(findViewById(R.id.main_viewpager_menu_more));
        CoordinatorLayout mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_Viewpager_content);
        if (Build.VERSION.SDK_INT > 19) {
            mCoordinatorLayout.setPadding(mCoordinatorLayout.getPaddingLeft(), Divice.getStatusBarHeight(this)
                    , mCoordinatorLayout.getPaddingRight(), mCoordinatorLayout.getBottom());
        }
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        isPhone = getResources().getBoolean(R.bool.large_layout);
        String username = getSharedPreferences(getString(R.string.shared_file_name), Context.MODE_PRIVATE).
                getString(getString(R.string.username), getString(R.string.visitor));
        View headerView = mNavigationView.inflateHeaderView(R.layout.nav_header);
        mNavNameTV = (TextView) headerView.findViewById(R.id.nav_header_username);
        mNavNameTV.setText(username);
        mNavHeadImg = (ImageView) headerView.findViewById(R.id.headIMG);
        Picasso.with(getBaseContext()).load(R.drawable.cheese_3)
                .transform(new RoundedTransformation(R.dimen.bottom_choose_height, 4))
                .resizeDimen(R.dimen.bottom_choose_height, R.dimen.bottom_choose_height).centerCrop()
                .into(mNavHeadImg);
    }

    @Override
    public void onStart() {
        super.onStart();
        initFinalValue();
        new LoadData(this).loadCollege().loadQuestionLable().loadVoiceLable();
        EventBus.getDefault().register(this);
    }

    private void initNavigationViewListener() {
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                mDrawerLayout.closeDrawers();
                item.setChecked(true);
                switch (item.getItemId()) {
                    case R.id.nav_alumni_circle:
                        open(AlumniDynamicFragment.newInstance());
                        break;
                    case R.id.nav_tucao:
                        open(MajorAskFragment.newInstance());
                        break;
                    case R.id.nav_heat_voice:
                        open(AlumniVoiceFragment.newInstance());
                        break;
                    case R.id.nav_recrict:
                        open(RecommendWorkFragment.newInstance());
                        break;
                    case R.id.my_circle:
                        open(MyCircleFragment.newInstance());
                        break;
                    case R.id.setting:
                        open(SettingFragment.newInstance());
                        break;
                }
                return false;
            }
        });
    }


    private void initFinalValue() {
        final int deviceWidth = Divice.getDisplayWidth(this);
        final int deviceHeight = Divice.getDisplayHeight(this);
        final int visibleDeviceHeight = deviceHeight - (int) (Divice.convertDpToPixel(mToolBar.getHeight(), this)
                + Divice.getStatusBarHeight(this));
        getSharedPreferences().edit().putInt(getString(R.string.diviceWidth), deviceWidth).commit();
        getSharedPreferences().edit().putInt(getString(R.string.diviceHeight), deviceHeight).commit();
        getSharedPreferences().edit().putInt(getString(R.string.visiDiviceHeight), visibleDeviceHeight).commit();

        //for only test
        Set<String> levels = new HashSet<>();
        levels.add("本科");
        levels.add("所有");
        levels.add("硕士");
        Set<String> degrees = new HashSet<>();
        degrees.add("本科;2010");
        degrees.add("硕士;2014");
        getSharedPreferences().edit().putStringSet(Constant.DEGREES, degrees).commit();
        getSharedPreferences().edit().putStringSet(getString(R.string.level), levels).commit();

        //设置推荐工作的默认查询参数
        setRecommendWorkDefaultQueryParam();
    }

    private void setRecommendWorkDefaultQueryParam() {
        Set<String> levels = getSharedPreferences().getStringSet(getString(R.string.level), new HashSet<String>());
        if (levels.size() == 4) {
            getSharedPreferences().edit().putString(Constant.DEGREE, Constant.DOCTOR);
        } else if (levels.size() == 3) {
            getSharedPreferences().edit().putString(Constant.DEGREE, Constant.MASTER);
        } else {
            getSharedPreferences().edit().putString(Constant.DEGREE, Constant.UNDERGRADUATE);
        }
        getSharedPreferences().edit().putString(Constant.WORK_TYP, 0 + "");
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
        open(fragment, false, null);
    }

    @Override
    public void open(BaseFragment fragment, boolean clearBackStack, Fragment fragmentToRemove) {

        if (fragment.getClass().getSimpleName().equals(CircleImageViewFragment.TAG)) {
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
        open(fragment, clearBackStack, null);
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
        return (LinearLayout) findViewById(R.id.main_viewpager_no_action_recommend_work_layout);
    }

    @Override
    public void display(int target) {
        for (int i = 0; i < actionBarViews.size(); i++) {
            if (target == i) {
                actionBarViews.get(i).setVisibility(View.VISIBLE);
            } else {
                actionBarViews.get(i).setVisibility(View.GONE);
            }
        }
    }

    @Override
    public boolean isPhone() {
        return isPhone;
    }


    @Override
    public String token() {
        return getSharedPreferences().getString(Constant.AUTHORIZATION, "").trim();
    }

    @Override
    public int userId() {
        return getSharedPreferences().getInt(Constant.USER_ID, 0);
    }

    @Override
    public void closeDrawLayout() {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    @Override
    public void openDrawLayout() {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    @Override
    public TextView navNameTV() {
        return mNavNameTV;
    }

    @Override
    public ImageView navHeadImg() {
        return mNavHeadImg;
    }

    @Override
    public IWXAPI wxApi() {
        return api;
    }


    @Override
    public void onBackPressed() {
        if (mLocalBackStack.isEmpty() || (mLocalBackStack.size() == 1)) {
            finish();
            return;
        }
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (mLocalBackStack.size() >= 2) {
            BaseFragment fragment = mLocalBackStack.peek();
            if (fragment instanceof PublishDynamicFragment || fragment instanceof AskPublishFragment
                    || fragment instanceof PublishVoiceFragment || fragment instanceof RecommendPublishFragment) {
                SchoolFriendDialog dialog = SchoolFriendDialog.exitReminderDialog(this, getString(R.string.are_you_sure_exit_this_eidt));
                dialog.getBuilder().onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                        mLocalBackStack.pop();
                        ft.replace(R.id.container, mLocalBackStack.peek());
                        ft.commit();
                    }
                });
                dialog.show();
            } else if (fragment instanceof RecommendCollectFragment) {
                RecommendCollectFragment collectFragment = (RecommendCollectFragment) fragment;
                if (collectFragment.isMore()) {
                    EventBus.getDefault().post(new MessageEventMore(""));
                } else {
                    mLocalBackStack.pop();
                    ft.replace(R.id.container, mLocalBackStack.peek());
                    ft.commit();
                }
            } else if (fragment instanceof AskCollectFragment) {
                AskCollectFragment collectFragment = (AskCollectFragment) fragment;
                if (collectFragment.isMore()) {
                    EventBus.getDefault().post(new MessageEventMore(""));
                } else {
                    mLocalBackStack.pop();
                    ft.replace(R.id.container, mLocalBackStack.peek());
                    ft.commit();
                }
            } else if (fragment instanceof VoiceCollectFragment) {
                VoiceCollectFragment collectFragment = (VoiceCollectFragment) fragment;
                if (collectFragment.isMore()) {
                    EventBus.getDefault().post(new MessageEventMore(""));
                } else {
                    mLocalBackStack.pop();
                    ft.replace(R.id.container, mLocalBackStack.peek());
                    ft.commit();
                }
            } else if (fragment instanceof DynamicCollectFragment) {
                DynamicCollectFragment collectFragment = (DynamicCollectFragment) fragment;
                if (collectFragment.isMore()) {
                    EventBus.getDefault().post(new MessageEventMore(""));
                } else {
                    mLocalBackStack.pop();
                    ft.replace(R.id.container, mLocalBackStack.peek());
                    ft.commit();
                }
            } else {
                mLocalBackStack.pop();
                ft.replace(R.id.container, mLocalBackStack.peek());
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


    private void initDataBase() {
        SchoolFriendDbHelper.newInstance(this);
    }

    @Subscribe
    public void onNetStateMessageState(NetworkStateChanged event) {
        if (!event.isInternetConnected()) {
            ToastUtil.showShortText(this, getString(R.string.network_connect_unavaible));
        }
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
}
