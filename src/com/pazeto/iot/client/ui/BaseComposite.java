package com.pazeto.iot.client.ui;

import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialModal;
import gwt.material.design.client.ui.MaterialModalContent;
import gwt.material.design.client.ui.MaterialModalFooter;
import gwt.material.design.client.ui.MaterialTitle;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.pazeto.iot.client.ui.DevicePage.DeviceTabs;
import com.pazeto.iot.shared.vo.Device;

public class BaseComposite extends Composite {


	private MaterialModal modal;
	private MaterialModalContent modalContent;
	private MaterialTitle modalText;
	private MaterialButton dialogCloseButton;
	private MaterialModalFooter modalFooter;

	protected void openHomePage() {
		UiViewHandler.getInstance().openHomePage();
	}
	
	public static void openDevicePage(Device dev, DeviceTabs tab) {
		UiViewHandler.getInstance().openDevicePage(dev, tab);
		
	}

	public BaseComposite() {
		if (modal == null) {
		    modal = new MaterialModal();
			modalContent = new MaterialModalContent();
			modalText = new MaterialTitle();
			modalContent.add(modalText);
			modalFooter = new MaterialModalFooter();
			dialogCloseButton = new MaterialButton();
            dialogCloseButton.setText("Fechar");
			modalFooter.add(dialogCloseButton);
			modalContent.add(modalFooter);
			
			modal.add(modalContent);
			
			
			RootPanel.get().add(modal);
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
	    modalText.setTitle(title);
	}

	public MaterialModal setDefaultDialogText(String text) {
	    modalText.setDescription(text);
		return modal;
	}

	public MaterialModal getDefaultDialogBox() {
		return modal;
	}
	
	protected void initBaseWidget(Panel panelForm) {
      initWidget(panelForm);
    }
	
}