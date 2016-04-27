package com.pazeto.iot.server.websocket;

import java.io.IOException;

import javax.websocket.CloseReason;
import javax.websocket.CloseReason.CloseCode;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.MessageHandler;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.pazeto.iot.server.dao.UserDAO;
import com.pazeto.iot.shared.Constants.DEVICE_STATUS;

@ServerEndpoint(value = "/user_con/{email}/{userPwd}")
public class UserConnectionWebSocket {

	private String email;
	private String pwd;

	@OnMessage
	private void onMessage(String message, Session s) {

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
			if (jsonMessage.has("chipId")) {
				String cid = jsonMessage.getString("chipId");
				if (HandleConnectedDevices.deviceIsConnected(cid)) {
					if (jsonMessage.has("values")) {
						// values sent from user to board
						JSONObject jsonToDevice = new JSONObject();
						jsonToDevice.put("user_email", this.email);
						jsonToDevice.put("values",
								jsonMessage.getJSONArray("values"));
						HandleConnectedDevices.sendMessageToDevice(cid,
								jsonToDevice);
					} else if (jsonMessage.has("status")) {
						JSONObject jsonToDevice = new JSONObject(message);
						jsonToDevice.put("status", jsonMessage.get("status"));
						jsonToDevice.put("user_email", this.email);
						System.out
								.println("Servidor recebeu requerimento de status do usuario "
										+ this.email
										+ "para "
										+ jsonMessage.get("status"));
						HandleConnectedDevices.sendMessageToDevice(cid,
								jsonToDevice);
					}
				} else {
					// send message back to user warning it that chipId is
					// DISCONNECTED
					JSONObject jsonResponse = new JSONObject();
					jsonResponse.put("chipId", cid);
					jsonResponse.put("status",
							DEVICE_STATUS.DISCONNECTED.name());
					s.getBasicRemote().sendText(jsonResponse.toString());
				}

			} else {
//				throw new JsonException("Unexpected Json Format!!");
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
	public void onOpen(@PathParam("email") final String email,
			@PathParam("userPwd") final String pwd, final Session session)
			throws Exception {
		this.email = email;
		this.pwd = pwd;
		System.out.println("User with email: " + email + ", trying connect..");
		if (UserDAO.doAuthentication(email, pwd) != null) {
			HandleConnectedUsers.onOpenUserSession(email, session);
			System.out.println("User with email: " + email + " connected..");
			session.addMessageHandler(new MessageHandler.Whole<String>() {
				@Override
				public void onMessage(String message) {
					UserConnectionWebSocket.this.onMessage(message, session);
				}
			});

		} else {
			// close connection
			session.close(new CloseReason(new CloseCode() {
				@Override
				public int getCode() {
					return CloseCodes.UNEXPECTED_CONDITION.getCode();
				}
			}, "Not authorized! invalid User"));
			System.out.println("Invalid User trying connect: " + email);
		}

	}

	@OnError
	public void onError(Throwable t) {
		t.printStackTrace();
	}

	@OnClose
	public void onClose(Session session) {
		HandleConnectedUsers.onCloseUserSession(session);
	}

}
