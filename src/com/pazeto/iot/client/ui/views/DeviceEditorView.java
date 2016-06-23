package com.pazeto.iot.client.ui.views;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.pazeto.iot.client.services.CustomAsyncCall;
import com.pazeto.iot.client.services.DeviceService;
import com.pazeto.iot.client.services.DeviceServiceAsync;
import com.pazeto.iot.client.services.UserService;
import com.pazeto.iot.client.services.UserServiceAsync;
import com.pazeto.iot.client.ui.BaseComposite;
import com.pazeto.iot.client.ui.DevicePage;
import com.pazeto.iot.shared.Util;
import com.pazeto.iot.shared.vo.Device;
import com.pazeto.iot.shared.vo.User;

public class DeviceEditorView extends BaseComposite {

	private static DeviceEditorView uniqueInstance;

	public static DeviceEditorView getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new DeviceEditorView();
		}
		refreshInfo();
		return uniqueInstance;
	}

	private static void refreshInfo() {
		if (DevicePage.getCurrentDev() == null) {
			DevicePage.setCurrentDev(new Device(Util.getUserLogged()));
			DevicePage.getCurrentDev().setCreateDate(new Date());
			chipIdField.setEnabled(true);
		}

		if (DevicePage.getCurrentDev().getChipId() != null
				&& !DevicePage.getCurrentDev().getChipId().isEmpty()) {
			chipIdField.setEnabled(false);
		}

		chipIdField.setText(DevicePage.getCurrentDev().getChipId());
		nameField.setText(DevicePage.getCurrentDev().getName());
		for (User u : userList) {
			if (u.getId().equals(DevicePage.getCurrentDev().getUserId())) {
				dropBoxUser.setSelectedIndex(userList.indexOf(u));
			}
		}
		refreshUserComboBox();

	}

	static List<User> userList = new ArrayList<User>();

	private static void refreshUserComboBox() {
		if (Util.isAdmin()) {
			new CustomAsyncCall<ArrayList<User>>() {
				@Override
				public void onSuccess(ArrayList<User> result) {
					dropBoxUser.clear();
					userList.clear();
					for (User user : result) {
						userList.add(user);
						dropBoxUser.addItem(user.getEmail(),
								String.valueOf(user.getId()));
					}
				}

				@Override
				public void onFailure(Throwable caught) {
					GWT.log("Failure on user combo box");
				}

				@Override
				protected void callService(AsyncCallback<ArrayList<User>> cb) {
					userService.listAllUsers(cb);
				}
			}.executeWithoutSpinner();
		} else {
			dropBoxUser.clear();
			userList.clear();
			userList.add(Util.getUserLogged());
		}
	}

	private final static UserServiceAsync userService = GWT
			.create(UserService.class);

	private final DeviceServiceAsync deviceService = GWT
			.create(DeviceService.class);

	// private static Device currentDev;
	private static TextBox chipIdField = new TextBox();
	private static TextBox nameField = new TextBox();
	private static ListBox dropBoxUser = new ListBox();

	private Button sendBtn;

	private DialogBox dialogBox;
	private Button closeDialogBoxButton;
	private Label textToServerLabel;

	public DeviceEditorView() {
		sendBtn = new Button("Enviar", new SaveDeviceButtonHandler());

		VerticalPanel vPanel = new VerticalPanel();

		FlexTable inputTable = new FlexTable();
		inputTable.setWidget(0, 0, new Label("Chip ID: "));
		inputTable.setWidget(0, 1, chipIdField);
		inputTable.setWidget(1, 0, new Label("Nome do Dispositivo: "));
		inputTable.setWidget(1, 1, nameField);
		inputTable.setWidget(2, 0, new Label("Usuário Proprietário: "));
		inputTable.setWidget(2, 1, dropBoxUser);
		inputTable.setWidget(4, 1, sendBtn);

		inputTable.addStyleName("loginTable");

		dialogBox = new DialogBox();
		dialogBox.setText("Salvar");
		dialogBox.setAnimationEnabled(true);
		closeDialogBoxButton = new Button("Fechar");
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
		initBaseWidget(vPanel);
	}

	class SaveDeviceButtonHandler implements ClickHandler {
		public void onClick(ClickEvent event) {
			DevicePage.getCurrentDev().setUserId(
					userList.get(dropBoxUser.getSelectedIndex()).getId());
			DevicePage.getCurrentDev().setName(nameField.getText());
			DevicePage.getCurrentDev().setChipId(chipIdField.getText());
			sendBtn.setEnabled(false);

			new CustomAsyncCall<Void>() {

				@Override
				public void onSuccess(Void result) {
					textToServerLabel.setText("Dispositivo "
							+ nameField.getText() + ":" + chipIdField.getText()
							+ " criado com sucesso");
					dialogBox.center();
					chipIdField.setEnabled(false);
					closeDialogBoxButton.setFocus(true);
					MenuView.getInstance().loadMyDevicesItemMenu();
				}

				@Override
				public void onFailure(Throwable caught) {
					caught.printStackTrace();
					GWT.log("Msg error: "+caught.getMessage());
					setModalText("Erro ao criar Dispositivo").openModal();
				}

				@Override
				protected void callService(AsyncCallback<Void> cb) {
					deviceService.saveDevice(DevicePage.getCurrentDev(), cb);
				}
			}.execute(0);
		}

	}
}
