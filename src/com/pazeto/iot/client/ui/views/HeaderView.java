package com.pazeto.iot.client.ui.views;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;

public class HeaderView extends Composite {
	final String IMR_URL = "https://pbs.twimg.com/media/CI13uX6UkAEIkcY.jpg";
	private static HeaderView uniqueInstance;

	public static HeaderView getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new HeaderView();
		}
		return uniqueInstance;
	}

	public HeaderView() {
		VerticalPanel vPanel = new VerticalPanel();
		final Image image = new Image();
		image.setUrl(IMR_URL);
		image.setHeight("30%");
		vPanel.add(image);
		vPanel.setStyleName("header");
		vPanel.setHeight("10%");
		HorizontalPanel hPanelButtons = new HorizontalPanel();
		hPanelButtons.setWidth("100%");
		vPanel.add(hPanelButtons);
		initWidget(vPanel);
	}

}
