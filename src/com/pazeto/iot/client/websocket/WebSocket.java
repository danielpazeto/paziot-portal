package com.pazeto.iot.client.websocket;

import com.google.gwt.core.client.JavaScriptObject;

public abstract class WebSocket {

	private String url;
	private JavaScriptObject ws;

	public WebSocket(String url) {
		super();
		this.url = url;
	}

	public void open() {
		ws = init();
	}

	public void close() {
		if (ws != null) {
			destroy();
			ws = null;
		}
	}

	private void onClose() {
		isConnected = false;
	}

	public abstract void onError();

	public abstract void onMessage(String msg);

	private boolean isConnected;

	public boolean isConnected() {
		return isConnected;
	}

	private void onOpen() {
		isConnected = true;
	}

	private native JavaScriptObject init() /*-{
		if (!$wnd.WebSocket) {
			throw "WebSocket not supported";
		}
		var websocket = new WebSocket(
				this.@com.pazeto.iot.client.websocket.WebSocket::url);
		var wrapper = this;
		websocket.onopen = function(evt) {
			wrapper.@com.pazeto.iot.client.websocket.WebSocket::onOpen()();
		};
		websocket.onclose = function(evt) {
			wrapper.@com.pazeto.iot.client.websocket.WebSocket::onClose()();
		};
		websocket.onmessage = function(evt) {
			wrapper.@com.pazeto.iot.client.websocket.WebSocket::onMessage(Ljava/lang/String;)(evt.data);
		};
		websocket.onerror = function(evt) {
			wrapper.@com.pazeto.iot.client.websocket.WebSocket::onError()();
		};
		return websocket;
	}-*/;

	public native void send(String message) /*-{
		this.@com.pazeto.iot.client.websocket.WebSocket::ws.send(message);
	}-*/;

	private native boolean destroy() /*-{
		this.@com.pazeto.iot.client.websocket.WebSocket::ws.close();
	}-*/;

}