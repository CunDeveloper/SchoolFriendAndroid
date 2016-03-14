package com.nju.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;



public class RecommendWork extends BaseEntity implements Parcelable {

	private AuthorInfo author;
	private String title;
	private String content;
	private String imgPaths;
	private int commentCount;
	private String email;
	private int type;
	
	private List<String> images = new ArrayList<>();

	public RecommendWork(){

	}

	protected RecommendWork(Parcel in) {
		title = in.readString();
		content = in.readString();
		imgPaths = in.readString();
		commentCount = in.readInt();
		type = in.readInt();
		images = in.createStringArrayList();
		email = in.readString();
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

	public String getContent() {
		return content;
	}
	
	public void setImages(List<String> images) {
		this.images = images;
		setImgPaths();
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	 
	public String getImgPaths() {
		return imgPaths;
	}
	
	public void setImgPaths() {
		StringBuilder builder = new StringBuilder();
		for(String path:images){
			builder.append(path);
			builder.append(",");
		}
		if(builder.length()>0){
			builder.deleteCharAt(builder.length()-1);
		}
		 this.imgPaths = builder.toString();
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public AuthorInfo getAuthor() {
		return author;
	}

	public void setAuthor(AuthorInfo author) {
		this.author = author;
	}

	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(title);
		dest.writeString(content);
		dest.writeString(imgPaths);
		dest.writeInt(commentCount);
		dest.writeInt(type);
		dest.writeStringList(images);
		dest.writeString(email);
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
