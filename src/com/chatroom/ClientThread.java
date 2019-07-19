package com.chatroom;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

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
//				Message.println("Message recieved from " + request.clientId);
				Server.requestqueue.add(this);
				if( Server.requestAnalyser.getState() == State.WAITING )
				{
					synchronized (Server.requestAnalyser) {
						Server.requestAnalyser.notify();
					}
				}
			}
			
		} catch (IOException | ClassNotFoundException e) {
			Message.println("Client Thread Deleted! request was " + request.contents + " client id = " + request.clientId);
			if( e.getClass() == java.io.EOFException.class )
			{
				// if client exited the terminal itself
				this.request.contents = "sv_logout";
				this.request.id = Request.Type.MSG.ordinal();
				Server.requestqueue.add(this);
				if( Server.requestAnalyser.getState() == State.WAITING )
				{
					synchronized (Server.requestAnalyser) {
						Server.requestAnalyser.notify();
					}
				}
			}
			// e.printStackTrace();
		}
	}
}
