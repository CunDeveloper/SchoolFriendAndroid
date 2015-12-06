package com.example.xiaojuzhang.test1;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.nju.activity.R;

public class SDCardImagesActivity extends AppCompatActivity {

    private Cursor cursor;
    private int columnIndex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sdcard_images);
        String[] projection ={MediaStore.Images.Thumbnails._ID};
        cursor = this.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                MediaStore.Images.Thumbnails.IMAGE_ID
        );
        columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails._ID);
        GridView mGridView = (GridView) findViewById(R.id.sdcard);
        mGridView.setAdapter(new ImageAdapter(this));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sdcard_images, menu);
        return true;
    }

    private class ImageAdapter extends BaseAdapter {

        private Context context;
        public ImageAdapter(Context context) {
            this.context = context;
        }
        @Override
        public int getCount() {
            return cursor.getCount();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                imageView = new ImageView(context);
                cursor.moveToPosition(position);
                int imageId = cursor.getInt(columnIndex);
                imageView.setImageURI(Uri.withAppendedPath(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,""+imageId));
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imageView.setPadding(8, 8, 8, 8);
                imageView.setLayoutParams(new GridView.LayoutParams(100,100));
            }
            else {
                imageView = (ImageView) convertView;
            }
            return imageView;
        }
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
