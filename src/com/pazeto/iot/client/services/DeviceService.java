package com.pazeto.iot.client.services;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.pazeto.iot.shared.vo.Device;
import com.pazeto.iot.shared.vo.User;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("device")
public interface DeviceService extends RemoteService {

	void saveDevice(Device dev) throws Exception;

	ArrayList<Device> listAll(User user) throws Exception;

}