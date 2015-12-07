package com.nju.fragment;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nju.View.CustomImageVIew;
import com.nju.activity.R;
import com.nju.util.Divice;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

public class OriginPicsViewFragment extends Fragment {

    private static final String IMG_PATH = "img_path";
    private String mImgPath;
    private CustomImageVIew mImageView;
    public static OriginPicsViewFragment newInstance(String imgPath) {

        OriginPicsViewFragment fragment = new OriginPicsViewFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_origin_pics_view, container, false);
        mImageView = (CustomImageVIew) view.findViewById(R.id.fragment_origin_pics_view_imageView);
        new LoadLocalImg().execute(mImgPath);
        return view;
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
