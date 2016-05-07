package com.nju.adatper;

import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nju.activity.R;
import com.nju.event.MessageContentIdEvent;
import com.nju.fragment.AlumniVoiceItemDetailFragment;
import com.nju.fragment.BaseFragment;
import com.nju.fragment.CircleImageViewFragment;
import com.nju.model.AlumniVoice;
import com.nju.util.Constant;
import com.nju.util.DateUtil;
import com.nju.util.HeadIcon;
import com.nju.util.PathConstant;
import com.nju.util.StringBase64;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by cun on 2016/3/15.
 */
public class AlumniVoiceItemAdapter extends BaseAdapter {

    private ArrayList<AlumniVoice> mVoices;
    private BaseFragment mFragment;

    public AlumniVoiceItemAdapter(BaseFragment fragment, ArrayList<AlumniVoice> voices) {
        mFragment = fragment;
        mVoices = voices;
    }

    @Override
    public int getCount() {
        return mVoices.size();
    }

    @Override
    public Object getItem(int position) {
        return mVoices.get(position);
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
            convertView = LayoutInflater.from(mFragment.getContext()).inflate(R.layout.alumni_voice_item, parent, false);
            holder.nameTV = (TextView) convertView.findViewById(R.id.alumni_vo_name);
            holder.labelTV = (TextView) convertView.findViewById(R.id.alumni_vo_label);
            holder.titleTV = (TextView) convertView.findViewById(R.id.alumni_vo_title);
            holder.dateTV = (TextView) convertView.findViewById(R.id.alumni_vo_date);
            holder.praiseCountTV = (TextView) convertView.findViewById(R.id.alumni_vo_praise_number);
            holder.commentTV = (TextView) convertView.findViewById(R.id.alumni_vo_comment_number);
            holder.simpleDescTV = (TextView) convertView.findViewById(R.id.alumni_vo_simple_desc);
            holder.picGridView = (GridView) convertView.findViewById(R.id.mGridView);
            holder.headImg = (ImageView) convertView.findViewById(R.id.head_icon_img);
            holder.deleteTV = (TextView) convertView.findViewById(R.id.delete_tv);
            convertView.setTag(holder);
        }
        final AlumniVoice voice = mVoices.get(position);
        holder = (ViewHolder) convertView.getTag();
        int author_id = voice.getAuthorInfo().getAuthorId();
        if (author_id == mFragment.getHostActivity().userId()) {
            holder.deleteTV.setText(Constant.DELETE);
        } else {
            holder.deleteTV.setText("");
        }
        holder.deleteTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new MessageContentIdEvent(voice.getId()));
            }
        });

        HeadIcon.setUp(holder.headImg, voice.getAuthorInfo());
        holder.commentTV.setText(voice.getCommentCount() + "");
        holder.praiseCountTV.setText(voice.getPraiseCount() + "");
        try {
            holder.titleTV.setText(StringBase64.decode(voice.getTitle()));
        } catch (IllegalArgumentException e) {
            holder.titleTV.setText(Constant.UNKNOWN_CHARACTER);
        }
        holder.titleTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFragment.getHostActivity().open(AlumniVoiceItemDetailFragment.newInstance(voice));
            }
        });
        final long time = DateUtil.getTime(voice.getDate());
        final String date = DateUtils.getRelativeTimeSpanString(time, new Date().getTime(), DateUtils.MINUTE_IN_MILLIS, DateUtils.FORMAT_NUMERIC_DATE).toString();
        holder.dateTV.setText(date);
        holder.labelTV.setText(voice.getAuthorInfo().getLabel());
        holder.nameTV.setText(voice.getAuthorInfo().getAuthorName());
        try {
            holder.simpleDescTV.setText(StringBase64.decode(voice.getContent()));
        } catch (IllegalArgumentException e) {
            holder.simpleDescTV.setText(Constant.UNKNOWN_CHARACTER);
        }
        if (voice.getImgPaths() == null) {
            holder.picGridView.setAdapter(new ContentPicAdater(mFragment.getContext(), PathConstant.ALUMNI_VOICE_IMG_PATH, Constant.EMPTY));
        } else {
            holder.picGridView.setAdapter(new ContentPicAdater(mFragment.getContext(), PathConstant.ALUMNI_VOICE_IMG_PATH, voice.getImgPaths().split(",")));
        }
        holder.picGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mFragment.getHostActivity().open(CircleImageViewFragment.newInstance(voice.getImgPaths().split(","), position, PathConstant.ALUMNI_VOICE_IMG_PATH));
            }
        });
        return convertView;
    }

    private class ViewHolder {
        private ImageView headImg;
        private TextView nameTV;
        private TextView labelTV;
        private TextView titleTV;
        private TextView dateTV;
        private TextView praiseCountTV;
        private TextView commentTV;
        private TextView simpleDescTV;
        private TextView deleteTV;
        private GridView picGridView;
    }
}
