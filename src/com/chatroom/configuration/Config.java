package com.chatroom.configuration;

import java.awt.Color;
import java.io.StringWriter;

public interface Config {
	public String USER_NAME  = "root";
	public String USER_PWD = "WHOamiqwerty@123!";
	public String DATABASE_URL = "jdbc:mysql://localhost:3306";
	public String DATABASE_NAME = "chatroom";
	public String TABLE_NAME = "users";
	public String CLIENT_ID = "client_id";
	public String CLIENT_NAME = "client_name";
	public String CLIENT_PWD = "client_pwd";
	public Color colorPrimary = new Color(108, 99, 255);
	public static StringWriter errors = new StringWriter();
}
