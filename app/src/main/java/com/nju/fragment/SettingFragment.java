package com.nju.fragment;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.nju.View.SchoolFriendDialog;
import com.nju.activity.R;
import com.nju.http.ImageDownloader;
import com.nju.util.Constant;
import com.nju.util.Divice;
import com.nju.util.PathConstant;

import java.util.HashSet;


public class SettingFragment extends BaseFragment {

    public SettingFragment() {
        // Required empty public constructor
    }

    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.arrows);
            actionBar.setTitle(R.string.setting);
        }
        getHostActivity().display(7);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        view.setPadding(view.getPaddingLeft(), Divice.getStatusBarHeight(getContext()), view.getPaddingRight(), view.getPaddingBottom());
        addViewEvent(view);
        return view;
    }

    private void addViewEvent(View view) {
        ImageView headImg = (ImageView) view.findViewById(R.id.headIconImg);
        String headName = getHostActivity().getSharedPreferences().
                getString(getString(R.string.head_url), "");
        if (!headName.equals("")) {
            String headUrl = PathConstant.IMAGE_PATH_SMALL + PathConstant.HEAD_ICON_IMG + headName;
            ImageDownloader.with(getContext()).download(headUrl, headImg);
        }
        TextView functionTV = (TextView) view.findViewById(R.id.function_desc);
        functionTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHostActivity().open(WebViewFragment.newInstance("file:///android_asset/SchoolFriendHtml/html/recommedShare.html"));
            }
        });
        TextView sysNoticeTV = (TextView) view.findViewById(R.id.sys_notice);
        sysNoticeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHostActivity().open(WebViewFragment.newInstance("file:///android_asset/html/sys_notice.html"));
            }
        });
        TextView helpTV = (TextView) view.findViewById(R.id.help_fellback);
        helpTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHostActivity().open(WebViewFragment.newInstance("file:///android_asset/html/help.html"));
            }
        });
        TextView jubaoTV = (TextView) view.findViewById(R.id.jubao_tousu);
        jubaoTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHostActivity().open(WebViewFragment.newInstance("file:///android_asset/html/jubao.html"));
            }
        });
        TextView checkVersionTV = (TextView) view.findViewById(R.id.check_new_version);
        checkVersionTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        TextView cleanStorageTV = (TextView) view.findViewById(R.id.clean_storage);
        cleanStorageTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        TextView exitTV = (TextView) view.findViewById(R.id.exit_alumni);
        exitTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SchoolFriendDialog dialog = SchoolFriendDialog.remindDialog(getContext(), getString(R.string.tip), getString(R.string.sure_exit_alumnus_group));
                dialog.getBuilder().onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                        getHostActivity().getSharedPreferences().edit().putString(Constant.AUTHORIZATION, "").commit();
                        getHostActivity().getSharedPreferences().edit().putString(getString(R.string.username),"").commit();
                        getHostActivity().getSharedPreferences().edit().putString(getString(R.string.head_url),"").commit();
                        getHostActivity().getSharedPreferences().edit().putString(Constant.COLLEGES, "").commit();
                        getHostActivity().getSharedPreferences().edit().putString(getString(R.string.bg_url),"").commit();
                        getHostActivity().getSharedPreferences().edit().putString(getString(R.string.person_info), "").commit();
                        getHostActivity().getSharedPreferences().edit().putStringSet(Constant.DEGREES, new HashSet<String>()).commit();
                        getHostActivity().getSharedPreferences().edit().putString(Constant.COLLEGES, "").commit();
                        getHostActivity().getSharedPreferences().edit().putStringSet(getString(R.string.level), new HashSet<String>()).commit();

                        getActivity().finish();
                    }
                });
                dialog.getBuilder().onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        RelativeLayout headLayout = (RelativeLayout) view.findViewById(R.id.setHeadLayout);
        headLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SingleChoosePicFragment fragment = SingleChoosePicFragment.newInstance(getString(R.string.update_head));
                getHostActivity().open(fragment,fragment);
            }
        });

    }


}
