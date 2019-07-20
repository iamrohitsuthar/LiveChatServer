package com.chatroom.models;

public class MessageTrackObject {
	int clientId;
	int MessagesCount;
	
	public MessageTrackObject(int clientId,int msgCount) {
		this.clientId = clientId;
		this.MessagesCount = msgCount;
	}
	
	public void updateCount() {
		MessagesCount++;
	}
	
	public int getCount() {
		return MessagesCount;
	}
	
	public int getId() {
		return clientId;
	}
}
