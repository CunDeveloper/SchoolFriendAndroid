package com.nju.model;

public class AuthenticationAccessToken {

	private String tokenId;
	private int userId;
	private String userName;
	private String diviceId;
	private String appId;
	
	public String getTokenId() {
		return tokenId;
	}
	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}
	 
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDiviceId() {
		return diviceId;
	}

	public void setDiviceId(String diviceId) {
		this.diviceId = diviceId;
	}

	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}

}
