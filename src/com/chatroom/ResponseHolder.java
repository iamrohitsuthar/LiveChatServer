package com.chatroom;

import java.io.ObjectOutputStream;

import models.Response;

public class ResponseHolder {
	Response response;
	ObjectOutputStream objectOutputStream;
	
	public ResponseHolder( Response r, ObjectOutputStream o )
	{
		response = r;
		objectOutputStream = o;
	}
}
