package com.nju.fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.nju.activity.R;
import com.nju.adatper.MultiChoosePicAdapter;
import com.nju.adatper.SingleChoosePicAdapter;
import com.nju.model.Image;
import com.nju.service.ChooseImageService;
import com.nju.util.Divice;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class SingleChoosePicFragment extends BaseFragment {
    public static final String TAG = SingleChoosePicFragment.class.getSimpleName();
    private static final String LABEL = "label";
    private String mLabel;
    private ProgressBar mProgressBar;
    private GridView mGridView;
    private ArrayList<Image> mImgPaths;
    public static SingleChoosePicFragment newInstance(String label) {
        SingleChoosePicFragment fragment = new SingleChoosePicFragment();
        Bundle args = new Bundle();
        args.putString(LABEL,label);
        fragment.setArguments(args);
        return fragment;
    }

    public SingleChoosePicFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            mLabel = getArguments().getString(LABEL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_single_choose_pic, container, false);
        view.setPadding(view.getPaddingLeft(), Divice.getStatusBarHeight(getContext()), view.getPaddingRight(), view.getPaddingBottom());
        mProgressBar = (ProgressBar) view.findViewById(R.id.mProgressBar);
        mProgressBar.setVisibility(View.VISIBLE);
        mGridView = (GridView) view.findViewById(R.id.mGridView);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UpdateHeadBgFragment fragment = UpdateHeadBgFragment.newInstance(mImgPaths.get(position).getData(), mLabel);
                getHostActivity().open(fragment,fragment);
            }
        });
        new LoadImg(this).execute();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.picture);
        }
        getHostActivity().display(9);
    }

    private  class LoadImg extends AsyncTask<Void,Void,ArrayList<Image>> {

        private final WeakReference<SingleChoosePicFragment> mWeakReference;
        public LoadImg(SingleChoosePicFragment fragment){
            mWeakReference = new WeakReference<>(fragment);
        }
        @Override
        protected ArrayList<Image> doInBackground(Void... params) {
            return ChooseImageService.queryImages(getActivity());
        }
        @Override
        protected void onPostExecute(ArrayList<Image> list) {
            super.onPostExecute(list);
            SingleChoosePicFragment fragment = mWeakReference.get();
            if (fragment != null){
                fragment.mImgPaths = list;
                fragment.mGridView.setAdapter(new SingleChoosePicAdapter(fragment,list));
                fragment.mProgressBar.setVisibility(View.GONE);
            }
        }
    }


}
