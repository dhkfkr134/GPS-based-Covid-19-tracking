package com.project.covid.model;

public class OwnerProfile {
	private String hostID;
	private String access_token;
	private String refresh_token;
	private String code;
	private String loc="";

	public OwnerProfile() {
		// TODO Auto-generated constructor stub
	}
	
	public OwnerProfile(String hostID, String access_token, String refresh_token, String code, String loc) {
		super();
		this.hostID = hostID;
		this.access_token = access_token;
		this.refresh_token = refresh_token;
		this.code = code;
		this.loc = loc;
	}
	public String getHostID() {
		return hostID;
	}
	public void setHostID(String hostID) {
		this.hostID = hostID;
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
	public void setCode(String code) {
		this.code = code;
	}
	public String getLoc() {
		return loc;
	}
	public void setLoc(String loc) {
		this.loc = loc;
	}
}
