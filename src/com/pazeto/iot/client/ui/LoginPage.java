package com.pazeto.iot.client.ui;

import java.util.Date;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.pazeto.iot.client.CustomAsyncCall;
import com.pazeto.iot.client.LoginService;
import com.pazeto.iot.client.LoginServiceAsync;
import com.pazeto.iot.client.UserService;
import com.pazeto.iot.client.UserServiceAsync;
import com.pazeto.iot.shared.vo.User;

public class LoginPage extends Composite {

	private static final int COOKIE_TIMEOUT = 1000 * 60 * 60 * 24;

	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	private final LoginServiceAsync loginService = GWT
			.create(LoginService.class);
	private final UserServiceAsync userService = GWT.create(UserService.class);

	private static final Logger LOG = Logger.getLogger(LoginPage.class
			.getName());

	private static LoginPage loginPageInstance;

	public static LoginPage getInstance() {
		if (loginPageInstance == null) {
			loginPageInstance = new LoginPage();
		}
		return loginPageInstance;
	}

	private UiViewHandler uiHandler;

	private TextBox emailField;
	private TextBox pwdField;
	private Label errorLabel;
	private Button loginButton, newUserButton;

	private Label textToServerLabel;
	private Button closeButton;

	private DialogBox dialogBox;

	public LoginPage() {
		uiHandler = UiViewHandler.getInstance();

		dialogBox = new DialogBox();
		dialogBox.setText("Login result");
		dialogBox.setAnimationEnabled(true);
		closeButton = new Button("Close");
		closeButton.getElement().setId("closeButton");
		textToServerLabel = new Label();
		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.addStyleName("dialogVPanel");
		dialogVPanel.add(textToServerLabel);
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		dialogVPanel.add(closeButton);
		dialogBox.setWidget(dialogVPanel);

		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				loginButton.setEnabled(true);
				newUserButton.setEnabled(true);
				loginButton.setFocus(true);
			}
		});

		initWidget(createLoginForm());

	}

	private Panel createLoginForm() {
		loginButton = new Button("Enviar", new LoginButtonHandler());
		newUserButton = new Button("Novo Usuário", new NewUserButtonHandler());
		emailField = new TextBox();
		pwdField = new TextBox();

		errorLabel = new Label();

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

		return panelForm;
	}

	class LoginButtonHandler implements ClickHandler {
		public void onClick(ClickEvent event) {
			doLogin();
		}

		private void doLogin() {
			errorLabel.setText("");
			final User user = new User();
			user.setEmail(emailField.getText());
			user.setPwd(pwdField.getText());
			loginButton.setEnabled(false);
			new CustomAsyncCall<User>() {

				@Override
				public void onSuccess(User result) {
					if (result != null) {
						textToServerLabel.setText("Logado com sucesso");
//						String sessionID = result.getSessionId();
//						final long DURATION = 1000 * 60 * 60 * 24 * 1;
//						Date expires = new Date(System.currentTimeMillis()
//								+ DURATION);
//						Cookies.setCookie("sid", sessionID, expires, null, "/",
//								false);
//						new Util().setUserLogged(result);
						uiHandler.openMainPage();

					} else {
						textToServerLabel.setText("Nome e/ou senha inválidos");
					}
					dialogBox.center();
					closeButton.setFocus(true);
				}

				@Override
				public void onFailure(Throwable caught) {
					textToServerLabel.setText("Nome e/ou senha inv�lidos");
				}

				@Override
				protected void callService(AsyncCallback<User> cb) {
					loginService.doAuthentication(user, cb);
				}
			}.go(0);
		}
	}

	/**
	 * Create new user handle button
	 * 
	 * @author dpazeto
	 *
	 */
	class NewUserButtonHandler implements ClickHandler {
		public void onClick(ClickEvent event) {
			// open my pop to new user
			new UserInfoForm().center();
//			loginWithGooglePlus();
		}

	}

	public void checkWithServerIfSessionIdIsStillLegal() {

		new CustomAsyncCall<User>() {

			@Override
			public void onSuccess(User result) {
				if (result == null) {
					uiHandler.openLoginPage();
				} else {
					textToServerLabel.setText("Logado com sucesso");
					dialogBox.center();
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				textToServerLabel.setText("Falha enquanto logando...");
				dialogBox.center();
			}

			@Override
			protected void callService(AsyncCallback<User> cb) {
				loginService.loginFromSessionServer(cb);
			}
		}.go(1);
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
	
	
//	String AUTH_URL = "https://accounts.google.com/o/oauth2/auth";
//	String CLIENT_ID = "954926222788-40vcb7hp46l9r776886mg1o3nk597ikk.apps.googleusercontent.com"; // available from the APIs console
//	String BUZZ_READONLY_SCOPE = "https://www.googleapis.com/auth/buzz.readonly";
//	String BUZZ_PHOTOS_SCOPE = "https://www.googleapis.com/auth/photos";

//	AuthRequest req = new AuthRequest(AUTH_URL, CLIENT_ID)
//	    .withScopes(BUZZ_READONLY_SCOPE, BUZZ_PHOTOS_SCOPE); // Can specify multiple scopes here
//	
//	void loginWithGooglePlus(){
//		
//		Auth.get().login(req, new Callback<String, Throwable>() {
//			  @Override
//			  public void onSuccess(String token) {
//			    // You now have the OAuth2 token needed to sign authenticated requests.
//			  }
//			  @Override
//			  public void onFailure(Throwable caught) {
//			    // The authentication process failed for some reason, see caught.getMessage()
//			  }
//			});
//	}

}
