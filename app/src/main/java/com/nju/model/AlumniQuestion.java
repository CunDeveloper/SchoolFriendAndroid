package com.nju.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xiaojuzhang on 2016/3/17.
 */
public class AlumniQuestion implements Parcelable {

    private int id;
    private String problem;
    private String description;
    private String imgPaths;
    private AuthorInfo authorInfo;
    private String date;
    private int replayCount;

    public AlumniQuestion(){}

    protected AlumniQuestion(Parcel in) {
        id = in.readInt();
        problem = in.readString();
        description = in.readString();
        imgPaths = in.readString();
        authorInfo = in.readParcelable(AuthorInfo.class.getClassLoader());
        date = in.readString();
        replayCount = in.readInt();
    }

    public static final Creator<AlumniQuestion> CREATOR = new Creator<AlumniQuestion>() {
        @Override
        public AlumniQuestion createFromParcel(Parcel in) {
            return new AlumniQuestion(in);
        }

        @Override
        public AlumniQuestion[] newArray(int size) {
            return new AlumniQuestion[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgPaths() {
        return imgPaths;
    }

    public void setImgPaths(String imgPaths) {
        this.imgPaths = imgPaths;
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

    public int getReplayCount() {
        return replayCount;
    }

    public void setReplayCount(int replayCount) {
        this.replayCount = replayCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(problem);
        dest.writeString(description);
        dest.writeString(imgPaths);
        dest.writeParcelable(authorInfo, flags);
        dest.writeString(date);
        dest.writeInt(replayCount);
    }
}
