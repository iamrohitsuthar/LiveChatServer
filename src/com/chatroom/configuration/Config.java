package com.chatroom.configuration;

import java.awt.Color;
import java.io.StringWriter;

public class Config {
	public static String USER_NAME  = "";
	public static String USER_PWD = "";
	public static String DATABASE_HOST = "";
	public static String DATABASE_URL = "jdbc:mysql://";
	public static String DATABASE_PORT = ":3306";
	public static final String DATABASE_NAME = "chatroom";
	public static final String TABLE_NAME = "users";
	public static final String CLIENT_ID = "client_id";
	public static final String CLIENT_NAME = "client_name";
	public static final String CLIENT_PWD = "client_pwd";
	public static final Color colorPrimary = new Color(108, 99, 255);;
	public static StringWriter errors = new StringWriter();
}
