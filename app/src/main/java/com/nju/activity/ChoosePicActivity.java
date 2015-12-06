package com.nju.activity;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.nju.adatper.ChooseImageAdapter;
import com.nju.fragment.ChooseImageFragment;
import com.nju.fragment.ChoosedImageViewFragment;
import com.nju.model.Image;
import com.nju.util.Divice;

import java.util.ArrayList;

public class ChoosePicActivity extends AppCompatActivity {

    private LinearLayout mLineLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_pic);
        Toolbar toolbar = (Toolbar)findViewById(R.id.activity_choose_images_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mLineLayout = (LinearLayout) findViewById(R.id.activity_choose_pic_main);
        if (Build.VERSION.SDK_INT>19) {
            mLineLayout.setPadding(mLineLayout.getPaddingLeft(), Divice.getStatusBarHeight(this)
                    , mLineLayout.getPaddingRight(), mLineLayout.getBottom());
        }
        getSupportFragmentManager().beginTransaction()
                .add(R.id.activity_choose_container, ChooseImageFragment.newInstance(), ChooseImageFragment.TAG).
                commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_choose, menu);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ChooseImageAdapter.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            ArrayList<Image> mUploadImgPaths = new ArrayList<>();
            Image  image = new Image();
            image.setData(data.getDataString());
            mUploadImgPaths.add(image);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_publish_text_with_pics_main, ChoosedImageViewFragment.newInstance(mUploadImgPaths,0)).commit();
    }
    }
}
