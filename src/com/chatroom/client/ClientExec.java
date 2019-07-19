package com.chatroom.client;

public class ClientExec {
	public static void main(String[] args) {
		Client client = new Client(args[0],Integer.parseInt(args[1]));
		client.connect();
	}
}
