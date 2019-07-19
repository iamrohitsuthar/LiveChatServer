package com.chatroom.server;

public class ServerExec {

	public static void main(String[] args) {
		Server server = new Server(Integer.parseInt(args[0]));
		server.connect();
	}
}
