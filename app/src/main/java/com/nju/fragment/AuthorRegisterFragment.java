package com.nju.fragment;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.nju.activity.R;
import com.nju.http.ResponseCallback;
import com.nju.http.response.ParseResponse;
import com.nju.model.Author;
import com.nju.service.AuthorService;
import com.nju.util.Constant;
import com.nju.util.Divice;
import com.nju.util.FragmentUtil;
import com.nju.util.ToastUtil;

import java.io.IOException;

public class AuthorRegisterFragment extends BaseFragment {

    private static final String TAG = AuthorRegisterFragment.class.getSimpleName();
    public static AuthorRegisterFragment newInstance() {
        AuthorRegisterFragment fragment = new AuthorRegisterFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public AuthorRegisterFragment() {
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
        View view = inflater.inflate(R.layout.fragment_author_register, container, false);
        view.setPadding(view.getPaddingLeft(), Divice.getStatusBarHeight(getContext()), view.getPaddingRight(), view.getPaddingBottom());
        initRegister(view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.alumni_register));
        }
    }


    private void initRegister(View view){
        Button regBn = (Button) view.findViewById(R.id.register);
        final EditText userNameET = (EditText) view.findViewById(R.id.etUsername);
        final TextView tipTV = (TextView) view.findViewById(R.id.tipTV);
        userNameET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                ToastUtil.showShortText(getContext(),"change");
            }
        });
        final EditText passwordET = (EditText) view.findViewById(R.id.etPassword);
        final EditText surePassET = (EditText) view.findViewById(R.id.etSurePassword);
        regBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = userNameET.getText().toString();
                String password = passwordET.getText().toString();
                String surePass = surePassET.getText().toString();
                if (!username.trim().equals("") && !password.trim().equals("") && !surePass.trim().equals("")){
                    tipTV.setText("");
                    tipTV.invalidate();
                    if (password.equals(surePass)){
                        Author authorInfo = new Author();
                        authorInfo.setLoginName(username);
                        authorInfo.setPassword(password);
                        AuthorService.saveAuthor(AuthorRegisterFragment.this, authorInfo, new ResponseCallback() {
                            @Override
                            public void onFail(Exception error) {
                                Log.e(TAG,error.getMessage());
                            }

                            @Override
                            public void onSuccess(String responseBody) {
                                Log.i(TAG,responseBody);
                                if (FragmentUtil.isAttachedToActivity(AuthorRegisterFragment.this)) {
                                    Log.i(TAG, responseBody);
                                    ParseResponse parseResponse = new ParseResponse();
                                    try {
                                        String str = parseResponse.getInfo(responseBody);
                                        if (str != null && str.equals(Constant.OK_MSG)) {
                                            ToastUtil.showShortText(getContext(),getString(R.string.register_ok));
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                    }else {
                        tipTV.setText(getString(R.string.twice_pass_diff));
                        tipTV.invalidate();
                    }
                }else {
                    tipTV.setText(getString(R.string.filed_not_empty));
                    tipTV.invalidate();
                }
            }
        });
    }


}
