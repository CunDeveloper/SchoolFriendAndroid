package com.nju.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.nju.fragment.BaseFragment;

/**
 * Created by cun on 2016/3/27.
 */
public class ContentComment extends BaseEntity implements Parcelable {
    private AuthorInfo commentAuthor;
    private AuthorInfo commentedAuthor;
    private String content;
    private int contentId;
    private int alumnicTalkId;
    public ContentComment(){}
    protected ContentComment(Parcel in) {
        commentAuthor = in.readParcelable(AuthorInfo.class.getClassLoader());
        commentedAuthor = in.readParcelable(AuthorInfo.class.getClassLoader());
        content = in.readString();
        contentId = in.readInt();
    }

    public static final Creator<ContentComment> CREATOR = new Creator<ContentComment>() {
        @Override
        public ContentComment createFromParcel(Parcel in) {
            return new ContentComment(in);
        }

        @Override
        public ContentComment[] newArray(int size) {
            return new ContentComment[size];
        }
    };

    public AuthorInfo getCommentAuthor() {
        return commentAuthor;
    }

    public void setCommentAuthor(AuthorInfo commentAuthor) {
        this.commentAuthor = commentAuthor;
    }

    public AuthorInfo getCommentedAuthor() {
        return commentedAuthor;
    }

    public void setCommentedAuthor(AuthorInfo commentedAuthor) {
        this.commentedAuthor = commentedAuthor;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getContentId() {
        return contentId;
    }

    public void setContentId(int contentId) {
        this.contentId = contentId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(commentAuthor, flags);
        dest.writeParcelable(commentedAuthor, flags);
        dest.writeString(content);
        dest.writeInt(contentId);
    }

    @Override
    public boolean equals(Object o) {
        ContentComment that = (ContentComment) o;
        if (this.getId() == that.getId()){
            return true;
        }
        return false;
    }

    public int getAlumnicTalkId() {
        return alumnicTalkId;
    }

    public void setAlumnicTalkId(int alumnicTalkId) {
        this.alumnicTalkId = alumnicTalkId;
    }
}
