package com.pazeto.iot.client.services;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.pazeto.iot.shared.vo.Device;
import com.pazeto.iot.shared.vo.IoPort;

@RemoteServiceRelativePath("ioPort")
public interface IoPortService extends RemoteService {

	ArrayList<IoPort> listAllPortsByDevice(Device dev);

	String savePort(IoPort port);

}
