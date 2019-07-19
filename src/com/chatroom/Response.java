package com.chatroom;

import java.io.Serializable;

public class Response implements Serializable{
	public enum Type {
		ACK,SIGN_UP, LOGIN, LOGOUT, CREATE_ROOM, JOIN_ROOM, VIEW_ROOMS,MSG;
	}
	int id;
	String content;
	boolean success;
	
	public Response(int id, boolean success, String content ) {
		this.id = id;
		this.success = success;
		this.content = content;
	}
}
