package com.pazeto.iot.client.ui.views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.HasDirection.Direction;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.StackLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.user.client.ui.Widget;
import com.pazeto.iot.client.services.CustomAsyncCall;
import com.pazeto.iot.client.services.DeviceService;
import com.pazeto.iot.client.services.DeviceServiceAsync;
import com.pazeto.iot.client.services.LoginService;
import com.pazeto.iot.client.services.LoginServiceAsync;
import com.pazeto.iot.client.ui.BaseComposite;
import com.pazeto.iot.client.ui.DevicePage.DeviceTabs;
import com.pazeto.iot.client.ui.UiViewHandler;
import com.pazeto.iot.client.websocket.UserClientWebSocket;
import com.pazeto.iot.client.websocket.UserClientWebSocket.MessageJavasCriptHandler;
import com.pazeto.iot.shared.Constants.DEVICE_STATUS;
import com.pazeto.iot.shared.Util;
import com.pazeto.iot.shared.vo.Device;

public class MenuView extends BaseComposite {

	private final static DeviceServiceAsync deviceService = GWT
			.create(DeviceService.class);
	private final LoginServiceAsync loginService = GWT
			.create(LoginService.class);

	private static MenuView uniqueInstance;

	public static MenuView getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new MenuView();
		}
		loadMyDevicesItemMenu();
		return uniqueInstance;
	}

	private VerticalPanel profileItemMenu = new VerticalPanel();
	private static VerticalPanel devicesItemMenu = new VerticalPanel();
	private VerticalPanel reportsItemMenu = new VerticalPanel();
	private Button btnAddNewDevice = new Button("Novo Disp.");
	private static VerticalPanel myDevicesList = new VerticalPanel();

	public MenuView() {
		if (Util.getUserLogged() != null) {
			UserClientWebSocket.getInstance().addMessageHandler(
					messageFromServer);
		}
		StackLayoutPanel p = new StackLayoutPanel(Unit.EM);
		p.add(devicesItemMenu, new HTML("Dispositivos"), 3);
		p.add(reportsItemMenu, new HTML("Relatórios"), 3);
		p.add(profileItemMenu, new HTML("Perfil"), 3);

		buildProfileItemsMenu();
		buildDevicesItemsMenu();

		initWidget(p);
	}

	private void buildDevicesItemsMenu() {
		btnAddNewDevice.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				UiViewHandler.getInstance().openDevicePage(null,
						DeviceTabs.PROFILE);
			}
		});
		HorizontalPanel hPanelTop = new HorizontalPanel();
		hPanelTop.add(btnAddNewDevice);
		hPanelTop.add(makeRefreshButton());
		hPanelTop.setWidth("100%");
		devicesItemMenu.add(hPanelTop);
		devicesItemMenu.setWidth("100%");
		devicesItemMenu.setHeight("100%");
		loadMyDevicesItemMenu();
	}

	private void buildProfileItemsMenu() {
		profileItemMenu.add(new Button("Logout", new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				new CustomAsyncCall<Void>() {

					@Override
					public void onFailure(Throwable caught) {

					}

					@Override
					public void onSuccess(Void result) {
						UiViewHandler.getInstance().openLoginPage();
					}

					@Override
					protected void callService(AsyncCallback<Void> cb) {
						loginService.logout(cb);
					}

				}.execute();
			}
		}));

		HorizontalPanel hpUserWebScoketConnection = new HorizontalPanel();
		hpUserWebScoketConnection.add(new Label("Websocket de Usuário"));
		StatusConnectionWidget iconStatus = new StatusConnectionWidget();
		if (Util.getUserLogged()!=null)
			iconStatus.setStatus(UserClientWebSocket.getInstance()
					.isConnected());
		hpUserWebScoketConnection.add(iconStatus);
		profileItemMenu.add(hpUserWebScoketConnection);
		profileItemMenu.setWidth("100%");
		profileItemMenu.setHeight("100%");

	}

	public static void loadMyDevicesItemMenu() {
		new CustomAsyncCall<ArrayList<Device>>() {

			@Override
			public void onSuccess(ArrayList<Device> result) {
				myDevicesList.clear();
				deviceStatusMenuIcon.clear();
				myDevicesList.setWidth("100%");
				if (result != null && result.size() > 0) {
					GWT.log(String.valueOf(result.size()));
					for (Device device : result) {
						myDevicesList.add(createMenuDeviceItem(device));
						UserClientWebSocket.getInstance().requestDeviceStatus(
								device.getChipId());
					}
					devicesItemMenu.add(myDevicesList);
				} else {
					myDevicesList.add(new HTML("Nenhum disp. adicionado"));
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				// caught.printStackTrace();
				// logger.info(caught.getMessage());
				// textToServerLabel.setText("Erro ao criar usuÃ¡rio");
				// dialogBox.center();
			}

			@Override
			protected void callService(AsyncCallback<ArrayList<Device>> cb) {
				if (Util.getUserLogged() != null) {
					GWT.log(Util.getUserLogged().getEmail());
					deviceService.listAll(Util.getUserLogged(), cb);
				}
			}
		}.executeWithoutSpinner(0);
	}

	private static HorizontalPanel createMenuDeviceItem(final Device dev) {
		HorizontalPanel hPanelDeviceItem = new HorizontalPanel();// TODO clear
																	// no HPANEL
																	// ao inves
																	// de criar
																	// outro
		hPanelDeviceItem.setWidth("100%");
		HTML htmlDevName = new HTML(dev.getName());
		htmlDevName.setWidth("100%");
		htmlDevName.setStyleName("device-item-menu");
		hPanelDeviceItem.add(htmlDevName);
		StatusConnectionWidget iconStatus = new StatusConnectionWidget();
		// iconStatus.setWidth("100%");
		hPanelDeviceItem.add(iconStatus);
		// hPanel.setCellWidth(htmlDevName, "50%");
		// hPanel.setCellWidth(iconStatus, "50%");
		hPanelDeviceItem.setCellHorizontalAlignment(iconStatus,
				HorizontalAlignmentConstant.startOf(Direction.RTL));
		iconStatus.setDeviceDisconnected();
		deviceStatusMenuIcon.put(dev.getChipId(), iconStatus);
		htmlDevName.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				openDevicePage(dev, DeviceTabs.PROFILE);
				// openDevicePage(dev, DeviceTabs.STATUS);
			}
		});

		return hPanelDeviceItem;
	}

	private Button makeRefreshButton() {

		Button btnRefresh = new Button(
				"<img class=img-Refresh-Icon src=res/refresh_icon.png>",
				new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						loadMyDevicesItemMenu();
					}
				});
		return btnRefresh;
	}

	private static Map<String, StatusConnectionWidget> deviceStatusMenuIcon = new HashMap<String, StatusConnectionWidget>();

	static MessageJavasCriptHandler messageFromServer = new MessageJavasCriptHandler() {

		@Override
		public void handleMessage(String message) {
			GWT.log("Menu->Recebeu msg: " + message.substring(0, 20) + "...");
			JSONObject json = JSONParser.parseLenient(message).isObject();
			if (json.containsKey("status")) {
				String cId = json.get("chipId").isString().stringValue();
				StatusConnectionWidget icon = deviceStatusMenuIcon.get(cId);
				if (icon != null) {
					if (json.get("status").isString() != null
							&& json.get("status").isString().stringValue()
									.equals(DEVICE_STATUS.DISCONNECTED.name())) {
						icon.setDeviceDisconnected();
					} else {
						// connected device
						icon.setDeviceConnected();
					}
				}
			}
		}
	};
}