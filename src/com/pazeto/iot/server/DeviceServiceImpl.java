package com.pazeto.iot.server;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.pazeto.iot.client.DeviceService;
import com.pazeto.iot.client.UserService;
import com.pazeto.iot.shared.vo.Device;
import com.pazeto.iot.shared.vo.User;

@SuppressWarnings("serial")
public class DeviceServiceImpl extends RemoteServiceServlet implements
		DeviceService {

	private static final Logger LOG = Logger.getLogger(DeviceServiceImpl.class
			.getName());

	DAO db = new DAO();

	public ArrayList<Device> listAll(User user) throws Exception {
		return (ArrayList<Device>) db.listOjects(Device.class, user);
	}

	@Override
	public void addDevice(Device dev) throws Exception {
		db.persistDevice(dev);
	}

}
