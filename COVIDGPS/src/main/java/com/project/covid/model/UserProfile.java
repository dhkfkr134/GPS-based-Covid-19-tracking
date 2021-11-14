package com.project.covid.model;

public class UserProfile {
	int uid;
	String access_token;
	String refresh_token;
	String jwt;
	String email;
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
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
	public String getJwt() {
		return jwt;
	}
	public void setJwt(String jwt) {
		this.jwt = jwt;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public UserProfile(int uid, String access_token, String refresh_token, String jwt, String email) {
		super();
		this.uid = uid;
		this.access_token = access_token;
		this.refresh_token = refresh_token;
		this.jwt = jwt;
		this.email = email;
	}
	
	
	
	
}
