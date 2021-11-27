package com.project.covid.model;

public class BluetoothVO {
	private Integer id;
	private Integer hostID;
	private String inTime;
	
	
	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
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

	
	public BluetoothVO(Integer id, Integer hostID, String inTime) {
		super();
		this.id = id;
		this.hostID = hostID;
		this.inTime = inTime;
	}


	public BluetoothVO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
}
