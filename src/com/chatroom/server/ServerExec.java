package com.chatroom.server;

import com.chatroom.Database.createdb;
import com.chatroom.configuration.Config;

public class ServerExec {

	public static void main(String[] args) {
		//updating the configuration
		Config.DATABASE_HOST = args[1];
		Config.USER_NAME = args[2];
		Config.USER_PWD = args[3];
		new createdb();
		Server server = new Server(Integer.parseInt(args[0]));
		server.connect();
	}
}
