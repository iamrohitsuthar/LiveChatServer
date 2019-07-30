package com.chatroom.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import com.chatroom.others.LogFileWriter;

public class ClientModel {
	private int clientID=-1;
	private int roomId = -1;
	private String host = "";
	private int port = -1;
	public static ObjectOutputStream objectOutputStream;
	public static ObjectInputStream objectInputStream;
	private Socket socket;
	
	public ClientModel(String host, int port) {
		super();
		this.host = host;
		this.port = port;
		
		// connecting
		try {
			socket = new Socket(host,port);
			objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
			objectInputStream = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getClientID() {
		return clientID;
	}

	public void setClientID(int clientID) {
		this.clientID = clientID;
	}

	public int getRoomId() {
		return roomId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

}
