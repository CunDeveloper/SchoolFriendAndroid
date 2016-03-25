package com.nju.fragment;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nju.View.SchoolFriendDialog;
import com.nju.activity.R;
import com.nju.http.HttpManager;
import com.nju.http.ResponseCallback;
import com.nju.http.request.MultiImgRequest;
import com.nju.model.BitmapWrapper;
import com.nju.model.ImageWrapper;
import com.nju.util.Constant;
import com.nju.util.Divice;
import com.nju.util.InputEmotionUtil;
import com.nju.util.PathConstant;
import com.nju.util.SoftInput;
import com.nju.util.StringBase64;
import com.nju.util.ToastUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class PublishDynamicFragment extends BaseFragment {
    public static final String TAG = PublishDynamicFragment.class.getSimpleName();
    private static final String PARAM_TITLE = "paramTitle";
    private static final String PARAM_UPLOAD_IMAGES = "paramUploadImage";
    private ArrayList<ImageWrapper> mUploadImgPaths;
    private String mTitle;
    private EditText mContentET;
    private TextView mLocationTV;
    private TextView whoScanTV;
    private SchoolFriendDialog mDialog;
    private String mLocation = null;
    private String mWhoScan = null;

    public static PublishDynamicFragment newInstance(String title,ArrayList<ImageWrapper> uploadImgPaths) {
        PublishDynamicFragment fragment = new PublishDynamicFragment();
        Bundle args = new Bundle();
        args.putString(PARAM_TITLE, title);
        args.putParcelableArrayList(PARAM_UPLOAD_IMAGES, uploadImgPaths);
        fragment.setArguments(args);
        return fragment;
    }

    public PublishDynamicFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTitle = getArguments().getString(PARAM_TITLE);
            ToastUtil.showShortText(getContext(),"size=");
            if (getArguments().getParcelableArrayList(PARAM_UPLOAD_IMAGES) != null){
                mUploadImgPaths = getArguments().getParcelableArrayList(PARAM_UPLOAD_IMAGES);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_publish_dynamic, container, false);
        view.setPadding(view.getPaddingLeft(), Divice.getStatusBarHeight(getContext()), view.getPaddingRight(), view.getPaddingBottom());
        InputEmotionUtil.initView(this, view, TAG);
        InputEmotionUtil.addViewPageEvent(getContext(), view);
        initView(view);

        if (mUploadImgPaths!=null&&mUploadImgPaths.size()>0){
            view.findViewById(R.id.add_pic).setVisibility(View.GONE);
            InputEmotionUtil.setUpGridView(this, view, mUploadImgPaths);
        }else {
            mUploadImgPaths = new ArrayList<>();
        }

        return view;
    }

    private void initView(View view){
        mLocationTV = (TextView) view.findViewById(R.id.location_tv);
        if (mLocation != null){
            mLocationTV.setText(mLocation);mLocationTV.invalidate();
        }
        mLocationTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHostActivity().open(UserLocationFragment.newInstance());
            }
        });
        RelativeLayout whoScanRelayout = (RelativeLayout) view.findViewById(R.id.who_can_relativeLayout);
        whoScanRelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHostActivity().open(WhoScanFragment.newInstance());
            }
        });
        whoScanTV = (TextView) view.findViewById(R.id.whoScanTV);
        if (mWhoScan != null){
            whoScanTV.setText(mWhoScan);
        }
        mContentET = (EditText) view.findViewById(R.id.content_editText);
        mContentET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Button button = getHostActivity().getMenuBn();
                if (count>=1){
                    button.setEnabled(true);
                    button.setAlpha(1);
                }else {
                    button.setEnabled(false);
                    button.setAlpha(0.8f);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void setLocation(String text){
        mLocation = text;
    }

    public void setWhoScan(String text){
        mWhoScan = text;
    }

    public  void setImages(ArrayList<ImageWrapper> images){
        mUploadImgPaths = images;
    }

    public void inputEmotion(String text) {
        String label = InputEmotionUtil.getLabel();
        if (label != null){
            if (label.equals(getString(R.string.content))){
                int selectionCursor = mContentET.getSelectionStart();
                mContentET.getText().insert(selectionCursor, text);
                mContentET.invalidate();
            }
        }
    }

    ResponseCallback callback = new ResponseCallback() {
        @Override
        public void onFail(Exception error) {
            mDialog.dismiss();
        }

        @Override
        public void onSuccess(String responseBody) {
            mDialog.dismiss();
        }
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(mTitle);
        }
        getHostActivity().display(0);
        final Button sendBn =  getHostActivity().getMenuBn();
        sendBn.setEnabled(false);sendBn.setAlpha(0.8f);
        sendBn.setText(getString(R.string.publish));
        sendBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoftInput.close(getContext(), sendBn);
                InputEmotionUtil.getEmoLayout().setVisibility(View.GONE);
                mDialog = SchoolFriendDialog.showProgressDialogNoTitle(getContext(), getString(R.string.uploading));
                mDialog.show();
                final String content = mContentET.getText().toString();
                final String whoScan = 1+"";
                final HashMap<String,String> params = new HashMap<>();
                params.put(Constant.CONTENT, StringBase64.encode(content));
                params.put(Constant.WHO_SCAN,whoScan);
                params.put(Constant.LOCATION,mLocation);
                params.put(Constant.AUTHORIZATION, PublishDynamicFragment.this.getHostActivity().token());
                final ArrayList<BitmapWrapper> bitmapWrappers = new ArrayList<>();
                BitmapWrapper bitmapWrapper;
                File sourceFile;
                for (ImageWrapper image :mUploadImgPaths) {
                    final String path = image.getPath();
                    bitmapWrapper = new BitmapWrapper();
                    sourceFile = new File(path);
                    bitmapWrapper.setPath(path);bitmapWrapper.setFileName(sourceFile.getName());
                    try {
                        bitmapWrapper.setFileType(sourceFile.toURL().openConnection().getContentType());
                        bitmapWrappers.add(bitmapWrapper);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                ArrayList<BitmapWrapper> bitmapWrapperArrayList = HttpManager.getInstance().compressBitmap(getContext(),bitmapWrappers);
                final String url = PathConstant.BASE_URL+PathConstant.ALUMNI_TALK_PATH + PathConstant.ALUMNI_TALK_SUB_PATH_SAVE+"?level=所有";
                HttpManager.getInstance().exeRequest(new MultiImgRequest(url,params,bitmapWrapperArrayList,callback));
            }
        });
    }


}
