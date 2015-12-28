package com.nju.fragment;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nju.View.SchoolFriendDialog;
import com.nju.activity.R;
import com.nju.http.ByteResponseCallback;
import com.nju.http.HttpManager;
import com.nju.http.SchoolFriendHttp;
import com.nju.http.request.PostRequest;
import com.nju.model.UserInfo;
import com.nju.util.Constant;
import com.nju.util.DateUtil;
import com.nju.util.Divice;
import com.nju.util.SchoolFriendGson;
import com.nju.util.SoftInput;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class XueXinAuthFragment extends BaseFragment {
    public static final String TAG = XueXinAuthFragment.class.getSimpleName();
    private static final int ERROR_USER_PASS = 0;
    private static final int USERNAME_EMPTY = 1;
    private static final int PASSWORD_EMPTY = 2;
    private EditText mUserNameEditText;
    private EditText mPassEditText;
    private EditText mCaptchaEditText;
    private TextView mTipTextView;
    private ImageView mCaptchaImg;
    private SchoolFriendDialog mDialog;
    private RelativeLayout mCaptchaLayout;
    private boolean isNeedCaptcha = false;
    private PostRequest request = new PostRequest();
    private  Handler handler = new MyHandler(this);

    public static XueXinAuthFragment newInstance() {
        return new XueXinAuthFragment();
    }

    public XueXinAuthFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_xue_xin_auth, container, false);
        view.setPadding(view.getPaddingLeft(),Divice.getStatusBarHeight(getActivity()),view.getPaddingRight(),view.getPaddingBottom());
        mUserNameEditText = (EditText) view.findViewById(R.id.etUsername);
        mPassEditText = (EditText) view.findViewById(R.id.etPassword);
        mCaptchaImg = (ImageView) view.findViewById(R.id.fragment_xue_xin_auth_image);
        mTipTextView = (TextView) view.findViewById(R.id.fragment_xue_xin_tip_textView);
        mCaptchaEditText = (EditText) view.findViewById(R.id.etCaptcha);
        mCaptchaLayout = (RelativeLayout) view.findViewById(R.id.fragment_xue_xin_captcha_layout);
        authClick(view);
        editTextChangeListener();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getHostActivity().getToolBar().setTitle(getString(R.string.xue_xin_auth));
        getHostActivity().geLinearLayout().setVisibility(View.GONE);
        getHostActivity().getMenuCameraView().setVisibility(View.GONE);
        getHostActivity().getMenuDeleteView().setVisibility(View.GONE);
        getHostActivity().getMenuBn().setVisibility(View.GONE);
    }

    ByteResponseCallback callbacks = new ByteResponseCallback() {
        private final SchoolFriendGson gson = SchoolFriendGson.newInstance();
        @Override
        public void onFail(Exception error) {
            error.printStackTrace();
            //Log.e(TAG,error.printStackTrace());
            mDialog.dismiss();
        }
        @Override
        public void onSuccess(byte[] responseBody) {
            try {
                String result = new String(responseBody,"utf-8");
                Log.e(TAG,result);
                if (result.equals(Constant.HTTP_ERROR) || result.equals(Constant.HTTP_URL_ERROR)) {
                    mDialog.dismiss();
                }else if(responseBody[responseBody.length-1]=='#') {
                    isNeedCaptcha = true;//设置验证码标签
                    mCaptchaLayout.setVisibility(View.VISIBLE);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(responseBody,0,responseBody.length-1);
                    mCaptchaImg.setImageBitmap(bitmap);
                    mCaptchaImg.invalidate();
                } else {
                    Map<String,Object> stringMap = gson.fromJsonToMap(result);
                    if (stringMap.containsKey(Constant.XUE_XIN_INFO)) {
                        String[] strs = result.split(":\\[");
                        String temp = "["+strs[1];
                        ArrayList<UserInfo> userInfos = gson.fromJsonToList(temp.substring(0,temp.length()-1),UserInfo.class);
                        saveUserInfo(userInfos);
                        getHostActivity().open(UserInfoFragment.newInstance(userInfos));
                    }
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }finally {
                mDialog.dismiss();
            }

        }
    };

    private void saveUserInfo(ArrayList<UserInfo> userInfos) {
        SharedPreferences.Editor editor= getHostActivity().getSharedPreferences().edit();
        editor.putInt(getString(R.string.size),userInfos.size());
        for (int i=1;i<=userInfos.size();i++) {
            UserInfo info = userInfos.get(i-1);
            editor.putString(getString(R.string.school)+i,info.getLabel()).apply();
            editor.putString(getString(R.string.xueyuan)+i,info.getFenYuan()).apply();
            editor.putString(getString(R.string.major)+i,info.getMajor()).apply();

            editor.putInt(getString(R.string.start_date)+i, DateUtil.year(DateUtil.getCalendar(info.getDate()))).apply();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        SchoolFriendHttp.close();
    }

    private void authClick(View view ) {
        Button button = (Button) view.findViewById(R.id.fragment_xue_xin_auth_bn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userName = mUserNameEditText.getText().toString();
                final String pass = mPassEditText.getText().toString();
                final String captcha = mCaptchaEditText.getText().toString();
                final HashMap<String, String> params = new HashMap<>();
                if (!isNeedCaptcha) {
                    if (validate(userName, pass)) {
                        params.put(Constant.XUE_XIN_USERNAME, userName);
                        params.put(Constant.XUE_XIN_PASSWORD, pass);
                        params.put(Constant.ANDROID_ID, Divice.getAndroidId(getActivity()));
                        mDialog = SchoolFriendDialog.showProgressDialog(getActivity(), getString(R.string.auth_progress_dialog_title), getString(R.string.auth_progress_dialog_content));
                        mDialog.show();
                        request.setUrl(Constant.XUE_AUTH);
                        request.setParams(params);
                        request.setCallback(callbacks);
                        HttpManager.getInstance().exeRequest(request);
                }
                } else {
                        params.put(Constant.XUE_XIN_USERNAME, userName);
                        params.put(Constant.XUE_XIN_PASSWORD, pass);
                        params.put(Constant.XUE_XIN_CAPTCHA, captcha);
                        params.put(Constant.ANDROID_ID, Divice.getAndroidId(getActivity()));
                        request.setParams(params);
                        mDialog = SchoolFriendDialog.showProgressDialog(getActivity(), getString(R.string.auth_progress_dialog_title), getString(R.string.auth_progress_dialog_content));
                        mDialog.show();
                        HttpManager.getInstance().exeRequest(request);

                }

            }
        });
    }

    private void editTextChangeListener() {
        mUserNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 1) {
                    mTipTextView.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mPassEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 1) {
                    mTipTextView.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    private boolean validate(String name, String pass) {
        if (name ==null || name.equals("")) {
            Message message = new Message();
            message.obj = getString(R.string.auth_username_empty_tip);
            message.what = USERNAME_EMPTY;
            handler.sendMessage(message);
            return false;
        }else if (pass == null || pass.equals("")){
            Message message = new Message();
            message.obj = getString(R.string.auth_password_empty_tip);
            message.what = PASSWORD_EMPTY;
            handler.sendMessage(message);
            return false;
        }
        else {
            return true;
        }
    }


    private static class MyHandler extends  Handler {

        private final WeakReference<XueXinAuthFragment> mXueXinAuthFragment;

        private MyHandler(XueXinAuthFragment xueXinAuthFragment) {
            this.mXueXinAuthFragment = new WeakReference<>(xueXinAuthFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            XueXinAuthFragment fragment = mXueXinAuthFragment.get();
            if (fragment != null) {
                super.handleMessage(msg);
                if(msg.what == ERROR_USER_PASS) {
                    String tip = (String) msg.obj;
                    fragment.mTipTextView.setText(tip);
                    fragment.mUserNameEditText.requestFocus();
                } else if (msg.what == USERNAME_EMPTY) {
                    String tip = (String) msg.obj;
                    fragment.mTipTextView.setText(tip);
                    fragment.mUserNameEditText.requestFocus();
                } else if (msg.what == PASSWORD_EMPTY) {
                    String tip = (String) msg.obj;
                    fragment.mTipTextView.setText(tip);
                    fragment.mPassEditText.requestFocus();

                }
                SoftInput.open(fragment.getActivity());
            }
        }
    }

}
