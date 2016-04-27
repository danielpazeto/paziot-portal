package com.pazeto.iot.server.websocket;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.websocket.Session;

import com.google.appengine.labs.repackaged.org.json.JSONObject;

public class HandleConnectedUsers {

	static Map<String, Session> connectedUsers = new HashMap<String, Session>();

	public static void onOpenUserSession(String email, Session session) {
		connectedUsers.put(email, session);
	}

	public static void onCloseUserSession(Session session) {
		for (Entry<String, Session> connectedUser : connectedUsers.entrySet()) {
			if (connectedUser.getValue().equals(session)) {
				connectedUsers.remove(connectedUser.getKey());
			}
		}
	}

	public static void sendMessageToUser(String email, JSONObject values)
			throws Exception {
		if (userIsConnected(email)) {
			connectedUsers.get(email).getBasicRemote()
					.sendText(values.toString());
		} else {
			System.out.println("User " + email + " isn't connected anymore!");
			throw new Exception("User " + email + " isn't connected anymore!");
		}

	}

	public static boolean userIsConnected(String cid) {
		return connectedUsers.containsKey(cid);
	}
		
}


