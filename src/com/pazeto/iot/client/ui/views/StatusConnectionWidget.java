package com.pazeto.iot.client.ui.views;

import com.google.gwt.user.client.ui.Image;
import com.pazeto.iot.client.ui.ResourcesIot;

public class StatusConnectionWidget extends Image {

	public void setDeviceConnected() {
		setResource(ResourcesIot.INSTANCE.connectedCircleIcon());
	}

	public void setDeviceDisconnected() {
		setResource(ResourcesIot.INSTANCE.disconnectedCircleIcon());
	}

	public void setStatus(boolean isConnected) {
		if (isConnected) {
			setDeviceConnected();
		} else {
			setDeviceDisconnected();
		}
	}

}
