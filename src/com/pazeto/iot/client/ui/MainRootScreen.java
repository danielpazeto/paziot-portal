package com.pazeto.iot.client.ui;

import java.util.logging.Logger;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.pazeto.iot.client.ui.views.HeaderView;
import com.pazeto.iot.client.ui.views.MenuView;

public class MainRootScreen extends Composite {

	private VerticalPanel rootPanel;
	private LayoutPanel rootBackgroundPanel;

	public static Logger logger;
	private LayoutPanel bodyView;
	private LayoutPanel contentView;

	private static MainRootScreen uniqueInstance;

	public static MainRootScreen getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new MainRootScreen();
		}
		return uniqueInstance;
	}

	public MainRootScreen() {

		rootPanel = new VerticalPanel();
		rootPanel.setWidth("100%");
		rootPanel.setHeight("100%");

		rootPanel.add(HeaderView.getInstance());
		rootPanel.setCellHeight(HeaderView.getInstance(), "10%");

		// bodyView = new LayoutPanel();
		//
		// bodyView.setWidth("100%");
		// bodyView.setStyleName("body");
		//
		// menuView = new MenuView();
		// contentView = new LayoutPanel();
		// bodyView.add(menuView);
		// bodyView.add(contentView);
		// bodyView.setWidgetLeftWidth(menuView, 0, Unit.PCT, 15, Unit.PCT);
		// bodyView.setWidgetRightWidth(contentView, 0, Unit.PCT, 85, Unit.PCT);
		loadMenuAndContentView();

		rootBackgroundPanel = new LayoutPanel();
		rootBackgroundPanel.setWidth("100%");
		rootBackgroundPanel.setHeight("100%");
		rootBackgroundPanel.setStyleName("main-div");
		rootBackgroundPanel.add(createBackgroundImage());
		rootBackgroundPanel.add(rootPanel);

		initWidget(rootBackgroundPanel);

	}

	public void loadMenuAndContentView() {
		bodyView = new LayoutPanel();

		bodyView.setWidth("100%");
		bodyView.setStyleName("body");

		contentView = new LayoutPanel();
		bodyView.add(MenuView.getInstance());
		bodyView.add(contentView);
		bodyView.setWidgetLeftWidth(MenuView.getInstance(), 0, Unit.PCT, 15, Unit.PCT);
		bodyView.setWidgetRightWidth(contentView, 0, Unit.PCT, 85, Unit.PCT);
		rootPanel.add(bodyView);
	}

	public LayoutPanel getContentView() {
		return contentView;
	}

	public LayoutPanel getBodyView() {
		return bodyView;
	}


	Image createBackgroundImage() {
		Image img = new Image(
				"http://www.planwallpaper.com/static/images/Light-Wood-Background-Wallpaper.jpg");
		img.setStyleName("background-image");
		return img;
	}

}