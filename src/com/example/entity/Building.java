package com.example.entity;

public class Building {
	/**
	 *
	 *@author yanling
	 *@date 2015-7-13 обнГ10:42:59
	 *@description
	 */
	
	private String classroom;
	
	private String library;
	
	
	public Building(){
		
	}
	
	public Building(String classroom, String library){
		this.classroom = classroom;
		this.library = library;
	}

	public String getClassroom() {
		return classroom;
	}

	public void setClassroom(String classroom) {
		this.classroom = classroom;
	}

	public String getLibrary() {
		return library;
	}

	public void setLibrary(String library) {
		this.library = library;
	}
}
