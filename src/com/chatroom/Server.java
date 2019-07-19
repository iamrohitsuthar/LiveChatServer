package com.chatroom;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.lang.Thread.State;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.Bidi;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import com.mysql.jdbc.PreparedStatement;

class RequestAnalyser extends Thread{
	String name = null;
	ResultSet resultSet = null;
	java.sql.PreparedStatement preparedStatement = null;
	String query = null;
	Set<Integer> clientIds;
	MessageTrackObject messageTrackObject;
	
	public void run()
	{
		try{
			
			Response response = null;
			Request request = null;
			ClientThread clientThread = null;
			while(true)
			{
				if(Server.requestqueue.isEmpty())
					synchronized(this){
						this.wait();
					}
				clientThread = Server.requestqueue.poll();
				request = clientThread.request;
				switch(request.id)
				{
					case 1:
						int clientID = -1;
						
						//first check if the user already exists or not
						name = request.contents;
						query = "SELECT " +Config.CLIENT_ID+ " from "+ Config.TABLE_NAME + " WHERE " + Config.CLIENT_NAME+"=?";
						preparedStatement = Server.connection.prepareStatement(query);
						preparedStatement.setString(1,name);
						resultSet = preparedStatement.executeQuery();
						
						if(!resultSet.isBeforeFirst()) {							
							//if user is not already present then insert data into database
							String query =  "INSERT INTO " + Config.TABLE_NAME + "("+Config.CLIENT_NAME+") VALUES(?)";
							preparedStatement = (PreparedStatement) Server.connection.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
							preparedStatement.setString(1, request.contents);
							
							int response_code = preparedStatement.executeUpdate();
							if(response_code > 0) {
								//success
								resultSet = preparedStatement.getGeneratedKeys();
								if(resultSet.next())
									clientID = resultSet.getInt(1);
								response = new Response( 1 , true, String.valueOf(clientID));
								//store the client in our client holder hash map
								Server.clientHolder.put(clientID, clientThread);
								//update the user message count
								Server.messagesTrackHashmap.put(clientID,0);
							}
							else {
								//something went wrong or if data is not inserted in database
								response = new Response( 1 , false, "Something went wrong while signing up ...");
							}
						}
						else {
							response = new Response( 1 , false, "User already present ...");
						}
						
						Server.responseMakerQueue.add(new ResponseHolder(response, clientThread.objectOutputStream));
						if( Server.responseMaker.getState() == State.WAITING )
						{
							synchronized (Server.responseMaker) {
								Server.responseMaker.notify();
							}
						}
						break;
					case 2:
						name = request.contents;
						query = "SELECT " +Config.CLIENT_ID+ " from "+ Config.TABLE_NAME + " WHERE " + Config.CLIENT_NAME+"=?";
						preparedStatement = Server.connection.prepareStatement(query);
						preparedStatement.setString(1,name);
						ResultSet resultSet = preparedStatement.executeQuery();
						if(!resultSet.isBeforeFirst()) {
							//no data
							response = new Response( 2 , false, "Username is not found in database ...");
						}
						else {
							resultSet.next();
							int client_id = resultSet.getInt(1);
							response = new Response( 2 , true, String.valueOf(client_id));
							//store the client in our client holder hash map
							Server.clientHolder.put(client_id, clientThread);
							
							//stores the client id and message count into the hash map
							Server.messagesTrackHashmap.put(client_id, 0);
						}
						
						Server.responseMakerQueue.add(new ResponseHolder(response, clientThread.objectOutputStream));
						if( Server.responseMaker.getState() == State.WAITING )
						{
							synchronized (Server.responseMaker) {
								Server.responseMaker.notify();
							}
						}
						break;
					case 3:
							//for logout
							logout(clientThread,request);
							break;
					case 4:
							//create room
							int roomId = Server.getRoomId();
							String roomName = request.contents;
							
							if(Server.roomsMapping.containsValue(roomName)) {
								//if the room name is already present
								response = new Response( 4 , false, "Room name already exists ...");
							}
							else {
								//insert the new mapping if it is not already present in the hash map
								Server.roomsMapping.put(roomId,roomName);
								Server.listOfRooms.add(roomId);
								clientIds = new HashSet<>();
								clientIds.add(request.clientId); //insert the client into the set
								Server.roomsHolder.put(roomId, clientIds);
								response = new Response( 4 , true, "Room #" + roomId  + " created and joined successfully");
							}
							Server.responseMakerQueue.add(new ResponseHolder(response, clientThread.objectOutputStream));
							if( Server.responseMaker.getState() == State.WAITING )
							{
								synchronized (Server.responseMaker) {
									Server.responseMaker.notify();
								}
							}
							break;
					case 5:
							//join room
						
							String roomName1 = request.contents;
							int clientId = request.clientId;
							int roomId1 = -1;
							roomId1 = Server.getKey(roomName1);
							
							if(Server.listOfRooms.contains(roomId1)) {
								//insert the client id in the set of the specific roomid in hash map
								if(Server.roomsHolder.get(roomId1).add(clientId)) {
									response = new Response( Response.Type.JOIN_ROOM.ordinal() , true, "Room #" + roomId1 + " joined successfully ...");
								}
								else {
									response = new Response( Response.Type.JOIN_ROOM.ordinal() , false, "Error while joining room ...");
								}
							}
							else {
								response = new Response( 5 , false, "Room not found ...");
							}
							Server.responseMakerQueue.add(new ResponseHolder(response, clientThread.objectOutputStream));
							if( Server.responseMaker.getState() == State.WAITING )
							{
								synchronized (Server.responseMaker) {
									Server.responseMaker.notify();
								}
							}
							break;
					case 6:
							//list of rooms
							String roomNames = "";
							if(Server.listOfRooms.size() > 0) {
								Iterator<Integer> iterator = Server.listOfRooms.iterator();
								while(iterator.hasNext()) {
									//get the room names from hash map
									roomNames += Server.roomsMapping.get(iterator.next()) + "\n";
								}
								response = new Response( 6 , true, roomNames);
							}
							else {
								response = new Response( 6 , false, "Currently there are no active rooms ...");
							}
							Server.responseMakerQueue.add(new ResponseHolder(response, clientThread.objectOutputStream));
							if( Server.responseMaker.getState() == State.WAITING )
							{
								synchronized (Server.responseMaker) {
									Server.responseMaker.notify();
								}
							}
							break;
							
					case 7:
							//message
							if(request.contents.equals(" ") || request.contents.equals(""))
								break;
							Server.messagequeue.add(request);
							if( Server.messageHandler.getState() == State.WAITING )
							{
								synchronized (Server.messageHandler) {
									Server.messageHandler.notify();
								}
							}
							break;
						
					default:
						Message.println("Invalid");
				}
			}
		}
		catch( Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void logout(ClientThread clientThread, Request request) {
		// TODO Auto-generated method stub
		Response response = new Response( Response.Type.LOGOUT.ordinal() , true, "Logout Succesfully");
		Server.responseMakerQueue.add(new ResponseHolder(response, clientThread.objectOutputStream));
		
		if(request.roomId != -1)
			Server.roomsHolder.get(request.roomId).remove(request.clientId);
		
		Server.clientHolder.remove(request.clientId);
		
		if( Server.responseMaker.getState() == State.WAITING )
		{
			synchronized (Server.responseMaker) {
				Server.responseMaker.notify();
			}
		}
		Message.println( request.clientId + " logged out sucessfully");
	}
}


class ResponseMaker  extends Thread{
	
	public void run()
	{
		ResponseHolder responseHolder = null;
		
		while(true)
		{
			try
			{
				if(Server.responseMakerQueue.isEmpty())
					synchronized(this){
						this.wait();
					}
				responseHolder = Server.responseMakerQueue.poll();
				responseHolder.objectOutputStream.writeObject(responseHolder.response);
				responseHolder.objectOutputStream.flush();
			}
			catch (Exception e) {
				
			}
		}
	}
}


class MessageHandler  extends Thread{
	
	public void run()
	{	
		Request request;
		ResultSet resultSet = null;
		java.sql.PreparedStatement preparedStatement = null;
		String query = null;
		String msg = "";
		String sender = "";
		String reciever = "";
		int recieverId = -1;
		while(true)
		{
			try
			{
				if(Server.messagequeue.isEmpty())
					synchronized(this){
						this.wait();
					}
				request = Server.messagequeue.poll();
				
				//TODO: get from database
				query = "SELECT " +Config.CLIENT_NAME+ " from "+ Config.TABLE_NAME + " WHERE " + Config.CLIENT_ID+"=?";
				preparedStatement = Server.connection.prepareStatement(query);
				preparedStatement.setInt(1,request.clientId);
				resultSet = preparedStatement.executeQuery();
				
				if(resultSet.next())
					sender = resultSet.getString(1);

				Set<Integer> set = Server.roomsHolder.get(request.roomId);
				
				//update the user message count
				
				Server.messagesTrackHashmap.put(request.clientId, Server.messagesTrackHashmap.get(request.clientId)+1);
				
				
				boolean personalMessage = request.contents.indexOf("@") !=-1? true: false;
				if(personalMessage)
				{

					reciever = request.contents.substring(request.contents.indexOf("@")+1,request.contents.indexOf(" "));
					Message.println(reciever);
					query = "SELECT " +Config.CLIENT_ID+ " from "+ Config.TABLE_NAME + " WHERE " + Config.CLIENT_NAME+"=?";
					preparedStatement = Server.connection.prepareStatement(query);
					preparedStatement.setString(1,reciever);
					resultSet = preparedStatement.executeQuery();
					
					if(resultSet.next())
					{
						recieverId = resultSet.getInt(1);
						msg = "\n<"+sender+"> ( PersonalMessage ): "+request.contents.substring(request.contents.indexOf(" "));
						ClientThread ct =  Server.clientHolder.get(recieverId); //gives the client thread object
						ObjectOutputStream oos = ct.objectOutputStream;
						Response res = new Response(Response.Type.MSG.ordinal(),true,msg);
						oos.writeObject(res);
						oos.flush();
						continue;
					}
					else
					{
						// user name wrong
						msg = "\n<server>: Wrong username " + reciever;
						ClientThread ct =  Server.clientHolder.get(request.clientId); //gives the client thread object
						ObjectOutputStream oos = ct.objectOutputStream;
						Response res = new Response(Response.Type.MSG.ordinal(),true,msg);
						oos.writeObject(res);
						oos.flush();
						
					}
				}
				
				Iterator<Integer> iterator = set.iterator();
				while(iterator.hasNext()) {
					int id = iterator.next();
					//take id and check if its not sender and then create response
					//if(id != request.clientId) {
						try {
							if(request.contents.equals("sv_exit") || request.contents.equals("sv_logout"))
							{
								msg = "\n" + sender + " has left the chat\n";
								if(id != request.clientId)
								{
									ClientThread ct =  Server.clientHolder.get(id); //gives the client thread object
									ObjectOutputStream oos = ct.objectOutputStream;
									Response res = new Response(Response.Type.MSG.ordinal(),true,msg);
									oos.writeObject(res);
									oos.flush();
								}
								else
								{
									ClientThread ct =  Server.clientHolder.get(id); //gives the client thread object
									try
									{
										if(request.contents.equals("sv_exit")) {
											ObjectOutputStream oos = ct.objectOutputStream;
											Response res = new Response(Response.Type.MSG.ordinal(),true,"sv_exit_successful");
											oos.writeObject(res);
											oos.flush();
										}
									}
									catch( Exception e )
									{
										Message.println("In Message Handler");
										e.printStackTrace();
									}
									finally
									{
										if( request.contents.equals("sv_logout") )
										{
											Message.println("sv_logout");
											RequestAnalyser.logout(ct,request);
										}	
										else
										{
											Message.println("sv_exit");
											Set<Integer> set1 = Server.roomsHolder.get(request.roomId);
											set1.remove(id);
										}
									}
								}
							}
							else
							{
								msg = "\n<"+sender+">: "+request.contents;
								ClientThread ct =  Server.clientHolder.get(id); //gives the client thread object
								ObjectOutputStream oos = ct.objectOutputStream;
								Response res = new Response(Response.Type.MSG.ordinal(),true,msg);
								oos.writeObject(res);
								oos.flush();
							}
							
						}
						catch(Exception e) {
							Message.println(id + " removed from server");
						}
					//}
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}

class MessagesTrackComparator implements Comparator<MessageTrackObject> {

	@Override
	public int compare(MessageTrackObject o1, MessageTrackObject o2) {
		// TODO Auto-generated method stub
		if(o1.getCount() > o2.getCount()) {
			return -1;
		}
		else if(o1.getCount() < o2.getCount())
			return 1;
		else
			return 0;
	}
}

public class Server {
	Scanner scanner = new Scanner(System.in);
	int choice;
	ServerSocket serverSocket;
	Socket socket;
	int port = -1;
	Request request;
	Response response;
	static Queue<ClientThread> requestqueue;
	static Queue<ResponseHolder> responseMakerQueue;
	static Queue<Request> messagequeue;
	static RequestAnalyser requestAnalyser;
	static ResponseMaker responseMaker;
	static MessageHandler messageHandler;
	static Connection connection = null;
	static ArrayList<Integer> listOfRooms;
	static HashMap<Integer,Set<Integer>> roomsHolder;
	static HashMap<Integer,String> roomsMapping;
	static HashMap<Integer,ClientThread> clientHolder; //hold the clients with their unique id
	static PriorityQueue<MessageTrackObject> messagesTrackQueue; //holds the log of which user sends how many messages
	static HashMap<Integer, Integer> messagesTrackHashmap;
	static ServerOperations serverOperations;
	static int roomIdGenerator = 0;
	
	public Server(int port)
	{
		this.port = port;
		requestqueue = new LinkedList<>();
		messagequeue = new LinkedList<>();
		responseMakerQueue = new LinkedList<>();
		requestAnalyser = new RequestAnalyser();
		requestAnalyser.start();
		responseMaker = new ResponseMaker();
		responseMaker.start();
		messageHandler = new MessageHandler();
		messageHandler.start();
		roomsHolder = new HashMap<>(); // for mapping the room id's and client id's in particular room
		roomsMapping = new HashMap<>();
		listOfRooms = new ArrayList<>();
		clientHolder = new HashMap<>();
		messagesTrackHashmap = new HashMap<>();
		messagesTrackQueue = new PriorityQueue<MessageTrackObject>(100,new MessagesTrackComparator());
		serverOperations = new ServerOperations();
		serverOperations.start();
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(Config.DATABASE_URL+"/"+Config.DATABASE_NAME,Config.USER_NAME,Config.USER_PWD);
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void finalize() throws Throwable {
		connection.close();
	}
	
	public static int getRoomId() {
		return roomIdGenerator++;
	}
	
	public static void shutdown() { 
		Message.println("Server stopped");
		System.exit(1);
	}
	
	public static Integer getKey(String value) {
        for (Entry<Integer, String> entry : roomsMapping.entrySet()) {
            if (entry.getValue().equals(value)) {
            	return entry.getKey();
            }
        }
        return -1;
	}
	
	public void connect() {
		try {
			serverSocket = new ServerSocket(port);
			while(true)
			{
				socket = serverSocket.accept();
				new ClientThread(socket).start();

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
