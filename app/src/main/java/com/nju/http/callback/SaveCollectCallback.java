package com.nju.http.callback;

import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.TextView;

import com.nju.activity.R;
import com.nju.fragment.BaseFragment;
import com.nju.http.ResponseCallback;
import com.nju.http.response.ParseResponse;
import com.nju.util.Constant;
import com.nju.util.FragmentUtil;
import com.nju.util.ToastUtil;

import java.io.IOException;

/**
 * Created by cun on 2016/5/12.
 */
public class SaveCollectCallback extends ResponseCallback {
    private String mTag;
    private BaseFragment mFragment;
    private TextView mCollectTV;

    public SaveCollectCallback(String tag,BaseFragment fragment,TextView collectTV){
        this.mTag = tag;
        this.mFragment = fragment;
        this.mCollectTV = collectTV;
    }
    @Override
    public void onFail(Exception error) {
        Log.e(mTag,error.getMessage());
        ToastUtil.ShowText(mFragment.getContext(), mFragment.getString(R.string.collect_faiure));
    }

    @Override
    public void onSuccess(String responseBody) {
        if (FragmentUtil.isAttachedToActivity(mFragment)) {
            Log.i(mTag, responseBody);
            ParseResponse parseResponse = new ParseResponse();
            try {
                String str = parseResponse.getInfo(responseBody);
                if (str != null && str.equals(Constant.OK_MSG)){
                    ToastUtil.ShowText(mFragment.getContext(), mFragment.getString(R.string.collect_ok));
                    mCollectTV.setTextColor(ContextCompat.getColor(mFragment.getContext(), android.R.color.holo_orange_dark));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
