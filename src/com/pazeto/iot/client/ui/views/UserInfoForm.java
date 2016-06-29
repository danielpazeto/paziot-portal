package com.pazeto.iot.client.ui.views;

import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialTextBox;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.googlecode.mgwt.ui.client.widget.panel.Panel;
import com.pazeto.iot.client.services.CustomAsyncCall;
import com.pazeto.iot.client.services.UserService;
import com.pazeto.iot.client.services.UserServiceAsync;
import com.pazeto.iot.client.ui.MainRootScreen;
import com.pazeto.iot.shared.vo.User;

public class UserInfoForm extends PopupPanel {

	private static UserInfoForm uniqueInstance;
	
	public static UserInfoForm getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new UserInfoForm();
		}
		MainRootScreen.getInstance().setModalTitle("Usuário");
		return uniqueInstance;
	}

	private final UserServiceAsync userService = GWT.create(UserService.class);

	private MaterialTextBox nameField = new MaterialTextBox();
	private MaterialTextBox lastNameField = new MaterialTextBox();
	private MaterialTextBox emailField = new MaterialTextBox();
	private MaterialTextBox pwdField = new MaterialTextBox();
	private MaterialButton sendBtn;
	private MaterialButton closeBtn;

	public UserInfoForm() {
		sendBtn = new MaterialButton();
		sendBtn.addClickHandler(new NewUserButtonHandler());
		sendBtn.setText("Cadastrar");
		
		closeBtn = new MaterialButton();
		closeBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				UserInfoForm.this.hide();

			}
		});
		closeBtn.setText("Cancelar");
		
		nameField.setPlaceholder("Nome");
		lastNameField.setPlaceholder("Nome");
		emailField.setPlaceholder("Email");
		pwdField.setPlaceholder("Senha");

		Panel vPanel = new Panel();

		vPanel.add(new Label("Cadastro de Usuário"));
		vPanel.add(nameField);
//		vPanel.add(lastNameField);
		vPanel.add(emailField);
		vPanel.add(pwdField);
		vPanel.add(sendBtn);
		vPanel.add(closeBtn);
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
					UserInfoForm.getInstance().hide();
					MainRootScreen.getInstance().setModalText("Usuário " + nameField.getText()
							+ " criado com sucesso").openModal();
				}

				@Override
				public void onFailure(Throwable caught) {
					GWT.log("Error message: ",caught);
					MainRootScreen.getInstance().setModalText("Erro ao criar usuário").openModal();
				}

				@Override
				protected void callService(AsyncCallback<Long> cb) {
					userService.addUser(user, cb);
				}
			}.execute(0);
		}

	}
}
