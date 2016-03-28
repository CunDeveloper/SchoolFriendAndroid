package com.nju.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cun on 2016/3/19.
 */
public class DynamicCollect implements Parcelable {

    public static final Creator<DynamicCollect> CREATOR = new Creator<DynamicCollect>() {
        @Override
        public DynamicCollect createFromParcel(Parcel in) {
            return new DynamicCollect(in);
        }

        @Override
        public DynamicCollect[] newArray(int size) {
            return new DynamicCollect[size];
        }
    };
    private int id;
    private String content;
    private String imgPath;
    private AuthorInfo authorInfo;
    private String date;

    public DynamicCollect() {

    }

    protected DynamicCollect(Parcel in) {
        id = in.readInt();
        content = in.readString();
        imgPath = in.readString();
        authorInfo = in.readParcelable(AuthorInfo.class.getClassLoader());
        date = in.readString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public AuthorInfo getAuthorInfo() {
        return authorInfo;
    }

    public void setAuthorInfo(AuthorInfo authorInfo) {
        this.authorInfo = authorInfo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(content);
        dest.writeString(imgPath);
        dest.writeParcelable(authorInfo, flags);
        dest.writeString(date);
    }
}
