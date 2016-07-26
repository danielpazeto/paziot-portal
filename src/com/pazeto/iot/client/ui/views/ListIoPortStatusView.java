package com.pazeto.iot.client.ui.views;

import gwt.material.design.client.constants.IconPosition;
import gwt.material.design.client.constants.IconType;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.HasDirection.Direction;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.googlecode.mgwt.ui.client.widget.input.checkbox.MCheckBox;
import com.pazeto.iot.client.services.CustomAsyncCall;
import com.pazeto.iot.client.services.IoPortService;
import com.pazeto.iot.client.services.IoPortServiceAsync;
import com.pazeto.iot.client.ui.BaseComposite;
import com.pazeto.iot.client.ui.DevicePage;
import com.pazeto.iot.client.ui.widgets.IotMaterialButton;
import com.pazeto.iot.client.websocket.UserClientWebSocket;
import com.pazeto.iot.client.websocket.UserClientWebSocket.MessageJavasCriptHandler;
import com.pazeto.iot.shared.Constants.DEVICE_STATUS;
import com.pazeto.iot.shared.vo.IoPort;

public class ListIoPortStatusView extends BaseComposite {

	static ListIoPortStatusView uniqueInstance;
	private static IoPortServiceAsync portService = GWT
			.create(IoPortService.class);

	// static UserClientWebSocket websocket;

	public static ListIoPortStatusView getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new ListIoPortStatusView();
		}
		refresh();
		return uniqueInstance;
	}

	static HashMap<String, MCheckBox> switchersIoMap = new HashMap<String, MCheckBox>();

	static void refresh() {
		// if (!websocket.isConnected()) {
		// GWT.log("webscoket not connected.. Trying connect..");
		// connectWebsocket();
		// }
		vPinsStatusPanel.clear();
		switchersIoMap.clear();

		if (DevicePage.getCurrentDev() != null) {
			new CustomAsyncCall<ArrayList<IoPort>>() {

				@Override
				public void onSuccess(ArrayList<IoPort> result) {
					makeIoPortTable(result);
					requestDeviceStatus();
				}

				private void makeIoPortTable(ArrayList<IoPort> result) {
					if (result != null) {
						for (final IoPort ioPort : result) {
							HorizontalPanel portRow = new HorizontalPanel();
							Label ioNumberLabel = new Label(
									ioPort.getiONumber());
							portRow.add(ioNumberLabel);
							portRow.add(new Label(ioPort.getDescription()));
							MCheckBox switchBtn = new MCheckBox();
							switchBtn
									.addValueChangeHandler(createChangeValueHandler(ioPort));
							switchersIoMap.put(ioPort.getiONumber(), switchBtn);
							portRow.add(switchBtn);
							portRow.setWidth("100%");
							portRow.setCellWidth(switchBtn, "50%");
							portRow.setCellHorizontalAlignment(switchBtn,
									HorizontalAlignmentConstant
											.startOf(Direction.RTL));
							portRow.setCellWidth(ioNumberLabel, "50%");
							vPinsStatusPanel.add(portRow);
						}
					}
				}

				private ValueChangeHandler<Boolean> createChangeValueHandler(
						final IoPort ioPort) {
					return new ValueChangeHandler<Boolean>() {

						@Override
						public void onValueChange(
								ValueChangeEvent<Boolean> event) {
							String responseJson;
							responseJson = UserClientWebSocket.makeMessage(
									ioPort, event.getValue() ? "1" : "0");

							UserClientWebSocket.getInstance().sendMessage(
									responseJson.toString());
							requestDeviceStatus();

						}

					};
				}

				@Override
				protected void callService(AsyncCallback<ArrayList<IoPort>> cb) {
					portService.listAllPortsByDevice(
							DevicePage.getCurrentDev(), cb);
				}
			}.executeWithoutSpinner();
		}
	}

	static VerticalPanel vPinsStatusPanel;
	static VerticalPanel vRootPanel;

	public ListIoPortStatusView() {
		vRootPanel = new VerticalPanel();
		UserClientWebSocket.getInstance().addMessageHandler(messageFromServer);
		vPinsStatusPanel = new VerticalPanel();
		IotMaterialButton refreshStatusBtn = new IotMaterialButton(
				"Atualiza Status", IconType.SYNC, IconPosition.RIGHT);
		refreshStatusBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				requestDeviceStatus();
			}
		});

		vRootPanel.add(refreshStatusBtn);
		vRootPanel.add(vPinsStatusPanel);
		vPinsStatusPanel.setWidth("100%");
		vRootPanel.setWidth("100%");
		initBaseWidget(vRootPanel);
	}

	/**
	 * Request device pinout/connection status
	 */
	private static void requestDeviceStatus() {
		if (DevicePage.getCurrentDev() != null
				&& DevicePage.getCurrentDev().getChipId() != null) {
			JSONObject responseJson = new JSONObject();
			responseJson.put("chipId", new JSONString(DevicePage
					.getCurrentDev().getChipId()));
			responseJson.put("status", new JSONString(DevicePage
					.getCurrentDev().getChipId()));
			UserClientWebSocket.getInstance().sendMessage(
					responseJson.toString());
		}
	}

	private static Label lbDisconnected = new Label("Dispositivo desconectado!");
	static MessageJavasCriptHandler messageFromServer = new MessageJavasCriptHandler() {

		@Override
		public void handleMessage(String message) {
			GWT.log("Lista de Status-> recebeu msg: "
					+ message.substring(0, 20) + "...");
			JSONObject json = JSONParser.parseLenient(message).isObject();
			if (json.containsKey("status")) {
				if (json.get("status").isString() != null
						&& json.get("status").isString().stringValue()
								.equals(DEVICE_STATUS.DISCONNECTED.name())) {
					vPinsStatusPanel.clear();
					vPinsStatusPanel.add(lbDisconnected);
					return;
				}
				if (lbDisconnected.isAttached()) {
					refresh();
				}
				JSONArray arrayStatus = json.isObject().get("status").isArray();
				for (int i = 0; i < arrayStatus.size(); i++) {
					JSONObject row = arrayStatus.get(i).isObject();
					System.out.println(row);
					if (row != null) {
						String id = row.get("ioNum").toString();
						String value = row.get("value").toString();
						refreshStatusList(id, value);
					}
				}
			}

		}

		private void refreshStatusList(String id, String value) {
			if (switchersIoMap.containsKey(id)) {
				switchersIoMap.get(id).setValue(value == "1", false);
			}
		}
	};

	@Override
	protected String getModalTitle() {
		return "Status";
	}
}