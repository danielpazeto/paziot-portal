package com.pazeto.iot.client.ui;

import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class MainScreen extends Composite {

	private VerticalPanel rootPanel;
	private LayoutPanel rootBackgroundPanel;

	public static Logger logger;
	VerticalPanel bodyView;
	private HorizontalPanel menuView;
	private HorizontalPanel headerView;

	private static MainScreen uniqueInstance;

	public static MainScreen getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new MainScreen();
		}
		return uniqueInstance;
	}

	public MainScreen() {
		
		rootBackgroundPanel = new LayoutPanel();
		rootPanel = new VerticalPanel();
		rootPanel.setWidth("100%");
//		mainPanel.setHeight("100%");
		rootPanel.setStyleName("footer");
		rootPanel.add(new HeaderView());
		//mainPanel.addSouth(makeFooterPanel(), 5);
//		mainPanel.addWest(makeMenuPanel(), 15);

		bodyView = new VerticalPanel();
//		bodyView.setHeight("100%");
		bodyView.setWidth("100%");
		bodyView.setStyleName("body");
		rootPanel.add(bodyView);
		
		
		rootBackgroundPanel.setWidth("100%");
		rootBackgroundPanel.setStyleName("main-div");
		rootBackgroundPanel.add(createBackgroundImage());
		rootBackgroundPanel.add(rootPanel);
		
		initWidget(rootBackgroundPanel);

	}


	private class HeaderView extends Composite {
		final String IMR_URL = "http://icons.iconarchive.com/icons/xaml-icon-studio/agriculture/128/Fruits-Vegetables-icon.png";

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
	
	Image createBackgroundImage(){
		Image img = new Image("http://www.planwallpaper.com/static/images/Light-Wood-Background-Wallpaper.jpg");
		img.setStyleName("background-image");
		return img;
	}
	


}
