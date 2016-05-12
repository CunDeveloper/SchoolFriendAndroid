package com.nju.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xiaojuzhang on 2016/3/17.
 */
public class AlumniQuestion implements Parcelable,Comparable<AlumniQuestion> {

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
    private int id;
    private String date;
    private String problem;
    private String description;
    private String imgPaths;
    private AuthorInfo author;
    private int replayCount;
    private boolean isSolved;
    private int whoScan;
    private String label;
    private int check = 0;


    public AlumniQuestion() {
    }

    protected AlumniQuestion(Parcel in) {
        id = in.readInt();
        date = in.readString();
        problem = in.readString();
        description = in.readString();
        imgPaths = in.readString();
        author = in.readParcelable(AuthorInfo.class.getClassLoader());
        replayCount = in.readInt();
        whoScan = in.readInt();
        label = in.readString();
    }

    public static Creator<AlumniQuestion> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(date);
        dest.writeString(problem);
        dest.writeString(description);
        dest.writeString(imgPaths);
        dest.writeParcelable(author, flags);
        dest.writeInt(replayCount);
        dest.writeInt(whoScan);
        dest.writeString(label);
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

    public AuthorInfo getAuthor() {
        return author;
    }

    public void setAuthor(AuthorInfo author) {
        this.author = author;
    }

    public int getReplayCount() {
        return replayCount;
    }

    public void setReplayCount(int replayCount) {
        this.replayCount = replayCount;
    }

    public boolean isSolved() {
        return isSolved;
    }

    public void setIsSolved(boolean isSolved) {
        this.isSolved = isSolved;
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

    @Override
    public boolean equals(Object o) {
        AlumniQuestion that = (AlumniQuestion) o;
        int thisId = this.getId();
        int thatId = that.getId();
        if (thisId == thatId) {
            return true;
        } else {
            return false;
        }
    }

    public int getCheck() {
        return check;
    }

    public void setCheck(int check) {
        this.check = check;
    }

    @Override
    public int compareTo(AlumniQuestion another) {
        if (this.getId() > another.getId()) {
            return 1;
        } else if (this.getId() < another.getId()){
            return -1;
        }
        return 0;
    }
}
