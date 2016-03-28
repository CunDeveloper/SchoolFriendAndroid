package com.nju.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xiaojuzhang on 2015/12/3.
 */
public class Image implements Parcelable {
    public static final Creator<Image> CREATOR = new Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel in) {
            return new Image(in);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };
    private int height;
    private int width;
    private String data;
    private String bucketDisplayName;

    public Image() {
    }

    protected Image(Parcel in) {
        height = in.readInt();
        width = in.readInt();
        data = in.readString();
        bucketDisplayName = in.readString();
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getBucketDisplayName() {
        return bucketDisplayName;
    }

    public void setBucketDisplayName(String bucketDisplayName) {
        this.bucketDisplayName = bucketDisplayName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(height);
        dest.writeInt(width);
        dest.writeString(data);
        dest.writeString(bucketDisplayName);
    }
}
