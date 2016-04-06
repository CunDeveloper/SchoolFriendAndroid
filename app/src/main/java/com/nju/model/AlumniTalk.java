package com.nju.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by cun on 2016/3/30.
 */
public class AlumniTalk implements Parcelable {
    private int id;
    private String date;
    private String content;
    private String imagePaths;
    private String location;
    private AuthorInfo authorInfo;



    private ArrayList<String> images = new ArrayList<>();
    public AlumniTalk(){}

    protected AlumniTalk(Parcel in) {
        id = in.readInt();
        date = in.readString();
        content = in.readString();
        imagePaths = in.readString();
        location = in.readString();
        authorInfo = in.readParcelable(AuthorInfo.class.getClassLoader());
    }

    public static final Creator<AlumniTalk> CREATOR = new Creator<AlumniTalk>() {
        @Override
        public AlumniTalk createFromParcel(Parcel in) {
            return new AlumniTalk(in);
        }

        @Override
        public AlumniTalk[] newArray(int size) {
            return new AlumniTalk[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(date);
        dest.writeString(content);
        dest.writeString(imagePaths);
        dest.writeString(location);
        dest.writeParcelable(authorInfo, flags);
    }

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImagePaths() {
        return imagePaths;
    }

    public void setImagePaths(String imagePaths) {
        this.imagePaths = imagePaths;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public AuthorInfo getAuthorInfo() {
        return authorInfo;
    }

    public void setAuthorInfo(AuthorInfo authorInfo) {
        this.authorInfo = authorInfo;
    }

    public static Creator<AlumniTalk> getCREATOR() {
        return CREATOR;
    }

    public ArrayList<String> getImages() {
        return images;
    }


}