package com.pazeto.iot.client.services;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.pazeto.iot.shared.vo.Device;
import com.pazeto.iot.shared.vo.User;

public interface DeviceServiceAsync {

	void addDevice(Device dev, AsyncCallback<Void> callback);

	void listAll(User user, AsyncCallback<ArrayList<Device>> callback);
}
