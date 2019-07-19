package com.chatroom;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.Thread.State;
import java.net.Socket;
import java.util.Scanner;

import models.Request;
import models.Response;

public class Client {
	int clientID=-1;
	int roomId = -1;
	Scanner scanner = new Scanner(System.in);
	int choice;
	String cont;
	String host = "";
	int port = -1;
	ObjectOutputStream objectOutputStream;
	ObjectInputStream objectInputStream;
	Socket socket;
	Request request = null;
	Response response = null;
	MessageListener messageListener;
	
	public Client(String host, int port) {
		this.host = host;
		this.port = port;
		messageListener = new MessageListener();
	}
	
	public void connect() {
		try {
			socket = new Socket(host,port);
			objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
			objectInputStream = new ObjectInputStream(socket.getInputStream());
			mainFunc();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void mainOptions() {
		try {
			Message.println("1. Create Room\n2. Join Room\n3. View Available Roooms\n4. Logout");
			choice = scanner.nextInt();
			switch(choice)
			{
				case 1:
					Message.println("Enter name of chat room to create:");
					cont = scanner.next();
					createAndJoinRoom(cont,true);
					break;
				case 2:
					Message.println("Enter name of chat room to join:");
					cont = scanner.next();
					createAndJoinRoom(cont,false);
					break;
				case 3:
					viewRooms();
					break;
				case 4:
					logOut();
					break;
				default: 
					Message.println("Invalid");
					System.exit(1);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			Message.println("In MainOptions");
		}
	}
	
	private void logOut() throws Exception{
		cont = "";
		request = new Request(Request.Type.LOGOUT.ordinal(),clientID,roomId,cont);
		objectOutputStream.writeObject(request);
		objectOutputStream.flush();
		response = (Response) objectInputStream.readObject();
		if( response.getSuccess())
		{
			Message.println(response.getContents());
			connect();
		}
		else
		{
			Message.println(response.getContents());
			mainOptions();
		}
	}

	private void viewRooms() throws Exception{
		try {
			cont = "";
			request = new Request(Request.Type.VIEW_ROOMS.ordinal(),clientID,roomId,cont);
			objectOutputStream.writeObject(request);
			objectOutputStream.flush();
			response = (Response) objectInputStream.readObject();
			if( response.getSuccess())
			{
				Message.println(response.getContents());
				Message.println("Enter name of chat room:");
				cont = scanner.next();
				createAndJoinRoom(cont, false);
			}
			else
			{
				Message.println(response.getContents());
				mainOptions();
			}
		}
		catch(Exception e) {
			
		}
	}

	private void createAndJoinRoom(String rName, boolean create) throws Exception
	{
		if(create)
			request = new Request(Request.Type.CREATE_ROOM.ordinal(),clientID,roomId,cont);
		else
			request = new Request(Request.Type.JOIN_ROOM.ordinal(),clientID,roomId,cont);
		
		objectOutputStream.writeObject(request);
		objectOutputStream.flush();
		Object obj =  objectInputStream.readObject();		
		if( obj.getClass() == Response.class )
			response = (Response) obj;
		else
		{
			//ObjectInputStream iis = (ObjectInputStream) obj;
			throw new Exception("Object returned is not of type Response. but of " + obj.getClass().toString() );
		}
		if( response.getSuccess())
		{
				//connected();
				Message.println(response.getContents()); //room created and joined successfully
				int hashIndex = response.getContents().indexOf('#');
				roomId = Integer.parseInt(response.getContents().substring(hashIndex+1, response.getContents().indexOf(" ", hashIndex)));
				Message.println(roomId+"");
				//send message interface
				conversation();
		}
		else
		{
			Message.println(response.getContents());
			mainOptions();
		}
	}
	
	private void conversation() throws Exception{
		if( messageListener.getState() == State.WAITING )
		{
			synchronized (messageListener) {
				messageListener.notify();
			}
		}
		else {
				messageListener.start();
		}
		
		Message.println("NOTE: You've entered the server. \n"
				+ "Type your message and press enter to send.\n"
				+ "To send message to a particular user use: '@user_name your_message' without quotes\n"
				+ "For exiting the room type 'sv_exit' without quotes\n"
				+ "For logging out type 'sv_logout' without quotes");
		
		request = new Request(Request.Type.MSG.ordinal(),clientID,roomId,"joined the chat");
		
		objectOutputStream.writeObject(request);
		objectOutputStream.flush();
		
		while(true) {
			cont = scanner.nextLine();
			request = new Request(Request.Type.MSG.ordinal(),clientID,roomId,cont);
			objectOutputStream.writeObject(request);
			objectOutputStream.flush();
			if( cont.equals("sv_exit") || cont.equals("sv_logout"))
			{
				break;
			}
		}
		if( cont.equals("sv_exit"))
		{
			mainOptions();
		}
		if( cont.equals("sv_logout"))
		{
			//mainFunc();
			connect();
		}
		
		
	}
	
	class MessageListener extends Thread{
		public void run()
		{
			while(true) {
				try {
					response = (Response) objectInputStream.readObject();
					Message.println(response.getContents());
					// || response.id != Response.Type.LOGOUT.ordinal()
					if(response.getContents().equals("sv_exit_successful")) {
						synchronized(this){
							this.wait();
						}
					}
					else if(response.getId() == Response.Type.LOGOUT.ordinal()) {
						synchronized(this){
							this.wait();
						}
					}
					
				} catch (ClassNotFoundException | IOException e) {
					e.printStackTrace();
					break;
				} catch (InterruptedException e) {
					Message.println("THREAD ERROR");
					e.printStackTrace();
				}
			}
		}
	}
	
	public void mainFunc() {
		try {
			Message.println("1. SIGN UP \n2. LOGIN");
			choice = scanner.nextInt();
			if(choice == 1) {
				Message.println("Enter username:");
				cont = scanner.next();
				request = new Request(Request.Type.SIGN_UP.ordinal(),clientID,roomId,cont);
				Message.println("Signing Up ... ");
			}
			else if(choice == 2) {
				Message.println("Enter username:");
				cont = scanner.next();
				request = new Request(Request.Type.LOGIN.ordinal(),clientID,roomId,cont);
				Message.println("Logging In ... ");
			}
			else {
				System.out.print("Invalid input!");
				System.exit(1);
			}
			objectOutputStream.writeObject(request);
			objectOutputStream.flush();
			
			response = (Response) objectInputStream.readObject();
			if( response.getId() == Response.Type.SIGN_UP.ordinal())
			{
				if(response.getSuccess()) {
					Message.println("Signed Up Successfully.");
					clientID = Integer.parseInt(response.getContents());
					Message.println("Your ID is: "+ clientID);
					mainOptions();
	
				}
				else {
					Message.println(response.getContents());
					mainFunc();
				}
			}
			else if(response.getId() == Request.Type.LOGIN.ordinal()) {
				if(response.getSuccess()) {
					Message.println("Login Successfull ... ");
					clientID = Integer.parseInt(response.getContents());
					Message.println("Your ID is: "+ clientID);
					mainOptions();
				}
				else {
					Message.println(response.getContents());
					mainFunc();
				}
			}

		}
		catch(Exception e) {
			e.printStackTrace();
			Message.println("In MainFunc");
		}
	}
}
