package com.nju.fragment;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nju.activity.R;
import com.nju.adatper.CircleImageViewAdapter;
import com.nju.event.BitmapEvent;
import com.nju.model.AlumniTalk;
import com.nju.model.AlumnicTalkPraise;
import com.nju.util.Constant;
import com.nju.util.PathConstant;
import com.nju.util.StringBase64;
import com.nju.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;


public class PersonCircleDetailPicFragment extends BaseFragment {

    private static final String TAG = PersonCircleDetailPicFragment.class.getSimpleName();
    private static final String TALK_PARAM = "talk_param";
    private static final String POSITION_PARAM = "position";
    private AlumniTalk  mTalk;
    private int mPosition;
    private boolean mIsPraise = false;
    private CircleImageViewAdapter mCircleImageViewAdapter;

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
        initView(view);
        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop(){
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onMessageBitmapEvent(BitmapEvent event) {
        mCircleImageViewAdapter.notifyDataSetChanged();
    }



    private void initView(View view) {
        int userId = getHostActivity().userId();
        ArrayList<AlumnicTalkPraise> praises = mTalk.getTalkPraises();
        for (AlumnicTalkPraise praise:praises){
            if (userId == praise.getPraiseAuthor().getAuthorId()){
                mIsPraise = true;
                break;
            }
        }
        TextView praiseIconTV = (TextView) view.findViewById(R.id.praiseIconTV);
        if (mIsPraise){
            praiseIconTV.setTextColor(ContextCompat.getColor(getContext(),android.R.color.holo_red_dark ));
        }
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.fragment_person_circle_detail_pic_viewpager);
        TextView  contentTV = (TextView) view.findViewById(R.id.fragment_person_circle_detail_pic_content_tv);
        if (mTalk != null){
            try{
                contentTV.setText(StringBase64.decode(mTalk.getContent()));
            }catch (IllegalArgumentException e){
                contentTV.setText(Constant.UNKNOWN_CHARACTER);
            }
        }
        assert mTalk != null;
        mCircleImageViewAdapter = new CircleImageViewAdapter(getFragmentManager(),mTalk.getImagePaths().split(","), PathConstant.ALUMNI_TALK_IMG_PATH);
        viewPager.setAdapter(mCircleImageViewAdapter);

        LinearLayout detailLayout = (LinearLayout) view.findViewById(R.id.displayDetailLayout);
        detailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHostActivity().open(PersonCircleDetailFragment.newInstance(mTalk));
            }
        });

        LinearLayout praiseLayout = (LinearLayout) view.findViewById(R.id.praiseTv);
        praiseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showShortText(getContext(),"praise");
            }
        });

        LinearLayout commentLayout = (LinearLayout) view.findViewById(R.id.commentTv);
        commentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showShortText(getContext(), "COMMENT");
            }
        });

        TextView commentCountTV = (TextView) view.findViewById(R.id.comment_number_tv);
        commentCountTV.setText(mTalk.getCommentCount()+"");
        TextView praiseCountTV = (TextView) view.findViewById(R.id.praise_number_tv);
        praiseCountTV.setText(mTalk.getPraiseCount()+"");
    }
}
