package com.pazeto.iot.client.services;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.pazeto.iot.shared.vo.User;

public interface LoginServiceAsync {

	void doAuthentication(User user, AsyncCallback<User> callback);
	
	void loginFromSessionServer(AsyncCallback<User> callback);

	void changePassword(String name, String newPassword,
			AsyncCallback<Boolean> callback);

	void logout(AsyncCallback<Void> callback);


}
