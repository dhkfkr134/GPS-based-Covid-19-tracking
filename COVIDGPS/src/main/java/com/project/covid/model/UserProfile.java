package com.project.covid.model;

public class UserProfile {
	private String id;
	private String idname;
	private String phone;
	private String address;
	
	
	public UserProfile(String id, String idname, String phone, String address) {
		super();
		this.id = id;
		this.idname = idname;
		this.phone = phone;
		this.address = address;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIdName() {
		return idname;
	}

	public void setName(String idname) {
		this.idname = idname;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
}
