package com.nju.fragment;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.nju.View.CustomImageVIew;
import com.nju.View.GestureImageView;
import com.nju.View.SchoolFriendDialog;
import com.nju.activity.BitmapEvent;
import com.nju.activity.MessageEvent;
import com.nju.activity.R;
import com.nju.http.ImageDownloader;
import com.nju.image.BitmapDownloader;
import com.nju.util.Divice;
import com.nju.util.PathConstant;
import com.nju.util.ToastUtil;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


public class CircleImageViewItemFragment extends BaseFragment {

    private static final String TAG = CircleImageViewItemFragment.class.getSimpleName();
    private static final String BITMAP = "bitmap";
    private static final String PARAM_BASE_PATH = "basePath";
    private String imgPath;
    private String mBaseImgPath;
    private GestureImageView mCustomImageView;
    private static String[]  dialog_items ;
    private ImageDownloader.BitmapDownloaderTask downloaderTask;
    public static CircleImageViewItemFragment newInstance(String path,String basePath) {
        CircleImageViewItemFragment fragment = new CircleImageViewItemFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BITMAP, path);
        bundle.putString(PARAM_BASE_PATH,basePath);
        fragment.setArguments(bundle);
        return fragment;
    }

    public CircleImageViewItemFragment() {
    }

    SchoolFriendDialog.ListItemCallback callback = new SchoolFriendDialog.ListItemCallback() {
        @Override
        public void onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
            Toast.makeText(getContext(),"test",Toast.LENGTH_LONG).show();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            imgPath = getArguments().getString(BITMAP);
            mBaseImgPath = getArguments().getString(PARAM_BASE_PATH);
        }
        if (dialog_items == null) {
            dialog_items = getActivity().getResources().getStringArray(R.array.dialog_item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_circle_image_view_item, container, false);
        mCustomImageView = (GestureImageView) view.findViewById(R.id.fragment_circle_image_view_item_iamge);
        mCustomImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                SchoolFriendDialog dialog = SchoolFriendDialog.listDialog(getContext(), dialog_items);
                dialog.show();
                return false;
            }
        });
        mCustomImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHostActivity().getBackStack().pop();
                BaseFragment baseFragment = getHostActivity().getBackStack().peek();
                getHostActivity().open(baseFragment);
            }
        });

        final String url = PathConstant.IMAGE_PATH + mBaseImgPath+ imgPath;


        downloaderTask = ImageDownloader.with(getContext()).download(url, mCustomImageView);
        //Picasso.with(getContext()).load(url).into(mCustomImageView);
        return view;
    }



    public ImageDownloader.BitmapDownloaderTask getTask(){
        return downloaderTask;
    }

}
