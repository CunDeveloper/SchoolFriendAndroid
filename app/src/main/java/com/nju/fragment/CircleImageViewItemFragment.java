package com.nju.fragment;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.nju.View.CustomImageVIew;
import com.nju.View.SchoolFriendDialog;
import com.nju.activity.R;
import com.nju.http.ImageDownloader;
import com.nju.util.PathConstant;


public class CircleImageViewItemFragment extends Fragment {

    private static final String TAG = CircleImageViewItemFragment.class.getSimpleName();
    private static final String BITMAP = "bitmap";
    private String imgPath;
    private CustomImageVIew mCustomImageView;
    private static String[]  dialog_items ;
    public static CircleImageViewItemFragment newInstance(String path) {
        CircleImageViewItemFragment fragment = new CircleImageViewItemFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BITMAP, path);
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
        mCustomImageView = (CustomImageVIew) view.findViewById(R.id.fragment_circle_image_view_item_iamge);
        mCustomImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                SchoolFriendDialog dialog = SchoolFriendDialog.listDialog(getContext(), dialog_items, callback);
                dialog.show();
                return true;
            }
        });
        mCustomImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        final String url = PathConstant.IMAGE_PATH + PathConstant.ALUMNI_TALK_IMG_PATH + imgPath;
        Log.i(TAG,url);
        ImageDownloader.download(url,mCustomImageView);
        return view;
    }


}
