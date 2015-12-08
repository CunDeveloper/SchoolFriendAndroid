package com.nju.fragment;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nju.activity.R;
import com.nju.adatper.EmotionPageAdater;
import com.nju.model.Image;
import com.nju.util.Divice;
import com.nju.util.SchoolFriendLayoutParams;
import com.nju.util.SoftInput;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PublishTextWithPicsFragment extends BaseFragment {

    public static final String TAG = PublishTextWithPicsFragment.class.getSimpleName() ;
    private LinearLayout emoLineLayout = null;
    private LinearLayout mainLayout;
    private ImageView mEmotionView;
    private boolean label = true;
    private ScrollView mScrollView;
    private boolean isEmotionOpen = true;
    private EditText mContentEditText;
    private ViewPager mViewPager;
    private int  rootHeight = 0;
    private int subHeight = 0;
    private View mView1,mView2,mView3;
    private List<ImageView> mUploadImgs;
    private ArrayList<Image> mUploadImgPaths;
    private SchoolFriendLayoutParams schoolFriendLayoutParams;
    private AppBarLayout mAppBarLayout;
    private Button mFinishBn;

    public static PublishTextWithPicsFragment newInstance(ArrayList<Image> uploadImgPaths) {
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
        emoLineLayout = (LinearLayout)view.findViewById(R.id.publish_wei_bo_emto_layout);
        mainLayout = (LinearLayout)view.findViewById(R.id.publish_wei_bo_main_layout);
        mEmotionView = (ImageView)view.findViewById(R.id.publish_wei_bo_emotion_fab);
        mScrollView = (ScrollView)view.findViewById(R.id.publish_wei_bo_scroll_layout);
        mContentEditText = (EditText)view.findViewById(R.id.publish_wei_bo_content_editText);
        mViewPager = (ViewPager)view.findViewById(R.id.emotion_pager);
        mView1 = view.findViewById(R.id.emotion_pager_view1);
        mView2 = view.findViewById(R.id.emotion_pager_view2);
        mView3 = view.findViewById(R.id.emotion_pager_view3);
        schoolFriendLayoutParams = new SchoolFriendLayoutParams(getActivity());
        mViewPager.setAdapter(new EmotionPageAdater(getFragmentManager()));
        initViewPagerListener();
        initOnGlobalListener();
        initFloaingBn();
        initUpladImageView(view);
        initLoadImage();
        openChooseLocation(view);
        openWhoScan(view);
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

    private void initViewPagerListener() {
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                changeSlectColor(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initFinishBnEvent() {
        mFinishBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void changeSlectColor(int mPagerPosition) {
        if(0 == mPagerPosition) {
            mView1.setBackground(ContextCompat.getDrawable(getActivity(),R.drawable.select_circle_label_bg));
            mView2.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.unselect_circle_label_bg));
            mView3.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.unselect_circle_label_bg));
        }else if(1 == mPagerPosition) {
            mView2.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.select_circle_label_bg));
            mView1.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.unselect_circle_label_bg));
            mView3.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.unselect_circle_label_bg));
        }else if(2 == mPagerPosition) {
            mView3.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.select_circle_label_bg));
            mView1.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.unselect_circle_label_bg));
            mView2.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.unselect_circle_label_bg));
        }
    }

    private void initOnGlobalListener() {
        mainLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                mScrollView.getWindowVisibleDisplayFrame(r);
                rootHeight = mainLayout.getRootView().getHeight();
                subHeight = r.bottom - r.top;
//                int rootHeight = mainLayout.getRootView().getHeight();
//                int subHeight = mainLayout.getHeight();
                if ((rootHeight - subHeight) < (rootHeight / 3) && label) {
                    emoLineLayout.setVisibility(View.GONE);
                    mScrollView.setLayoutParams(schoolFriendLayoutParams.noSoftInputParams(subHeight));
                } else if ((rootHeight - subHeight) < (rootHeight / 3) && isEmotionOpen) {
                    label = true;
                } else if ((rootHeight - subHeight) > (rootHeight / 3)) {
                    if(Divice.isPhone()) {
                        mScrollView.setLayoutParams(schoolFriendLayoutParams.softInputParams(subHeight,45,mAppBarLayout));
                    } else {
                        mScrollView.setLayoutParams(schoolFriendLayoutParams.softInputParamsFrame(subHeight, 90));
                    }
                    emoLineLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void initFloaingBn() {
        mEmotionView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN && isEmotionOpen) {
                    mEmotionView.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.mipmap.ic_menu_emoticons));
                    SoftInput.close(getActivity(), mEmotionView);
                    isEmotionOpen = false;
                    label = false;
                } else if (event.getAction() == MotionEvent.ACTION_DOWN && !isEmotionOpen) {
                    mEmotionView.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.mipmap.ic_menu_emoticons));
                    SoftInput.open(getActivity());
                    isEmotionOpen = true;
                    label = false;
                }
                return true;
            }
        });
    }

    private void initUpladImageView(View view) {
        mUploadImgs = new ArrayList<>();
        ImageView imageView ;
        imageView = (ImageView)view.findViewById(R.id.activity_publish_weibo_image_im1);
        mUploadImgs.add(imageView);
        imageView = (ImageView)view.findViewById(R.id.activity_publish_weibo_image_im2);
        mUploadImgs.add(imageView);
        imageView = (ImageView)view.findViewById(R.id.activity_publish_weibo_image_im3);
        mUploadImgs.add(imageView);

        imageView = (ImageView)view.findViewById(R.id.activity_publish_weibo_image_im4);
        mUploadImgs.add(imageView);
        imageView = (ImageView)view.findViewById(R.id.activity_publish_weibo_image_im5);
        mUploadImgs.add(imageView);
        imageView = (ImageView)view.findViewById(R.id.activity_publish_weibo_image_im6);
        mUploadImgs.add(imageView);

        imageView = (ImageView)view.findViewById(R.id.activity_publish_weibo_image_im7);
        mUploadImgs.add(imageView);
        imageView = (ImageView)view.findViewById(R.id.activity_publish_weibo_image_im8);
        mUploadImgs.add(imageView);
        imageView = (ImageView)view.findViewById(R.id.activity_publish_weibo_image_im9);
        mUploadImgs.add(imageView);

    }

    private void initLoadImage() {
        int length = mUploadImgPaths.size();
        int targetWidth;
        if(Divice.isPhone()){
            targetWidth = Divice.dividerScreen(getActivity(), 4);
        } else {
            targetWidth = Divice.dividerScreen(getActivity(), 5);
        }
        for (int i = 0; i < length;i++){
            mUploadImgs.get(i).setLayoutParams(SchoolFriendLayoutParams.phoneImageMarginBottom(getContext()));
            Picasso.with(getActivity()).load(new File(mUploadImgPaths.get(i).getData())).resize(targetWidth,targetWidth).centerCrop()
                    .into(mUploadImgs.get(i));
            final int finalI = i;
            mUploadImgs.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getFragmentManager().beginTransaction()
                            .replace(R.id.container,ChoosedImageViewFragment.newInstance(mUploadImgPaths,finalI)).commit();
                }
            });
        }
    }

    public void inputEmotion(Drawable drawable) {
        int selectionCursor = mContentEditText.getSelectionStart();
        mContentEditText.getText().insert(selectionCursor, ".");
        selectionCursor = mContentEditText.getSelectionStart();
        SpannableStringBuilder builder = new SpannableStringBuilder(mContentEditText.getText());
        builder.setSpan(new ImageSpan(drawable
        ), selectionCursor - ".".length(), selectionCursor, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mContentEditText.setText(builder);
        mContentEditText.setSelection(selectionCursor);
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
