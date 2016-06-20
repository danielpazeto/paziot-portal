package com.pazeto.iot.client.services;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.pazeto.iot.shared.vo.Device;
import com.pazeto.iot.shared.vo.IoPort;

public interface IoPortServiceAsync {

	void listAllPortsByDevice(Device dev, AsyncCallback<ArrayList<IoPort>> callback);

	void savePort(IoPort port, AsyncCallback<String> callback);

}
