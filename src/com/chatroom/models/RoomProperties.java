package com.chatroom.models;

public class RoomProperties {
	private String name;
	private String pwd;
	
	public RoomProperties(String name, String pwd) {
		this.name = name;
		this.pwd = pwd;
	}
	
	public String getRoomName() {
		return name;
	}
	
	public String getRoomPwd() {
		return pwd;
	}
	
}
