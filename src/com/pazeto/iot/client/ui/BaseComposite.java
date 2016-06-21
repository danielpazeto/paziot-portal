package com.pazeto.iot.client.ui;

import gwt.material.design.client.constants.ModalType;
import gwt.material.design.client.ui.MaterialModal;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.pazeto.iot.client.ui.DevicePage.DeviceTabs;
import com.pazeto.iot.shared.vo.Device;

public class BaseComposite extends Composite {


	private MaterialModal modal;
	private Label textLabelDialogBox;
	private Button dialogCloseButton;


	protected void openHomePage() {
		UiViewHandler.getInstance().openHomePage();
	}
	
	public static void openDevicePage(Device dev, DeviceTabs tab) {
		UiViewHandler.getInstance().openDevicePage(dev, tab);
		
	}

	public BaseComposite() {
		if (modal == null) {
		    modal = new MaterialModal();
		    RootPanel.get().add(modal);
			dialogCloseButton = new Button("Fechar");
			dialogCloseButton.getElement().setId("closeButton");
			textLabelDialogBox = new Label();
			VerticalPanel dialogVPanel = new VerticalPanel();
			dialogVPanel.addStyleName("dialogVPanel");
			dialogVPanel.add(textLabelDialogBox);
			dialogVPanel.add(dialogCloseButton);
			modal.add(dialogVPanel);
		}
		dialogCloseButton.addClickHandler(getCloseButtonHandlerClick());
	}

	public ClickHandler getCloseButtonHandlerClick() {
		return new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				modal.closeModal();
			}
		};

	}

	public void setDefaultDialogBoxTitle(String title) {
		modal.setTitle(title);
	}

	public MaterialModal setDefaultDialogText(String text) {
		textLabelDialogBox.setText(text);
		return modal;
	}

	public MaterialModal getDefaultDialogBox() {
		return modal;
	}
	
	protected void initBaseWidget(Panel panelForm) {
      initWidget(panelForm);
    }
	
}