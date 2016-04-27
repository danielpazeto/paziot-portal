package com.pazeto.iot.client.ui.views;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.ui.Image;
import com.pazeto.iot.client.ui.ResourcesIot;

public class DeviceStatusConnectionWidget extends Image {

	public void setDeviceConnected() {
		setResource(ResourcesIot.INSTANCE.connectedCircleIcon());
	}

	public void setDeviceDisconnected() {
		setResource(ResourcesIot.INSTANCE.disconnectedCircleIcon());
	}

}
