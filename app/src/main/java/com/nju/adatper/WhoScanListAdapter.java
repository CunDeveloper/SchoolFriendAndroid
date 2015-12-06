package com.nju.adatper;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nju.activity.R;
import com.nju.model.Entry;

import java.util.ArrayList;


/**
 * Created by xiaojuzhang on 2015/12/2.
 */
public class WhoScanListAdapter implements ExpandableListAdapter {

    private ArrayList<Entry> mGropItems;
    private Context mContext;
    public WhoScanListAdapter(Context context,ArrayList<Entry> gropItems){
        this.mGropItems = gropItems;
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
        return mGropItems.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mGropItems.get(groupPosition).getChildItems().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mGropItems.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mGropItems.get(groupPosition).getChildItems().get(childPosition);
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
        ViewHodler hodler;
        if (convertView == null) {
            hodler = new ViewHodler();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.who_scan_group,parent,false);
            hodler.bigTextView = (TextView) convertView.findViewById(R.id.who_scan_group_item_big_label);
            hodler.smallTextView = (TextView) convertView.findViewById(R.id.who_scan_group_item_small_label);
            hodler.imageView = (ImageView) convertView.findViewById(R.id.who_scan_group_img);
            convertView.setTag(hodler);
        } else
        {
            hodler = (ViewHodler) convertView.getTag();
        }
        hodler.bigTextView.setText(mGropItems.get(groupPosition).getBigLabel());
        hodler.smallTextView.setText(mGropItems.get(groupPosition).getSamllLabel());
        hodler.imageView.setImageDrawable(mGropItems.get(groupPosition).getDrawable());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHodler hodler ;
        if (convertView == null) {
            hodler = new ViewHodler();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.who_scan_group_child,parent,false);
            hodler.bigTextView = (TextView) convertView.findViewById(R.id.who_scan_group_item_child_big_label);
            hodler.smallTextView = (TextView) convertView.findViewById(R.id.who_scan_group_item_small_child_label);
            hodler.checkBox = (CheckBox) convertView.findViewById(R.id.who_scan_group_child_check);
            convertView.setTag(hodler);
        }
        else {
            hodler = (ViewHodler) convertView.getTag();
        }
        if (isLastChild){
            hodler.bigTextView.setTextColor(mContext.getResources().getColor(R.color.light_blue));
            hodler.bigTextView.setText(mContext.getString(R.string.edit_label));
            hodler.checkBox.setVisibility(View.GONE);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext,"Hello",Toast.LENGTH_LONG).show();
                }
            });
        }
        hodler.bigTextView.setText(mGropItems.get(groupPosition).getChildItems().get(childPosition).getBigLabel());
        hodler.smallTextView.setText(mGropItems.get(groupPosition).getChildItems().get(childPosition).getSamllLabel());
        return convertView;
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

    private class ViewHodler {
        public TextView bigTextView;
        public TextView smallTextView;
        public ImageView imageView;
        public CheckBox checkBox;
    }
}
