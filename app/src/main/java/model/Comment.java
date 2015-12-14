package model;

public class Comment {

	private int id;
	private String comment_content;
	private int user_id;
	private int re_user_id;
	private int con_id;


	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getComment_content() {
		return comment_content;
	}
	public void setComment_content(String comment_content) {
		this.comment_content = comment_content;
	}
	 
	public int getRe_user_id() {
		return re_user_id;
	}
	public void setRe_user_id(int re_user_id) {
		this.re_user_id = re_user_id;
	}
	public int getCon_id() {
		return con_id;
	}
	public void setCon_id(int con_id) {
		this.con_id = con_id;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	
	
}
