package com.nju.service;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.nju.model.Image;

import java.util.ArrayList;

/**
 * Created by xiaojuzhang on 2015/12/3.
 */
public class ChooseImageService {

    public static ArrayList<Image> queryImages(Context context){
        ArrayList<Image> images = new ArrayList<>();
        String[] projection = new String[]{
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.WIDTH,
                MediaStore.Images.Media.HEIGHT,
                MediaStore.Images.Media.DATA
        };
        String orderBy = MediaStore.Images.Media.DATE_TAKEN + " DESC";
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = MediaStore.Images.Media.query(context.getContentResolver(),uri,projection,null,null,orderBy);
        int bucketColumn = cursor.getColumnIndex(
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        int widthColumn = cursor.getColumnIndex(MediaStore.Images.Media.WIDTH);
        int heightColumn = cursor.getColumnIndex(MediaStore.Images.Media.HEIGHT);
        int dataColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
        Image image;
        while (cursor.moveToNext()){
            image = new Image();
            image.setBucketDisplayName(cursor.getString(bucketColumn));
            image.setHeight(cursor.getInt(heightColumn));
            image.setWidth(cursor.getInt(widthColumn));
            image.setData(cursor.getString(dataColumn));
            images.add(image);
        }
        return images;
    }

    public static ArrayList<Image> queryImagesByPath(Context context,String path){
        ArrayList<Image> imgs = new ArrayList<>();
        String[] projection = new String[]{
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.WIDTH,
                MediaStore.Images.Media.HEIGHT
        };
        String data = MediaStore.Images.Media.DATA+"=?";
        String[] selectionArgs = new String[]{
                path
        };
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = MediaStore.Images.Media.query(context.getContentResolver(),uri,projection,data,selectionArgs,null);
        int bucketColumn = cursor.getColumnIndex(
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        int widthColumn = cursor.getColumnIndex(MediaStore.Images.Media.WIDTH);
        int heightColumn = cursor.getColumnIndex(MediaStore.Images.Media.HEIGHT);
        Image image;
        if (cursor.moveToNext()){
            image = new Image();
            image.setBucketDisplayName(cursor.getString(bucketColumn));
            image.setHeight(cursor.getInt(heightColumn));
            image.setWidth(cursor.getInt(widthColumn));
            image.setData(path);
            imgs.add(image);
        }
        return imgs;
    }

        public static ArrayList<String> queryImagesPath(Context context){
        ArrayList<String> imgPaths = new ArrayList<>();
        String[] projection = new String[]{
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.WIDTH,
                MediaStore.Images.Media.HEIGHT,
                MediaStore.Images.Media.DATA
        };
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = MediaStore.Images.Media.query(context.getContentResolver(), uri, projection);
        int dataColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
        while (cursor.moveToNext()){
            imgPaths.add(cursor.getString(dataColumn));
        }
        return imgPaths;
    }
}
