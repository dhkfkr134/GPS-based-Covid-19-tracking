package com.project.covid.model;

public class OnwerProfile {
	private String hostID;
	private String loc;

	public OnwerProfile(String hostID, String loc) {
		super();
		this.hostID = hostID;
		this.loc = loc;
	}
	public String getHostID() {
		return hostID;
	}
	public void setHostID(String hostID) {
		this.hostID = hostID;
	}
	public String getLoc() {
		return loc;
	}
	public void setLoc(String loc) {
		this.loc = loc;
	}
	
}
