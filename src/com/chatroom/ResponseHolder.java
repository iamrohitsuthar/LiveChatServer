package com.chatroom;

import java.io.ObjectOutputStream;

public class ResponseHolder {
	Response response;
	ObjectOutputStream objectOutputStream;
	
	public ResponseHolder( Response r, ObjectOutputStream o )
	{
		response = r;
		objectOutputStream = o;
	}
}
