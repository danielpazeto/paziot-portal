package com.pazeto.iot.server.websocket;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.websocket.DeploymentException;

import org.glassfish.tyrus.server.Server;

import com.pazeto.iot.shared.Constants;

public class InitWebSocketListener implements ServletContextListener {

	private Server deviceWebCosketServer = null;
	private Server userClientWebCosketServer = null;

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		System.out.println("WEB SOCKETS DESTROYED...");
		deviceWebCosketServer.stop();
		userClientWebCosketServer.stop();
	}

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		try {
			deviceWebCosketServer = new Server("localhost",
					Constants.WEBSOCKET_DEV_PORT, "/", null,
					DeviceConnectionWebSocket.class);
			deviceWebCosketServer.start();
			System.out.println("DEVICE WEB SOCKET STARTED");
			userClientWebCosketServer = new Server("localhost",
					Constants.WEBSOCKET_USER_PORT, "/", null,
					UserConnectionWebSocket.class);
			userClientWebCosketServer.start();
			System.out.println("USER WEB SOCKET STARTED");
		} catch (final DeploymentException e1) {
			e1.printStackTrace();
		}

	}
}
