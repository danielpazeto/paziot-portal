package com.pazeto.iot.server.websocket;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.websocket.DeploymentException;

import org.glassfish.tyrus.server.Server;

import com.google.gwt.core.shared.GWT;

public class InitWebSocketListener implements ServletContextListener {
	
	private Server server = null;

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		System.out.println("WEB SOCKET DESTROYED...");
		server.stop();
	}

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		try {
			server = new Server("localhost", 8025, "/", null,  DeviceConnectionWebSocket.class);
			server.start();
			System.out.println("WEB SOCKET LAUNCHED AND RUNNNING");
		} catch (final DeploymentException e1) {
			e1.printStackTrace();
		}

	}
}
