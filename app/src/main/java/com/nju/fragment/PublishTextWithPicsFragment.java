package com.nju.fragment;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
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

public class PublishTextWithPicsFragment extends Fragment {

    public static final String TAG = PublishTextWithPicsFragment.class.getSimpleName() ;
    private List<Drawable> lists = null;
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
    private AppBarLayout mAppBarLayout;
    private SchoolFriendLayoutParams schoolFriendLayoutParams;

    public static PublishTextWithPicsFragment newInstance(ArrayList<String> uploadImgPaths) {
        PublishTextWithPicsFragment fragment = new PublishTextWithPicsFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(TAG, uploadImgPaths);
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
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.publish_text_with_pics_toolbar);
        Button button = (Button) toolbar.findViewById(R.id.publish_text_with_pics_toolbar_finish_button);
        button.setVisibility(View.VISIBLE);
        mAppBarLayout = (AppBarLayout) getActivity().findViewById(R.id.publish_text_with_activity_appbar);
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

    private void changeSlectColor(int mPagerPosition) {
        if(0 == mPagerPosition) {
            mView1.setBackground(getResources().getDrawable(R.drawable.select_circle_label_bg));
            mView2.setBackground(getResources().getDrawable(R.drawable.unselect_circle_label_bg));
            mView3.setBackground(getResources().getDrawable(R.drawable.unselect_circle_label_bg));
        }else if(1 == mPagerPosition) {
            mView2.setBackground(getResources().getDrawable(R.drawable.select_circle_label_bg));
            mView1.setBackground(getResources().getDrawable(R.drawable.unselect_circle_label_bg));
            mView3.setBackground(getResources().getDrawable(R.drawable.unselect_circle_label_bg));
        }else if(2 == mPagerPosition) {
            mView3.setBackground(getResources().getDrawable(R.drawable.select_circle_label_bg));
            mView1.setBackground(getResources().getDrawable(R.drawable.unselect_circle_label_bg));
            mView2.setBackground(getResources().getDrawable(R.drawable.unselect_circle_label_bg));
        }
    }

    private void initOnGlobalListener() {
        mainLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                //r will be populated with the coordinates of your view that area still visible.
                mainLayout.getWindowVisibleDisplayFrame(r);
                int heightDiff = mainLayout.getRootView().getHeight() - (r.bottom - r.top);
                rootHeight = mainLayout.getRootView().getHeight();
                subHeight = r.bottom - r.top;
                if ((rootHeight - subHeight) < (rootHeight / 3) && label) {
                    emoLineLayout.setVisibility(View.GONE);
                    mScrollView.setLayoutParams(schoolFriendLayoutParams.noSoftInputParams(subHeight));
                } else if ((rootHeight - subHeight) < (rootHeight / 3) && isEmotionOpen) {
                    label = true;
                } else if ((rootHeight - subHeight) > (rootHeight / 3)) {
                    if(Divice.isPhone()) {
                        mScrollView.setLayoutParams(schoolFriendLayoutParams.softInputParams(subHeight, 50, mAppBarLayout));
                    } else {
                        mScrollView.setLayoutParams(schoolFriendLayoutParams.softInputParams(subHeight, 90));
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
                    mEmotionView.setImageDrawable(getResources().getDrawable(R.mipmap.ic_menu_emoticons));
                    SoftInput.close(getActivity(), mEmotionView);
                    isEmotionOpen = false;
                    label = false;
                } else if (event.getAction() == MotionEvent.ACTION_DOWN && !isEmotionOpen) {
                    mEmotionView.setImageDrawable(getResources().getDrawable(R.mipmap.ic_menu_emoticons));
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
        ImageView imageView = null;
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
                            .replace(R.id.activity_publish_text_with_pics_main,ChoosedImageViewFragment.newInstance(mUploadImgPaths,finalI)).commit();
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
                getFragmentManager().beginTransaction().replace(R.id.activity_publish_text_with_pics_main,
                        UserLocationFragment.newInstance()).commit();
            }
        });
    }

    private void openWhoScan(View view) {
        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.fragment_publish_text_with_pics_relativeLayout);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.activity_publish_text_with_pics_main,
                        WhoScanFragment.newInstance()).commit();
            }
        });
    }
}
