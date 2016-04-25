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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.nju.View.SchoolFriendDialog;
import com.nju.activity.MessageEvent;
import com.nju.activity.R;
import com.nju.adatper.CollageAdapter;
import com.nju.http.HttpManager;
import com.nju.http.ResponseCallback;
import com.nju.http.request.MultiImgRequest;
import com.nju.http.response.ParseResponse;
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
import com.nju.util.ToastUtil;
import com.nju.util.WhoScanUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class PublishVoiceFragment extends BaseFragment {
    public static final String TAG = PublishVoiceFragment.class.getSimpleName();
    private static final String PARAM_TITLE = "paramTitle";
    private static final String PARAM_UPLOAD_IMAGES = "paramUploadImage";
    private ArrayList<ImageWrapper> mUploadImgPaths;
    private static CharSequence mTitle;
    private EditText mTitleET;
    private EditText mContentET;
    private TextView mWhoScanTV;
    private SchoolFriendDialog mDialog;
    private CharSequence mWhoScan;
    private SyncChoosePublish mChoosePublish;
    private CharSequence mVoiceLabel;
    private Spinner mLabelSpinner;
    public static PublishVoiceFragment newInstance(String title,ArrayList<ImageWrapper> uploadImgPaths) {
        PublishVoiceFragment fragment = new PublishVoiceFragment();
        Bundle args = new Bundle();
        args.putString(PARAM_TITLE,title);
        args.putParcelableArrayList(PARAM_UPLOAD_IMAGES, uploadImgPaths);
        fragment.setArguments(args);
        return fragment;
    }

    public PublishVoiceFragment() {
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
        View view =  inflater.inflate(R.layout.fragment_publish_voice, container, false);
        view.setPadding(view.getPaddingLeft(), Divice.getStatusBarHeight(getContext()), view.getPaddingRight(), view.getPaddingBottom());
        InputEmotionUtil.initView(this, view, TAG);
        InputEmotionUtil.addViewPageEvent(getContext(), view);
        mChoosePublish = SyncChoosePublish.newInstance(view).sync(this);
        if (mUploadImgPaths!=null&&mUploadImgPaths.size()>0){
            view.findViewById(R.id.add_pic).setVisibility(View.GONE);
            InputEmotionUtil.setUpGridView(this, view, mUploadImgPaths);
        }else {
            mUploadImgPaths = new ArrayList<>();
        }
        initView(view);
        mVoiceLabel = null;
        return view;
    }

    public void inputEmotion(String text) {
        String label = InputEmotionUtil.getLabel();
        if (label != null){
            if (label.equals(getString(R.string.content))){
                int selectionCursor = mContentET.getSelectionStart();
                mContentET.getText().insert(selectionCursor, text);
                mContentET.invalidate();
            }else if (label.equals(getString(R.string.title))){
                int selectionCursor = mTitleET.getSelectionStart();
                mTitleET.getText().insert(selectionCursor, text);
                mTitleET.invalidate();
            }
        }
    }

    public void setWhoScan(String text){
        mWhoScan = text;
    }

    public  void setImages(ArrayList<ImageWrapper> images){
        mUploadImgPaths = images;
    }

    private void initView(View view){
        mContentET = (EditText) view.findViewById(R.id.content_editText);
        mContentET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Button sendBn = getHostActivity().getMenuBn();
                if (count>=1){
                    sendBn.setEnabled(true);
                    sendBn.setAlpha(1);
                }else {
                    sendBn.setEnabled(false);
                    sendBn.setAlpha(0.7f);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mTitleET = (EditText) view.findViewById(R.id.title);
        RelativeLayout whoScanRelative = (RelativeLayout) view.findViewById(R.id.who_scan_relativeLayout);
        whoScanRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHostActivity().open(WhoScanFragment.newInstance());
            }
        });
        mWhoScanTV = (TextView) view.findViewById(R.id.whoScanTV);
        if (mWhoScan != null){
            mWhoScanTV.setText(mWhoScan);
        }

        mLabelSpinner = (Spinner) view.findViewById(R.id.voiceTypeACT);
        Set<String> voiceSet = getHostActivity().getSharedPreferences()
                .getStringSet(Constant.VOICE_LABEL,new HashSet<String>());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_dropdown_item_1line, voiceSet.toArray(new String[voiceSet.size()]));
        mLabelSpinner.setAdapter(adapter);
        TextView textView = (TextView) view.findViewById(R.id.edit_label_tv);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SchoolFriendDialog.inputDialog(getContext(),Constant.EDIT,Constant.PLEASE_LABEL_CONTENT,null).show();
            }
        });
    }

    ResponseCallback callback = new ResponseCallback() {
        @Override
        public void onFail(Exception error) {
            Log.e(TAG,error.getLocalizedMessage());
            mDialog.dismiss();
        }

        @Override
        public void onSuccess(String responseBody) {
            Log.i(TAG,responseBody);
            mDialog.dismiss();
            ParseResponse parseResponse = new ParseResponse();
            try {
                String info = parseResponse.getInfo(responseBody);
                Log.i(TAG,info);
                if (info != null && info.equals(Constant.OK_MSG)) {
                    ToastUtil.showShortText(getContext(), Constant.PUBLISH_OK);
                    getHostActivity().open(AlumniVoiceFragment.newInstance(),PublishVoiceFragment.this);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };


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
    public void onMessageLabel(MessageEvent event){
        Log.i(TAG,event.getMessage());
        mVoiceLabel = event.getMessage();
    }

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
        final Button button = getHostActivity().getMenuBn();
        button.setText(getString(R.string.publish));
        button.setEnabled(false);button.setAlpha(0.7f);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoftInput.close(getContext(), button);
                InputEmotionUtil.getEmoLayout().setVisibility(View.GONE);
                mDialog = SchoolFriendDialog.showProgressDialogNoTitle(getContext(), getString(R.string.uploading));
                mDialog.show();
                final String content = mContentET.getText().toString();
                final String title = mTitleET.getText().toString();
                final String whoScan = WhoScanUtil.access(mWhoScanTV.getText().toString());
                final CharSequence label;
                if (mVoiceLabel == null){
                    label = mLabelSpinner.getSelectedItem().toString();
                }else {
                    label = mVoiceLabel;
                }
                Log.i(TAG,label.toString());
                Log.i(TAG,whoScan);
                final HashMap<String,String> params = new HashMap<>();
                params.put(Constant.CONTENT, StringBase64.encode(content));
                params.put(Constant.TITLE,StringBase64.encode(title));
                params.put(Constant.WHO_SCAN,whoScan);
                params.put(Constant.A_LABEL,label.toString());
                params.put(Constant.AUTHORIZATION,PublishVoiceFragment.this.getHostActivity().token());
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
                final String url = PathConstant.BASE_URL+PathConstant.ALUMNS_VOICE_PATH + PathConstant.ALUMNS_VOICE_SUB_PATH_SAVE+"?level="+mChoosePublish.level();
                Log.i(TAG,url);
                HttpManager.getInstance().exeRequest(new MultiImgRequest(url,params,bitmapWrapperArrayList,callback));
            }
        });
    }
}
