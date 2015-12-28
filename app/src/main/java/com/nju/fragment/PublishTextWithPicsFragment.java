package com.nju.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.nju.View.SchoolFriendDialog;
import com.nju.activity.R;
import com.nju.adatper.EmotionPageAdapter;
import com.nju.adatper.NinePicsGridAdapter;
import com.nju.http.HttpManager;
import com.nju.http.ResponseCallback;
import com.nju.http.request.MultiImgRequest;
import com.nju.model.BitmapWrapper;
import com.nju.model.ImageWrapper;
import com.nju.util.Constant;
import com.nju.util.Divice;
import com.nju.util.SchoolFriendLayoutParams;
import com.nju.util.SoftInput;
import com.nju.util.StringBase64;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PublishTextWithPicsFragment extends BaseFragment {

    public static final String TAG = PublishTextWithPicsFragment.class.getSimpleName() ;
    private LinearLayout emoLineLayout = null;
    private LinearLayout mainLayout;
    private TextView mEmotionView;
    private boolean label = true;
    private ScrollView mScrollView;
    private boolean isEmotionOpen = true;
    private EditText mContentEditText;
    private ViewPager mViewPager;
    private int  rootHeight = 0;
    private int subHeight = 0;
    private List<ImageView> mUploadImgs;
    private ArrayList<ImageWrapper> mUploadImgPaths;
    private SchoolFriendLayoutParams schoolFriendLayoutParams;
    private AppBarLayout mAppBarLayout;
    private Button mFinishBn;
    private ArrayList<View> mSlideCircleViews;
    private int mSlidePosition = 0;
    private SchoolFriendDialog mDialog;
    private GridView mGridView;

    public static PublishTextWithPicsFragment newInstance(ArrayList<ImageWrapper> uploadImgPaths) {
        PublishTextWithPicsFragment fragment = new PublishTextWithPicsFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(TAG, uploadImgPaths);
        fragment.setArguments(args);
        return fragment;
    }

    public PublishTextWithPicsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUploadImgPaths = getArguments().getParcelableArrayList(TAG);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_publish_text_with_pics, container, false);
        view.setPadding(view.getPaddingLeft(),Divice.getStatusBarHeight(getActivity()),view.getPaddingRight(),view.getPaddingBottom());
        mAppBarLayout = (AppBarLayout) getActivity().findViewById(R.id.main_viewpager_appbar);
        emoLineLayout = (LinearLayout)view.findViewById(R.id.emotion_layout);
        mainLayout = (LinearLayout)view.findViewById(R.id.publish_wei_bo_main_layout);
        mEmotionView = (TextView)view.findViewById(R.id.emotion_icon);
        mScrollView = (ScrollView)view.findViewById(R.id.publish_wei_bo_scroll_layout);
        mContentEditText = (EditText)view.findViewById(R.id.publish_wei_bo_content_editText);
        mViewPager = (ViewPager)view.findViewById(R.id.emotion_pager);
        schoolFriendLayoutParams = new SchoolFriendLayoutParams(getActivity());
        mViewPager.setAdapter(new EmotionPageAdapter(getFragmentManager(), TAG));
        initViewPagerListener();
        initOnGlobalListener();
        initFloatingBn();
        //initUpladImageView(view);
        //initLoadImage();
        openChooseLocation(view);
        openWhoScan(view);
        initSlideCircleViews(view);
        initPicsGridView(view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getHostActivity().getToolBar().setTitle(getString(R.string.empty));
        getHostActivity().getMenuCameraView().setVisibility(View.GONE);
        getHostActivity().getMenuDeleteView().setVisibility(View.GONE);
        mFinishBn = getHostActivity().getMenuBn();
        mFinishBn.setText(getString(R.string.finish));
        mFinishBn.setVisibility(View.VISIBLE);
        initFinishBnEvent();
    }

    private void initSlideCircleViews(View view) {
        View mView;
        mSlideCircleViews = new ArrayList<>();
        mView = view.findViewById(R.id.emotion_pager_view1);
        mSlideCircleViews.add(mView);
        mView = view.findViewById(R.id.emotion_pager_view2);
        mSlideCircleViews.add(mView);
        mView = view.findViewById(R.id.emotion_pager_view3);
        mSlideCircleViews.add(mView);
    }

    private void initPicsGridView(View view) {
        mGridView = (GridView) view.findViewById(R.id.fragment_publish_text_with_pics_gridview);
        NinePicsGridAdapter adapter = new NinePicsGridAdapter(getContext(),mUploadImgPaths);
        mGridView.setAdapter(adapter);
    }


    private void initViewPagerListener() {
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mSlideCircleViews.get(mSlidePosition).setBackground(ContextCompat.getDrawable(getContext(), R.drawable.unselect_circle_label_bg));
                mSlidePosition = position;
                mSlideCircleViews.get(mSlidePosition).setBackground(ContextCompat.getDrawable(getContext(), R.drawable.select_circle_label_bg));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    ResponseCallback callback = new ResponseCallback() {

        @Override
        public void onFail(Exception error) {
            mDialog.dismiss();
        }

        @Override
        public void onSuccess(String responseBody) {
            Toast.makeText(getContext(),responseBody,Toast.LENGTH_LONG).show();
            mDialog.dismiss();
        }
    };

    private void initFinishBnEvent() {
        mFinishBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog = SchoolFriendDialog.showProgressDialogNoTitle(getContext(),getString(R.string.uploading));
                mDialog.show();
                SoftInput.close(getContext(),mFinishBn);
                String content = mContentEditText.getText().toString();
                final HashMap<String,String> params = new HashMap<>();
                int user_id = getHostActivity().getSharedPreferences().getInt(Constant.USER_ID, 1);
                params.put(Constant.USER_ID,String.valueOf(user_id));
                params.put(Constant.CONTENT, StringBase64.encode(content));
                final ArrayList<BitmapWrapper> bitmapWrappers = new ArrayList<>();
                BitmapWrapper bitmapWrapper;
                File sourceFile;
                for (ImageWrapper image :mUploadImgPaths) {
                    final String path = image.getPath();
                    bitmapWrapper = new BitmapWrapper();
                    sourceFile = new File(path);
                    bitmapWrapper.setPath(path);bitmapWrapper.setFileName(sourceFile.getName());
                    try {
                        bitmapWrapper.setFileType(sourceFile.toURL().openConnection().getContentType());
                        bitmapWrappers.add(bitmapWrapper);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                ArrayList<BitmapWrapper> bitmapWrapperArrayList = HttpManager.getInstance().compressBitmap(getContext(),bitmapWrappers);
                HttpManager.getInstance().exeRequest(new MultiImgRequest(Constant.BASE_URL + Constant.PUBLISH_TEXT_WITH_PIC_URL,params,bitmapWrapperArrayList,callback));

            }
        });
    }


    private void initOnGlobalListener() {
        mainLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                mScrollView.getWindowVisibleDisplayFrame(r);
                rootHeight = mainLayout.getRootView().getHeight();
                subHeight = r.bottom - r.top;
                if ((rootHeight - subHeight) < (rootHeight / 3) && label) {
                    emoLineLayout.setVisibility(View.GONE);
                    mScrollView.setLayoutParams(schoolFriendLayoutParams.noSoftInputParams(subHeight));
                } else if ((rootHeight - subHeight) < (rootHeight / 3) && isEmotionOpen) {
                    label = true;
                } else if ((rootHeight - subHeight) > (rootHeight / 3)) {
                    if(getHostActivity().isPhone()) {
                        mScrollView.setLayoutParams(schoolFriendLayoutParams.softInputParams(subHeight,45,mAppBarLayout));
                    } else {
                        mScrollView.setLayoutParams(schoolFriendLayoutParams.softInputParamsFrame(subHeight, 90));
                    }
                    emoLineLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void initFloatingBn() {
        mEmotionView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN && isEmotionOpen) {
                    mEmotionView.setText(getString(R.string.keyboard));
                    SoftInput.close(getActivity(), mEmotionView);
                    isEmotionOpen = false;
                    label = false;
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mEmotionView.setText(getString(R.string.smile));
                    SoftInput.open(getActivity());
                    isEmotionOpen = true;
                    label = false;
                }
                return true;
            }
        });
    }

