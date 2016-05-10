package com.nju.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nju.View.SchoolFriendTextView;
import com.nju.activity.R;
import com.nju.model.AlumniQuestion;
import com.nju.model.AlumniTalk;
import com.nju.util.Constant;
import com.nju.util.StringBase64;

import java.util.ArrayList;

import model.Content;


public class PersonCircleDetailPicFragment extends BaseFragment {

    private static final String TAG = PersonCircleDetailPicFragment.class.getSimpleName();
    private static final String TALK_PARAM = "talk_param";
    private static final String POSITION_PARAM = "position";
    private AlumniTalk  mTalk;
    private int mPosition;

    public PersonCircleDetailPicFragment() {

    }

    public static PersonCircleDetailPicFragment newInstance(AlumniTalk talk, int position) {
        PersonCircleDetailPicFragment fragment = new PersonCircleDetailPicFragment();
        Bundle args = new Bundle();
        args.putParcelable(TALK_PARAM,talk);
        args.putInt(POSITION_PARAM, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTalk = getArguments().getParcelable(TALK_PARAM);
            mPosition = getArguments().getInt(POSITION_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_person_circle_detail_pic, container, false);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.head);

        initView(view);
        return view;
    }

    private void initView(View view) {
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.fragment_person_circle_detail_pic_viewpager);
        TextView  contentTV = (TextView) view.findViewById(R.id.fragment_person_circle_detail_pic_content_tv);
        if (mTalk != null){
            try{
                contentTV.setText(StringBase64.decode(mTalk.getContent()));
            }catch (IllegalArgumentException e){
                contentTV.setText(Constant.UNKNOWN_CHARACTER);
            }

        }
    }
}
