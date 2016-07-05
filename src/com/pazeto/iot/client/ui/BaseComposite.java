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

public abstract class BaseComposite extends Composite {

	private static MaterialModal modal;
	private static MaterialModalContent modalContent;
	private static MaterialTitle modalText;
	private static MaterialButton dialogCloseButton;
	private static MaterialModalFooter modalFooter;

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
	}

	public ClickHandler getCloseButtonHandlerClick() {
		return new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				modal.closeModal();
			}
		};

	}

	public MaterialModal setModalText(String text) {
	    modalText.setDescription(text);
	    modalText.setTitle(getModalTitle());
	    dialogCloseButton.addClickHandler(getCloseButtonHandlerClick());
		return modal;
	}
	

	public MaterialModal getModal() {
		return modal;
	}
	
	protected void initBaseWidget(Panel panelForm) {
		initWidget(panelForm);
    }
	
	protected void initBaseWidget(com.googlecode.mgwt.ui.client.widget.panel.Panel panelForm) {
	    initWidget(panelForm);
	}
	
	protected abstract String getModalTitle();
	
}