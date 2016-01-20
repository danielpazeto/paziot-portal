package com.pazeto.iot.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.pazeto.iot.shared.vo.User;

public interface UserServiceAsync {

   void addUser(User user, AsyncCallback<Void> callback);
}
