package com.pazeto.iot.client.ui.views;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.StackLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.pazeto.iot.client.CustomIotPazetoAsyncCall;
import com.pazeto.iot.client.services.DeviceService;
import com.pazeto.iot.client.services.DeviceServiceAsync;
import com.pazeto.iot.client.services.LoginService;
import com.pazeto.iot.client.services.LoginServiceAsync;
import com.pazeto.iot.client.ui.BaseComposite;
import com.pazeto.iot.client.ui.DevicePage.DeviceTabs;
import com.pazeto.iot.client.ui.UiViewHandler;
import com.pazeto.iot.shared.Util;
import com.pazeto.iot.shared.vo.Device;

public class MenuView extends BaseComposite {

	private final DeviceServiceAsync deviceService = GWT
			.create(DeviceService.class);
	private final LoginServiceAsync loginService = GWT
			.create(LoginService.class);

	private static MenuView uniqueInstance;

	public static MenuView getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new MenuView();
		}
		return uniqueInstance;
	}

	private VerticalPanel profileItemMenu = new VerticalPanel();
	private VerticalPanel devicesItemMenu = new VerticalPanel();
	private VerticalPanel reportsItemMenu = new VerticalPanel();
	private Button btnAddNewDevice = new Button("Novo Disp.");
	private VerticalPanel myDevicesList = new VerticalPanel();

	public MenuView() {

		StackLayoutPanel p = new StackLayoutPanel(Unit.EM);
		p.add(devicesItemMenu, new HTML("Dispositivos"), 3);
		p.add(reportsItemMenu, new HTML("Relat�rios"), 3);
		p.add(profileItemMenu, new HTML("Perfil"), 3);

		buildProfileItemsMenu();
		buildDevicesItemsMenu();

		initWidget(p);
	}

	private void buildDevicesItemsMenu() {
		// TODO if(admin)
		btnAddNewDevice.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				UiViewHandler.getInstance().openDevicePage(null,
						DeviceTabs.PROFILE);
			}
		});
		devicesItemMenu.add(btnAddNewDevice);

		devicesItemMenu.add(makeRefreshButton());
		devicesItemMenu.setWidth("100%");
		devicesItemMenu.setHeight("100%");
		loadMyDevicesItemMenu();
	}

	private void buildProfileItemsMenu() {
		profileItemMenu.add(new Button("Logout", new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				new CustomIotPazetoAsyncCall<Void>() {

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
		profileItemMenu.setWidth("100%");
		profileItemMenu.setHeight("100%");

	}

	public void loadMyDevicesItemMenu() {
		new CustomIotPazetoAsyncCall<ArrayList<Device>>() {

			@Override
			public void onSuccess(ArrayList<Device> result) {
				myDevicesList.clear();
				if (result != null && result.size() > 0) {
					GWT.log(String.valueOf(result.size()));
					for (Device device : result) {
						myDevicesList.add(createMenuDeviceItem(device));
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
				// textToServerLabel.setText("Erro ao criar usuário");
				// dialogBox.center();
			}

			@Override
			protected void callService(AsyncCallback<ArrayList<Device>> cb) {
				if (Util.getUserLogged() != null) {
					GWT.log(Util.getUserLogged().getEmail());
				}
				deviceService.listAll(Util.getUserLogged(), cb);
			}
		}.executeWithoutSpinner(0);
	}

	private HorizontalPanel createMenuDeviceItem(final Device dev) {
		HorizontalPanel hPanel = new HorizontalPanel();
		HTML htmlDevName = new HTML(dev.getName());
		htmlDevName.setStyleName("device-item-menu");
		hPanel.add(htmlDevName);
		htmlDevName.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				openDevicePage(dev, DeviceTabs.PROFILE);
				// openDevicePage(dev, DeviceTabs.STATUS);
			}
		});
		hPanel.setWidth("100%");
		return hPanel;
	}

	private Button makeRefreshButton() {

		Button btnRefresh = new Button(
				"Atualizar <img class=img-Refresh-Icon src=res/refresh_icon.png>",
				new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						loadMyDevicesItemMenu();
					}
				});
		return btnRefresh;
	}
}