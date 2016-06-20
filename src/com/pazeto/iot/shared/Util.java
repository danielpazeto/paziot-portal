package com.pazeto.iot.shared;

import com.pazeto.iot.shared.vo.User;

public class Util {

	private static User userLogged;
	private static boolean isAdmin = false;

	public static User getUserLogged() {
		return userLogged;
	}

	public static void setUserLogged(User u) {
		if (u.getEmail().equals("a"))
			isAdmin = true;
		userLogged = u;
	}

	public static boolean isAdmin() {
		return isAdmin;
	}

}
