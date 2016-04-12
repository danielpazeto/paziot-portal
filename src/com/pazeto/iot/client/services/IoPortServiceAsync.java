package com.pazeto.iot.client.services;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.pazeto.iot.shared.vo.Device;
import com.pazeto.iot.shared.vo.IoPort;
import com.pazeto.iot.shared.vo.User;

public interface IoPortServiceAsync {

	void listAll(Device dev, AsyncCallback<ArrayList<IoPort>> callback);

	void savePort(IoPort port, AsyncCallback<String> callback);

}
