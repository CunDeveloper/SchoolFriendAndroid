package com.nju.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by cun on 2016/3/15.
 */
public class AlumniVoice implements Parcelable,Comparable<AlumniVoice> {

    public static final Creator<AlumniVoice> CREATOR = new Creator<AlumniVoice>() {
        @Override
        public AlumniVoice createFromParcel(Parcel in) {
            return new AlumniVoice(in);
        }

        @Override
        public AlumniVoice[] newArray(int size) {
            return new AlumniVoice[size];
        }
    };
    private int id;
    private String title;
    private String content;
    private String imgPaths;
    private int praiseCount;
    private int commentCount;
    private String date;
    private AuthorInfo author;
    private int whoScan;
    private String label;
    private ArrayList<AuthorInfo> praiseAuthors;
    private ArrayList<ContentComment> contentComments;

    private int check = 0;

    public AlumniVoice() {

    }

    protected AlumniVoice(Parcel in) {
        id = in.readInt();
        whoScan = in.readInt();
        label = in.readString();
        title = in.readString();
        content = in.readString();
        imgPaths = in.readString();
        praiseCount = in.readInt();
        commentCount = in.readInt();
        date = in.readString();
        author = in.readParcelable(AuthorInfo.class.getClassLoader());
    }

    public static Creator<AlumniVoice> getCREATOR() {
        return CREATOR;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getPraiseCount() {
        return praiseCount;
    }

    public void setPraiseCount(int praiseCount) {
        this.praiseCount = praiseCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public AuthorInfo getAuthorInfo() {
        return author;
    }

    public void setAuthorInfo(AuthorInfo authorInfo) {
        this.author = authorInfo;
    }

    public ArrayList<AuthorInfo> getPraiseAuthors() {
        return praiseAuthors;
    }

    public void setPraiseAuthors(ArrayList<AuthorInfo> praiseAuthors) {
        this.praiseAuthors = praiseAuthors;
    }

    public ArrayList<ContentComment> getContentComments() {
        return contentComments;
    }

    public void setContentComments(ArrayList<ContentComment> contentComments) {
        this.contentComments = contentComments;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(whoScan);
        dest.writeString(label);
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeString(imgPaths);
        dest.writeInt(praiseCount);
        dest.writeInt(commentCount);
        dest.writeString(date);
        dest.writeParcelable(author, flags);
    }

    @Override
    public boolean equals(Object o) {
        AlumniVoice that = (AlumniVoice) o;
        if (this.getId() == that.getId()) {
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

    @Override
    public int compareTo(AlumniVoice another) {
        if (this.getId() > another.getId()) {
            return 1;
        }else if (this.getId() < another.getId()) {
            return -1;
        }
        return 0;
    }

    public int getWhoScan() {
        return whoScan;
    }

    public void setWhoScan(int whoScan) {
        this.whoScan = whoScan;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
