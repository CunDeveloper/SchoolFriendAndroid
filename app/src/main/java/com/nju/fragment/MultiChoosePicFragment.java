package com.nju.fragment;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nju.activity.R;
import com.nju.adatper.MultiChoosePicAdapter;
import com.nju.model.Image;
import com.nju.service.ChooseImageService;
import com.nju.util.AsyncCompress;
import com.nju.util.CapturePic;
import com.nju.util.Divice;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class MultiChoosePicFragment extends BaseFragment {
    private static final String PARAM_LABEL = "paramLabel";
    private String mLabel;
    public static final String TAG = MultiChoosePicFragment.class.getSimpleName();
    private static final float OPAQUE = 1.0F;
    private static final float TRANSLUCENT = 0.5F;
    private static final String LEFT_BRACKETS ="(";
    private static final String RIGHT_BRACKETS = ")";
    private static final String SLASH = "/";
    private static final int MAX_NUMBER_OF_PICS = 9;
    private ProgressBar mProgressBar;
    private GridView mGridView;
    private ArrayList<Image> mImgPaths ;
    private ArrayList<Image> chooseImgPaths = new ArrayList<>();
    private Button mFinishBn;
    private TextView mReviewTv;
    private final  MyHandler mHandler = new MyHandler(this);

    public static MultiChoosePicFragment newInstance(String label) {
        MultiChoosePicFragment choosePicFragment = new MultiChoosePicFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_LABEL,label);
        choosePicFragment.setArguments(bundle);
        return choosePicFragment;
    }

    public MultiChoosePicFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null){
            mLabel = getArguments().getString(PARAM_LABEL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_multi_choosed_pic, container, false);
        view.setPadding(view.getPaddingLeft(),Divice.getStatusBarHeight(getActivity()),view.getPaddingRight(),view.getPaddingBottom());
        mImgPaths = new ArrayList<>();
        mGridView = (GridView)view.findViewById(R.id.activity_choose_images_gridView);
        mProgressBar = (ProgressBar)view.findViewById(R.id.activity_choose_images_progressBar);
        mProgressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getActivity(),R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
        mReviewTv = (TextView)view.findViewById(R.id.activity_choose_image_review_textView);
        initReviewText();
        initGridItemClickListener();
        new loadImg().execute();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getHostActivity().getToolBar().setTitle(getString(R.string.picture));
        getHostActivity().getMenuCameraView().setVisibility(View.GONE);
        getHostActivity().getMenuDeleteView().setVisibility(View.GONE);
        mFinishBn = getHostActivity().getMenuBn();
        mFinishBn.setEnabled(false);
        mFinishBn.setText(getString(R.string.finish));
        mFinishBn.setAlpha(TRANSLUCENT);
        mFinishBn.setVisibility(View.VISIBLE);
        initFinishBnEvent();
    }

    private void initGridItemClickListener() {
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if(position == 0) {
                    new CapturePic(MultiChoosePicFragment.this).dispatchTakePictureIntent();
                }
            }
        });
    }
    @SuppressWarnings("unchecked")
    private void initFinishBnEvent () {
        mFinishBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncCompress(MultiChoosePicFragment.this,mLabel).execute(chooseImgPaths);
            }
        });
    }

    private void initReviewText() {
        mReviewTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHostActivity().open(CameraImageViewFragment.newInstance(chooseImgPaths,0,getString(R.string.choosedReview),mLabel));

            }
        });
    }

    private  void finishReviewToggle(int choosePicNumber,boolean label,Message msg) {
        if (choosePicNumber != 0) {
            if (label) {
                mFinishBn.setEnabled(true);
                mReviewTv.setEnabled(true);
                mFinishBn.setAlpha(OPAQUE);
                mReviewTv.setAlpha(OPAQUE);
            }
            mFinishBn.setText(getResources().getString(R.string.finish)+LEFT_BRACKETS+msg.obj+SLASH+MAX_NUMBER_OF_PICS+RIGHT_BRACKETS);
            mReviewTv.setText(getResources().getString(R.string.review)+LEFT_BRACKETS+msg.obj+RIGHT_BRACKETS);
            mFinishBn.invalidate();
            mReviewTv.invalidate();
        }
        else{
            mFinishBn.setAlpha(TRANSLUCENT);
            mReviewTv.setAlpha(TRANSLUCENT);
            mFinishBn.setText(getResources().getString(R.string.finish));
            mReviewTv.setText(getResources().getString(R.string.review));
            mReviewTv.setEnabled(true);
            mReviewTv.setEnabled(true);
            mFinishBn.invalidate();
            mReviewTv.invalidate();

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CapturePic.REQUEST_TAKE_PHOTO && resultCode == FragmentActivity.RESULT_OK){
            CapturePic.galleryAddPic(MultiChoosePicFragment.this);
            Image image = new Image();
            image.setData(CapturePic.getImgPath());
            ArrayList<Image> images = new ArrayList<>();
            images.add(image);
            getHostActivity().open(CameraImageViewFragment.newInstance(images,0,getString(R.string.capture_image),mLabel),this);
        }
    }

    private  class loadImg extends AsyncTask<Void,Void,ArrayList<Image>> {

        @Override
        protected ArrayList<Image> doInBackground(Void... params) {
            return ChooseImageService.queryImages(getActivity());
        }
        @Override
        protected void onPostExecute(ArrayList<Image> list) {
            super.onPostExecute(list);
            mImgPaths = list;
            AppCompatActivity activity = (AppCompatActivity) getActivity();
            mGridView.setAdapter(new MultiChoosePicAdapter(activity,list,mHandler,MultiChoosePicFragment.this,mLabel));
            mProgressBar.setVisibility(View.GONE);
        }
    }

    private static class MyHandler extends  Handler {
        private final WeakReference<MultiChoosePicFragment> mChoosePicFragment;
        private boolean label = true;
        private MyHandler(MultiChoosePicFragment choosePicFragment) {
            this.mChoosePicFragment = new WeakReference<>(choosePicFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            MultiChoosePicFragment fragment = mChoosePicFragment.get();
            if (fragment != null) {
                super.handleMessage(msg);
                if (msg.what == MultiChoosePicAdapter.CHOOSE_OK) {
                    int choosePicNumber = (int) msg.obj;
                    fragment.finishReviewToggle(choosePicNumber,label,msg);
                }
                if (msg.what == MultiChoosePicAdapter.ADD_PIC_OK) {
                    int position = (int) msg.obj;
                    fragment.chooseImgPaths.add(fragment.mImgPaths.get(position));
                }
                if (msg.what == MultiChoosePicAdapter.REMOVE_PIC_OK) {
                    int position = (int) msg.obj;
                    fragment.chooseImgPaths.remove(fragment.mImgPaths.get(position));
                }
            }
        }
    }

}
