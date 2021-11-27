package com.project.covid.model;

import java.sql.Date;

public class UserLocation {
	private int uid;
	private String mcode;
	private String loc;
	private Date inTime;
	private Date outTime;
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getMcode() {
		return mcode;
	}
	public void setMcode(String mcode) {
		this.mcode = mcode;
	}
	public String getLoc() {
		return loc;
	}
	public void setLoc(String loc) {
		this.loc = loc;
	}
	public Date getInTime() {
		return inTime;
	}
	public void setInTime(Date inTime) {
		this.inTime = inTime;
	}
	public Date getOutTime() {
		return outTime;
	}
	public void setOutTime(Date outTime) {
		this.outTime = outTime;
	}
	public UserLocation(int uid, String mcode, String loc, Date inTime, Date outTime) {
		super();
		this.uid = uid;
		this.mcode = mcode;
		this.loc = loc;
		this.inTime = inTime;
		this.outTime = outTime;
	}
	public UserLocation() {
		super();
	}
	
}
