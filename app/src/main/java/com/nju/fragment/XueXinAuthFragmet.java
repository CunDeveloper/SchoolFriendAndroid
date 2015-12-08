package com.nju.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.nju.View.SchoolFriendDialog;
import com.nju.activity.R;
import com.nju.http.SchoolFriendHttp;
import com.nju.model.UserInfo;
import com.nju.util.Constant;
import com.nju.util.Divice;
import com.nju.util.SoftInput;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class XueXinAuthFragmet extends BaseFragment {
    public static final String TAG = XueXinAuthFragmet.class.getSimpleName();
    private static final int ERROR_USER_PASS = 0;
    private static final int USERNAME_EMPTY = 1;
    private static final int PASSWORD_EMPTY = 2;
    private EditText mUserNameEditText;
    private EditText mPassEditText;
    private Button mButton;
    private TextView mTipTextView;
    private OpenFragmentListener mListener;
    private  Handler handler = new MyHandler(this);

    public static XueXinAuthFragmet newInstance() {
        return new XueXinAuthFragmet();
    }

    public XueXinAuthFragmet() {
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
        mUserNameEditText = (EditText) view.findViewById(R.id.etUsername);
        mPassEditText = (EditText) view.findViewById(R.id.etPassword);
        mButton = (Button) view.findViewById(R.id.fragment_xue_xin_auth_bn);
        mTipTextView = (TextView) view.findViewById(R.id.fragment_xue_xin_tip_textView);
        authClick();
        editTextChangeListener();
        return view;
    }
    private void authClick() {
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userName = mUserNameEditText.getText().toString();
                final String pass = mPassEditText.getText().toString();
                if (validte(userName, pass)) {
                    exeAuth(userName, pass);
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
                if (count==1) {
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

    private void exeAuth(final String userName,final String pass) {
        final SchoolFriendDialog dialog = SchoolFriendDialog.showProgressDialog(getActivity(), getString(R.string.auth_progress_dialog_title), getString(R.string.auth_progress_dialog_content));
        dialog.show();
        ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).
                hideSoftInputFromWindow(mPassEditText.getWindowToken(), 0);
        new Thread() {
            @Override
            public void run() {
                try {
                    Map<String,String> params = new HashMap<String,String>();
                    params.put(Constant.XUE_XIN_USERNAME,userName);
                    params.put(Constant.XUE_XIN_PASSWORD, pass);
                    params.put(Constant.ANDROID_ID, Divice.getAndroidId(getActivity()));
                    String result= SchoolFriendHttp.postForm(Constant.BASE_URL + Constant.XUE_AUTH,params);
                    Message message = new Message();
                    message.obj = result;
                    message.what = ERROR_USER_PASS;
                    handler.sendMessage(message);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                finally {
                    dialog.dismiss();
                }
            }
        }.start();
    }

    private boolean  validte(String name,String pass) {
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
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OpenFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OpenFragmentListener {
         void openFragment(ArrayList<UserInfo> lists);
    }

    private static class MyHandler extends  Handler {

        private final WeakReference<XueXinAuthFragmet> mXueXinAuthFragment;

        private MyHandler(XueXinAuthFragmet xueXinAuthFragmet) {
            this.mXueXinAuthFragment = new WeakReference<>(xueXinAuthFragmet);
        }

        @Override
        public void handleMessage(Message msg) {
            XueXinAuthFragmet fragment = mXueXinAuthFragment.get();
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
