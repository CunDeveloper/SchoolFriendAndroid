package com.nju.fragment;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nju.activity.R;
import com.nju.http.ImageDownloader;
import com.nju.model.AlumniDynamicCollect;
import com.nju.model.DynamicCollect;
import com.nju.util.Constant;
import com.nju.util.Divice;
import com.nju.util.PathConstant;
import com.nju.util.StringBase64;


public class DynamicCollectDetailFragment extends BaseFragment {
    private static final String PARAM_KEY = "paramKey";
    private AlumniDynamicCollect mDynamicCollect;

    public DynamicCollectDetailFragment() {
        // Required empty public constructor
    }

    public static DynamicCollectDetailFragment newInstance(AlumniDynamicCollect dynamicCollect) {
        DynamicCollectDetailFragment fragment = new DynamicCollectDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(PARAM_KEY, dynamicCollect);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mDynamicCollect = getArguments().getParcelable(PARAM_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dynamic_collect_detail, container, false);
        view.setPadding(view.getPaddingLeft(), Divice.getStatusBarHeight(getContext()), view.getPaddingRight(), view.getPaddingBottom());
        initView(view);
        return view;
    }

    private void initView(View view) {
        TextView nameTV = (TextView) view.findViewById(R.id.name_tv);
        //nameTV.setText(mDynamicCollect.getAuthorInfo().getAuthorName());
        TextView labelTV = (TextView) view.findViewById(R.id.label_tv);
        //labelTV.setText(mDynamicCollect.getAuthorInfo().getLabel());
        TextView contentTV = (TextView) view.findViewById(R.id.content_tv);
        ImageView headImg = (ImageView) view.findViewById(R.id.head_icon);
        String headUrl = getHostActivity().getSharedPreferences().getString(getString(R.string.head_url),"");
        ImageDownloader.with(getContext()).download(headUrl,headImg);
        ImageView contentImg = (ImageView) view.findViewById(R.id.collect_img);
        TextView dateTV = (TextView) view.findViewById(R.id.date_tv);
        dateTV.setText(getString(R.string.collect_with) + " " + mDynamicCollect.getDate());
        if (mDynamicCollect.getText() != null) {
            try {
                contentTV.setText(StringBase64.decode(mDynamicCollect.getText()));
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                contentTV.setText(Constant.UNKNOWN_CHARACTER);
            }

        } else {
            String url = PathConstant.IMAGE_PATH + PathConstant.ALUMNI_TALK_IMG_PATH + mDynamicCollect.getImagePath();
            ImageDownloader.with(getContext()).download(url,contentImg);
        }
    }
}
