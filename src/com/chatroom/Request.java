package com.chatroom;
import java.io.Serializable;
public class Request implements Serializable{
	public enum Type {
		ACK,SIGN_UP, LOGIN, LOGOUT, CREATE_ROOM, JOIN_ROOM, VIEW_ROOMS, MSG;
	}
	int id;
	int clientId;
	int roomId;
	String contents = "";
	
	public Request(int id, int clientId, int roomId, String contents) {
		this.id = id;
		this.clientId = clientId;
		this.roomId = roomId;
		this.contents = contents;
	}
}
