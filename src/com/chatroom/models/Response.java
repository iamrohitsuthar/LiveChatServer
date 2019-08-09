package com.chatroom.models;

import java.io.Serializable;

public class Response implements Serializable{
	public enum Type {
		ACK,SIGN_UP, LOGIN, LOGOUT, CREATE_ROOM, JOIN_ROOM, VIEW_ROOMS,MSG, STATUS_MSG, P_MSG, GEN;
	}
	int id;
	String content;
	boolean success;
	
	public Response(int id, boolean success, String content ) {
		this.id = id;
		this.success = success;
		this.content = content;
	}
	
	public int getId() {
		return id;
	}
	
	public String getContents() {
		return content;
	}
	
	public boolean getSuccess() {
		return success;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setContents(String content) {
		this.content = content;
	}
	
	public void setSuccess(boolean success) {
		this.success = success;
	}
}