//    private void initUpladImageView(View view) {
//        mUploadImgs = new ArrayList<>();
//        ImageView imageView ;
//        imageView = (ImageView)view.findViewById(R.id.activity_publish_weibo_image_im1);
//        mUploadImgs.add(imageView);
//        imageView = (ImageView)view.findViewById(R.id.activity_publish_weibo_image_im2);
//        mUploadImgs.add(imageView);
//        imageView = (ImageView)view.findViewById(R.id.activity_publish_weibo_image_im3);
//        mUploadImgs.add(imageView);
//
//        imageView = (ImageView)view.findViewById(R.id.activity_publish_weibo_image_im4);
//        mUploadImgs.add(imageView);
//        imageView = (ImageView)view.findViewById(R.id.activity_publish_weibo_image_im5);
//        mUploadImgs.add(imageView);
//        imageView = (ImageView)view.findViewById(R.id.activity_publish_weibo_image_im6);
//        mUploadImgs.add(imageView);
//
//        imageView = (ImageView)view.findViewById(R.id.activity_publish_weibo_image_im7);
//        mUploadImgs.add(imageView);
//        imageView = (ImageView)view.findViewById(R.id.activity_publish_weibo_image_im8);
//        mUploadImgs.add(imageView);
//        imageView = (ImageView)view.findViewById(R.id.activity_publish_weibo_image_im9);
//        mUploadImgs.add(imageView);
//
//    }

//    private void initLoadImage() {
//        int length = mUploadImgPaths.size();
//        int targetWidth;
//        if(getHostActivity().isPhone()){
//            targetWidth = Divice.dividerScreen(getActivity(), 4);
//        } else {
//            targetWidth = Divice.dividerScreen(getActivity(), 5);
//        }
//        for (int i = 0; i < length;i++){
//            mUploadImgs.get(i).setLayoutParams(SchoolFriendLayoutParams.phoneImageMarginBottom(getContext()));
//            Picasso.with(getActivity()).load(new File(mUploadImgPaths.get(i).getData())).resize(targetWidth,targetWidth).centerCrop()
//                    .into(mUploadImgs.get(i));
//            final int finalI = i;
//            mUploadImgs.get(i).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    getHostActivity().open(ChoosedImageViewFragment.newInstance(mUploadImgPaths,finalI));
//                 }
//            });
//        }
//    }

    public void inputEmotion(String text) {
        int selectionCursor = mContentEditText.getSelectionStart();
        mContentEditText.getText().insert(selectionCursor, text);
        mContentEditText.invalidate();
    }

    private void openChooseLocation(View view) {
        TextView textView = (TextView)view.findViewById(R.id.publish_wei_bo_userloaction_text);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.container,
                        UserLocationFragment.newInstance()).commit();
            }
        });
    }

    private void openWhoScan(View view) {
        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.fragment_publish_text_with_pics_relativeLayout);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.container,
                        WhoScanFragment.newInstance()).commit();
            }
        });
    }
}
