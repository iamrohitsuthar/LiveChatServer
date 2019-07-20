package com.chatroom.Database;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.chatroom.configuration.Config;
import com.chatroom.others.LogFileWriter;
import com.chatroom.server.Server;


public class createdb {
	static StringWriter errors = new StringWriter();
	public static void main(String[] args) {
		Connection connection = null;
		java.sql.Statement statement= null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
			//creating database
			connection = DriverManager.getConnection(Config.DATABASE_URL,Config.USER_NAME,Config.USER_PWD);
			String Query = "CREATE DATABASE IF NOT EXISTS "+ Config.DATABASE_NAME;
			statement = connection.createStatement();
			statement.executeUpdate(Query);
			connection.close();
			
			//for execute multiple queries separate queries by semicolon
			connection = DriverManager.getConnection(Config.DATABASE_URL+'/'+Config.DATABASE_NAME+"?allowMultiQueries=true",Config.USER_NAME,Config.USER_PWD);
			String Queries = "CREATE TABLE " + Config.TABLE_NAME + "(" + Config.CLIENT_ID + " int auto_increment," + Config.CLIENT_NAME + " VARCHAR(50) not null, "+ Config.CLIENT_PWD + " VARCHAR(150), " +"primary key(" +Config.CLIENT_ID+ "))";
			
			statement = connection.createStatement();
			statement.executeUpdate(Queries);
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			LogFileWriter.Log(e.getMessage());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			LogFileWriter.Log(e.getMessage());
		}
		finally {
			try {
				connection.close(); //close the database connection
			} catch (SQLException e) {
				e.printStackTrace(new PrintWriter(errors));
				LogFileWriter.Log(errors.toString());
			}
		}
	}
}
