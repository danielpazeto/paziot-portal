package com.pazeto.iot.client.ui.views;

import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialListBox;
import gwt.material.design.client.ui.MaterialTextBox;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.googlecode.mgwt.ui.client.widget.panel.Panel;
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
	private static MaterialTextBox chipIdField = new MaterialTextBox();
	private static MaterialTextBox nameField = new MaterialTextBox();
	private static MaterialListBox dropBoxUser = new MaterialListBox();

	private MaterialButton sendBtn = new MaterialButton();

	public DeviceEditorView() {

		sendBtn.setText("Salvar");
		sendBtn.addClickHandler(new SaveDeviceButtonHandler());
		chipIdField.setPlaceholder("Chip ID");
		nameField.setPlaceholder("Nome do dispositivo");
		dropBoxUser.setPlaceholder("Usuário Proprietário");
		Panel inputTable = new Panel();
		inputTable.add(chipIdField);
		inputTable.add(nameField);
		inputTable.add(dropBoxUser);
		inputTable.add(sendBtn);

		inputTable.addStyleName("loginTable");

		initBaseWidget(inputTable);
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
					setModalText(
							"Dispositivo " + nameField.getText() + ":"
									+ chipIdField.getText()
									+ " criado com sucesso").openModal();
					chipIdField.setEnabled(false);
					sendBtn.setEnabled(true);
					MenuView.getInstance().loadMyDevicesItemMenu();
				}

				@Override
				public void onFailure(Throwable caught) {
					caught.printStackTrace();
					GWT.log("Msg error: " + caught.getMessage());
					setModalText("Erro ao criar Dispositivo").openModal();
					sendBtn.setEnabled(true);
				}

				@Override
				protected void callService(AsyncCallback<Void> cb) {
					deviceService.saveDevice(DevicePage.getCurrentDev(), cb);
				}
			}.execute(0);
		}

	}

	@Override
	protected String getModalTitle() {
		return "Dispositivo";
	}
}
