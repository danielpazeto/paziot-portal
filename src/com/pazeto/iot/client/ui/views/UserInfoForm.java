package com.pazeto.iot.client.ui.views;

import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.pazeto.iot.client.services.CustomAsyncCall;
import com.pazeto.iot.client.services.UserService;
import com.pazeto.iot.client.services.UserServiceAsync;
import com.pazeto.iot.shared.vo.User;

public class UserInfoForm extends PopupPanel {

	private static UserInfoForm uniqueInstance;

	public static UserInfoForm getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new UserInfoForm();
		}
		return uniqueInstance;
	}

	private final UserServiceAsync userService = GWT.create(UserService.class);

    private static final Logger logger = Logger.getLogger(UserInfoForm.class.getName());
	
	private TextBox nameField;
	private TextBox pwdField;
	private TextBox emailField;
	private TextBox lastNameField;
	private Button sendBtn;
	private Button closeBtn;

	private DialogBox dialogBox;

	private Button closeDialogBoxButton;

	private Label textToServerLabel;

	public UserInfoForm() {
		sendBtn = new Button("Enviar", new NewUserButtonHandler());
		closeBtn = new Button("Cancelar", new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				UserInfoForm.this.hide();

			}
		});

		nameField = new TextBox();
		lastNameField = new TextBox();
		emailField = new TextBox();
		pwdField = new TextBox();

		VerticalPanel vPanel = new VerticalPanel();

		vPanel.add(new Label("Cadastro de Usu√°rio"));

		FlexTable inputTable = new FlexTable();
		inputTable.setWidget(0, 0, new Label("Nome: "));
		inputTable.setWidget(0, 1, nameField);
		inputTable.setWidget(1, 0, new Label("Sobrenome: "));
		inputTable.setWidget(1, 1, lastNameField);
		inputTable.setWidget(2, 0, new Label("Email: "));
		inputTable.setWidget(2, 1, emailField);
		inputTable.setWidget(3, 0, new Label("Senha: "));
		inputTable.setWidget(3, 1, pwdField);
		inputTable.setWidget(4, 1, closeBtn);
		inputTable.setWidget(4, 0, sendBtn);

		inputTable.addStyleName("loginTable");

		dialogBox = new DialogBox();
		dialogBox.setText("Cadastro");
		dialogBox.setAnimationEnabled(true);
		closeDialogBoxButton = new Button("Fechar");
		closeDialogBoxButton.getElement().setId("closeButton");
		textToServerLabel = new Label();
		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.addStyleName("dialogVPanel");
		dialogVPanel.add(textToServerLabel);
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		dialogVPanel.add(closeDialogBoxButton);
		dialogBox.setWidget(dialogVPanel);

		closeDialogBoxButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				sendBtn.setEnabled(true);
			}
		});

		vPanel.add(inputTable);
		this.setModal(true);
		this.add(vPanel);

	}

	/**
	 * Create new user handle button
	 * 
	 * @author dpazeto
	 * 
	 */
	class NewUserButtonHandler implements ClickHandler {
		public void onClick(ClickEvent event) {
			final User user = new User();
			user.setName(nameField.getText());
			user.setLastName(lastNameField.getText());
			user.setEmail(emailField.getText());
			user.setPwd(pwdField.getText());

			sendBtn.setEnabled(false);

			new CustomAsyncCall<Long>() {

				@Override
				public void onSuccess(Long result) {
					textToServerLabel.setText("Usu·rio " + nameField.getText()
							+ " criado com sucesso");
					UserInfoForm.getInstance().hide();
					dialogBox.center();
					closeDialogBoxButton.setFocus(true);
				}

				@Override
				public void onFailure(Throwable caught) {
					caught.printStackTrace();
					GWT.log("Error message",caught);
					textToServerLabel.setText("Erro ao criar usu·rio");
					dialogBox.center();
				}

				@Override
				protected void callService(AsyncCallback<Long> cb) {
					userService.addUser(user, cb);
				}
			}.execute(0);
		}

	}
}
