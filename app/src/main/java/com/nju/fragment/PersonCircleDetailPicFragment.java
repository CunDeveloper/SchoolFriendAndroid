package com.nju.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nju.View.SchoolFriendTextView;
import com.nju.activity.R;

import java.util.ArrayList;

import model.Content;


public class PersonCircleDetailPicFragment extends BaseFragment {
    private static final String TAG = PersonCircleDetailPicFragment.class.getSimpleName();
    private static final String CONTENT_PARAM = "content_param";
    private static final String POSITION_PARAM = "position";
    private ArrayList<Content> mContents;
    private int mPosition;

    public PersonCircleDetailPicFragment() {

    }

    public static PersonCircleDetailPicFragment newInstance(ArrayList<Content> contents, int position) {
        PersonCircleDetailPicFragment fragment = new PersonCircleDetailPicFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(CONTENT_PARAM, contents);
        args.putInt(POSITION_PARAM, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mContents = getArguments().getParcelableArrayList(CONTENT_PARAM);
            mPosition = getArguments().getInt(POSITION_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_person_circle_detail_pic, container, false);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cheese_1);
        SchoolFriendTextView textView = (SchoolFriendTextView) view.findViewById(R.id.fragment_person_circle_detail_pic_content_tv);
        textView.setText(mContents.get(0).getContent());
        initViewPager(view);
        return view;
    }

    private void initViewPager(View view) {
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.fragment_person_circle_detail_pic_viewpager);

    }
}
