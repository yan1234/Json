package com.example.entity;

public class School {
	/**
	 *
	 *@author yanling
	 *@date 2015-7-13 ÏÂÎç9:41:03
	 *@description ŒWĞ£î
	 */
	
	private String name;
	
	private String address;
	
	private Building building;
	
	public Building getBuilding() {
		return building;
	}

	public void setBuilding(Building building) {
		this.building = building;
	}

	public School(){
		
	}
	
	public School(String name, String address){
		this.name = name;
		this.address = address;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
