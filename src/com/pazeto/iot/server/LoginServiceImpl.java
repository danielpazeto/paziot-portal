package com.pazeto.iot.server;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.pazeto.iot.client.LoginService;
import com.pazeto.iot.shared.vo.User;

@SuppressWarnings("serial")
public class LoginServiceImpl extends RemoteServiceServlet implements
		LoginService {

	DAO db = new DAO();

	public User doAuthentication(User user) throws IllegalArgumentException {
		DAO db = new DAO();
		User u = db.doAuthentication(user);
		if (u != null) {
			storeUserInSession(u);
			u.setLoggedIn(true);
		}
		return u;

	}

	private void storeUserInSession(User user) {
		HttpServletRequest httpServletRequest = this.getThreadLocalRequest();
		HttpSession session = httpServletRequest.getSession(true);
		session.setAttribute("user", user);
	}

	private User getUserAlreadyFromSession() {
		User user = null;
		HttpServletRequest httpServletRequest = this.getThreadLocalRequest();
		HttpSession session = httpServletRequest.getSession();
		Object userObj = session.getAttribute("user");
		if (userObj != null && userObj instanceof User) {
			user = (User) userObj;
		}
		return user;
	}
	@Override
	public void logout() {
		deleteUserFromSession();
	}

	@Override
	public User loginFromSessionServer() {
		return getUserAlreadyFromSession();
	}

	@Override
	public boolean changePassword(String name, String newPassword) {
		return false;
	}

	private void deleteUserFromSession() {
		HttpServletRequest httpServletRequest = this.getThreadLocalRequest();
		HttpSession session = httpServletRequest.getSession();
		session.removeAttribute("user");
	}

}
