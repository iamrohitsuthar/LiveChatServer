package com.chatroom.client;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import com.chatroom.configuration.Config;
import com.chatroom.others.LogFileWriter;
import com.chatroom.ui.MainSplash;

public class ClientExec {
	public static void main(String[] args) {		
		//create the log file if it is not present
		String path = System.getProperty("user.home");
		path += "/CHATROOM";
		File file = new File(path);
		if(!file.exists())
			file.mkdir();
				
		File f = new File(System.getProperty("user.home")+"/CHATROOM/LOGS.txt");
		if(!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace(new PrintWriter(Config.errors));
				LogFileWriter.Log(Config.errors.toString());
			}
		}
		
		if(args.length == 3 && args[2].equals("--console")) {
			Client client = new Client(args[0],Integer.parseInt(args[1]));
			client.connect();
		}
		else if(args.length == 2) {
			try {
				new MainSplash(new ClientModel(args[0], Integer.parseInt(args[1])));
			} catch (NumberFormatException e) {
				e.printStackTrace(new PrintWriter(Config.errors));
				LogFileWriter.Log(Config.errors.toString());
			} catch (IOException e) {
				e.printStackTrace(new PrintWriter(Config.errors));
				LogFileWriter.Log(Config.errors.toString());
			}
		}
	}
}
