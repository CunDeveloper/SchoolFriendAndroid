package com.nju.model;

import android.os.Parcel;
import android.os.Parcelable;

public class AuthorInfo implements Parcelable {

    public static final Creator<AuthorInfo> CREATOR = new Creator<AuthorInfo>() {
        @Override
        public AuthorInfo createFromParcel(Parcel in) {
            return new AuthorInfo(in);
        }

        @Override
        public AuthorInfo[] newArray(int size) {
            return new AuthorInfo[size];
        }
    };
    private int authorId;
    private String authorName;
    private String headUrl;
    private String label;
    private String username;
    private String password;

    public AuthorInfo(int authorId, String authorName) {
        this.authorId = authorId;
        this.authorName = authorName;
    }

    public AuthorInfo() {

    }

    protected AuthorInfo(Parcel in) {
        authorId = in.readInt();
        authorName = in.readString();
        headUrl = in.readString();
        label = in.readString();
    }

    public static Creator<AuthorInfo> getCREATOR() {
        return CREATOR;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(authorId);
        dest.writeString(authorName);
        dest.writeString(headUrl);
        dest.writeString(label);
    }
}
