package com.pazeto.iot.server;

import java.io.IOException;
import java.io.StringReader;

import javax.websocket.MessageHandler;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.google.appengine.labs.repackaged.org.json.JSONObject;

@ServerEndpoint(value = "/dev_con")
public class DeviceConnectionWebSocket {

	@OnMessage
	private void onMessage(String message, final Session s) {

		System.out.println("Received message: " + message);
		JSONObject jsonMessage = new JSONObject(new StringReader(message));
		try {
			s.getBasicRemote().sendText("Valeu recebi" + message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@OnOpen
	public void onOpen(final Session session) throws IOException {
		session.getBasicRemote().sendText("onOpen");
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
