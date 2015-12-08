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
import com.nju.model.UserInfo;

import java.util.ArrayList;


public class UserInfoFragement extends BaseFragment {

    public static final String TAG = UserInfoFragement.class.getSimpleName() ;
    private static UserInfoFragement fragment = null;
    private ArrayList<UserInfo> lists = null;
    private ListView mListView;
    public static UserInfoFragement newInstance( ) {
        if(fragment == null) {
            fragment = new UserInfoFragement();
        }

        return fragment;
    }

    public UserInfoFragement() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null) {
            lists = getArguments().getParcelableArrayList("VALUE");
        }

        getActivity().getSharedPreferences(getString(R.string.shared_file_name), Context.MODE_PRIVATE).
                edit().putString(getString(R.string.username),lists.get(0).getName()).commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_info_fragement, container, false);
        TextView nameText = (TextView) view.findViewById(R.id.fragment_user_info_fragement_name_text);
        nameText.setText(lists.get(0).getName());
        TextView sexText = (TextView) view.findViewById(R.id.fragment_user_info_fragement_sex_text);
        sexText.setText(lists.get(0).getSex());
        mListView = (ListView) view.findViewById(R.id.fragment_user_info_fragement_list);
        mListView.setAdapter(new MyAdapter());
        return view;
    }


    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return lists.size();
        }

        @Override
        public Object getItem(int position) {
            return lists.get(position);
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
            UserInfo info = lists.get(position);
            holder.labeTextView.setText(info.getLabel());
            holder.xueYuanTextView.setText(info.getYuanXiaoName());
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
