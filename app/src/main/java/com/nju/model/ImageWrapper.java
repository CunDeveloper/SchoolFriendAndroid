package com.nju.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xiaojuzhang on 2015/12/28.
 */
public class ImageWrapper implements Parcelable {
    private Bitmap bitmap;
    private String path;

    public ImageWrapper(){

    }

    protected ImageWrapper(Parcel in) {
        bitmap = in.readParcelable(Bitmap.class.getClassLoader());
        path = in.readString();
    }

    public static final Creator<ImageWrapper> CREATOR = new Creator<ImageWrapper>() {
        @Override
        public ImageWrapper createFromParcel(Parcel in) {
            return new ImageWrapper(in);
        }

        @Override
        public ImageWrapper[] newArray(int size) {
            return new ImageWrapper[size];
        }
    };

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(bitmap, flags);
        dest.writeString(path);
    }
}
