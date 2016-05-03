package com.nju.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xiaojuzhang on 2015/11/12.
 */
public class UserInfo implements Parcelable {

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel in) {
            return new UserInfo(in);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };
    private String label;
    private String name;
    private String sex;
    private String yuanXiaoName;
    private String fenYuan;
    private String major;
    private String date;


    public UserInfo() {

    }

    public UserInfo(Parcel in) {
        label = in.readString();
        name = in.readString();
        sex = in.readString();
        yuanXiaoName = in.readString();
        fenYuan = in.readString();
        major = in.readString();
        date = in.readString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getYuanXiaoName() {
        return yuanXiaoName;
    }

    public void setYuanXiaoName(String yuanXiaoName) {
        this.yuanXiaoName = yuanXiaoName;
    }

    public String getFenYuan() {
        return fenYuan;
    }

    public void setFenYuan(String fenYuan) {
        this.fenYuan = fenYuan;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
        dest.writeString(label);
        dest.writeString(name);
        dest.writeString(sex);
        dest.writeString(yuanXiaoName);
        dest.writeString(fenYuan);
        dest.writeString(major);
        dest.writeString(date);
    }
}
