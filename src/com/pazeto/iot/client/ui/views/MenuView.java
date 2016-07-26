package com.pazeto.iot.client.ui.views;

import gwt.material.design.addins.client.sideprofile.MaterialSideProfile;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.SideNavType;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialNavBar;
import gwt.material.design.client.ui.MaterialNavBrand;
import gwt.material.design.client.ui.MaterialSideNav;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.pazeto.iot.client.services.CustomAsyncCall;
import com.pazeto.iot.client.services.DeviceService;
import com.pazeto.iot.client.services.DeviceServiceAsync;
import com.pazeto.iot.client.services.LoginService;
import com.pazeto.iot.client.services.LoginServiceAsync;
import com.pazeto.iot.client.ui.BaseComposite;
import com.pazeto.iot.client.ui.DevicePage.DeviceTabs;
import com.pazeto.iot.client.ui.UiViewHandler;
import com.pazeto.iot.client.ui.widgets.DeviceMenuItemWidget;
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
		uniqueInstance.loadMyDevicesItemMenu();
		return uniqueInstance;
	}

	private MaterialSideProfile profileItemMenu = new MaterialSideProfile();
	MaterialIcon iconNewDevice = new MaterialIcon(IconType.ADD_CIRCLE);

	private MaterialNavBar navTopBar = new MaterialNavBar();
	private MaterialSideNav sideNav = new MaterialSideNav(SideNavType.PUSH);

	public MenuView() {
		if (Util.getUserLogged() != null) {
			UserClientWebSocket.getInstance().addMessageHandler(
					messageFromServer);
		}
		navTopBar.setActivates("navSideMenu");
		// navTopBar.setType(NavBarType.FIXED);
		navTopBar.addStyleName("top-bar-menu");
		sideNav.setId("navSideMenu");
		sideNav.setWidth(200);
		navTopBar.add(sideNav);
		sideNav.setCloseOnClick(true);

		buildProfileItemsMenu();
		buildDevicesItemsMenu();
		MaterialNavBrand brand = new MaterialNavBrand();
		brand.setText("Pazeto Iot");
		navTopBar.add(brand);
		initWidget(navTopBar);
	}

	private void buildDevicesItemsMenu() {
		iconNewDevice.addStyleName("material-icons-bordered");
		iconNewDevice.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				UiViewHandler.getInstance().openDevicePage(null,
						DeviceTabs.PROFILE);
				sideNav.hide();
			}
		});

		loadMyDevicesItemMenu();
	}

	private void buildProfileItemsMenu() {
		MaterialButton logoutBtn = new MaterialButton();
		logoutBtn.addClickHandler(new ClickHandler() {

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
		});
		logoutBtn.setText("Logout");

		DeviceMenuItemWidget iconStatus = new DeviceMenuItemWidget();
		logoutBtn.add(iconStatus);
		if (Util.getUserLogged() != null)
			iconStatus.setStatus(UserClientWebSocket.getInstance()
					.isConnected());
		iconStatus.setText("Olá " + Util.getUserLogged().getName());
		profileItemMenu.add(iconStatus);
		profileItemMenu.add(logoutBtn);
		profileItemMenu.setWidth("100%");
		profileItemMenu.setHeight("100%");
		sideNav.add(profileItemMenu);

	}

	private VerticalPanel userDeviceWidget = new VerticalPanel();

	public void loadMyDevicesItemMenu() {
		new CustomAsyncCall<ArrayList<Device>>() {

			@Override
			public void onSuccess(ArrayList<Device> result) {
				userDeviceWidget.clear();
				HorizontalPanel hPanelTop = new HorizontalPanel();
				hPanelTop.add(iconNewDevice);
				hPanelTop.add(makeRefreshButton());
				hPanelTop.setWidth("100%");
				userDeviceWidget.add(hPanelTop);
				deviceStatusMenuIcon.clear();
				if (result != null && result.size() > 0) {
					GWT.log(String.valueOf(result.size()));
					for (Device device : result) {
						userDeviceWidget.add(createMenuDeviceItem(device));
						UserClientWebSocket.getInstance().requestDeviceStatus(
								device.getChipId());
					}
				} else {
					userDeviceWidget.add(new MaterialLink(
							"Nenhum disp. adicionado"));
				}
				userDeviceWidget.setWidth("100%");
				userDeviceWidget.setHeight("100%");
				sideNav.add(userDeviceWidget);
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

	private static DeviceMenuItemWidget createMenuDeviceItem(final Device dev) {

		DeviceMenuItemWidget deviceMenuItem = new DeviceMenuItemWidget();
		deviceMenuItem.setText(dev.getName());
		deviceMenuItem.setDeviceDisconnected();
		deviceStatusMenuIcon.put(dev.getChipId(), deviceMenuItem);
		deviceMenuItem.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				openDevicePage(dev, DeviceTabs.PROFILE);
				// openDevicePage(dev, DeviceTabs.STATUS);
			}
		});

		return deviceMenuItem;
	}

	private MaterialIcon makeRefreshButton() {
		MaterialIcon btnRefresh = new MaterialIcon(IconType.AUTORENEW);
		btnRefresh.addStyleName("material-icons-bordered");
		btnRefresh.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				loadMyDevicesItemMenu();
			}
		});
		return btnRefresh;
	}

	private static Map<String, DeviceMenuItemWidget> deviceStatusMenuIcon = new HashMap<String, DeviceMenuItemWidget>();

	static MessageJavasCriptHandler messageFromServer = new MessageJavasCriptHandler() {

		@Override
		public void handleMessage(String message) {
			GWT.log("Menu->Recebeu msg: " + message.substring(0, 20) + "...");
			JSONObject json = JSONParser.parseLenient(message).isObject();
			if (json.containsKey("status")) {
				String cId = json.get("chipId").isString().stringValue();
				DeviceMenuItemWidget icon = deviceStatusMenuIcon.get(cId);
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

	@Override
	protected String getModalTitle() {
		return "Menu";
	}

}