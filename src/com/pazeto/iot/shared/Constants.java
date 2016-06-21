package com.pazeto.iot.shared;

public class Constants {

//	public final static String HOST_URI = "52.67.51.187";
	public final static String HOST_URI = "localhost";
	
	public final static int WEBSOCKET_DEV_PORT = 8025;
	public final static int WEBSOCKET_USER_PORT = 8026;

	public final static String WEBSOCKET_DEV_URI = "ws://" + HOST_URI + ":"
			+ WEBSOCKET_DEV_PORT + "/dev_con/";
	public final static String WEBSOCKET_USER_URI = "ws://" + HOST_URI + ":"
			+ WEBSOCKET_USER_PORT + "/user_con/";

	public enum DEVICE_STATUS {
		CONNECTED, DISCONNECTED;
	}

}