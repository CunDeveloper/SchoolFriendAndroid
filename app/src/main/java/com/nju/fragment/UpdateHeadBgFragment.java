package com.nju.fragment;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.nju.View.CustomImageVIew;
import com.nju.View.SchoolFriendDialog;
import com.nju.activity.R;
import com.nju.http.HttpManager;
import com.nju.http.ResponseCallback;
import com.nju.http.request.MultiImgRequest;
import com.nju.http.response.ParseResponse;
import com.nju.model.BitmapWrapper;
import com.nju.model.ImageWrapper;
import com.nju.util.Constant;
import com.nju.util.Divice;
import com.nju.util.FragmentUtil;
import com.nju.util.PathConstant;
import com.nju.util.ToastUtil;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class UpdateHeadBgFragment extends BaseFragment {

    private static final String TAG = UpdateHeadBgFragment.class.getSimpleName();
    private static final String IMG_PATH = "imgPath";
    private static final String LABEL = "label";
    private CharSequence mImgPath;
    private CustomImageVIew mImageView;
    private CharSequence mLabel;
    private SchoolFriendDialog mDialog;


    public static UpdateHeadBgFragment newInstance(String imgPath,String label) {
        UpdateHeadBgFragment fragment = new UpdateHeadBgFragment();
        Bundle args = new Bundle();
        args.putString(IMG_PATH,imgPath);
        args.putString(LABEL,label);
        fragment.setArguments(args);
        return fragment;
    }

    public UpdateHeadBgFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mImgPath = getArguments().getString(IMG_PATH);
            mLabel = getArguments().getString(LABEL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.upload_head_bg_img, container, false);
        view.setPadding(view.getPaddingLeft(), Divice.getStatusBarHeight(getContext()),view.getPaddingRight(),view.getPaddingBottom());
        mImageView = (CustomImageVIew) view.findViewById(R.id.mImageView);
        new LoadLocalImg(this).execute(mImgPath.toString());
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getString(R.string.replace_img));
        }
        getHostActivity().display(0);

        Button button = getHostActivity().getMenuBn();
        button.setText(getString(R.string.use));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog = SchoolFriendDialog.showProgressDialogNoTitle(getContext(), getString(R.string.uploading));
                mDialog.show();
                final HashMap<String,String> params = new HashMap<>();
                params.put(Constant.AUTHORIZATION,getHostActivity().token());
                Log.i(TAG,getHostActivity().token());
                final ArrayList<BitmapWrapper> bitmapWrappers = new ArrayList<>();
                BitmapWrapper bitmapWrapper;
                File sourceFile;
                final String path = mImgPath.toString();
                bitmapWrapper = new BitmapWrapper();
                sourceFile = new File(path);
                bitmapWrapper.setPath(path);bitmapWrapper.setFileName(sourceFile.getName());
                try {
                    bitmapWrapper.setFileType(sourceFile.toURL().openConnection().getContentType());
                    bitmapWrappers.add(bitmapWrapper);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ArrayList<BitmapWrapper> bitmapWrapperArrayList = HttpManager.getInstance().compressBitmap(getContext(),bitmapWrappers);
                CharSequence url ="";
                if (mLabel.equals(getString(R.string.update_head))){
                    url = PathConstant.BASE_URL+PathConstant.AUTHOR_PATH + PathConstant.AUTHOR_SUB_PATH_UPDATE_HEAD_ICON;
                }else if (mLabel.equals(getString(R.string.update_bg))){
                    url = PathConstant.BASE_URL+PathConstant.AUTHOR_PATH + PathConstant.AUTHOR_SUB_PATH_UPDATE_BG_ICON;
                }
                Log.i(TAG, url.toString());
                HttpManager.getInstance().exeRequest(new MultiImgRequest(url.toString(), params, bitmapWrapperArrayList, new ResponseCallback() {
                    @Override
                    public void onFail(Exception error) {
                        Log.e(TAG,error.getMessage());
                        mDialog.dismiss();
                    }

                    @Override
                    public void onSuccess(String responseBody) {
                        Log.i(TAG,responseBody);
                        mDialog.dismiss();
                        if (FragmentUtil.isAttachedToActivity(UpdateHeadBgFragment.this)) {
                            ParseResponse parseResponse = new ParseResponse();
                            try {
                                String str = parseResponse.getInfo(responseBody);
                                if (str != null && str.equals(Constant.OK_MSG)) {
                                    ToastUtil.showShortText(getContext(),getString(R.string.update_ok));
                                    Stack<BaseFragment> fragments = getHostActivity().getBackStack();
                                    if (fragments.size()>=1){
                                        getHostActivity().open(fragments.peek());
                                    }
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }));
            }
        });
    }

    private static class LoadLocalImg extends AsyncTask<String,Void,Bitmap> {

        private final WeakReference<UpdateHeadBgFragment> weakReference;
        private int mTargetWidth,mTargetHeight;

        public LoadLocalImg(UpdateHeadBgFragment fragment){
            weakReference = new WeakReference<>(fragment);
            int diviceWidth = Divice.getDisplayWidth(fragment.getContext());
            mTargetWidth = (int) (diviceWidth-Divice.convertDpToPixel(40, fragment.getContext()));
            mTargetHeight = (int) Divice.convertDpToPixel(300,fragment.getContext());
            Log.i(TAG,"targetWidth="+mTargetWidth+"targetHeight="+mTargetHeight);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            UpdateHeadBgFragment fragment = weakReference.get();
            Bitmap bitmap = null;
            if (fragment != null){
                try {
                    bitmap = Picasso.with(fragment.getContext()).load(new File(params[0]))
                            .resize(mTargetWidth,mTargetHeight).get();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return bitmap;
            } else {
                return bitmap;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
             UpdateHeadBgFragment fragment = weakReference.get();
            if (fragment != null){
                if (bitmap != null){
                    fragment.mImageView.setImageBitmap(bitmap);
                }
            }

        }
    }

}
