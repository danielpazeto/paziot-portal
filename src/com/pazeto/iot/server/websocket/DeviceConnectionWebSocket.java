package com.pazeto.iot.server.websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.websocket.CloseReason;
import javax.websocket.CloseReason.CloseCode;
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
import com.pazeto.iot.server.dao.EventDAO;
import com.pazeto.iot.shared.dto.EventDTO;

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
			// verifica se há algum valor para ser salva na base
			if (jsonMessage.has("events")) {
				JSONArray values = jsonMessage.getJSONArray("events");
				List<EventDTO> eventsToSave = new ArrayList<EventDTO>();
				for (int i = 0; i < values.length(); i++) {
					System.out.println(values.getJSONObject(i));
					eventsToSave.add(EventDAO.jsonToObject(values
							.getJSONObject(i),chipId));
				}
				EventDAO.saveEvent(eventsToSave);
			}else if(jsonMessage.has("status")){
				JSONArray statusArray = jsonMessage.getJSONArray("status");
				String toUserEmail = jsonMessage.getString("user_email");
				HandleConnectedUsers.sendMessageToUser(toUserEmail, jsonMessage);
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
			// close connection
			session.getBasicRemote().sendText("Not authorized! invalid ID or this Device didn't assigned in system yet!");
			session.close(new CloseReason(new CloseCode() {
				@Override
				public int getCode() {
					return  CloseReason.CloseCodes.VIOLATED_POLICY.getCode();
				}
			}, "Not authorized! invalid ID"));
			
			System.out.println("Invalid chip ID trying connect: " + chipId);
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
