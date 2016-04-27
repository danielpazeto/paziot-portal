package com.pazeto.iot.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface ResourcesIot extends ClientBundle {

	public static final ResourcesIot INSTANCE = GWT.create(ResourcesIot.class);

	@Source("images/disconnected_circle_icon.png")
	ImageResource disconnectedCircleIcon();
	
	@Source("images/connected_circle_icon.png")
	ImageResource connectedCircleIcon();

	

}
