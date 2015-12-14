package com.nju.fragment;


import android.graphics.Bitmap;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.nju.View.CustomImageVIew;
import com.nju.View.SchoolFriendDialog;
import com.nju.activity.R;


public class CircleImageViewItemFragment extends Fragment {

    private static final String BITMAP = "bitmap";
    private Bitmap mBitmap;
    private CustomImageVIew mCustomImageView;
    private static String[]  dialog_items ;
    public static CircleImageViewItemFragment newInstance(Bitmap bitmap) {
        CircleImageViewItemFragment fragment = new CircleImageViewItemFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(BITMAP,bitmap);
        fragment.setArguments(bundle);
        return fragment;
    }

    public CircleImageViewItemFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
             mBitmap = getArguments().getParcelable(BITMAP);
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
                 SchoolFriendDialog dialog =SchoolFriendDialog.listDialog(getContext(), dialog_items, null);
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
        mCustomImageView.setImageBitmap(mBitmap);
        return view;
    }


}
