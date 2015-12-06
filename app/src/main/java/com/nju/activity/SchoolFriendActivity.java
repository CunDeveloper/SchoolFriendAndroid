package com.nju.activity;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.VelocityTrackerCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.nju.View.SchoolFriendListView;
import com.nju.adatper.FriendContentAdapter;
import com.nju.adatper.UserCommentItemListAdapter;
import com.nju.model.Comment;
import com.nju.model.FriendWeibo;
import com.nju.test.WeiBoData;
import com.nju.util.Divice;
import com.nju.util.SchoolFriendLayoutParams;
import com.nju.util.SoftInput;
import java.util.List;

public class SchoolFriendActivity extends AppCompatActivity {

    private static final  String TAG = SchoolFriendActivity.class.getSimpleName();
    private ListView mListView;
    private EditText mCommentEditText;
    private Button mSendButton;
    private List<FriendWeibo> weibos;
    private List<FriendWeibo> sumWeiBos;
    private FriendContentAdapter mFriendContentAdapter;
    private LinearLayout mMainLayout;
    private ImageView mEmotionImageView;
    private RelativeLayout mInputLayout;
    private AppBarLayout mAppBarLayout;
    private SchoolFriendLayoutParams schoolFriendLayoutParams;
    private boolean label = false;
    private int mPosition = 1;
    private int startP;
    private int endP;
    private LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//    private SwipeRefreshLayout mRefreshLayout;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == UserCommentItemListAdapter.PRAISE_OK) {
                mFriendContentAdapter.notifyDataSetChanged();
            }
            if(msg.what == UserCommentItemListAdapter.COMMENT_OK) {
                mFriendContentAdapter.notifyDataSetChanged();
                mListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                SoftInput.open(getApplicationContext());
                mPosition = (int) msg.obj;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_friend);
        Toolbar toolbar = (Toolbar) findViewById(R.id.school_friend_toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        mListView = (ListView) findViewById(R.id.school_friend_listview);
        mMainLayout = (LinearLayout) findViewById(R.id.activity_school_friend_main);
        if (Build.VERSION.SDK_INT>19) {
            mMainLayout.setPadding(mMainLayout.getPaddingLeft(), getStatusBarHeight()
                    , mMainLayout.getPaddingRight(), mMainLayout.getBottom());
//            AppBarLayout.LayoutParams params = new AppBarLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            params.setMargins(0, getStatusBarHeight(), 0, 0);
//            toolbar.setLayoutParams(params);
        }
        mInputLayout = (RelativeLayout) findViewById(R.id.activity_school_friend_input_layout);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.activity_school_friend_appbar);
        mCommentEditText = (EditText) findViewById(R.id.activity_school_friend_comment_edittext);
        mSendButton = (Button) findViewById(R.id.activity_school_friend_send_button);
        mEmotionImageView = (ImageView) findViewById(R.id.activity_school_friend_emotion_img);
