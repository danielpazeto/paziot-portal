package com.pazeto.iot.server;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.pazeto.iot.client.services.UserService;
import com.pazeto.iot.server.dao.UserDAO;
import com.pazeto.iot.shared.dto.UserDTO;
import com.pazeto.iot.shared.vo.User;

@SuppressWarnings("serial")
public class UserServiceImpl extends RemoteServiceServlet implements
		UserService {

	private static final Logger LOG = Logger.getLogger(UserServiceImpl.class
			.getName());

	public Long addUser(User user) throws Exception {
		return UserDAO.addNewUser(user);
	}

	@Override
	public ArrayList<User> listAllUsers() throws Exception {
		return (ArrayList<User>) UserDAO.listUsers();
	}

}
