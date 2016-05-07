package com.nju.fragment;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nju.activity.R;
import com.nju.http.ImageDownloader;
import com.nju.model.DegreeInfo;
import com.nju.model.UserInfo;
import com.nju.util.Constant;
import com.nju.util.Divice;
import com.nju.util.SchoolFriendGson;

import java.util.ArrayList;


public class DetailPersonInfo extends BaseFragment {

    private static final String TAG = DetailPersonInfo.class.getSimpleName();
    private ArrayList<DegreeInfo> mDegreeInfos = new ArrayList<>();

    public DetailPersonInfo() {
        // Required empty public constructor
    }

    public static DetailPersonInfo newInstance() {
        DetailPersonInfo fragment = new DetailPersonInfo();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_person_info, container, false);
        view.setPadding(view.getPaddingLeft(), Divice.getStatusBarHeight(getContext()), view.getPaddingRight(), view.getPaddingBottom());
        String info = getHostActivity().getSharedPreferences().getString(getString(R.string.person_info), "");
        Log.i(TAG, info);
        if (!info.equals("")) {
            mDegreeInfos = SchoolFriendGson.newInstance().fromJsonToList(info, DegreeInfo.class);
        }
        initView(view);
        return view;
    }

    private void initView(View view) {
        TextView infoTV = (TextView) view.findViewById(R.id.infoTV);
        final ArrayList<UserInfo> mUserInfos = new ArrayList<>();

        infoTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInfo info;
                for (DegreeInfo degreeInfo : mDegreeInfos) {
                    info = new UserInfo();
                    info.setLabel(degreeInfo.getLevel() + "-" + degreeInfo.getUniversityName());
                    info.setDate(degreeInfo.getStartDate() + "");
                    info.setMajor(degreeInfo.getMajor());
                    info.setName(degreeInfo.getRealName());
                    info.setSex(degreeInfo.getSex());
                    info.setFenYuan(degreeInfo.getSchoolName());
                    mUserInfos.add(info);
                }
                getHostActivity().open(UserInfoFragment.newInstance(mUserInfos, getString(R.string.person_info)));
            }
        });
        ImageView headImg = (ImageView) view.findViewById(R.id.imageView);
        String headUrl = getHostActivity().getSharedPreferences().getString(getString(R.string.head_url), "");
        if (!headUrl.equals("")) {
            ImageDownloader.with(getContext()).download(headUrl, headImg);
        }
        String name = "", label = "";
        int size = mDegreeInfos.size();
        switch (size) {
            case 3: {
                for (DegreeInfo info : mDegreeInfos) {
                    if (info.getLevel().equals(Constant.DOCTOR)) {
                        name = info.getRealName();
                        label = info.getLevel() + " " + info.getUniversityName() + " " + info.getSchoolName() + " " + info.getDate();
                        break;
                    }
                }
                break;
            }
            case 2: {
                for (DegreeInfo info : mDegreeInfos) {
                    if (info.getLevel().equals(Constant.MASTER)) {
                        name = info.getRealName();
                        label = info.getLevel() + " " + info.getUniversityName() + " " + info.getSchoolName() + " " + info.getStartDate();
                        break;
                    }
                }
                break;
            }
            case 1: {
                for (DegreeInfo info : mDegreeInfos) {
                    name = info.getRealName();
                    label = info.getLevel() + " " + info.getUniversityName() + " " + info.getSchoolName() + " " + info.getDate();
                }
                break;
            }
        }

        TextView nameTV = (TextView) view.findViewById(R.id.nameTV);
        TextView labelTV = (TextView) view.findViewById(R.id.labeTv);
        nameTV.setText(name);
        labelTV.setText(label);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.detail_info);
        }
        getHostActivity().display(8);
    }

}
