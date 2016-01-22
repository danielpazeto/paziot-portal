package com.pazeto.iot.client.ui.views;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.pazeto.iot.client.ui.UiViewHandler;

public class HeaderView extends Composite {
	final String IMR_URL = "http://icons.iconarchive.com/icons/xaml-icon-studio/agriculture/128/Fruits-Vegetables-icon.png";
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
		vPanel.add(image);
		vPanel.setStyleName("header");
		vPanel.setHeight("10%");

		// Add a normal ToggleButton
		Button normalToggleButton = new Button("Home");
		normalToggleButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				UiViewHandler.getInstance().openMainPage();
			}
		});

		HorizontalPanel hPanelButtons = new HorizontalPanel();
		hPanelButtons.setWidth("100%");
		hPanelButtons.add(normalToggleButton);

		vPanel.add(hPanelButtons);

		initWidget(vPanel);
	}

}
