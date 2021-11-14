package com.project.covid.model;

public class OnwerProfile {
	private int hostID;
	private String loc;
	
	public int getHostID() {
		return hostID;
	}
	public void setHostID(int hostID) {
		this.hostID = hostID;
	}
	public String getLoc() {
		return loc;
	}
	public void setLoc(String loc) {
		this.loc = loc;
	}
	public OnwerProfile(int hostID, String loc) {
		super();
		this.hostID = hostID;
		this.loc = loc;
	}

	
	
}
