package com.nju.adatper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.nju.activity.R;
import com.nju.fragment.BaseFragment;
import com.nju.util.ToastUtil;

import java.util.ArrayList;

/**
 * Created by cun on 2016/5/1.
 */
public class ComplainAdapter extends BaseAdapter {
    private Context mContext;
    private int index = -1;

    private String[] contents = {
            "色情低俗",
            "广告骚扰",
            "诱导分享",
            "谣言",
            "政治敏感",
            "违法(暴力恐怖、违禁品等)",
            "侵权",
            "售假",
            "其他"
    };
    ArrayList<RadioButton> radioButtons = new ArrayList<>();
    public ComplainAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return contents.length;
    }

    @Override
    public Object getItem(int position) {
        return contents[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.complain_item,parent,false);
            holder.textView = (TextView) convertView.findViewById(R.id.content_tv);
            holder.radioButton = (RadioButton) convertView.findViewById(R.id.radioButton);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        holder.textView.setText(contents[position]);
        final RadioButton radioButton = holder.radioButton;
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioButton.setChecked(true);
            }
        });
        return convertView;
    }

    private class ViewHolder{
        private TextView textView;
        private RadioButton radioButton;
    }
}
