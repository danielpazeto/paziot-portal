package com.pazeto.iot.server.websocket;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.websocket.Session;

public class HandleConnectedDevices {

	static Map<String, Session> connecteDevices = new HashMap<String, Session>();

	public static void onOpenDeviceSession(String chipId, Session session) {
		connecteDevices.put(chipId, session);
	}

	public static void onCloseDeviceSession(Session session) {
		for (Entry<String, Session> connectedDev : connecteDevices.entrySet()) {
			if (connectedDev.getValue().equals(session)) {
				connecteDevices.remove(connectedDev.getKey());
			}
		}
	}

}
