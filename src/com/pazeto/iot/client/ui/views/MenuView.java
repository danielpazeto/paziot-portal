package com.pazeto.iot.client.ui.views;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.StackLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.pazeto.iot.client.CustomAsyncCall;
import com.pazeto.iot.client.DeviceService;
import com.pazeto.iot.client.DeviceServiceAsync;
import com.pazeto.iot.shared.Util;
import com.pazeto.iot.shared.vo.Device;

public class MenuView extends Composite {

	private final DeviceServiceAsync deviceService = GWT
			.create(DeviceService.class);


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
		p.add(profileItemMenu, new HTML("Perfil"), 3);
		p.add(devicesItemMenu, new HTML("Dispositivos"), 3);
		p.add(reportsItemMenu, new HTML("Relatórios"), 3);

		devicesItemMenu.add(btnAddNewDevice);

		loadMyDevicesItemMenu();

		btnAddNewDevice.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				DeviceEditorView.getInstance().center();
				;

			}
		});
		initWidget(p);
	}

	public void loadMyDevicesItemMenu() {
		myDevicesList.clear();

		new CustomAsyncCall<ArrayList<Device>>() {

			@Override
			public void onSuccess(ArrayList<Device> result) {
				if (result != null && result.size() > 0) {
					// TODO carregar todos os devices e colocar aqui com o nome
					// que o
					for (Device device : result) {
						myDevicesList.add(new HTML(device.getChipId()));
					}
					devicesItemMenu.add(myDevicesList);
				} else {
					devicesItemMenu.add(new HTML("Nenhum disp. adicionado"));
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
				deviceService.listAll(Util.getUserLogged(), cb);
			}
		}.go(0);

	}
}