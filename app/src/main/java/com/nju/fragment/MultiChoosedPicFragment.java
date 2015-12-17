package com.nju.fragment;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nju.activity.R;
import com.nju.adatper.MultiChoosedPicAdapter;
import com.nju.model.Image;
import com.nju.service.ChoosedImageService;
import com.nju.util.CapturePic;
import com.nju.util.Constant;
import com.nju.util.Divice;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class MultiChoosedPicFragment extends BaseFragment {

    public static final String TAG = MultiChoosedPicFragment.class.getSimpleName();
    private ProgressBar mProgressBar;
    private GridView mGridView;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private ArrayList<Image> mImgPaths ;
    private ArrayList<Image> chooseImgPaths = new ArrayList<>();
    private Button mFinishBn;
    private TextView mReviewTv;
    private final  MyHandler mHandler = new MyHandler(this);
    public static MultiChoosedPicFragment newInstance() {
        return new MultiChoosedPicFragment();
    }

    public MultiChoosedPicFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        initGridItemClistener();
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
        mFinishBn.setAlpha(0.5F);
        mFinishBn.setVisibility(View.VISIBLE);
        initFinishBnEvent();
    }

    private void initGridItemClistener() {
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if(position == 0) {
                    new CapturePic(MultiChoosedPicFragment.this).dispatchTakePictureIntent();
                }
            }
        });
    }
    private void initFinishBnEvent () {
        mFinishBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHostActivity().open(PublishTextWithPicsFragment.newInstance(chooseImgPaths));
            }
        });
    }

    private void initReviewText() {
        mReviewTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.container, CameraImageViewFragment.newInstance(chooseImgPaths,0,getString(R.string.choosedReview))).
                        commit();
            }
        });
    }

    private  void finishReviewToggle(int choosedPicNumber,boolean label,Message msg) {
        if (choosedPicNumber != 0) {
            if (label) {
                mFinishBn.setEnabled(true);
                mReviewTv.setEnabled(true);
                mFinishBn.setAlpha(1.0F);
                mReviewTv.setAlpha(1.0F);
            }
            mFinishBn.setText(getResources().getString(R.string.finish)+"("+msg.obj+"/9)");
            mReviewTv.setText(getResources().getString(R.string.review)+"("+msg.obj+")");
            mFinishBn.invalidate();
            mReviewTv.invalidate();
        }
        else{
            mFinishBn.setAlpha(0.5f);
            mReviewTv.setAlpha(0.5f);
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
            CapturePic.galleryAddPic(MultiChoosedPicFragment.this);
            Image image = new Image();
            image.setData(CapturePic.getImgPath());
            ArrayList<Image> images = new ArrayList<>();
            images.add(image);
            getHostActivity().open(CameraImageViewFragment.newInstance(images,0,getString(R.string.capture_image)));
        }
    }

    private  class loadImg extends AsyncTask<Void,Void,ArrayList<Image>> {

        @Override
        protected ArrayList<Image> doInBackground(Void... params) {
            return ChoosedImageService.queryImages(getActivity());
        }
        @Override
        protected void onPostExecute(ArrayList<Image> list) {
            super.onPostExecute(list);
            mImgPaths = list;
            AppCompatActivity activity = (AppCompatActivity) getActivity();
            mGridView.setAdapter(new MultiChoosedPicAdapter(activity,list,mHandler,MultiChoosedPicFragment.this));
            mProgressBar.setVisibility(View.GONE);
        }
    }

    private static class MyHandler extends  Handler {
        private final WeakReference<MultiChoosedPicFragment> mChooedPicFragment;
        private boolean label = true;
        private MyHandler(MultiChoosedPicFragment chooedPicFragment) {
            this.mChooedPicFragment = new WeakReference<>(chooedPicFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            MultiChoosedPicFragment fragment = mChooedPicFragment.get();
            if (fragment != null) {
                super.handleMessage(msg);
                if (msg.what == MultiChoosedPicAdapter.CHOOSE_OK) {
                    int choosedPicNumber = (int) msg.obj;
                    fragment.finishReviewToggle(choosedPicNumber,label,msg);
                }
                if (msg.what == MultiChoosedPicAdapter.ADD_PIC_OK) {
                    int postion = (int) msg.obj;
                    fragment.chooseImgPaths.add(fragment.mImgPaths.get(postion));
                }
                if (msg.what == MultiChoosedPicAdapter.REMOVE_PIC_OK) {
                    int position = (int) msg.obj;
                    fragment.chooseImgPaths.remove(fragment.mImgPaths.get(position));
                }
            }
        }
    }

}
