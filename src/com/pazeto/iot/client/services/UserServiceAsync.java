package com.pazeto.iot.client.services;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.pazeto.iot.shared.vo.User;

public interface UserServiceAsync {

   void addUser(User user, AsyncCallback<Long> callback);
   
   void listAllUsers(AsyncCallback<ArrayList<User>> callback);
}
