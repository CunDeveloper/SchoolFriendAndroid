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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

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
import com.nju.util.InputEmotionUtil;
import com.nju.util.PathConstant;
import com.nju.util.SoftInput;
import com.nju.util.StringBase64;
import com.nju.util.SyncChoosePublish;
import com.nju.util.ToastUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class RecommendPublishFragment extends BaseFragment {

    public static final String TAG = RecommendPublishFragment.class.getSimpleName();
    private static final String PARAM_TITLE = "paramTitle";
    private static final String PARAM_UPLOAD_IMAGES = "paramUploadImage";
    private static CharSequence mTitle;
    private ArrayList<ImageWrapper> mUploadImgPaths;
    private EditText mTitleET;
    private EditText mContentET;
    private EditText mEmailET;
    private CharSequence mType = 1 + "";
    private SchoolFriendDialog mDialog;
    ResponseCallback callback = new ResponseCallback() {
        @Override
        public void onFail(Exception error) {
            Log.e(TAG, error.getLocalizedMessage());
            mDialog.dismiss();
            ToastUtil.showShortText(getContext(), Constant.PUBLISH_ERREOR);
        }

        @Override
        public void onSuccess(String responseBody) {
            Log.i(TAG, responseBody);
            mDialog.dismiss();
            ParseResponse parseResponse = new ParseResponse();
            try {
                String info = parseResponse.getInfo(responseBody);
                Log.i(TAG, info);
                if (info != null && info.equals(Constant.OK_MSG)) {
                    ToastUtil.showShortText(getContext(), Constant.PUBLISH_OK);
                    getHostActivity().open(RecommendWorkFragment.newInstance(), RecommendPublishFragment.this);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    };
    private SyncChoosePublish choosePublish;

    public RecommendPublishFragment() {
        // Required empty public constructor
    }

    public static RecommendPublishFragment newInstance(String title, ArrayList<ImageWrapper> uploadImgPaths) {
        RecommendPublishFragment fragment = new RecommendPublishFragment();
        Bundle args = new Bundle();
        args.putString(PARAM_TITLE, title);
        args.putParcelableArrayList(PARAM_UPLOAD_IMAGES, uploadImgPaths);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTitle = getArguments().getString(PARAM_TITLE);
            if (getArguments().getParcelableArrayList(PARAM_UPLOAD_IMAGES) != null) {
                mUploadImgPaths = getArguments().getParcelableArrayList(PARAM_UPLOAD_IMAGES);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recommend_publish, container, false);
        view.setPadding(view.getPaddingLeft(), Divice.getStatusBarHeight(getContext()), view.getPaddingRight(), view.getPaddingBottom());
        InputEmotionUtil.initView(this, view, TAG);
        InputEmotionUtil.addViewPageEvent(getContext(), view);
        if (mUploadImgPaths != null && mUploadImgPaths.size() > 0) {
            view.findViewById(R.id.add_pic).setVisibility(View.GONE);
            InputEmotionUtil.setUpGridView(this, view, mUploadImgPaths);
        } else {
            mUploadImgPaths = new ArrayList<>();
        }
        choosePublish = SyncChoosePublish.newInstance(view).sync(this);
        initView(view);
        initCheckBox(view);
        return view;
    }

    private void initView(View view) {
        final int[] contentWordCount = {0};
        final int[] titleWordCount = {0};
        final int[] emailWordCount = {0};
        final Button sendBn = getHostActivity().getMenuBn();
        mContentET = (EditText) view.findViewById(R.id.content_editText);
        mTitleET = (EditText) view.findViewById(R.id.title);
        mContentET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                contentWordCount[0] = count;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (contentWordCount[0] >= 1 && titleWordCount[0] >= 1 && emailWordCount[0] >= 1) {
                    sendBn.setAlpha(1);
                    sendBn.setEnabled(true);
                } else {
                    sendBn.setAlpha(0.7f);
                    sendBn.setEnabled(false);
                }
                contentWordCount[0] = count;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mTitleET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                titleWordCount[0] = count;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (contentWordCount[0] >= 1 && titleWordCount[0] >= 1 && emailWordCount[0] >= 1) {
                    sendBn.setAlpha(1);
                    sendBn.setEnabled(true);
                } else {
                    sendBn.setAlpha(0.7f);
                    sendBn.setEnabled(false);
                }
                titleWordCount[0] = count;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mEmailET = (EditText) view.findViewById(R.id.email_tv);
        mEmailET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (contentWordCount[0] >= 1 && titleWordCount[0] >= 1 && emailWordCount[0] >= 1) {
                    sendBn.setAlpha(1);
                    sendBn.setEnabled(true);
                } else {
                    sendBn.setAlpha(0.7f);
                    sendBn.setEnabled(false);
                }
                emailWordCount[0] = count;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        final RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.emotion_layout);
        final LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout2);
        mEmailET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    relativeLayout.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.GONE);
                } else {
                    linearLayout.setVisibility(View.VISIBLE);
                }

            }
        });
        mEmailET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relativeLayout.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.GONE);
            }
        });

    }

    private void initCheckBox(View view) {
        final CheckBox shiXiCheckBox = (CheckBox) view.findViewById(R.id.shixi_checkBox);
        final CheckBox xiaoZhaoCheckBox = (CheckBox) view.findViewById(R.id.xiaozhao_checkBox);
        final CheckBox sheZhaoCheckBox = (CheckBox) view.findViewById(R.id.shezhao_checkBox);
        shiXiCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                computeType(shiXiCheckBox, xiaoZhaoCheckBox, sheZhaoCheckBox);
            }
        });
        xiaoZhaoCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                computeType(shiXiCheckBox, xiaoZhaoCheckBox, sheZhaoCheckBox);
            }
        });
        sheZhaoCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                computeType(shiXiCheckBox, xiaoZhaoCheckBox, sheZhaoCheckBox);
            }
        });

    }

    private void computeType(CheckBox shiXiCheckBox, CheckBox xiaoZhaoCheckBox, CheckBox sheZhaoCheckBox) {
        if (shiXiCheckBox.isChecked() && xiaoZhaoCheckBox.isChecked() && sheZhaoCheckBox.isChecked()) {
            mType = 7 + "";
        } else if (xiaoZhaoCheckBox.isChecked() && sheZhaoCheckBox.isChecked()) {
            mType = 6 + "";
        } else if (shiXiCheckBox.isChecked() && xiaoZhaoCheckBox.isChecked()) {
            mType = 5 + "";
        } else if (shiXiCheckBox.isChecked() && sheZhaoCheckBox.isChecked()) {
            mType = 4 + "";
        } else if (sheZhaoCheckBox.isChecked()) {
            mType = 3 + "";
        } else if (xiaoZhaoCheckBox.isChecked()) {
            mType = 2 + "";
        } else if (shiXiCheckBox.isChecked()) {
            mType = 1 + "";
        }
    }

    public void setImages(ArrayList<ImageWrapper> images) {
        mUploadImgPaths = images;
    }

    public void inputEmotion(String text) {
        String label = InputEmotionUtil.getLabel();
        if (label != null) {
            if (label.equals(getString(R.string.content))) {
                int selectionCursor = mContentET.getSelectionStart();
                mContentET.getText().insert(selectionCursor, text);
                mContentET.invalidate();
            } else if (label.equals(getString(R.string.title))) {
                int selectionCursor = mTitleET.getSelectionStart();
                mTitleET.getText().insert(selectionCursor, text);
                mTitleET.invalidate();
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
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
                final String content = mContentET.getText().toString();
                final String title = mTitleET.getText().toString();
                final String email = mEmailET.getText().toString();
                final HashMap<String, String> params = new HashMap<>();
                params.put(Constant.CONTENT, StringBase64.encode(content));
                params.put(Constant.TITLE, StringBase64.encode(title));
                params.put(Constant.TYPE, mType.toString());
                params.put(Constant.EMAIL, email);
                params.put(Constant.AUTHORIZATION, RecommendPublishFragment.this.getHostActivity().token());
                final ArrayList<BitmapWrapper> bitmapWrappers = new ArrayList<>();
                BitmapWrapper bitmapWrapper;
                File sourceFile;
                for (ImageWrapper image : mUploadImgPaths) {
                    final String path = image.getPath();
                    bitmapWrapper = new BitmapWrapper();
                    sourceFile = new File(path);
                    bitmapWrapper.setPath(path);
                    bitmapWrapper.setFileName(sourceFile.getName());
                    try {
                        bitmapWrapper.setFileType(sourceFile.toURL().openConnection().getContentType());
                        bitmapWrappers.add(bitmapWrapper);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                ArrayList<BitmapWrapper> bitmapWrapperArrayList = HttpManager.getInstance().compressBitmap(getContext(), bitmapWrappers);
                String level = choosePublish.level();
                Log.i(TAG, "level value " + level);
                final String url = PathConstant.BASE_URL + PathConstant.RECOMMEND_WORK_PATH + PathConstant.RECOMMEND_WORK_SUB_PATH_SAVE + "?level=" + level;
                HttpManager.getInstance().exeRequest(new MultiImgRequest(url, params, bitmapWrapperArrayList, callback));
            }
        });
    }

}
