package com.example.xiaojuzhang.test1;



import android.app.Service;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;


import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.nju.activity.R;
import com.nju.adatper.EmotionAdatper;
import com.nju.util.Divice;
import com.nju.util.Emotion;

import java.util.ArrayList;
import java.util.List;

public class PublishWeiBoFragment extends Fragment {

    private static final String TAG = PublishWeiBoFragment.class.getSimpleName() ;
    private static PublishWeiBoFragment fragment = null;
    private  List<Drawable> lists = null;
    private LinearLayout emoLineLayout = null;
    private LinearLayout mainLayout;
    private FloatingActionButton mFloatingAbn;
    private int softKeyWordHeiht = 0;
    private int subTopHeight = 0 ;
    private boolean label = true;
    private InputMethodManager imm;
    private GridLayout gridLayout;
    private ScrollView mScrollView;
    private boolean isEmotionOpen = true;
    private EditText mContentEditText;
    private GridView mEmotionGridView;
    private List<Drawable> mEmotions;
    public static PublishWeiBoFragment newInstance() {
        if (fragment == null) {
           fragment = new PublishWeiBoFragment();
        }
        return fragment;
    }

    public PublishWeiBoFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lists = new ArrayList<Drawable>();
        lists.add(getResources().getDrawable(R.drawable.cheese_1));
        lists.add(getResources().getDrawable(R.drawable.cheese_2));
        lists.add(getResources().getDrawable(R.drawable.cheese_3));
        lists.add(getResources().getDrawable(R.drawable.cheese_4));
        lists.add(getResources().getDrawable(R.drawable.cheese_5));
        lists.add(getResources().getDrawable(R.drawable.cheese_1));
        lists.add(getResources().getDrawable(R.drawable.cheese_2));
        imm =  (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        mEmotions = new Emotion(getActivity()).getEmotions();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_publish_wei_bo, container, false);
        gridLayout = (GridLayout) view.findViewById(R.id.publish_wei_bo_gridView);
        emoLineLayout = (LinearLayout) view.findViewById(R.id.publish_wei_bo_emto_layout);
        mainLayout = (LinearLayout) view.findViewById(R.id.publish_wei_bo_main_layout);
        mFloatingAbn = (FloatingActionButton) view.findViewById(R.id.publish_wei_bo_emotion_fab);
        mScrollView = (ScrollView) view.findViewById(R.id.publish_wei_bo_scroll_layout);
        mContentEditText = (EditText) view.findViewById(R.id.publish_wei_bo_content_editText);
        mEmotionGridView = (GridView) view.findViewById(R.id.publish_wei_bo_emotion_gridview);
        mEmotionGridView.setAdapter(new EmotionAdatper(mEmotions, getActivity()));
        initOnGlobalListener();
        initFloaingBn();
        initScrollListener();
        initGridViewListener();
        addImage();
        return view;
    }

    private void initScrollListener() {
          mScrollView.setFocusable(true);
          mScrollView.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  imm.hideSoftInputFromWindow(mFloatingAbn.getWindowToken(), 0);
              }
          });
    }

    private void initGridViewListener() {
        mEmotionGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int selectionCursor = mContentEditText.getSelectionStart();
                mContentEditText.getText().insert(selectionCursor, ".");
                selectionCursor = mContentEditText.getSelectionStart();
                SpannableStringBuilder builder = new SpannableStringBuilder(mContentEditText.getText());
                builder.setSpan(new ImageSpan(mEmotions.get(position)), selectionCursor - ".".length(), selectionCursor, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                mContentEditText.setText(builder);
                mContentEditText.setSelection(selectionCursor);
                mContentEditText.invalidate();
            }
        });
    }

    private void initOnGlobalListener() {
        mainLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                 int  rootHeight = mainLayout.getRootView().getHeight();
                 int subHeight = mainLayout.getHeight();

                if ((rootHeight -subHeight) < (rootHeight/ 3)&&label) {
                    emoLineLayout.setVisibility(View.GONE);
                    subTopHeight = rootHeight - subHeight;
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,subHeight);
                    mScrollView.setLayoutParams(params);
                }else if ((rootHeight -subHeight) < (rootHeight/ 3)&&isEmotionOpen){
                     label =true;
                }
                else if((rootHeight -subHeight) > (rootHeight/ 3)) {
                    int scrollWidth = subHeight-subTopHeight;
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,scrollWidth);
                    mScrollView.setLayoutParams(params);
                    emoLineLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void initFloaingBn() {


        mFloatingAbn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN && isEmotionOpen){
                    mFloatingAbn.setImageDrawable(getResources().getDrawable(R.drawable.ic_notification_ime_default));
                    imm.hideSoftInputFromWindow(mFloatingAbn.getWindowToken(), 0);
                    isEmotionOpen = false;
                    label = false;
                }
                else if(event.getAction() == MotionEvent.ACTION_DOWN && !isEmotionOpen) {
                    mFloatingAbn.setImageDrawable(getResources().getDrawable(R.mipmap.ic_menu_emoticons));
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
                    isEmotionOpen = true;
                    label = false;
                }

                return true;
            }
        });
    }
    private void addImage() {
        ImageView view = (ImageView) LayoutInflater.from(getActivity()).inflate(R.layout.publish_content_image_item, null, false);
        int screenWidth = Divice.getDisplayWidth(getActivity());
        float paddingLeft = getResources().getDimension(R.dimen.padding_right);
        int paddingWidth = (int) Divice.convertDpToPixel(2*paddingLeft,getActivity());
        int width = (screenWidth-paddingWidth)/4;
        int height = width;
        AbsListView.LayoutParams parms = new AbsListView.LayoutParams(width,height);
        view.setLayoutParams(parms);
        view.setImageDrawable(lists.get(0));
        gridLayout.addView(view);
        ViewGroup viewGroup = (ViewGroup) view.getParent();
        if(viewGroup!=null) {
            viewGroup.removeView(view);
        }
        gridLayout.addView(view);

    }
    private class ImageAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return lists.size();
        }

        @Override
        public Object getItem(int position) {
            return lists.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (viewHolder == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.publish_content_image_item,null);
                viewHolder.imageView = (ImageView) convertView;
                int screenWidth = Divice.getDisplayWidth(getActivity());
                float paddingLeft = getResources().getDimension(R.dimen.padding_right);

                int paddingWidth = (int) Divice.convertDpToPixel(2*paddingLeft,getActivity());
                int width = (screenWidth-paddingWidth)/4;
                int height = width;

                AbsListView.LayoutParams parms = new AbsListView.LayoutParams(width,height);
                viewHolder.imageView.setLayoutParams(parms);

                convertView.setTag(viewHolder);

            }
            else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.imageView.setBackground(lists.get(position));
            return convertView;
        }
    }
    private class ViewHolder {
        public ImageView imageView;
    }


}
