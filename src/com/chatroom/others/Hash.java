package com.chatroom.others;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.chatroom.server.Server;

public class Hash {
	static StringWriter errors = new StringWriter();
	public static String getHash(String input) {
		String hash;
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
			byte[] bytes = messageDigest.digest(input.getBytes());
			BigInteger integer = new BigInteger(1, bytes);
			hash = integer.toString(16);
			while(hash.length() < 32) {
				hash = "0" + hash;
			}
			return hash;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace(new PrintWriter(errors));
			LogFileWriter.Log(errors.toString());
			return null;
		}
		
	}
}
