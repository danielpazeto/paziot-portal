package com.pazeto.iot.client.ui.views;

import gwt.material.design.client.ui.MaterialIntegerBox;
import gwt.material.design.client.ui.MaterialListBox;
import gwt.material.design.client.ui.MaterialTextBox;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.pazeto.iot.client.services.CustomAsyncCall;
import com.pazeto.iot.client.services.IoPortService;
import com.pazeto.iot.client.services.IoPortServiceAsync;
import com.pazeto.iot.client.ui.DevicePage;
import com.pazeto.iot.client.ui.MainRootScreen;
import com.pazeto.iot.shared.vo.IoPort;
import com.pazeto.iot.shared.vo.IoPort.PORT_TYPE;

public class IoPortPopupForm extends BasePopupForm {

	private static IoPortPopupForm uniqueInstance;

	public static IoPortPopupForm getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new IoPortPopupForm();
		}
		MainRootScreen.getInstance().setMainModalTitle("Porta");
		return uniqueInstance;
	}

	private final IoPortServiceAsync portService = GWT
			.create(IoPortService.class);

	private static MaterialIntegerBox ioNumberField = new MaterialIntegerBox();
	private static MaterialTextBox descriptionField = new MaterialTextBox();
	private static MaterialListBox portType = new MaterialListBox();

	private static IoPort currentPort;

	public IoPortPopupForm() {

		ioNumberField.setPlaceholder("Número da porta");
		descriptionField.setPlaceholder("Descrição");
		portType.setPlaceholder("Tipo");
		for (String type : PORT_TYPE.getTypeNames()) {
			portType.addItem(type);
		}

		getBasePanel().add(new Label("Cadastro de Porta"));
		getBasePanel().add(ioNumberField);
		getBasePanel().add(descriptionField);
		getBasePanel().add(portType);

		this.setModal(true);

		addDefaultButtons();
	}

	@Override
	ClickHandler getSaveClickHandler() {
		return new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				final IoPort port = new IoPort();
				port.setDeviceId(DevicePage.getCurrentDev().getChipId());
				port.setIONumber(ioNumberField.getText());
				port.setDescription(descriptionField.getText());
				port.setType(portType.getSelectedItemText());
				getSaveBtn().setEnabled(false);

				new CustomAsyncCall<String>() {

					@Override
					public void onSuccess(String result) {
						IoPortPopupForm.getInstance().hide();
						MainRootScreen
								.getInstance()
								.setModalText(
										"Porta <b>"
												+ ioNumberField.getText()
												+ ":"
												+ descriptionField.getText()
												+ "</b> salva com sucesso no dispositivo <b>"
												+ DevicePage.getCurrentDev()
														.getName() + "</b>")
								.openModal();
						ListIoPortView.getInstance().refresh();
					}

					@Override
					public void onFailure(Throwable caught) {
						GWT.log("Error message: ", caught);
						MainRootScreen.getInstance()
								.setModalText("Erro ao criar porta")
								.openModal();
					}

					@Override
					protected void callService(AsyncCallback<String> cb) {
						portService.savePort(port, cb);
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
				IoPortPopupForm.this.hide();
			}
		};
	}

	public static IoPortPopupForm getInstance(IoPort port) {
		getInstance();
		currentPort = port;
		ioNumberField.setText(port.getiONumber());
		descriptionField.setText(port.getDescription());
		portType.setSelectedValue(port.getType());
		return uniqueInstance;
	}
}
