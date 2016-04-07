package com.pazeto.iot.client.ui;

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
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;
import com.pazeto.iot.client.CustomIotPazetoAsyncCall;
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

	private TextBox emailField;
	private TextBox pwdField;
	private Button loginButton, newUserButton;

	public LoginPage() {
		super();
		setDefaultDialogBoxTitle("Login");

		loginButton = new Button("Enviar", new LoginButtonHandler());
		newUserButton = new Button("Novo Usu·rio", new NewUserButtonHandler());
		emailField = new TextBox();
		pwdField = new TextBox();

		loginButton.addStyleName("sendButton");
		newUserButton.addStyleName("sendButton");

		FlexTable table = new FlexTable();
		table.setWidget(0, 0, new Label("Nome: "));
		table.setWidget(1, 0, new Label("Senha: "));
		table.setWidget(0, 1, emailField);
		table.setWidget(1, 1, pwdField);
		table.setWidget(0, 2, loginButton);
		table.setWidget(1, 2, newUserButton);
		table.addStyleName("loginTable");

		emailField.setFocus(true);
		emailField.selectAll();
		Panel panelForm = new LayoutPanel();
		table.addStyleName("loginTable");
		panelForm.add(table);
		panelForm.setStyleName("body");

		initWidget(panelForm);
	}

	@Override
	public ClickHandler getCloseButtonHandlerClick() {
		return new ClickHandler() {
			public void onClick(ClickEvent event) {
				getDefaultDialogBox().hide();
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
			new CustomIotPazetoAsyncCall<User>() {

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

						new Util().setUserLogged(result);
						openHomePage();
					} else {
						setDefaultDialogText("Nome e/ou senha inv√°lidos")
								.center();
					}
				}

				@Override
				public void onFailure(Throwable caught) {
					caught.printStackTrace();
					setDefaultDialogText("Erro ao executar o login.").center();
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

	// public void checkWithServerIfSessionIdIsStillLegal() {
	//
	// new CustomAsyncCall<User>() {
	//
	// @Override
	// public void onSuccess(User result) {
	// if (result == null) {
	// uiHandler.openLoginPage();
	// } else {
	// textToServerLabel.setText("Logado com sucesso");
	// dialogBox.center();
	// }
	// }
	//
	// @Override
	// public void onFailure(Throwable caught) {
	// textToServerLabel.setText("Falha enquanto logando...");
	// dialogBox.center();
	// }
	//
	// @Override
	// protected void callService(AsyncCallback<User> cb) {
	// loginService.loginFromSessionServer(cb);
	// }
	// }.go(1);
	// }

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

	// String AUTH_URL = "https://accounts.google.com/o/oauth2/auth";
	// String CLIENT_ID =
	// "954926222788-40vcb7hp46l9r776886mg1o3nk597ikk.apps.googleusercontent.com";
	// // available from the APIs console
	// String BUZZ_READONLY_SCOPE =
	// "https://www.googleapis.com/auth/buzz.readonly";
	// String BUZZ_PHOTOS_SCOPE = "https://www.googleapis.com/auth/photos";

	// AuthRequest req = new AuthRequest(AUTH_URL, CLIENT_ID)
	// .withScopes(BUZZ_READONLY_SCOPE, BUZZ_PHOTOS_SCOPE); // Can specify
	// multiple scopes here
	//
	// void loginWithGooglePlus(){
	//
	// Auth.get().login(req, new Callback<String, Throwable>() {
	// @Override
	// public void onSuccess(String token) {
	// // You now have the OAuth2 token needed to sign authenticated requests.
	// }
	// @Override
	// public void onFailure(Throwable caught) {
	// // The authentication process failed for some reason, see
	// caught.getMessage()
	// }
	// });
	// }

}
