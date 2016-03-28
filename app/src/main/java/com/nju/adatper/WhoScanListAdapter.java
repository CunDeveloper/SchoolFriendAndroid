package com.nju.adatper;

import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ExpandableListAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.nju.activity.BaseActivity;
import com.nju.activity.R;
import com.nju.fragment.BaseFragment;
import com.nju.fragment.PublishDynamicFragment;
import com.nju.fragment.PublishVoiceFragment;
import com.nju.model.Entry;

import java.util.ArrayList;


/**
 * Created by xiaojuzhang on 2015/12/2.
 */
public class WhoScanListAdapter implements ExpandableListAdapter {
    private ArrayList<Entry> mGroupItems;
    private BaseFragment mContext;

    public WhoScanListAdapter(BaseFragment context, ArrayList<Entry> gropItems) {
        this.mGroupItems = gropItems;
        this.mContext = context;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getGroupCount() {
        return mGroupItems.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mGroupItems.get(groupPosition).getChildItems().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mGroupItems.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mGroupItems.get(groupPosition).getChildItems().get(childPosition);
    }


    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext.getContext()).inflate(R.layout.who_scan_group, parent, false);
            holder.bigTextView = (TextView) convertView.findViewById(R.id.who_scan_group_item_big_label);
            holder.smallTextView = (TextView) convertView.findViewById(R.id.who_scan_group_item_small_label);
            holder.imageView = (TextView) convertView.findViewById(R.id.who_scan_group_img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.bigTextView.setText(mGroupItems.get(groupPosition).getBigLabel());
        holder.smallTextView.setText(mGroupItems.get(groupPosition).getSmallLabel());
        holder.imageView.setText(mGroupItems.get(groupPosition).getDrawable());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext.getContext()).inflate(R.layout.who_scan_group_child, parent, false);
            holder.bigTextView = (TextView) convertView.findViewById(R.id.who_scan_group_item_child_big_label);
            holder.smallTextView = (TextView) convertView.findViewById(R.id.who_scan_group_item_small_child_label);
            holder.radioButton = (RadioButton) convertView.findViewById(R.id.who_scan_group_child_check);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                openFragment(holder.bigTextView);
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment(holder.bigTextView);
            }
        });
        holder.bigTextView.setText(mGroupItems.get(groupPosition).getChildItems().get(childPosition).getBigLabel());
        holder.smallTextView.setText(mGroupItems.get(groupPosition).getChildItems().get(childPosition).getSmallLabel());
        return convertView;
    }

    private void openFragment(TextView bigText) {
        BaseActivity.LocalStack stack = mContext.getHostActivity().getBackStack();
        stack.pop();
        if (!stack.isEmpty()) {
            BaseFragment fragment = (BaseFragment) stack.peek();
            if (fragment instanceof PublishDynamicFragment) {
                PublishDynamicFragment dynamicFragment = (PublishDynamicFragment) fragment;
                dynamicFragment.setWhoScan(bigText.getText().toString());
                mContext.getHostActivity().open(dynamicFragment);
            } else if (fragment instanceof PublishVoiceFragment) {
                PublishVoiceFragment voiceFragment = (PublishVoiceFragment) fragment;
                voiceFragment.setWhoScan(bigText.getText().toString());
                mContext.getHostActivity().open(voiceFragment);
            }
        }


    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {

    }

    @Override
    public void onGroupCollapsed(int groupPosition) {

    }

    @Override
    public long getCombinedChildId(long groupId, long childId) {
        return 0;
    }

    @Override
    public long getCombinedGroupId(long groupId) {
        return 0;
    }

    private class ViewHolder {
        public TextView bigTextView;
        public TextView smallTextView;
        public TextView imageView;
        public RadioButton radioButton;
    }
}
