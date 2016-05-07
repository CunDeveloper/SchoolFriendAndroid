package com.nju.fragment;


import android.support.v4.app.Fragment;

import com.nju.activity.FragmentHostActivity;

public class BaseFragment extends Fragment {


    public FragmentHostActivity getHostActivity() {
        return (FragmentHostActivity) getActivity();
    }
}
