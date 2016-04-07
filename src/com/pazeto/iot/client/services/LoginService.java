package com.pazeto.iot.client.services;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.pazeto.iot.shared.vo.User;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("login")
public interface LoginService extends RemoteService {
	User doAuthentication(User user) throws IllegalArgumentException, Exception;

	User loginFromSessionServer();

	boolean changePassword(String name, String newPassword);

	void logout();
	

}