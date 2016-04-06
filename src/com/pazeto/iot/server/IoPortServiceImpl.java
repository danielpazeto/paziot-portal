package com.pazeto.iot.server;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.pazeto.iot.client.services.IoPortService;
import com.pazeto.iot.server.dao.IoPortDAO;
import com.pazeto.iot.shared.vo.Device;
import com.pazeto.iot.shared.vo.IoPort;

@SuppressWarnings("serial")
public class IoPortServiceImpl extends RemoteServiceServlet implements
		IoPortService {

	private static final Logger LOG = Logger.getLogger(IoPortServiceImpl.class
			.getName());

	private IoPortDAO portDAO = new IoPortDAO();

	@Override
	public ArrayList<IoPort> listAll(Device dev) {
		return portDAO.listAllPortsByDevice(dev);
	}

	@Override
	public Long savePort(IoPort port) {
		return (Long) portDAO.persistIoPort(port);
	}

}
