package com.nju.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cun on 2016/5/12.
 */
public class RecommendCollect implements Parcelable,Comparable<RecommendCollect> {
    private int id;
    private String date;
    private RecommendWork recommendWork;
    private int authorId;
    private int check = 0;


    protected RecommendCollect(Parcel in) {
        id = in.readInt();
        date = in.readString();
        recommendWork = in.readParcelable(RecommendWork.class.getClassLoader());
        authorId = in.readInt();
    }

    public static final Creator<RecommendCollect> CREATOR = new Creator<RecommendCollect>() {
        @Override
        public RecommendCollect createFromParcel(Parcel in) {
            return new RecommendCollect(in);
        }

        @Override
        public RecommendCollect[] newArray(int size) {
            return new RecommendCollect[size];
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
        dest.writeParcelable(recommendWork, flags);
        dest.writeInt(authorId);
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

    public RecommendWork getRecommendWork() {
        return recommendWork;
    }

    public void setRecommendWork(RecommendWork recommendWork) {
        this.recommendWork = recommendWork;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public static Creator<RecommendCollect> getCREATOR() {
        return CREATOR;
    }

    public int getCheck() {
        return check;
    }

    public void setCheck(int check) {
        this.check = check;
    }

    @Override
    public int compareTo(RecommendCollect another) {
        if (this.getId() > another.getId()) {
            return 1;
        }else if (this.getId() < another.getId()){
            return -1;
        }
        return 0;
    }
}
