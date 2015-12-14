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
import android.widget.Toast;

import com.nju.View.CustomImageVIew;
import com.nju.activity.R;
import com.nju.util.Constant;
import com.nju.util.Divice;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

public class OriginPicsViewFragment extends BaseFragment {

    public static final String TAG = OriginPicsViewFragment.class.getSimpleName();
    private static final String IMG_PATH = "img_path";
    private String mImgPath;
    private CustomImageVIew mImageView;
    private boolean label = true;
    private static final String LABEL ="label";
    private SharedPreferences mPreferences;
    private static CameraImageViewFragment mCameraImageViewFragment;
    public static OriginPicsViewFragment newInstance(String imgPath) {
        OriginPicsViewFragment  fragment = new OriginPicsViewFragment();
        Bundle args = new Bundle();
        args.putString(IMG_PATH,imgPath);
        fragment.setArguments(args);
        return fragment;
    }

    public OriginPicsViewFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mImgPath = getArguments().getString(IMG_PATH);
        }
        if (mCameraImageViewFragment == null) {
            mCameraImageViewFragment = (CameraImageViewFragment) getHostActivity().getBackStack().peek();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_origin_pics_view, container, false);
        mImageView = (CustomImageVIew) view.findViewById(R.id.fragment_origin_pics_view_imageView);
        new LoadLocalImg().execute(mImgPath);
        imageViewClickEvent();
        mPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        return view;
    }

    private void imageViewClickEvent(){
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPreferences.getBoolean(LABEL,true)) {
                    Divice.hideStatusBar((AppCompatActivity) getActivity());
                    mPreferences.edit().putBoolean(LABEL, false).apply();
                    mCameraImageViewFragment.hideBottomLayout();
                } else {
                    Divice.showStatusBar((AppCompatActivity) getActivity());
                    mCameraImageViewFragment.showBottomLayout();
                    mPreferences.edit().putBoolean(LABEL,true).apply();
                }
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        if (mPreferences.getBoolean(LABEL,true)) {
            Divice.showStatusBar((AppCompatActivity) getActivity());
            mCameraImageViewFragment.showBottomLayout();
        } else{
            Divice.hideStatusBar((AppCompatActivity) getActivity());
            mCameraImageViewFragment.hideBottomLayout();
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        Divice.showStatusBar((AppCompatActivity) getActivity());
    }

    private class LoadLocalImg extends AsyncTask<String,Void,Bitmap>{
        private int width = Divice.getDisplayWidth(getActivity());

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap = null;
            try {
               bitmap = Picasso.with(getActivity()).load(new File(params[0])).resize(width,width).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            mImageView.setImageBitmap(bitmap);
            mImageView.invalidate();
        }
    }

}
