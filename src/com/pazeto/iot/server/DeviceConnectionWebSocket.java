package com.pazeto.iot.server;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.websocket.MessageHandler;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.pazeto.iot.server.dao.DeviceDAO;
import com.pazeto.iot.server.dao.MonitoredValueDAO;
import com.pazeto.iot.shared.dto.MonitoredValueDTO;

@ServerEndpoint(value = "/dev_con")
public class DeviceConnectionWebSocket {

	@OnMessage
	private void onMessage(String message, final Session s) {

		try {
			String response = handleMessage(message, s);
			if (response != null) {
				s.getBasicRemote().sendText(response);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private String handleMessage(String message, Session s) throws IOException {
		String chipId = null;
		try {
			JSONObject jsonMessage = new JSONObject(message);
			System.out.println(jsonMessage.toString());
			System.out.println(jsonMessage.getString("chipId"));
			chipId = jsonMessage.getString("chipId");

			if (DeviceDAO.isValidDevice(chipId)) {
				// verifica se há algum valor para ser salva na base
				if (jsonMessage.has("monitored_values")) {
					JSONArray values = jsonMessage
							.getJSONArray("monitored_values");
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
			} else {
				throw new Exception("Not valid ID");
			}

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
	public void onOpen(final Session session) throws IOException {
		session.addMessageHandler(new MessageHandler.Whole<String>() {
			@Override
			public void onMessage(String message) {
				DeviceConnectionWebSocket.this.onMessage(message, session);
			}
		});
	}

	@OnError
	public void onError(Throwable t) {
		t.printStackTrace();
	}

	@OnClose
	public void onClose(Session session) {

	}
}
