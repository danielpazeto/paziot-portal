package com.pazeto.iot.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.pazeto.iot.shared.vo.User;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("user")
public interface UserService extends RemoteService {

	
	void addUser(User user) throws Exception;

	
	
}