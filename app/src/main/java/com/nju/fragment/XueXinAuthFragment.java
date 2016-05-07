package com.nju.fragment;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.nju.View.SchoolFriendDialog;
import com.nju.activity.R;
import com.nju.http.ByteResponseCallback;
import com.nju.http.HttpManager;
import com.nju.http.ResponseCallback;
import com.nju.http.SchoolFriendHttp;
import com.nju.http.request.PostRequest;
import com.nju.http.response.ParseResponse;
import com.nju.model.AuthenticationAccessToken;
import com.nju.model.Author;
import com.nju.model.UserInfo;
import com.nju.service.AuthorService;
import com.nju.service.UserDegreeInfoService;
import com.nju.util.Constant;
import com.nju.util.CryptUtil;
import com.nju.util.DateUtil;
import com.nju.util.Divice;
import com.nju.util.FragmentUtil;
import com.nju.util.SchoolFriendGson;
import com.nju.util.SoftInput;
import com.nju.util.ToastUtil;

import java.io.IOException;
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
    private static final String LABEL_PARAM = "labelParam";
    private String mLabel;
    private EditText mUserNameEditText;
    private EditText mPassEditText;
    private EditText mCaptchaEditText;
    private TextView mTipTV;
    private ImageView mCaptchaImg;
    private SchoolFriendDialog mDialog;
    private RelativeLayout mCaptchaLayout;
    private boolean isNeedCaptcha = false;
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
                String result = new String(responseBody, "utf-8");
                Log.e(TAG, result);
                if (result.equals(Constant.HTTP_ERROR) || result.equals(Constant.HTTP_URL_ERROR)) {
                    mDialog.dismiss();
                } else if (responseBody[responseBody.length - 1] == '#') {
                    isNeedCaptcha = true;//设置验证码标签
                    mCaptchaLayout.setVisibility(View.VISIBLE);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(responseBody, 0, responseBody.length - 1);
                    mCaptchaImg.setImageBitmap(bitmap);
                    mCaptchaImg.invalidate();
                } else {
                    Log.i(TAG, result);
                    Map<String, Object> stringMap = gson.fromJsonToMap(result);
                    if (stringMap.containsKey(Constant.XUE_XIN_INFO)) {
                        String[] strs = result.split(":\\[");
                        String temp = "[" + strs[1];
                        ArrayList<UserInfo> userInfos = gson.fromJsonToList(temp.substring(0, temp.length() - 1), UserInfo.class);
                        getHostActivity().getSharedPreferences().edit().putString(getString(R.string.person_info), gson.toJson(userInfos)).commit();
                        getHostActivity().getSharedPreferences().edit().putInt(getString(R.string.is_authorization), 1).commit();
                        getHostActivity().open(UserInfoFragment.newInstance(userInfos));
                    }
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } finally {
                mDialog.dismiss();
            }

        }
    };
    private CharSequence mTitleChar = "登录";
    private PostRequest request = new PostRequest();
    private Button mAuthBn;
    private ArrayList<LinearLayout> mLayouts;
    private RelativeLayout mXueXinIconLayout;
    private Handler handler = new MyHandler(this);
    private ResponseCallback loginCallback = new ResponseCallback() {
        @Override
        public void onFail(Exception error) {
            Log.e(TAG, error.getMessage());
            mDialog.dismiss();
        }

        @Override
        public void onSuccess(String responseBody) {
            Log.i(TAG, responseBody);
            if (FragmentUtil.isAttachedToActivity(XueXinAuthFragment.this)) {
                Log.i(TAG, responseBody);
                mDialog.dismiss();
                ParseResponse parseResponse = new ParseResponse();
                try {
                    String str = parseResponse.getInfo(responseBody);
                    try {
                        int count = Integer.valueOf(str);
                        if (count > 0) {
                            getHostActivity().getSharedPreferences().edit().putInt(getString(R.string.is_authorization), 1).commit();
                            getHostActivity().open(AlumniDynamicFragment.newInstance(), XueXinAuthFragment.this);
                        } else if (count == 0) {
                            mXueXinIconLayout.setVisibility(View.VISIBLE);
                            for (int i = 0; i < mLayouts.size(); i++) {
                                if (i == 2) {
                                    mLayouts.get(i).setVisibility(View.VISIBLE);
                                    mAuthBn.setText(getString(R.string.authorization));
                                    setTitle(getString(R.string.authorization));
                                } else {
                                    mLayouts.get(i).setVisibility(View.GONE);
                                }
                            }
                        }
                    } catch (NumberFormatException e) {
                        ToastUtil.showShortText(getContext(), getString(R.string.login_error));
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    public XueXinAuthFragment() {
        // Required empty public constructor
    }

    public static XueXinAuthFragment newInstance(String label) {
        XueXinAuthFragment fragment = new XueXinAuthFragment();
        Bundle args = new Bundle();
        args.putString(LABEL_PARAM, label);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mLabel = getArguments().getString(LABEL_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_xue_xin_auth, container, false);
        view.setPadding(view.getPaddingLeft(), Divice.getStatusBarHeight(getActivity()), view.getPaddingRight(), view.getPaddingBottom());
        initView(view);
        authClick(view);
        editTextChangeListener();
        if (mLabel.equals(getString(R.string.hasToken))) {
            mXueXinIconLayout.setVisibility(View.VISIBLE);
            for (int i = 0; i < mLayouts.size(); i++) {
                if (i == 2) {
                    mLayouts.get(i).setVisibility(View.VISIBLE);
                    mAuthBn.setText(getString(R.string.authorization));
                    setTitle(getString(R.string.authorization));
                } else {
                    mLayouts.get(i).setVisibility(View.GONE);
                }
            }
        }
        return view;
    }

    private void initView(View view) {
        mLayouts = new ArrayList<>();
        mLayouts.add((LinearLayout) view.findViewById(R.id.register_layout));
        mLayouts.add((LinearLayout) view.findViewById(R.id.loginLayout));
        mLayouts.add((LinearLayout) view.findViewById(R.id.authorizationLayout));
        mUserNameEditText = (EditText) view.findViewById(R.id.etUsername);
        mPassEditText = (EditText) view.findViewById(R.id.etPassword);
        mCaptchaImg = (ImageView) view.findViewById(R.id.fragment_xue_xin_auth_image);
        mTipTV = (TextView) view.findViewById(R.id.fragment_xue_xin_tip_textView);
        mCaptchaEditText = (EditText) view.findViewById(R.id.etCaptcha);
        mCaptchaLayout = (RelativeLayout) view.findViewById(R.id.fragment_xue_xin_captcha_layout);
        mXueXinIconLayout = (RelativeLayout) view.findViewById(R.id.xuexinIconLayout);
        mAuthBn = (Button) view.findViewById(R.id.fragment_xue_xin_auth_bn);
        mAuthBn.setText(mTitleChar);
        TextView moreTV = (TextView) view.findViewById(R.id.moreTV);
        moreTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                String[] items = {
                        getString(R.string.register),
                        getString(R.string.login)
                };
                SchoolFriendDialog.listItemDialog(getContext(), items, new SchoolFriendDialog.ListItemCallback() {
                    @Override
                    public void onSelection(MaterialDialog materialDialog, View view, int position, CharSequence charSequence) {
                        mTitleChar = charSequence.toString();
                        mAuthBn.setText(mTitleChar);
                        setTitle(mTitleChar.toString());
                        for (int i = 0; i < 2; i++) {
                            if (i == position) {
                                mLayouts.get(i).setVisibility(View.VISIBLE);
                            } else {
                                mLayouts.get(i).setVisibility(View.GONE);
                            }
                        }
                    }
                }).show();
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setTitle(mTitleChar.toString());
        getHostActivity().display(9);
    }

    private void setTitle(String title) {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setTitle(title);
        }
    }

    private void saveUserInfo(ArrayList<UserInfo> userInfos) {
        SharedPreferences.Editor editor = getHostActivity().getSharedPreferences().edit();
        editor.putInt(getString(R.string.size), userInfos.size());
        for (int i = 1; i <= userInfos.size(); i++) {
            UserInfo info = userInfos.get(i - 1);
            editor.putString(getString(R.string.school) + i, info.getLabel()).apply();
            editor.putString(getString(R.string.xueyuan) + i, info.getFenYuan()).apply();
            editor.putString(getString(R.string.major) + i, info.getMajor()).apply();

            editor.putInt(getString(R.string.start_date) + i, DateUtil.year(DateUtil.getCalendar(info.getDate()))).apply();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        SchoolFriendHttp.close();
    }

    private void authClick(final View view) {

        mAuthBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoftInput.close(getContext(), mAuthBn);
                String key = mAuthBn.getText().toString();
                if (key.equals(getString(R.string.authorization))) {
                    auth();
                } else if (key.equals(getString(R.string.login))) {
                    login(view);
                } else if (key.equals(getString(R.string.register))) {
                    register(view);
                }
            }
        });
    }

    private void auth() {
        final String userName = mUserNameEditText.getText().toString();
        final String pass = mPassEditText.getText().toString();
        final String captcha = mCaptchaEditText.getText().toString();
        final HashMap<String, String> params = new HashMap<>();
        if (!isNeedCaptcha) {
            if (validate(userName, pass)) {
                params.put(Constant.XUE_XIN_USERNAME, userName);
                params.put(Constant.XUE_XIN_PASSWORD, pass);
                params.put(Constant.AUTHOR_ID, getHostActivity().userId() + "");
                Log.i(TAG, Constant.AUTHOR_ID + getHostActivity().userId());
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
            params.put(Constant.AUTHOR_ID, getHostActivity().userId() + "");
            params.put(Constant.ANDROID_ID, Divice.getAndroidId(getActivity()));
            request.setParams(params);
            mDialog = SchoolFriendDialog.showProgressDialog(getActivity(), getString(R.string.auth_progress_dialog_title), getString(R.string.auth_progress_dialog_content));
            mDialog.show();
            HttpManager.getInstance().exeRequest(request);
        }
    }

    private void login(View view) {
        mDialog = SchoolFriendDialog.showProgressDialog(getContext(), getString(R.string.login)
                , getString(R.string.logining));
        mDialog.show();
        String loginName = ((EditText) view.findViewById(R.id.etLoginname)).getText().toString();
        String password = ((EditText) view.findViewById(R.id.etLoginpass)).getText().toString();
        if (!loginName.trim().equals("") && !password.trim().equals("")) {
            mTipTV.setText("");
            mTipTV.invalidate();
            Author authorInfo = new Author();
            authorInfo.setLoginName(loginName);
            authorInfo.setPassword(password);
            AuthorService.login(XueXinAuthFragment.this, authorInfo, new ResponseCallback() {
                @Override
                public void onFail(Exception error) {
                    Log.e(TAG, error.getMessage());
                }

                @Override
                public void onSuccess(String responseBody) {
                    Log.i(TAG, responseBody);
                    if (FragmentUtil.isAttachedToActivity(XueXinAuthFragment.this)) {
                        Log.i(TAG, responseBody);
                        ParseResponse parseResponse = new ParseResponse();
                        try {
                            AuthenticationAccessToken token = (AuthenticationAccessToken) parseResponse.getInfo(responseBody, AuthenticationAccessToken.class);
                            if (token != null) {
                                String tokenValue = SchoolFriendGson.newInstance().toJson(token);
                                Log.i(TAG, tokenValue);
                                getHostActivity().getSharedPreferences().edit().putInt(Constant.USER_ID, token.getUserId()).commit();
                                getHostActivity().getSharedPreferences().edit().putString(Constant.AUTHORIZATION, CryptUtil.getEncryptiedData(tokenValue)).commit();
                                UserDegreeInfoService.isAuthorization(XueXinAuthFragment.this, loginCallback);

                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } else {
            mTipTV.setText(getString(R.string.filed_not_empty));
            mTipTV.invalidate();
        }
    }

    private void register(View view) {
        mDialog = SchoolFriendDialog.showProgressDialog(getContext(), getString(R.string.register)
                , getString(R.string.registering));
        mDialog.show();
        String loginName = ((EditText) view.findViewById(R.id.etReUsername)).getText().toString();
        String password = ((EditText) view.findViewById(R.id.etRePassword)).getText().toString();
        String surePass = ((EditText) view.findViewById(R.id.etReSurePassword)).getText().toString();
        if (!loginName.trim().equals("") && !password.trim().equals("") && !surePass.trim().equals("")) {
            mTipTV.setText("");
            mTipTV.invalidate();
            if (password.equals(surePass)) {
                Author authorInfo = new Author();
                authorInfo.setLoginName(loginName);
                authorInfo.setPassword(password);
                AuthorService.saveAuthor(XueXinAuthFragment.this, authorInfo, new ResponseCallback() {
                    @Override
                    public void onFail(Exception error) {
                        Log.e(TAG, error.getMessage());
                    }

                    @Override
                    public void onSuccess(String responseBody) {
                        Log.i(TAG, responseBody);
                        if (FragmentUtil.isAttachedToActivity(XueXinAuthFragment.this)) {
                            Log.i(TAG, responseBody);
                            ParseResponse parseResponse = new ParseResponse();
                            try {
                                AuthenticationAccessToken token = (AuthenticationAccessToken) parseResponse.getInfo(responseBody, AuthenticationAccessToken.class);
                                if (token != null) {
                                    getHostActivity().getSharedPreferences().edit().putInt(Constant.USER_ID, token.getUserId()).commit();
                                    String tokenValue = SchoolFriendGson.newInstance().toJson(token);
                                    Log.i(TAG, tokenValue);
                                    getHostActivity().getSharedPreferences().edit().putString(Constant.AUTHORIZATION, CryptUtil.getEncryptiedData(tokenValue)).commit();
                                    UserDegreeInfoService.isAuthorization(XueXinAuthFragment.this, loginCallback);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                });
            } else {
                mTipTV.setText(getString(R.string.twice_pass_diff));
                mTipTV.invalidate();
            }
        } else {
            mTipTV.setText(getString(R.string.filed_not_empty));
            mTipTV.invalidate();
        }
    }

    private void editTextChangeListener() {
        mUserNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 1) {
                    mTipTV.setText("");
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
                    mTipTV.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    private boolean validate(String name, String pass) {
        if (name == null || name.equals("")) {
            Message message = new Message();
            message.obj = getString(R.string.auth_username_empty_tip);
            message.what = USERNAME_EMPTY;
            handler.sendMessage(message);
            return false;
        } else if (pass == null || pass.equals("")) {
            Message message = new Message();
            message.obj = getString(R.string.auth_password_empty_tip);
            message.what = PASSWORD_EMPTY;
            handler.sendMessage(message);
            return false;
        } else {
            return true;
        }
    }


    private static class MyHandler extends Handler {

        private final WeakReference<XueXinAuthFragment> mXueXinAuthFragment;

        private MyHandler(XueXinAuthFragment xueXinAuthFragment) {
            this.mXueXinAuthFragment = new WeakReference<>(xueXinAuthFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            XueXinAuthFragment fragment = mXueXinAuthFragment.get();
            if (fragment != null) {
                super.handleMessage(msg);
                if (msg.what == ERROR_USER_PASS) {
                    String tip = (String) msg.obj;
                    fragment.mTipTV.setText(tip);
                    fragment.mUserNameEditText.requestFocus();
                } else if (msg.what == USERNAME_EMPTY) {
                    String tip = (String) msg.obj;
                    fragment.mTipTV.setText(tip);
                    fragment.mUserNameEditText.requestFocus();
                } else if (msg.what == PASSWORD_EMPTY) {
                    String tip = (String) msg.obj;
                    fragment.mTipTV.setText(tip);
                    fragment.mPassEditText.requestFocus();

                }
                SoftInput.open(fragment.getActivity());
            }
        }
    }

}
