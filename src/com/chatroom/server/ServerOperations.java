package com.chatroom.server;

import java.util.Map;
import java.util.Scanner;
import com.chatroom.others.Message;

public class ServerOperations extends Thread {
	static Scanner scanner = new Scanner(System.in);
	MessageTrackObject messageTrackObject;
	public void run() {
		int input;
		while(true) {
			Message.println("1. For Messages logs");
			//TODO : kitne user or rooms hai or queue ki size kitni hai
			Message.println("2. Server Shutdown");
			input = scanner.nextInt();
			if(input == 1) {
				Server.messagesTrackQueue.clear();
				for(Map.Entry<Integer, Integer> entry : Server.messagesTrackHashmap.entrySet()) {
					messageTrackObject = new MessageTrackObject(entry.getKey(), entry.getValue()-1);
					Server.messagesTrackQueue.add(messageTrackObject);
				}
				Message.println("CLIENT ID \t MESSAGES COUNT");
				for(MessageTrackObject messageTrackObject1 : Server.messagesTrackQueue) {
					System.out.println(messageTrackObject1.getId()+"\t\t"+messageTrackObject1.getCount());
				}
			}
			else if(input == 2){
				//stops the server
				Server.shutdown();
			}
		}
	}
}
