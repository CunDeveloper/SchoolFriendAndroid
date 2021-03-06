package model;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Content implements Parcelable {

	private int id;
	private int user_id;
	private String content;
	private List<String> imageList;
	private String date;
	private String day;
	private String month;
	private String location;
	private int is_contain_image;
	private String praiceUserName;/*要废掉*/
	private List<Praise> praiseList;
	private List<Comment> commentList;

    public Content(){}

	protected Content(Parcel in) {
		id = in.readInt();
		user_id = in.readInt();
		content = in.readString();
		imageList = in.createStringArrayList();
		is_contain_image = in.readInt();
		praiceUserName = in.readString();
		date = in.readString();
		location = in.readString();
		day = in.readString();
		month = in.readString();
	}

	public static final Creator<Content> CREATOR = new Creator<Content>() {
		@Override
		public Content createFromParcel(Parcel in) {
			return new Content(in);
		}

		@Override
		public Content[] newArray(int size) {
			return new Content[size];
		}
	};

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public List<String> getImageList() {
		return imageList;
	}
	public void setImageList(List<String> imageList) {
		this.imageList = imageList;
	}
	public int getIs_contain_image() {
		return is_contain_image;
	}
	public void setIs_contain_image(int is_contain_image) {
		this.is_contain_image = is_contain_image;
	}
	public String getPraiceUserName() {
		return praiceUserName;
	}
	public void setPraiceUserName(String praiceUserName) {
		this.praiceUserName = praiceUserName;
	}
	public List<Comment> getCommentList() {
		return commentList;
	}
	public void setCommentList(List<Comment> commentList) {
		this.commentList = commentList;
	}
	public List<Praise> getPraiseList() {
		return praiseList;
	}
	public void setPraiseList(List<Praise> praiseList) {
		this.praiseList = praiseList;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeInt(user_id);
		dest.writeString(content);
		dest.writeStringList(imageList);
		dest.writeInt(is_contain_image);
		dest.writeString(praiceUserName);
		dest.writeString(date);
		dest.writeString(location);
		dest.writeString(day);
		dest.writeString(month);
	}
}
