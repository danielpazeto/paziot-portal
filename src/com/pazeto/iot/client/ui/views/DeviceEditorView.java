package com.pazeto.iot.client.ui.views;

import java.util.ArrayList;
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
import com.pazeto.iot.client.CustomAsyncCall;
import com.pazeto.iot.client.services.DeviceService;
import com.pazeto.iot.client.services.DeviceServiceAsync;
import com.pazeto.iot.client.services.UserService;
import com.pazeto.iot.client.services.UserServiceAsync;
import com.pazeto.iot.client.ui.BaseComposite;
import com.pazeto.iot.shared.Util;
import com.pazeto.iot.shared.vo.Device;
import com.pazeto.iot.shared.vo.User;

public class DeviceEditorView extends BaseComposite {

	private static DeviceEditorView uniqueInstance;

	public static DeviceEditorView getInstance() {
		return getInstance(null);
	}

	public static DeviceEditorView getInstance(Device device) {
		currentDev = device;
		if (uniqueInstance == null) {
			uniqueInstance = new DeviceEditorView();
		}
		refreshInfo();
		return uniqueInstance;
	}

	private static void refreshInfo() {
		if (currentDev == null) {
			currentDev = new Device(Util.getUserLogged());
		} else {
			chipIdField.setEnabled(false);
		}
		chipIdField.setText(currentDev.getChipId());
		nameField.setText(currentDev.getName());
		refreshUserComboBox();
		for (User u : userList) {
			if (u.getId().equals(currentDev.getUserId())) {
				dropBoxUser.setSelectedIndex(userList.indexOf(u));
			}
		}
	}

	static List<User> userList = new ArrayList<User>();

	private static void refreshUserComboBox() {
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
			}

			@Override
			protected void callService(AsyncCallback<ArrayList<User>> cb) {
				userService.listAll(cb);
			}
		}.go();
	}

	private final static UserServiceAsync userService = GWT
			.create(UserService.class);

	private final DeviceServiceAsync deviceService = GWT
			.create(DeviceService.class);

	private static Device currentDev;
	private static TextBox chipIdField = new TextBox();
	private static TextBox nameField = new TextBox();
	private static ListBox dropBoxUser = new ListBox();

	private Button sendBtn;
	private Button closeBtn;

	private DialogBox dialogBox;
	private Button closeDialogBoxButton;
	private Label textToServerLabel;

	public DeviceEditorView() {
		sendBtn = new Button("Enviar", new SaveDeviceButtonHandler());
		closeBtn = new Button("Cancelar", new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// DeviceEditorView.this.hide();
			}
		});

		VerticalPanel vPanel = new VerticalPanel();

		FlexTable inputTable = new FlexTable();
		inputTable.setWidget(0, 0, new Label("Chip ID: "));
		inputTable.setWidget(0, 1, chipIdField);
		inputTable.setWidget(1, 0, new Label("Nome do Dispositivo: "));
		inputTable.setWidget(1, 1, nameField);
		inputTable.setWidget(2, 0, new Label("Usu·rio Propriet·rio: "));
		inputTable.setWidget(2, 1, dropBoxUser);
		inputTable.setWidget(4, 0, closeBtn);
		inputTable.setWidget(4, 1, sendBtn);

		inputTable.addStyleName("loginTable");

		dialogBox = new DialogBox();
		dialogBox.setText("Salvar");
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
		// this.setModal(true);
		initWidget(vPanel);
	}

	/**
	 * Create new user handle button
	 * 
	 * @author dpazeto
	 * 
	 */
	class SaveDeviceButtonHandler implements ClickHandler {
		public void onClick(ClickEvent event) {
			GWT.log(dropBoxUser.getSelectedValue());
			final Device dev = new Device(userList.get(dropBoxUser
					.getSelectedIndex()));
			dev.setName(nameField.getText());
			dev.setChipId(chipIdField.getText());

			sendBtn.setEnabled(false);

			new CustomAsyncCall<Void>() {

				@Override
				public void onSuccess(Void result) {
					textToServerLabel.setText("Dispositivo "
							+ nameField.getText() + ":" + chipIdField.getText()
							+ " criado com sucesso");
					dialogBox.center();
					closeDialogBoxButton.setFocus(true);
				}

				@Override
				public void onFailure(Throwable caught) {
					caught.printStackTrace();
					GWT.log(caught.getMessage());
					textToServerLabel.setText("Erro ao criar usu√°rio");
					dialogBox.center();
				}

				@Override
				protected void callService(AsyncCallback<Void> cb) {
					deviceService.addDevice(dev, cb);
				}
			}.go(0);
		}

	}
}
