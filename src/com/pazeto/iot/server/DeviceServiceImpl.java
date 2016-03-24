package com.pazeto.iot.server;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.pazeto.iot.client.services.DeviceService;
import com.pazeto.iot.server.dao.DeviceDAO;
import com.pazeto.iot.shared.dto.DeviceDTO;
import com.pazeto.iot.shared.vo.Device;
import com.pazeto.iot.shared.vo.User;

@SuppressWarnings("serial")
public class DeviceServiceImpl extends RemoteServiceServlet implements
		DeviceService {

	private static final Logger LOG = Logger.getLogger(DeviceServiceImpl.class
			.getName());

	public ArrayList<Device> listAll(User user) throws Exception {
		return (ArrayList<Device>) DeviceDAO.listDeviceByUser(user);
	}

	@Override
	public void addDevice(Device dev) throws Exception {
		DeviceDAO.persistDevice(dev);
	}

}
