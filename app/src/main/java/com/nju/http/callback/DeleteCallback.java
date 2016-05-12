package com.nju.http.callback;

import android.util.Log;

import com.nju.activity.R;
import com.nju.fragment.BaseFragment;
import com.nju.http.ResponseCallback;
import com.nju.http.response.ParseResponse;
import com.nju.util.FragmentUtil;
import com.nju.util.ToastUtil;

import java.io.IOException;

/**
 * Created by cun on 2016/5/12.
 */
public class DeleteCallback extends ResponseCallback {
    private String mTag ;
    private BaseFragment mFragment;

    public DeleteCallback(String tag,BaseFragment fragment){
        this.mTag = tag;
        this.mFragment = fragment;
    }

    @Override
    public void onFail(Exception error) {
        Log.e(mTag,error.getMessage());
        ToastUtil.showShortText(mFragment.getContext(), mFragment.getString(R.string.delete_failure));
    }

    @Override
    public void onSuccess(String responseBody) {
        if (FragmentUtil.isAttachedToActivity(mFragment)) {
            Log.i(mTag, responseBody);
            ParseResponse parseResponse = new ParseResponse();
            try {
                ToastUtil.showShortText(mFragment.getContext(),mFragment.getString(R.string.delete_ok));
                String str = parseResponse.getInfo(responseBody);
                mFragment.getHostActivity().getBackStack().pop();
                mFragment.getHostActivity().open(mFragment.getHostActivity().getBackStack().peek());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
