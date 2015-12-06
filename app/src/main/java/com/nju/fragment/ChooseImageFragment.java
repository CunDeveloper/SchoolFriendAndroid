package com.nju.fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nju.activity.PublishTextWithPicsActivity;
import com.nju.activity.R;
import com.nju.adatper.ChooseImageAdapter;
import com.nju.model.Image;
import com.nju.service.ChoosedImageService;

import java.util.ArrayList;
import java.util.List;

public class ChooseImageFragment extends Fragment {

    public static final String TAG = ChooseImageFragment.class.getSimpleName();
    private ProgressBar mProgressBar;
    private GridView mGridView;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private ArrayList<Image> mImgPaths ;
    private ArrayList<Image> chooseImgPaths = new ArrayList<>();
    private ChooseImageAdapter mAdapter;
    private Button mFinishBn;
    private TextView mReviewTv;
    private Handler mHandler = new Handler(){
        private boolean label = true;
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == ChooseImageAdapter.CHOOSE_OK) {
                int choosedPicNumber = (int) msg.obj;
                finishReviewToggle(choosedPicNumber,label,msg);
            }
            if (msg.what == ChooseImageAdapter.ADD_PIC_OK) {
                int postion = (int) msg.obj;
                chooseImgPaths.add(mImgPaths.get(postion));
            }
            if (msg.what == ChooseImageAdapter.REMOVE_PIC_OK) {
                int position = (int) msg.obj;
                chooseImgPaths.remove(mImgPaths.get(position).getData());
            }
        }
    };

    public static ChooseImageFragment newInstance() {
      return new ChooseImageFragment();
    }

    public ChooseImageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_image, container, false);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        mImgPaths = new ArrayList<>();
        mGridView = (GridView)view.findViewById(R.id.activity_choose_images_gridView);
        mProgressBar = (ProgressBar)view.findViewById(R.id.activity_choose_images_progressBar);
        mProgressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
        mFinishBn = (Button) getActivity().findViewById(R.id.activity_choose_images_finish_button);
        mReviewTv = (TextView)view.findViewById(R.id.activity_choose_image_review_textView);
        initFinishBnEvent();
        initReviewText();
        initGridItemClistener();
        new loadImg().execute();
        return view;
    }


    private void initGridItemClistener() {
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if(position == 0) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                }
            }
        });
    }
    private void initFinishBnEvent () {
        mFinishBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),PublishTextWithPicsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(getString(R.string.IMGS), chooseImgPaths);
                intent.putExtras(bundle);
                startActivity(intent);
                ChooseImageFragment.this.onDestroy();
                getActivity().finish();
            }
        });
    }

    private void initReviewText() {
        mReviewTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.activity_choose_container, CameraImageViewFragment.newInstance(chooseImgPaths,0,getString(R.string.choosedReview))).
                        commit();
            }
        });
    }

    private void finishReviewToggle(int choosedPicNumber,boolean label,Message msg) {
        if (choosedPicNumber != 0) {
            if (label) {
                mFinishBn.setEnabled(true);
                mReviewTv.setEnabled(true);
                mFinishBn.setAlpha(1.0F);
                mReviewTv.setAlpha(1.0F);
                label = false;
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
            label = true;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == getActivity().RESULT_OK){

        }
    }

    private class loadImg extends AsyncTask<Void,Void,ArrayList<Image>> {

        @Override
        protected ArrayList<Image> doInBackground(Void... params) {
            return ChoosedImageService.queryImages(getActivity());
        }
        @Override
        protected void onPostExecute(ArrayList<Image> list) {
            super.onPostExecute(list);
            mImgPaths = list;
            AppCompatActivity activity = (AppCompatActivity) getActivity();
            mGridView.setAdapter(new ChooseImageAdapter(activity,list,mHandler));
            mProgressBar.setVisibility(View.GONE);
        }
    }
}
