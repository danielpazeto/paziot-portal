package com.pazeto.iot.server.websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.websocket.MessageHandler;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.pazeto.iot.server.dao.DeviceDAO;
import com.pazeto.iot.server.dao.MonitoredValueDAO;
import com.pazeto.iot.shared.dto.MonitoredValueDTO;

@ServerEndpoint(value = "/dev_con/{chipId}")
public class DeviceConnectionWebSocket {

	@OnMessage
	private void onMessage(String chipd, String message, Session s) {

		try {
			String response = handleMessage(chipd, message, s);
			if (response != null) {
				s.getBasicRemote().sendText(response);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private String handleMessage(String cId, String message, Session s)
			throws IOException {
		String chipId = null;
		try {
			JSONObject jsonMessage = new JSONObject(message);
			System.out.println(jsonMessage.toString());
			chipId = cId;
			// verifica se h� algum valor para ser salva na base
			if (jsonMessage.has("monitored_values")) {
				JSONArray values = jsonMessage.getJSONArray("monitored_values");
				List<MonitoredValueDTO> valuesToSave = new ArrayList<MonitoredValueDTO>();
				for (int i = 0; i < values.length(); i++) {
					System.out.println(values.getJSONObject(i));
					valuesToSave.add(MonitoredValueDAO.jsonToObject(values
							.getJSONObject(i)));
				}
				MonitoredValueDAO.saveMonitoredValue(valuesToSave);
			}

			// get setted values from db
			JSONObject responseJson = new JSONObject();
			responseJson.put("chipId", chipId);

			return responseJson.toString();
		} catch (JSONException e) {
			e.printStackTrace();
			s.getBasicRemote().sendText("Json Error: " + e.toString());
		} catch (Exception e) {
			e.printStackTrace();
			s.getBasicRemote().sendText(e.toString());
		}
		return null;
	}

	@OnOpen
	public void onOpen(@PathParam("chipId") final String chipId,
			final Session session) throws Exception {
		System.out.println(chipId);
		if (DeviceDAO.isValidDevice(chipId)) {
			HandleConnectedDevices.onOpenDeviceSession(chipId, session);

			session.addMessageHandler(new MessageHandler.Whole<String>() {
				@Override
				public void onMessage(String message) {
					DeviceConnectionWebSocket.this.onMessage(chipId, message,
							session);
				}
			});

		} else {
			session.close();
			throw new Exception("Not valid ID: " + chipId);
		}

	}

	@OnError
	public void onError(Throwable t) {
		t.printStackTrace();
	}

	@OnClose
	public void onClose(Session session) {
		HandleConnectedDevices.onCloseDeviceSession(session);
	}
}
