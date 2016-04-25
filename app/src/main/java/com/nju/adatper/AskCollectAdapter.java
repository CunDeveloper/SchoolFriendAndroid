package com.nju.adatper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.nju.activity.R;
import com.nju.model.AlumniQuestion;
import com.nju.util.Constant;
import com.nju.util.DateUtil;
import com.nju.util.StringBase64;

import java.util.ArrayList;

/**
 * Created by cun on 2016/3/19.
 */
public class AskCollectAdapter extends BaseAdapter {

    private ArrayList<AlumniQuestion> mAlumniQuestions;
    private Context mContext;

    public AskCollectAdapter(Context context, ArrayList<AlumniQuestion> alumniQuestions) {
        mContext = context;
        mAlumniQuestions = alumniQuestions;
    }

    @Override
    public int getCount() {
        return mAlumniQuestions.size();
    }

    @Override
    public Object getItem(int position) {
        return mAlumniQuestions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.voice_collect_item, parent, false);
            holder = new ViewHolder();
            holder.nameTV = (TextView) convertView.findViewById(R.id.name_tv);
            holder.labelTV = (TextView) convertView.findViewById(R.id.label_tv);
            holder.dateTV = (TextView) convertView.findViewById(R.id.date_tv);
            holder.contentTV = (TextView) convertView.findViewById(R.id.content_tv);
            holder.titleTV = (TextView) convertView.findViewById(R.id.title_tv);
            holder.headImg = (ImageView) convertView.findViewById(R.id.head_icon);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.chooseCB);
            convertView.setTag(holder);
        }
        AlumniQuestion alumniQuestion = mAlumniQuestions.get(position);
        holder = (ViewHolder) convertView.getTag();
        holder.nameTV.setText(alumniQuestion.getAuthor().getAuthorName());
        holder.labelTV.setText(alumniQuestion.getAuthor().getLabel());
        holder.dateTV.setText(DateUtil.getRelativeTimeSpanString(alumniQuestion.getDate()));
        try{
            holder.titleTV.setText(StringBase64.decode(alumniQuestion.getProblem()));
        }catch (IllegalArgumentException e){
            holder.titleTV.setText(Constant.UNKNOWN_CHARACTER);
        }
        try{
            holder.contentTV.setText(StringBase64.decode(alumniQuestion.getDescription()));
        }catch (IllegalArgumentException e){
            holder.contentTV.setText(Constant.UNKNOWN_CHARACTER);
        }

        if (alumniQuestion.getCheck() == 0){
            holder.checkBox.setVisibility(View.GONE);
        }else if (alumniQuestion.getCheck() == 1){
            holder.checkBox.setVisibility(View.VISIBLE);
        }else if (alumniQuestion.getCheck() == 2){
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.checkBox.setChecked(true);
        }


        return convertView;
    }

    private class ViewHolder {
        private ImageView headImg, contentImg;
        private TextView nameTV, labelTV, dateTV, contentTV, titleTV;
        private CheckBox checkBox;
    }
}
