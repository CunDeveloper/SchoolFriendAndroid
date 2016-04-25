package com.nju.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;


public class RecommendWork  implements Parcelable {
    private int id;
    private String date;
    private AuthorInfo authorInfo;
    private String title;
    private String content;
    private String imgPaths;
    private int commentCount;
    private String email;
    private int type;
    private ArrayList<ContentComment> comments;
    private int check = 0;


    public RecommendWork() {
    }


    protected RecommendWork(Parcel in) {
        id = in.readInt();
        date = in.readString();
        authorInfo = in.readParcelable(AuthorInfo.class.getClassLoader());
        title = in.readString();
        content = in.readString();
        imgPaths = in.readString();
        commentCount = in.readInt();
        email = in.readString();
        type = in.readInt();
        comments = in.createTypedArrayList(ContentComment.CREATOR);
    }

    public static final Creator<RecommendWork> CREATOR = new Creator<RecommendWork>() {
        @Override
        public RecommendWork createFromParcel(Parcel in) {
            return new RecommendWork(in);
        }

        @Override
        public RecommendWork[] newArray(int size) {
            return new RecommendWork[size];
        }
    };

    public AuthorInfo getAuthor() {
        return authorInfo;
    }

    public void setAuthor(AuthorInfo author) {
        this.authorInfo = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImgPaths() {
        return imgPaths;
    }

    public void setImgPaths(String imgPaths) {
        this.imgPaths = imgPaths;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public ArrayList<ContentComment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<ContentComment> comments) {
        this.comments = comments;
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

    public AuthorInfo getAuthorInfo() {
        return authorInfo;
    }

    public void setAuthorInfo(AuthorInfo authorInfo) {
        this.authorInfo = authorInfo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(date);
        dest.writeParcelable(authorInfo, flags);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeString(imgPaths);
        dest.writeInt(commentCount);
        dest.writeString(email);
        dest.writeInt(type);
        dest.writeTypedList(comments);
    }

    @Override
    public boolean equals(Object o) {
        RecommendWork that = (RecommendWork) o;
        if (this.getId() == that.getId()){
            return true;
        }
        return false;
    }

    public int getCheck() {
        return check;
    }

    public void setCheck(int check) {
        this.check = check;
    }
}
