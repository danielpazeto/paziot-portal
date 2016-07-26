package com.pazeto.iot.client.ui.views;

import gwt.material.design.client.ui.MaterialTextBox;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.pazeto.iot.client.services.CustomAsyncCall;
import com.pazeto.iot.client.services.UserService;
import com.pazeto.iot.client.services.UserServiceAsync;
import com.pazeto.iot.client.ui.MainRootScreen;
import com.pazeto.iot.shared.vo.User;

public class UserPopupForm extends BasePopupForm {

	private static UserPopupForm uniqueInstance;

	public static UserPopupForm getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new UserPopupForm();
		}
		MainRootScreen.getInstance().setMainModalTitle("Usuário");
		return uniqueInstance;
	}

	private final UserServiceAsync userService = GWT.create(UserService.class);

	private MaterialTextBox nameField = new MaterialTextBox();
	private MaterialTextBox lastNameField = new MaterialTextBox();
	private MaterialTextBox emailField = new MaterialTextBox();
	private MaterialTextBox pwdField = new MaterialTextBox();

	public UserPopupForm() {

		nameField.setPlaceholder("Nome");
		lastNameField.setPlaceholder("Nome");
		emailField.setPlaceholder("Email");
		pwdField.setPlaceholder("Senha");

		getBasePanel().add(new Label("Cadastro de Usuário"));
		getBasePanel().add(nameField);
		// vPanel.add(lastNameField);
		getBasePanel().add(emailField);
		getBasePanel().add(pwdField);

		this.setModal(true);

		addDefaultButtons();
	}

	@Override
	ClickHandler getSaveClickHandler() {
		return new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				final User user = new User();
				user.setName(nameField.getText());
				user.setLastName(lastNameField.getText());
				user.setEmail(emailField.getText());
				user.setPwd(pwdField.getText());
				getSaveBtn().setEnabled(false);

				new CustomAsyncCall<Long>() {

					@Override
					public void onSuccess(Long result) {
						UserPopupForm.getInstance().hide();
						MainRootScreen
								.getInstance()
								.setModalText(
										"Usuário " + nameField.getText()
												+ " criado com sucesso")
								.openModal();
					}

					@Override
					public void onFailure(Throwable caught) {
						GWT.log("Error message: ", caught);
						MainRootScreen.getInstance()
								.setModalText("Erro ao criar usuário")
								.openModal();
					}

					@Override
					protected void callService(AsyncCallback<Long> cb) {
						userService.addUser(user, cb);
					}
				}.execute(0);
			}
		};
	}

	@Override
	ClickHandler getCloseClickHandler() {
		return new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				UserPopupForm.this.hide();
			}
		};
	}
}
