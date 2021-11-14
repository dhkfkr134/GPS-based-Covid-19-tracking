package com.project.covid.model;

public class BluetoothVO {
	private Integer uid;
	private Integer hostID;
	private String inTime;
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	public Integer getHostID() {
		return hostID;
	}
	public void setHostID(Integer hostID) {
		this.hostID = hostID;
	}
	public String getInTime() {
		return inTime;
	}
	public void setInTime(String inTime) {
		this.inTime = inTime;
	}
	public BluetoothVO(Integer uid, Integer hostID, String inTime) {
		super();
		this.uid = uid;
		this.hostID = hostID;
		this.inTime = inTime;
	}
	public BluetoothVO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
}
