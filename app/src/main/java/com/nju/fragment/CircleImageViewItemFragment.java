package com.nju.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.nju.View.SchoolFriendDialog;
import com.nju.activity.R;
import com.nju.http.ImageDownloader;
import com.nju.util.PathConstant;


public class CircleImageViewItemFragment extends BaseFragment {

    private static final String TAG = CircleImageViewItemFragment.class.getSimpleName();
    private static final String BITMAP = "bitmap";
    private static final String PARAM_BASE_PATH = "basePath";
    private static String[] dialog_items;
    SchoolFriendDialog.ListItemCallback callback = new SchoolFriendDialog.ListItemCallback() {
        @Override
        public void onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
            Toast.makeText(getContext(), "test", Toast.LENGTH_LONG).show();
        }
    };
    private String imgPath;
    private String mBaseImgPath;
    private ImageView mCustomImageView;
    private ImageDownloader.BitmapDownloaderTask downloaderTask;

    public CircleImageViewItemFragment() {
    }

    public static CircleImageViewItemFragment newInstance(String path, String basePath) {
        CircleImageViewItemFragment fragment = new CircleImageViewItemFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BITMAP, path);
        bundle.putString(PARAM_BASE_PATH, basePath);
        fragment.setArguments(bundle);
        return fragment;
    }

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
        mCustomImageView = (ImageView) view.findViewById(R.id.fragment_circle_image_view_item_iamge);

        mCustomImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHostActivity().getBackStack().pop();
                BaseFragment baseFragment = getHostActivity().getBackStack().peek();
                getHostActivity().open(baseFragment);
            }
        });

        final String url = PathConstant.IMAGE_PATH + mBaseImgPath + imgPath;
        final String smallUrl = PathConstant.IMAGE_PATH_SMALL + mBaseImgPath + imgPath;
        ImageView smallImg = (ImageView) view.findViewById(R.id.smallImage);
        downloaderTask = ImageDownloader.with(getContext()).download(smallUrl, smallImg);
        downloaderTask = ImageDownloader.with(getContext()).download(url, mCustomImageView);

        return view;
    }


    public ImageDownloader.BitmapDownloaderTask getTask() {
        return downloaderTask;
    }

}
