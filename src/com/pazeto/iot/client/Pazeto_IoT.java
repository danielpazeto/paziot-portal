package com.pazeto.iot.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.pazeto.iot.client.services.LoginService;
import com.pazeto.iot.client.services.LoginServiceAsync;
import com.pazeto.iot.client.ui.MainRootScreen;
import com.pazeto.iot.client.ui.UiViewHandler;
import com.pazeto.iot.shared.vo.User;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Pazeto_IoT implements EntryPoint {

	private final LoginServiceAsync loginService = GWT
			.create(LoginService.class);

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

//		MainRootScreen mainScreen = MainRootScreen.getInstance();
//
//		RootPanel.get().add(mainScreen);
//		RootPanel.get().setStyleName("main-div");
		
//		if (LoginPage.isUserLogged()) {
//			UiViewHandler.getInstance().openMainPage();
//		} else {
//			UiViewHandler.getInstance().openLoginPage();
//		}

		String sessionID = Cookies.getCookie("sid");
		if (sessionID == null) {
			UiViewHandler.getInstance().openLoginPage();
		} else {
			checkWithServerIfSessionIdIsStillLegal(sessionID);
		}

	}

	private void checkWithServerIfSessionIdIsStillLegal(String sessionID) {
		loginService.loginFromSessionServer(new AsyncCallback<User>() {
			@Override
			public void onFailure(Throwable caught) {
				UiViewHandler.getInstance().openLoginPage();
			}

			@Override
			public void onSuccess(User result) {
				if (result == null) {
					GWT.log("1");
					UiViewHandler.getInstance().openLoginPage();
				} else {
					GWT.log("2");
					if (result.getLoggedIn()) {
						GWT.log("3");
						UiViewHandler.getInstance().openHomePage();
					} else {
						GWT.log("4");
						UiViewHandler.getInstance().openLoginPage();
					}
				}
			}

		});
	}
}
