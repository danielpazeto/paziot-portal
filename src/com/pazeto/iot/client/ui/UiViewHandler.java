package com.pazeto.iot.client.ui;

import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;
import com.pazeto.iot.client.ui.DevicePage.DeviceTabs;
import com.pazeto.iot.client.ui.views.MenuView;
import com.pazeto.iot.shared.vo.Device;

/**
 * Class to handle open/close views
 * 
 * @author dpazeto
 *
 */
public class UiViewHandler {

	private static final Logger LOG = Logger.getLogger(UiViewHandler.class
			.getName());

	private MainRootScreen mainScreen;

	public UiViewHandler() {
		mainScreen = MainRootScreen.getInstance();
		RootPanel.get().add(mainScreen);
		RootPanel.get().setStyleName("main-div");
	}

	private static UiViewHandler uniqueInstance;

	public static UiViewHandler getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new UiViewHandler();
		}
		return uniqueInstance;
	}

	public void openHomePage() {
		MenuView.getInstance().setVisible(true);
		mainScreen.getContentView().clear();
		mainScreen.getContentView().add(HomePage.getInstance());
	}

	public void openLoginPage() {
		mainScreen.getContentView().clear();
		MenuView.getInstance().setVisible(false);
		mainScreen.getContentView().add(LoginPage.getInstance());
	}

	public void openDevicePage(Device dev, DeviceTabs tab) {
		mainScreen.getContentView().clear();
		if(DeviceTabs.STATUS.equals(tab)){
			DevicePage.getInstance(dev).openStatus();		
		}else if(DeviceTabs.PROFILE.equals(tab)){
			DevicePage.getInstance(dev).openDeficeProfile();
		}
		DevicePage dvpg=DevicePage.getInstance();
		GWT.log("aeeee passei");
		GWT.log(mainScreen.getContentView().toString());
		mainScreen.getContentView().add(dvpg);
		GWT.log("aeeee passei  1233");
	}

}
