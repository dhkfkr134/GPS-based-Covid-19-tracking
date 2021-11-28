package com.project.covid.model;

import java.sql.Date;

public class UserLocation {
	private String id;
	private String mcode;
	private String loc;
	private Date inTime;
	private Date outTime;
	
	public UserLocation(String id, String mcode, String loc, Date inTime, Date outTime) {
		super();
		this.id = id;
		this.mcode = mcode;
		this.loc = loc;
		this.inTime = inTime;
		this.outTime = outTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public UserLocation() {
		super();
	}
	
}
