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
import com.nju.http.ResponseCallback;
import com.nju.http.request.PostRequestJson;
import com.nju.http.response.ParseResponse;
import com.nju.model.AlumniTalk;
import com.nju.model.AuthorInfo;
import com.nju.model.ContentComment;
import com.nju.model.DegreeInfo;
import com.nju.model.UserInfo;
import com.nju.service.UserDegreeInfoService;
import com.nju.util.Constant;
import com.nju.util.Divice;
import com.nju.util.FragmentUtil;
import com.nju.util.PathConstant;
import com.nju.util.SchoolFriendGson;

import java.io.IOException;
import java.util.ArrayList;


public class DetailPersonInfoFragment extends BaseFragment {

    private static final String TAG = DetailPersonInfoFragment.class.getSimpleName();
    private ArrayList<DegreeInfo> mDegreeInfos = new ArrayList<>();
    private static final String AUTHOR_PARAM = "authorParam";
    private PostRequestJson mQueryJson;
    private AuthorInfo mAuthor;
    private View mView;

    private ResponseCallback callback = new ResponseCallback() {
        @Override
        public void onFail(Exception error) {
            Log.e(TAG,error.getMessage());
        }

        @Override
        public void onSuccess(String responseBody) {
            Log.i(TAG, responseBody);
            if (FragmentUtil.isAttachedToActivity(DetailPersonInfoFragment.this)) {
                Log.i(TAG, responseBody);
                ParseResponse parseResponse = new ParseResponse();
                try {
                    Object object = parseResponse.getInfo(responseBody,DegreeInfo.class);
                    if (object != null) {
                        ArrayList degrees = (ArrayList) object;
                        if (degrees.size() > 0) {

                            for (Object obj : degrees) {
                                DegreeInfo degreeInfo = (DegreeInfo) obj;
                                mDegreeInfos.add(degreeInfo);
                            }
                        }
                    }
                  initView(mView);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    public DetailPersonInfoFragment() {
        // Required empty public constructor
    }

    public static DetailPersonInfoFragment newInstance(AuthorInfo authorInfo) {
        DetailPersonInfoFragment fragment = new DetailPersonInfoFragment();
        Bundle args = new Bundle();
        args.putParcelable(AUTHOR_PARAM,authorInfo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mAuthor = getArguments().getParcelable(AUTHOR_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_detail_person_info, container, false);
        mView.setPadding(mView.getPaddingLeft(), Divice.getStatusBarHeight(getContext()), mView.getPaddingRight(), mView.getPaddingBottom());
        int userId = getHostActivity().userId();
        if (userId == mAuthor.getAuthorId()) {
            String info = getHostActivity().getSharedPreferences().getString(getString(R.string.person_info), "");
            Log.i(TAG, info);
            if (!info.equals("")) {
                mDegreeInfos = SchoolFriendGson.newInstance().fromJsonToList(info, DegreeInfo.class);
            }
        } else {
            mQueryJson = UserDegreeInfoService.queryOtherAuthorDegrees(this,mAuthor,callback);
        }

        initView(mView);
        return mView;
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
        String headName = getHostActivity().getSharedPreferences().getString(getString(R.string.head_url), "");
        if (!headName.equals("")) {
            String headUrl = PathConstant.IMAGE_PATH_SMALL + PathConstant.HEAD_ICON_IMG + headName;
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
