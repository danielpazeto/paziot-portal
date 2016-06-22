package com.pazeto.iot.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.pazeto.iot.client.services.LoginService;
import com.pazeto.iot.client.services.LoginServiceAsync;
import com.pazeto.iot.client.ui.UiViewHandler;
import com.pazeto.iot.shared.Util;
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
			public void onSuccess(User user) {
				if (user == null) {
					UiViewHandler.getInstance().openLoginPage();
				} else {
					if (user.getLoggedIn()) {
						Util.setUserLogged(user);
						UiViewHandler.getInstance().openHomePage();
					} else {
						UiViewHandler.getInstance().openLoginPage();
					}
				}
			}

		});
	}
}
