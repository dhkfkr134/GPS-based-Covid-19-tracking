package com.project.covid.model;

public class BluetoothVO {
	private String id;
	private String hostID;
	private String inTime;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getHostID() {
		return hostID;
	}
	public void setHostID(String hostID) {
		this.hostID = hostID;
	}
	public String getInTime() {
		return inTime;
	}
	public void setInTime(String inTime) {
		this.inTime = inTime;
	}
	public BluetoothVO(String id, String hostID, String inTime) {
		super();
		this.id = id;
		this.hostID = hostID;
		this.inTime = inTime;
	}
	
	

	
	
	
}
