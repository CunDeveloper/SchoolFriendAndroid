package com.nju.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cun on 2016/4/14.
 */
public class AlumnicTalkPraise implements Parcelable {
    public static final Creator<AlumnicTalkPraise> CREATOR = new Creator<AlumnicTalkPraise>() {
        @Override
        public AlumnicTalkPraise createFromParcel(Parcel in) {
            return new AlumnicTalkPraise(in);
        }

        @Override
        public AlumnicTalkPraise[] newArray(int size) {
            return new AlumnicTalkPraise[size];
        }
    };
    private AuthorInfo praiseAuthor;
    private int contentId;
    private int id;
    private String date;

    public AlumnicTalkPraise() {
    }

    protected AlumnicTalkPraise(Parcel in) {
        praiseAuthor = in.readParcelable(AuthorInfo.class.getClassLoader());
        contentId = in.readInt();
        id = in.readInt();
        date = in.readString();
    }

    public AuthorInfo getPraiseAuthor() {
        return praiseAuthor;
    }

    public void setPraiseAuthor(AuthorInfo praiseAuthor) {
        this.praiseAuthor = praiseAuthor;
    }

    public int getContentId() {
        return contentId;
    }

    public void setContentId(int contentId) {
        this.contentId = contentId;
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

    @Override
    public boolean equals(Object o) {
        AlumnicTalkPraise that = (AlumnicTalkPraise) o;
        if (that.getId() == this.getId()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(praiseAuthor, flags);
        dest.writeInt(contentId);
        dest.writeInt(id);
        dest.writeString(date);
    }
}
