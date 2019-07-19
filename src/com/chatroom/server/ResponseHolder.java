package com.chatroom.server;

import java.io.ObjectOutputStream;

import com.chatroom.models.Response;

public class ResponseHolder {
	Response response;
	ObjectOutputStream objectOutputStream;
	
	public ResponseHolder( Response r, ObjectOutputStream o )
	{
		response = r;
		objectOutputStream = o;
	}
}
