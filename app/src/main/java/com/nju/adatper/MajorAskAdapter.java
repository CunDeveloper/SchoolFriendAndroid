package com.nju.adatper;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nju.activity.R;
import com.nju.model.AlumniQuestion;
import com.nju.util.Constant;
import com.nju.util.DateUtil;
import com.nju.util.StringBase64;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by cun on 2016/3/16.
 */
public class MajorAskAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<AlumniQuestion> mQuestions;

    public MajorAskAdapter(Context context, ArrayList<AlumniQuestion> questions) {
        mContext = context;
        mQuestions = questions;
    }

    @Override
    public int getCount() {
        return mQuestions.size();
    }

    @Override
    public Object getItem(int position) {
        return mQuestions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.alumni_question_item, parent, false);
            holder.problemTV = (TextView) convertView.findViewById(R.id.title_tv);
            holder.nameTV = (TextView) convertView.findViewById(R.id.name_tv);
            holder.dateTV = (TextView) convertView.findViewById(R.id.date_tv);
            holder.replayCountTV = (TextView) convertView.findViewById(R.id.count_tv);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        AlumniQuestion alumniQuestion = mQuestions.get(position);
        try{
            holder.problemTV.setText(StringBase64.decode(alumniQuestion.getProblem()));
        }catch (IllegalArgumentException e){
            holder.problemTV.setText(Constant.UNKNOWN_CHARACTER);
        }
        holder.nameTV.setText(alumniQuestion.getAuthor().getAuthorName());
        final long time = DateUtil.getTime(alumniQuestion.getDate());
        final String date = DateUtils.getRelativeTimeSpanString(time, new Date().getTime(), DateUtils.MINUTE_IN_MILLIS, DateUtils.FORMAT_NUMERIC_DATE).toString();
        holder.dateTV.setText(date);
        holder.replayCountTV.setText(alumniQuestion.getReplayCount() + "");
        return convertView;
    }

    private class ViewHolder {
        private TextView problemTV;
        private TextView dateTV;
        private TextView nameTV;
        private TextView replayCountTV;
    }
}
