package com.project.covid.model;

import java.sql.Date;

public class InfectionLoc {
	private String mcode;
	private Date inTime;
	private Date outTime;
	
	public String getMcode() {
		return mcode;
	}
	
	public void setMcode(String mcode) {
		this.mcode = mcode;
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
	
	public InfectionLoc(String mcode, Date inTime, Date outTime) {
		super();
		this.mcode = mcode;
		this.inTime = inTime;
		this.outTime = outTime;
	}
	
}
