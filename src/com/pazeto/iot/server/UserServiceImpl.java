package com.pazeto.iot.server;

import java.util.logging.Logger;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.pazeto.iot.client.UserService;
import com.pazeto.iot.shared.vo.User;

@SuppressWarnings("serial")
public class UserServiceImpl extends RemoteServiceServlet implements
		UserService {

	private static final Logger LOG = Logger.getLogger(UserServiceImpl.class
			.getName());

	DAO db = new DAO();
	
	public void addUser(User user) {
		db.persistObject(user);
	}
}
