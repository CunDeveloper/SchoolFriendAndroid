package com.nju.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cun on 2016/5/12.
 */
public class AlumniDynamicCollect implements Comparable<AlumniDynamicCollect>, Parcelable {
    private int id;
    private String date;
    private int authorId;
    private String text;
    private String imagePath;
    private int check;
    public AlumniDynamicCollect(){

    }

    protected AlumniDynamicCollect(Parcel in) {
        id = in.readInt();
        date = in.readString();
        authorId = in.readInt();
        text = in.readString();
        imagePath = in.readString();
        check = in.readInt();
    }

    public static final Creator<AlumniDynamicCollect> CREATOR = new Creator<AlumniDynamicCollect>() {
        @Override
        public AlumniDynamicCollect createFromParcel(Parcel in) {
            return new AlumniDynamicCollect(in);
        }

        @Override
        public AlumniDynamicCollect[] newArray(int size) {
            return new AlumniDynamicCollect[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public int compareTo(AlumniDynamicCollect another) {
        if (this.getId() > another.getId()){
            return 1;
        }else if (this.getId() < another.getId()){
            return -1;
        }
        return 0;
    }

    public int getCheck() {
        return check;
    }

    public void setCheck(int check) {
        this.check = check;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(date);
        dest.writeInt(authorId);
        dest.writeString(text);
        dest.writeString(imagePath);
        dest.writeInt(check);
    }
}
