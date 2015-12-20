package com.nju.fragment;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.nju.activity.R;
import com.nju.adatper.UserInfoAdapter;
import com.nju.model.UserInfo;
import com.nju.util.Divice;

import java.util.ArrayList;


public class UserInfoFragement extends BaseFragment {

    public static final String TAG = UserInfoFragement.class.getSimpleName() ;
    private static final String USER_INFO = "user_info";
    private ArrayList<UserInfo> mUserInfos = null;
    private ListView mListView;
    public static UserInfoFragement newInstance(ArrayList<UserInfo> userInfos ) {
        UserInfoFragement fragment = new UserInfoFragement();
        Bundle args = new Bundle();
        args.putParcelableArrayList(USER_INFO,userInfos);
        fragment.setArguments(args );
        return fragment;
    }

    public UserInfoFragement() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null) {
            mUserInfos = getArguments().getParcelableArrayList(USER_INFO);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_info, container, false);
        view.setPadding(view.getPaddingLeft(), Divice.getStatusBarHeight(getContext()),view.getPaddingRight(),view.getPaddingBottom());
        mListView = (ListView) view;
        View headView = inflater.inflate(R.layout.fragment_user_info_listview_header,mListView,false);
        mListView.addHeaderView(headView);
        initHeaderText(headView);
        mListView.setAdapter(new UserInfoAdapter(getContext(),mUserInfos));
        return view;
    }

    private void initHeaderText(View view){
        TextView nameTV,sexTV;
        nameTV = (TextView) view.findViewById(R.id.fragment_user_info_listview_name_tv);
        sexTV = (TextView) view.findViewById(R.id.fragment_user_info_listview_sex_tv);
        for (UserInfo userInfo:mUserInfos){
            nameTV.setText(userInfo.getName());
            sexTV.setText(userInfo.getSex());
            break;
        }
    }

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mUserInfos.size();
        }

        @Override
        public Object getItem(int position) {
            return mUserInfos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
               convertView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_user_info_item,null);
                holder = new ViewHolder();
                holder.labeTextView = (TextView) convertView.findViewById(R.id.fragment_user_info_item_label_text);
                holder.xueYuanTextView = (TextView) convertView.findViewById(R.id.fragment_user_info_item_xueyuan_text);
                holder.majorTextView = (TextView) convertView.findViewById(R.id.fragment_user_info_item_major_text);
                holder.dateTextView = (TextView) convertView.findViewById(R.id.fragment_user_info_item_date_text);
                convertView.setTag(holder);
            }
            else{
                holder = (ViewHolder) convertView.getTag();
            }
            UserInfo info = mUserInfos.get(position);
            holder.labeTextView.setText(info.getLabel());
            holder.xueYuanTextView.setText(info.getFenYuan());
            holder.majorTextView.setText(info.getMajor());
            holder.dateTextView.setText(info.getDate());
            return convertView;
        }
    }
    private class ViewHolder{
        public TextView labeTextView;
        public TextView xueYuanTextView;
        public TextView majorTextView;
        public TextView dateTextView;
    }
}
