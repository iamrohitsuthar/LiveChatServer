package com.chatroom.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.chatroom.models.Request;
import com.chatroom.others.Message;

public class ClientThread extends Thread{

	Socket socket;
	ObjectInputStream objectInputStream;
	ObjectOutputStream objectOutputStream;
	Request request;
	
	public ClientThread(Socket s) {
		socket = s;
	}
	
	public void run()
	{
		try {
			objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
			objectInputStream = new ObjectInputStream(socket.getInputStream());
			
			while(true)
			{
				request = (Request) objectInputStream.readObject();
//				Message.println("Message received from " + request.clientId);
				Server.requestqueue.add(this);
				if( Server.requestAnalyser.getState() == State.WAITING )
				{
					synchronized (Server.requestAnalyser) {
						Server.requestAnalyser.notify();
					}
				}
			}
			
		} catch (IOException | ClassNotFoundException e) {
			if( e.getClass() == java.io.EOFException.class )
			{
				// if client exited the terminal itself
				if(request.getRoomId() != -1) {
					this.request.setContents("sv_logout");
					this.request.setId(Request.Type.MSG.ordinal());
					Server.requestqueue.add(this);
				}
				else {
					this.request.setContents("");
					this.request.setId(Request.Type.LOGOUT.ordinal());
					Server.requestqueue.add(this);
				}
				
				if( Server.requestAnalyser.getState() == State.WAITING )
				{
					synchronized (Server.requestAnalyser) {
						Server.requestAnalyser.notify();
					}
				}
			}
		}
	}
}
