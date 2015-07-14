package com.example.entity;

import java.util.List;

public class Student {
	/**
	 *
	 *@author yanling
	 *@date 2015-7-13 ÏÂÎç9:39:23
	 *@description ŒWÉúî
	 */
	
	private String name;
	
	private String sex;
	
	private School school;
	
	private List<String> contact;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}
	
	
	
}
