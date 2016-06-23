package com.pazeto.iot.client.ui;

import gwt.material.design.client.constants.ButtonType;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialTextBox;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.googlecode.mgwt.ui.client.widget.panel.Panel;
import com.pazeto.iot.client.services.CustomAsyncCall;
import com.pazeto.iot.client.services.LoginService;
import com.pazeto.iot.client.services.LoginServiceAsync;
import com.pazeto.iot.client.ui.views.UserInfoForm;
import com.pazeto.iot.shared.Util;
import com.pazeto.iot.shared.vo.User;

public class LoginPage extends BaseComposite {

	private static final int COOKIE_TIMEOUT = 1000 * 60 * 60 * 24;

	private final LoginServiceAsync loginService = GWT
			.create(LoginService.class);

	private static LoginPage loginPageInstance;

	public static LoginPage getInstance() {
		if (loginPageInstance == null) {
			loginPageInstance = new LoginPage();
		}
		return loginPageInstance;
	}

	private MaterialTextBox emailField;
	private MaterialTextBox pwdField;
	private MaterialButton loginButton;

    private MaterialButton newUserButton;

	public LoginPage() {
		super();
		setModalTitle("Login");

		loginButton = new MaterialButton();
		loginButton.addClickHandler(new LoginButtonHandler());
		loginButton.setText("Enviar");
		    
		newUserButton = new MaterialButton(ButtonType.LINK);
		newUserButton.addClickHandler(new NewUserButtonHandler());
		newUserButton.setText("Registrar");
		
		emailField = new MaterialTextBox();emailField.setPlaceholder("Email");
		pwdField = new MaterialTextBox();pwdField.setPlaceholder("Senha");

		loginButton.addStyleName("sendButton");
		newUserButton.addStyleName("sendButton");

		Panel vPanel = new Panel();
		vPanel.add(emailField);
		vPanel.add(pwdField);
		vPanel.add(loginButton);
		vPanel.add(newUserButton);

		emailField.setFocus(true);
		vPanel.addStyleName("loginTable");
		initBaseWidget(vPanel);
	}

    @Override
	public ClickHandler getCloseButtonHandlerClick() {
		return new ClickHandler() {
			public void onClick(ClickEvent event) {
				getModal().closeModal();
				loginButton.setEnabled(true);
				newUserButton.setEnabled(true);
				loginButton.setFocus(true);
			}
		};
	}

	class LoginButtonHandler implements ClickHandler {
		public void onClick(ClickEvent event) {
			doLogin();
		}

		private void doLogin() {
			final User user = new User();
			user.setEmail(emailField.getText());
			user.setPwd(pwdField.getText());
			loginButton.setEnabled(false);
			new CustomAsyncCall<User>() {

				@Override
				public void onSuccess(User result) {
					if (result != null /* && result.getLoggedIn() */) {
						// set session cookie for 1 day expiry.
						String sessionID = result.getSessionId();
						final long DURATION = 1000 * 60 * 60 * 24 * 1;
						Date expires = new Date(System.currentTimeMillis()
								+ DURATION);
						Cookies.setCookie("sid", sessionID, expires, null, "/",
								false);

						Util.setUserLogged(result);
						openHomePage();
					} else {
						setModalText("Nome e/ou senha inválidos")
								.openModal();
					}
				}

				@Override
				public void onFailure(Throwable caught) {
					caught.printStackTrace();
					setModalText("Erro ao executar o login.").openModal();
				}

				@Override
				protected void callService(AsyncCallback<User> cb) {
					loginService.doAuthentication(user, cb);
				}
			}.execute();
		}
	}

	/**
	 * Create new user handle button
	 * 
	 * @author dpazeto
	 */
	class NewUserButtonHandler implements ClickHandler {
		public void onClick(ClickEvent event) {
			// open my pop to new user
			UserInfoForm.getInstance().center();
			// loginWithGooglePlus();
		}

	}

	final static String COOKIE_NAME = "__user_logged";

	void setCookieUserLogged() {
		Date expires = new Date((new Date()).getTime() + COOKIE_TIMEOUT);

		// Verify the name is valid
		// if (name.length() < 1) {
		// Window.alert(constants.cwCookiesInvalidCookie());
		// return;
		// }

		// Set the cookie value
		Cookies.setCookie(COOKIE_NAME, emailField.getText(), expires);
	}

	public static boolean isUserLogged() {
		return Cookies.getCookie(COOKIE_NAME) != null;
	}

	public static String getUserLoggedName() {
		return Cookies.getCookie(COOKIE_NAME);
	}

}
