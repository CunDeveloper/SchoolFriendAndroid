package com.nju.fragment;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nju.activity.R;
import com.nju.adatper.PersonCircleAdapter;
import com.nju.http.HttpManager;
import com.nju.http.ResponseCallback;
import com.nju.http.request.PostRequest;
import com.nju.util.Constant;
import com.nju.util.DateUtil;
import com.nju.util.Divice;
import com.nju.util.SchoolFriendGson;
import com.nju.util.StringBase64;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import model.Content;

public class PersonCircleFragment extends BaseFragment {
    public static final String TAG = PersonCircleFragment.class.getSimpleName();
    private String mUserName;
    private static final String USERNAME = "username";
    private ListView mListView;
    private ArrayList<Content> mDecodeContents;
    private ArrayList<Content> mContainPicContentList;
    private static PersonCircleFragment mFragment;

    public static PersonCircleFragment newInstance(String userName) {
        if(mFragment == null) {
            mFragment = new PersonCircleFragment();
        }
        Bundle bundle = new Bundle();
        bundle.putString(USERNAME,userName);
        mFragment.setArguments(bundle);
        return mFragment;
    }

    public PersonCircleFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            mUserName = getArguments().getString(USERNAME);
        }
        HashMap<String,String> paras = new HashMap<>();
        paras.put(Constant.USER_ID,String.valueOf(51));
        paras.put(Constant.LABEL,Constant.QUERY_ALL);
        HttpManager.getInstance().exeRequest(new PostRequest(Constant.BASE_URL + Constant.PERSON_CIRCLE_URL, paras, callback, TAG));
    }

    ResponseCallback callback = new ResponseCallback() {
        @Override
        public void onFail(Exception error) {

        }

        @Override
        public void onSuccess(String responseBody) {
            Log.i(TAG, responseBody);
            ArrayList<Content> contents = SchoolFriendGson.newInstance().fromJsonToList(responseBody, Content.class);

            Collections.sort(contents, new Comparator<Content>() {
                @Override
                public int compare(Content lhs, Content rhs) {
                    return rhs.getId()-lhs.getId();
                }
            });

            mDecodeContents = new ArrayList<>();
            mContainPicContentList = new ArrayList<>();
            Calendar calendar;
            int day=-1,month=-1;
            for (Content content:contents) {
                Content temp = content;
                try {
                    temp.setContent(StringBase64.decode(content.getContent()));
                }catch (IllegalArgumentException e){
                    //Mint.initAndStartSession(MyActivity.this, "ba2df536");
                    temp.setContent(Constant.UNKNOWN_CHARACTER);
                }

                calendar = DateUtil.getCalendar(content.getDate());
                int tempDay = DateUtil.day(calendar);
                int tempMonth = DateUtil.month(calendar);
                if (tempDay != day || tempMonth != month){
                    temp.setDay(tempDay+"");
                    temp.setMonth(tempMonth+"月");
                    day = tempDay;
                    month = tempMonth;
                } else {
                    temp.setDay("");
                    temp.setMonth("");
                }
                if (temp.getIs_contain_image()>0) {
                    mContainPicContentList.add(temp);
                }
                mDecodeContents.add(temp);
            }

            if (contents.size()>0){
                getHostActivity().getSharedPreferences().edit().putInt(Constant.MAX_ID_SAVE_CONTENT,mDecodeContents.get(0).getId()).commit();
            }
            Message message = new Message();
            message.what = Constant.SAVE_CONTENT_MESG;
            message.obj = mDecodeContents;
            //getHostActivity().getAppHandler().sendMessage(message);
            mListView.setAdapter(new PersonCircleAdapter(getContext(), mDecodeContents));
        }

    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person_circle, container, false);
        view.setPadding(view.getPaddingLeft(), Divice.getStatusBarHeight(getContext()),view.getPaddingRight(),view.getPaddingBottom());
        mListView = (ListView) view.findViewById(R.id.fragment_person_circle_listview);
        initListViewClickEvent();
        return view;
    }

    private void initListViewClickEvent() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Content content = mDecodeContents.get(position);
                if (content.getIs_contain_image()>0) {
                    getHostActivity().open(PersonCircleDetailPicFragment.newInstance(mContainPicContentList,position));
                }
                else {
                    getHostActivity().open(PersonCircleDetailFragment.newInstance(content));
                }

            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getHostActivity().hideAllMenuView();
        getHostActivity().getToolBar().setTitle(mUserName);
    }
}
