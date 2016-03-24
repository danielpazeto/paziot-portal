package com.pazeto.iot.server;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.websocket.DeploymentException;

import org.glassfish.tyrus.server.Server;

import com.google.gwt.core.shared.GWT;

public class InitWebSocketListener implements ServletContextListener {
	
	private Server server = null;

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		GWT.log("WebSocket Server stopped!");
		server.stop();
	}

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		try {
			GWT.log("WebSocket Server launched!");
			server = new Server("localhost", 8025, "/", null,  DeviceConnectionWebSocket.class);
			server.start();
		} catch (final DeploymentException e1) {
			e1.printStackTrace();
		}

	}
}
