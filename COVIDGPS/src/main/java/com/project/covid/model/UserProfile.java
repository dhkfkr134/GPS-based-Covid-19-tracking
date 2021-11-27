package com.project.covid.model;

public class UserProfile {
	String uid;
	String access_token;
	String refresh_token;
	String code;
	
	public UserProfile() {
		// TODO Auto-generated constructor stub
	}
	
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getAccess_token() {
		return access_token;
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	public String getRefresh_token() {
		return refresh_token;
	}
	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}
	public String getCode() {
		return code;
	}
	public void setcode(String code) {
		this.code = code;
	}

	public UserProfile(String uid, String access_token, String refresh_token, String code) {
		super();
		this.uid = uid;
		this.access_token = access_token;
		this.refresh_token = refresh_token;
		this.code = code;
	}
	
	
	
	
}
