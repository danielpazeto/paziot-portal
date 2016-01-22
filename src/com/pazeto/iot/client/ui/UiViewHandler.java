package com.pazeto.iot.client.ui;

import java.util.logging.Logger;

import com.pazeto.iot.client.ui.views.MenuView;
import com.pazeto.iot.shared.Util;

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
	}

	private static UiViewHandler uniqueInstance;

	public static UiViewHandler getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new UiViewHandler();
		}
		return uniqueInstance;
	}

	public void openMainPage() {
		if (checkUserLogged()) {
			mainScreen.getContentView().clear();
			mainScreen.getContentView().add(HomePage.getInstance());
		}
	}

	public void openLoginPage() {
		mainScreen.getContentView().clear();
		MenuView.getInstance().setVisible(false);
		LoginPage loginPage = LoginPage.getInstance();
		mainScreen.getContentView().add(loginPage);
	}

	// public void openProductsPage() {
	// checkUserLogged();
	// mainScreen.bodyView.clear();
	// ProductsPage productsPage = ProductsPage.getInstance();
	// mainScreen.bodyView.add(productsPage);
	// }

	private boolean checkUserLogged() {
		LOG.info("Usuario logado : " + Util.getUserLogged().toString());
		if (Util.getUserLogged() != null) {
			if (!MenuView.getInstance().isVisible()) {
				MenuView.getInstance().setVisible(true);
			}
			return true;
		}
		openLoginPage();
		return false;
	}

	// public void openCompanysPage() {
	// checkUserLogged();
	// mainScreen.bodyView.clear();
	// CompanysPage cpPage = CompanysPage.getInstance();
	// mainScreen.bodyView.add(cpPage);
	//
	// }
	//
}
