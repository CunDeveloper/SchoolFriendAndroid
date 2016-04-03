package com.nju.fragment;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.nju.View.SchoolFriendDialog;
import com.nju.activity.R;
import com.nju.http.HttpManager;
import com.nju.http.ResponseCallback;
import com.nju.http.request.MultiImgRequest;
import com.nju.model.BitmapWrapper;
import com.nju.model.ImageWrapper;
import com.nju.test.TestData;
import com.nju.util.Constant;
import com.nju.util.Divice;
import com.nju.util.InputEmotionUtil;
import com.nju.util.PathConstant;
import com.nju.util.SoftInput;
import com.nju.util.StringBase64;
import com.nju.util.SyncChoosePublish;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class AskPublishFragment extends BaseFragment {

    public static final String TAG = AskPublishFragment.class.getSimpleName();
    private static final String PARAM_TITLE = "paramTitle";
    private static final String PARAM_UPLOAD_IMAGES = "paramUploadImage";
    private ArrayList<ImageWrapper> mUploadImgPaths;
    private static String mTitle;
    private EditText mDescriptionET;
    private EditText mProblemET;
    private SchoolFriendDialog mDialog;
    public static AskPublishFragment newInstance(String title,ArrayList<ImageWrapper> uploadImgPaths) {
        AskPublishFragment fragment = new AskPublishFragment();
        Bundle args = new Bundle();
        args.putString(PARAM_TITLE,title);
        args.putParcelableArrayList(PARAM_UPLOAD_IMAGES, uploadImgPaths);
        fragment.setArguments(args);
        return fragment;
    }

    public AskPublishFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTitle = getArguments().getString(PARAM_TITLE);
            if (getArguments().getParcelableArrayList(PARAM_UPLOAD_IMAGES) != null){
                mUploadImgPaths = getArguments().getParcelableArrayList(PARAM_UPLOAD_IMAGES);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ask_publish, container, false);
        view.setPadding(view.getPaddingLeft(), Divice.getStatusBarHeight(getContext()), view.getPaddingRight(), view.getPaddingBottom());
        InputEmotionUtil.initView(this, view, TAG);
        InputEmotionUtil.addViewPageEvent(getContext(), view);
        if (mUploadImgPaths!=null&&mUploadImgPaths.size()>0){
            view.findViewById(R.id.add_pic).setVisibility(View.GONE);
            InputEmotionUtil.setUpGridView(this, view, mUploadImgPaths);
        }else {
            mUploadImgPaths = new ArrayList<>();
        }
        SyncChoosePublish.sync(this, view);
        initView(view);
        return view;
    }

    private void initView(View view){
        final int[] contentWordCount = {0};
        final int[] titleWordCount = {0};
        final Button sendBn = getHostActivity().getMenuBn();
        mDescriptionET = (EditText) view.findViewById(R.id.content_editText);
        mProblemET = (EditText) view.findViewById(R.id.title);
        mDescriptionET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                contentWordCount[0] = count;
                if (contentWordCount[0] >= 1 && titleWordCount[0] >= 1) {
                    sendBn.setAlpha(1);
                    sendBn.setEnabled(true);
                } else {
                    sendBn.setAlpha(0.7f);
                    sendBn.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mProblemET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                titleWordCount[0] = count;
                if (contentWordCount[0] >= 1 && titleWordCount[0] >= 1) {
                    sendBn.setAlpha(1);
                    sendBn.setEnabled(true);
                } else {
                    sendBn.setAlpha(0.7f);
                    sendBn.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        Spinner completeTextView = (Spinner) view.findViewById(R.id.typeSpinner);
        String[] majorTypes= TestData.getMajorType();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_dropdown_item_1line, majorTypes);
        completeTextView.setAdapter(adapter);
    }

    public void inputEmotion(String text) {
        String label = InputEmotionUtil.getLabel();
        if (label != null){
            if (label.equals(getString(R.string.content))){
                int selectionCursor = mDescriptionET.getSelectionStart();
                mDescriptionET.getText().insert(selectionCursor, text);
                mDescriptionET.invalidate();
            }else if (label.equals(getString(R.string.title))){
                int selectionCursor = mProblemET.getSelectionStart();
                mProblemET.getText().insert(selectionCursor, text);
                mProblemET.invalidate();
            }
        }
    }

    public  void setImages(ArrayList<ImageWrapper> images){
        mUploadImgPaths = images;
    }

    ResponseCallback callback = new ResponseCallback() {
        @Override
        public void onFail(Exception error) {
            Log.e(TAG, error.getLocalizedMessage());
            mDialog.dismiss();
        }

        @Override
        public void onSuccess(String responseBody) {
            Log.i(TAG,responseBody);
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
        final Button sendBn = getHostActivity().getMenuBn();
        sendBn.setText(getString(R.string.publish));
        sendBn.setAlpha(0.7f);
        sendBn.setEnabled(false);
        sendBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoftInput.close(getContext(), sendBn);
                InputEmotionUtil.getEmoLayout().setVisibility(View.GONE);
                mDialog = SchoolFriendDialog.showProgressDialogNoTitle(getContext(), getString(R.string.uploading));
                mDialog.show();
                final String description = mDescriptionET.getText().toString();
                final String  problem = mProblemET.getText().toString();
                final String whoScan = 1+"";
                final HashMap<String,String> params = new HashMap<>();
                params.put(Constant.DESCRIPTION, StringBase64.encode(description));
                params.put(Constant.PROBLEM,StringBase64.encode(problem));
                params.put(Constant.WHO_SCAN,whoScan);
                params.put(Constant.AUTHORIZATION, AskPublishFragment.this.getHostActivity().token());
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
                final String url = PathConstant.BASE_URL+PathConstant.ALUMNIS_QUESTION_PATH + PathConstant.ALUMNIS_QUESTION_SUB_PATH_SAVE+"?level=所有";
                HttpManager.getInstance().exeRequest(new MultiImgRequest(url,params,bitmapWrapperArrayList,callback));
            }
        });
     }

}
