package com.chatroom.others;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogFileWriter {
	static PrintWriter printWriter;
	//writes the exception messages to the file
	public static void Log(String message) {
		try {
			printWriter = new PrintWriter(new FileOutputStream(new File(System.getProperty("user.home")+"/CHATROOM/LOGS.txt"),true));
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			printWriter.append(dateFormat.format(date) + "\t" + message + "\n");
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
				printWriter.close();
		}
	}
}
