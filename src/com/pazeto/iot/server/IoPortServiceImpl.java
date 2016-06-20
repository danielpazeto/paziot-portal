package com.pazeto.iot.server;

import java.util.ArrayList;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.pazeto.iot.client.services.IoPortService;
import com.pazeto.iot.server.dao.IoPortDAO;
import com.pazeto.iot.server.dao.ScheduleDAO;
import com.pazeto.iot.shared.vo.Device;
import com.pazeto.iot.shared.vo.IoPort;
import com.pazeto.iot.shared.vo.Schedule;

@SuppressWarnings("serial")
public class IoPortServiceImpl extends RemoteServiceServlet implements
		IoPortService {

	private IoPortDAO portDAO = new IoPortDAO();

	@Override
	public ArrayList<IoPort> listAllPortsByDevice(Device dev) {
		return portDAO.listAllPortsByDevice(dev);
	}

	@Override
	public String savePort(IoPort port) {
		return (String) portDAO.persistIoPort(port);
	}


}
