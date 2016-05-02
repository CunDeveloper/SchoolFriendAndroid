package com.nju.fragment;


import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;

import com.nju.activity.FragmentHostActivity;

public class BaseFragment extends Fragment {


    public FragmentHostActivity getHostActivity(){
        return (FragmentHostActivity) getActivity();
    }
}
