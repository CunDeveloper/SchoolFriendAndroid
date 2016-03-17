package com.nju.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.nju.fragment.BaseFragment;
import com.nju.util.Constant;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Stack;

public abstract class BaseActivity extends AppCompatActivity implements FragmentHostActivity {

    private static final String TAG = BaseFragment.class.getSimpleName();
    protected LocalStack<BaseFragment> mLocalBackStack = new LocalStack<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public LocalStack<BaseFragment> getBackStack() {
        return mLocalBackStack;
    }

    @Override
    public ApplicationHandler getAppHandler() {
        return ApplicationHandler.newInstance();
    }

    @Override
    public SharedPreferences getSharedPreferences(){
        return getSharedPreferences(Constant.SCHOOL_FRIEND_SHARED_PREFERENCE, Context.MODE_PRIVATE);
    }

    public static class LocalStack<E> extends Stack<E> {
        private static final long serialVersionUID = 1553870122121064116L;

        public E getSecondLastElement() {
            E topElem = pop();
            E returnElem = null;
            try {
                returnElem = peek();
            } catch (EmptyStackException e) {
                Log.e(TAG, "EmptyStackException");
            }
            push(topElem);
            return returnElem;
        }
    }
}
