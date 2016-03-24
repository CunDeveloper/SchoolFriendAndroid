package com.nju.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;



public class RecommendWork extends BaseEntity implements Parcelable {

	private AuthorInfo authorInfo;
	private String title;
	private String content;
	private String imgPaths;
	private int commentCount;
	private String email;
	private int type;

	public RecommendWork(){}


	protected RecommendWork(Parcel in) {
		authorInfo = in.readParcelable(AuthorInfo.class.getClassLoader());
		title = in.readString();
		content = in.readString();
		imgPaths = in.readString();
		commentCount = in.readInt();
		email = in.readString();
		type = in.readInt();
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

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(authorInfo, flags);
		dest.writeString(title);
		dest.writeString(content);
		dest.writeString(imgPaths);
		dest.writeInt(commentCount);
		dest.writeString(email);
		dest.writeInt(type);
	}

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

	public static Creator<RecommendWork> getCREATOR() {
		return CREATOR;
	}
}