//        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_school_friend_swiperefresh);
        schoolFriendLayoutParams = new SchoolFriendLayoutParams(this);
        weibos = WeiBoData.weiBos(this);
        sumWeiBos = WeiBoData.weiBos(this);
        mFriendContentAdapter = new FriendContentAdapter(weibos,this,mHandler,mListView);
        mListView.setAdapter(mFriendContentAdapter);
        initOnGlobalListener();
        initSendListener();
        initChooseImage();
       // initDrag();
    }

    private void initDrag() {
//        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//
//
//            }
//        });

        mListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    startP = (int) event.getY();
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {

                    endP = (int) event.getY();
                    layoutParams.setMargins(0,endP-startP,0,0);
                    mListView.setLayoutParams(layoutParams);
                    mListView.invalidate();
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    return true;
                }
                else {
                    return false;
                }
            }
        });
    }
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
//    private VelocityTracker mVelocityTracker = null;
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        int index = event.getActionIndex();
//        int action = event.getActionMasked();
//        int pointerId = event.getPointerId(index);
//
//        switch(action) {
//            case MotionEvent.ACTION_DOWN:
//                if(mVelocityTracker == null) {
//                    // Retrieve a new VelocityTracker object to watch the velocity of a motion.
//                    mVelocityTracker = VelocityTracker.obtain();
//                }
//                else {
//                    // Reset the velocity tracker back to its initial state.
//                    mVelocityTracker.clear();
//                }
//                // Add a user's movement to the tracker.
//                mVelocityTracker.addMovement(event);
//                break;
//            case MotionEvent.ACTION_MOVE:
//                mVelocityTracker.addMovement(event);
//                // When you want to determine the velocity, call
//                // computeCurrentVelocity(). Then call getXVelocity()
//                // and getYVelocity() to retrieve the velocity for each pointer ID.
//                mVelocityTracker.computeCurrentVelocity(1000);
//                // Log velocity of pixels per second
//                // Best practice to use VelocityTrackerCompat where possible.
//                Log.d(TAG, "X velocity: " +
//                        VelocityTrackerCompat.getXVelocity(mVelocityTracker,
//                                pointerId));
//                Log.d(TAG, "Y velocity: " +
//                        VelocityTrackerCompat.getYVelocity(mVelocityTracker,
//                                pointerId));
//                break;
//            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_CANCEL:
//                // Return a VelocityTracker object back to be re-used by others.
//                mVelocityTracker.recycle();
//                break;
//        }
//        return true;
//    }
    private void initChooseImage() {
        ImageView imageView = (ImageView) findViewById(R.id.activity_school_friend_campera_imgV);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SchoolFriendActivity.this,ChoosePicActivity.class);
                startActivity(intent);
            }
        });
        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(SchoolFriendActivity.this,PublishTextActivity.class);
                startActivity(intent);
                return true;
            }
        });
    }
    private void initOnGlobalListener(){
        mMainLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int rootHeight = mMainLayout.getRootView().getHeight();
                int subHeight = mMainLayout.getHeight();

                if ((rootHeight - subHeight) < (rootHeight / 3)) {
                    if(Divice.isPhone()) {
                        mListView.setLayoutParams(schoolFriendLayoutParams.noSoftInputParams(subHeight, mAppBarLayout,getStatusBarHeight()));
                    }
                    else{
                        mListView.setLayoutParams(schoolFriendLayoutParams.noSoftInputParams(subHeight, mAppBarLayout));
                    }
                    mInputLayout.setVisibility(View.GONE);
                    if (mInputLayout.getVisibility() == View.GONE && label) {
                        label = false;
                        for (int i = mPosition + 1; i < sumWeiBos.size(); i++) {
                            weibos.add(sumWeiBos.get(i));
                        }
                        mFriendContentAdapter.notifyDataSetChanged();
                        mListView.setTranscriptMode(ListView.ITEM_VIEW_TYPE_IGNORE);
                    }
                } else {
                    if(Divice.isPhone()){
                        mListView.setLayoutParams(schoolFriendLayoutParams.softInputParams(subHeight,80, mAppBarLayout));
                        Toast.makeText(SchoolFriendActivity.this,"Hello",Toast.LENGTH_LONG).show();
                    } else {
                        mListView.setLayoutParams(schoolFriendLayoutParams.softInputParams(subHeight,80, mAppBarLayout));
                    }
                    mInputLayout.setVisibility(View.VISIBLE);
                    mCommentEditText.requestFocus();
                    if (mInputLayout.getVisibility() == View.VISIBLE) {
                        label = true;
                    }
                }
            }
        });
    };

    private void initSendListener(){
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = mCommentEditText.getText().toString();
                Comment comment = new Comment();
                comment.setContent(text);
                comment.setcUserName("ZhangXiaojun");
                weibos.get(mPosition).getComments().add(comment);
                mCommentEditText.setText("");
                SoftInput.close(SchoolFriendActivity.this, mSendButton);
            }
        });
    }

    private void intEmotionOpen(){
        mEmotionImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SchoolFriendActivity.this,"Hello",Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_school_friend, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
