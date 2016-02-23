package com.pazeto.iot.shared;

import com.pazeto.iot.shared.vo.User;

public class Util {

	private static User userLogged;

	public static User getUserLogged() {
		return userLogged;
	}

	public void setUserLogged(User userLogged) {
		this.userLogged = userLogged;
	}
}
