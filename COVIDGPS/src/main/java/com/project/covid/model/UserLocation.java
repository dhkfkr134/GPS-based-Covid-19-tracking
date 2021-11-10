package com.project.covid.model;

public class UserLocation {
	private String uid;
	private String w3w;
	private String loc;
	private String time;
	
	public UserLocation(String uid, String w3w, String loc, String time) {
		super();
		this.uid = uid;
		this.w3w = w3w;
		this.loc = loc;
		this.time = time;
	}
	
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getW3w() {
		return w3w;
	}
	public void setW3w(String w3w) {
		this.w3w = w3w;
	}
	public String getLoc() {
		return loc;
	}
	public void setLoc(String loc) {
		this.loc = loc;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
	
}
