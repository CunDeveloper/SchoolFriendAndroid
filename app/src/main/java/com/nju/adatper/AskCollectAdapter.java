package com.nju.adatper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.nju.activity.R;
import com.nju.model.AlumniQuestion;
import com.nju.model.AlumniVoice;

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
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.voice_collect_item,parent,false);
            holder = new ViewHolder();
            holder.nameTV = (TextView) convertView.findViewById(R.id.name_tv);
            holder.labelTV = (TextView) convertView.findViewById(R.id.label_tv);
            holder.dateTV = (TextView) convertView.findViewById(R.id.date_tv);
            holder.contentTV = (TextView) convertView.findViewById(R.id.content_tv);
            holder.titleTV = (TextView) convertView.findViewById(R.id.title_tv);
            holder.headImg = (ImageView) convertView.findViewById(R.id.head_icon);
            convertView.setTag(holder);
        }
        AlumniQuestion alumniQuestion = mAlumniQuestions.get(position);
        holder = (ViewHolder) convertView.getTag();
        holder.nameTV.setText(alumniQuestion.getAuthorInfo().getAuthorName());
        holder.labelTV.setText(alumniQuestion.getAuthorInfo().getLabel());
        holder.dateTV.setText(alumniQuestion.getDate());
        holder.titleTV.setText(alumniQuestion.getProblem());
        holder.contentTV.setText(alumniQuestion.getDescription());
        return convertView;
    }

    private class ViewHolder{
        private ImageView headImg,contentImg;
        private TextView nameTV,labelTV,dateTV,contentTV,titleTV;
    }
}
