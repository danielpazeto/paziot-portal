package com.pazeto.iot.client.ui.widgets;

import gwt.material.design.client.constants.IconPosition;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.ui.MaterialLink;

public class DeviceMenuItemWidget extends MaterialLink {

	public void setDeviceConnected() {
		setIconColor("green");
		addStyleName("status-icon");
		setIconType(IconType.SIGNAL_WIFI_4_BAR);
		setIconPosition(IconPosition.RIGHT);
	}

	public void setDeviceDisconnected() {
		setIconColor("red");
		addStyleName("status-icon");
		setIconType(IconType.SIGNAL_WIFI_OFF);
		setIconPosition(IconPosition.RIGHT);	
	}

	public void setStatus(boolean isConnected) {
		if (isConnected) {
			setDeviceConnected();
		} else {
			setDeviceDisconnected();
		}
	}

}
