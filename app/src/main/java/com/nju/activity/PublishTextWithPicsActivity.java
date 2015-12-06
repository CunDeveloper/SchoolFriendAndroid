package com.nju.activity;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;

import com.nju.fragment.EmotionPagerFragment;
import com.nju.fragment.PublishTextWithPicsFragment;
import com.nju.util.Divice;

import java.util.ArrayList;

import util.dialog.SchoolFriendDialog;

public class PublishTextWithPicsActivity extends AppCompatActivity implements EmotionPagerFragment.OnFragmentInputEmotionListener {

    private  PublishTextWithPicsFragment fragment ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_text_with_pics);
        Bundle bundle = getIntent().getExtras();
        ArrayList<String> mUploadImgPaths = bundle.getStringArrayList(getString(R.string.IMGS));
        Toolbar toolbar = (Toolbar) findViewById(R.id.publish_text_with_pics_toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.activity_publish_text_with_pics_linelayout);
        if (Build.VERSION.SDK_INT>19) {
            linearLayout.setPadding(linearLayout.getPaddingLeft(), Divice.getStatusBarHeight(this)
                    , linearLayout.getPaddingRight(), linearLayout.getBottom());
        }
        fragment = PublishTextWithPicsFragment.newInstance(mUploadImgPaths);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_publish_text_with_pics_main,fragment, PublishTextWithPicsFragment.TAG)
        .commit();
    }

    @Override
    public void onFragmentInputEmotion(Drawable drawable) {
        if (fragment != null) {
           fragment.inputEmotion(drawable);
        }
    }

    @Override
    public void onBackPressed() {
        SchoolFriendDialog.exitReminderDialog(this, getResources().getString(R.string.exit_this_edit)).show();
    }
}
