package com.pazeto.iot.server.websocket;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.websocket.Session;

import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.pazeto.iot.server.dao.EventDAO;
import com.pazeto.iot.shared.vo.Event.ENTITIES;

public class HandleConnectedDevices {

	static Map<String, Session> connecteDevices = new HashMap<String, Session>();

	public static void onOpenDeviceSession(String chipId, Session session) {
		connecteDevices.put(chipId, session);
		EventDAO.saveConnectionEvent(chipId, ENTITIES.DEVICE);
	}

	public static void onCloseDeviceSession(Session session) {
		for (Entry<String, Session> connectedDev : connecteDevices.entrySet()) {
			if (connectedDev.getValue().equals(session)) {
				connecteDevices.remove(connectedDev.getKey());
				EventDAO.saveDisconnectionEvent(connectedDev.getKey(),
						ENTITIES.DEVICE);
			}
		}
	}

	public static void sendMessageToDevice(String cid, JSONObject values)
			throws Exception {
		if (deviceIsConnected(cid)) {
			connecteDevices.get(cid).getBasicRemote()
					.sendText(values.toString());
		} else {
			throw new Exception("Device" + cid + "isn't connected!");
		}
	}

	public static boolean deviceIsConnected(String cid) {
		return connecteDevices.containsKey(cid);
	}

}
