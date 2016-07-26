package com.pazeto.iot.client.websocket;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONException;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.pazeto.iot.shared.Constants;
import com.pazeto.iot.shared.Util;
import com.pazeto.iot.shared.vo.IoPort;

public class UserClientWebSocket extends WebSocket {

	public interface MessageJavasCriptHandler {
		public void handleMessage(String message);
	}

	private static UserClientWebSocket uniqueInstance;
	MessageJavasCriptHandler messageHandler;

	public static UserClientWebSocket getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new UserClientWebSocket();
		}
		return uniqueInstance;
	}

	public UserClientWebSocket() {
		super(Constants.WEBSOCKET_USER_URI + Util.getUserLogged().getEmail()
				+ "/" + Util.getUserLogged().getPwd());
		messageHandlers = new ArrayList<MessageJavasCriptHandler>();
		connectWebsocket();
	}

	@Override
	public void onMessage(String msg) {
		// this.messageHandler.handleMessage(msg);
		for (MessageJavasCriptHandler handler : messageHandlers) {
			handler.handleMessage(msg);
		}

	}

	public void addMessageHandler(MessageJavasCriptHandler messageHandler) {
		// this.messageHandler = messageHandler;
		messageHandlers.add(messageHandler);
	}

	private List<MessageJavasCriptHandler> messageHandlers;

	@Override
	public void onError() {
		GWT.log("Error on websocketclient");

	}

	private void connectWebsocket() {
		open();
	}

	public void sendMessage(String message) {
		if (this.isConnected())
			send(message);
	}

	/**
	 * Request device pinout/connection status
	 */
	public void requestDeviceStatus(String chipId) {
		JSONObject responseJson = new JSONObject();
		responseJson.put("chipId", new JSONString(chipId));
		responseJson.put("status", new JSONString(chipId));
		UserClientWebSocket.getInstance().sendMessage(responseJson.toString());
	}
	
	/**
	 * BIG WORKAROUND there is the same method in the server side, because the libs JSON 
	 * @param v
	 * @return
	 * @throws JSONException
	 */
	public static String makeMessage(IoPort port, String v) throws JSONException {
		JSONObject responseJson = new JSONObject();
		JSONObject value = new JSONObject();
		value.put("ioNumber", new JSONString(port.getiONumber()));
		value.put("value", new JSONString(v));
		JSONArray arrayValues = new JSONArray();
		arrayValues.set(0, value);
		responseJson.put("values", arrayValues);
		responseJson.put("chipId", new JSONString(port.getDeviceId()));
		return responseJson.toString();
	}

}
