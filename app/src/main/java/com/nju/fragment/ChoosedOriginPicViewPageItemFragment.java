package com.nju.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nju.View.CustomImageVIew;
import com.nju.activity.R;
import com.nju.util.Divice;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

public class ChoosedOriginPicViewPageItemFragment extends BaseFragment {

    public static final String TAG = ChoosedOriginPicViewPageItemFragment.class.getSimpleName();
    private static final String PARAM = "param";
    private static final String LABEL = "label";
    private static SharedPreferences mPreference;
    private String mImgPath;
    private CustomImageVIew mImgView;

    public ChoosedOriginPicViewPageItemFragment() {

    }

    public static ChoosedOriginPicViewPageItemFragment newInstance(String imgPath) {
        ChoosedOriginPicViewPageItemFragment fragment = new ChoosedOriginPicViewPageItemFragment();
        Bundle args = new Bundle();
        args.putString(PARAM, imgPath);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mImgPath = getArguments().getString(PARAM);
        }
        if (mPreference == null) {
            mPreference = getActivity().getPreferences(Context.MODE_PRIVATE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choosed_origin_pic_view_page_item, container, false);
        mImgView = (CustomImageVIew) view.findViewById(R.id.fragment_choosed_origin_pic_view_page_item_imageView);
        new LoadLocalImg().execute(mImgPath);
        initImageViewClickEvent();
        return view;
    }

    private void initImageViewClickEvent() {
        mImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPreference.getBoolean(LABEL, true)) {
                    Divice.hideStatusBar((AppCompatActivity) getActivity());
                    mPreference.edit().putBoolean(LABEL, false).apply();
                } else {
                    Divice.showStatusBar((AppCompatActivity) getActivity());
                    mPreference.edit().putBoolean(LABEL, true).apply();
                }
            }
        });
    }

    private class LoadLocalImg extends AsyncTask<String, Void, Bitmap> {

        private int mTargetHeight = Divice.getDisplayWidth(getActivity());

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap = null;
            try {
                bitmap = Picasso.with(getActivity()).load(new File(params[0])).
                        resize(mTargetHeight, mTargetHeight).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            mImgView.setImageBitmap(bitmap);
            mImgView.invalidate();
        }
    }
}
