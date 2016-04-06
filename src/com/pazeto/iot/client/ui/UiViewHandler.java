package com.pazeto.iot.client.ui;

import java.util.logging.Logger;

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
		mainScreen.setContentViewFullScreen(false);
	}

	public void openLoginPage() {
		mainScreen.getContentView().clear();
		MenuView.getInstance().setVisible(false);
		mainScreen.setContentViewFullScreen(true);
		mainScreen.getContentView().add(LoginPage.getInstance());
	}

	public void openDevicePage(Device dev, DeviceTabs tab) {
		mainScreen.getContentView().clear();
		DevicePage dvpg = DevicePage.getInstance(dev);
		if (DeviceTabs.STATUS.equals(tab)) {
			dvpg.openStatus();
		} else if (DeviceTabs.PROFILE.equals(tab)) {
			dvpg.openDeficeProfile();
		}
		mainScreen.getContentView().add(dvpg);
	}

}
