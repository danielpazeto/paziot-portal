package com.pazeto.iot.client.services;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.pazeto.iot.shared.vo.User;

public interface UserServiceAsync {

   void addUser(User user, AsyncCallback<Void> callback);
   
   void listAll(AsyncCallback<ArrayList<User>> callback);
}
