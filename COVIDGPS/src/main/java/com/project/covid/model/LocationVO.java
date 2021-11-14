package com.project.covid.model;

import java.sql.Date;

public class LocationVO {
	private int uid;
	private String mcode;
	private String loc;
	private String inTime;
	private String outTime;
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
	public String getInTime() {
		return inTime;
	}
	public void setInTime(String inTime) {
		this.inTime = inTime;
	}
	public String getOutTime() {
		return outTime;
	}
	public void setOutTime(String outTime) {
		this.outTime = outTime;
	}
	
	public LocationVO(int uid, String mcode, String loc, String inTime, String outTime) {
		super();
		this.uid = uid;
		this.mcode = mcode;
		this.loc = loc;
		this.inTime = inTime;
		this.outTime = outTime;
	}
	
}
