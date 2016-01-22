package com.pazeto.iot.shared;

import com.google.gwt.core.client.GWT;
import com.pazeto.iot.client.DeviceService;
import com.pazeto.iot.client.DeviceServiceAsync;
import com.pazeto.iot.client.UserService;
import com.pazeto.iot.client.UserServiceAsync;
import com.pazeto.iot.shared.vo.User;

public class Util {

	private static User userLogged;

	public static User getUserLogged() {
		return userLogged;
	}

	public void setUserLogged(User userLogged) {
		this.userLogged = userLogged;
	}

	
//	public DeviceServiceAsync getDeviceservice() {
//		return deviceService;
//	}
}
