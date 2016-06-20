package com.pazeto.iot.client.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.pazeto.iot.client.ui.DevicePage.DeviceTabs;
import com.pazeto.iot.shared.vo.Device;

public class BaseComposite extends Composite {


	private DialogBox dialogBox;
	private Label textLabelDialogBox;
	private Button dialogCloseButton;


	protected void openHomePage() {
		UiViewHandler.getInstance().openHomePage();
	}
	
	public static void openDevicePage(Device dev, DeviceTabs tab) {
		UiViewHandler.getInstance().openDevicePage(dev, tab);
		
	}

	public BaseComposite() {
		if (dialogBox == null) {
			dialogBox = new DialogBox();
			dialogBox.setAnimationEnabled(true);
			dialogCloseButton = new Button("Fechar");
			dialogCloseButton.getElement().setId("closeButton");
			textLabelDialogBox = new Label();
			VerticalPanel dialogVPanel = new VerticalPanel();
			dialogVPanel.addStyleName("dialogVPanel");
			dialogVPanel.add(textLabelDialogBox);
			dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
			dialogVPanel.add(dialogCloseButton);
			dialogBox.setWidget(dialogVPanel);
		}
		dialogCloseButton.addClickHandler(getCloseButtonHandlerClick());
	}

	public ClickHandler getCloseButtonHandlerClick() {
		return new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				dialogBox.hide();
			}
		};

	}

	public void setDefaultDialogBoxTitle(String title) {
		dialogBox.setText(title);
	}

	public DialogBox setDefaultDialogText(String text) {
		textLabelDialogBox.setText(text);
		return dialogBox;
	}

	public DialogBox getDefaultDialogBox() {
		return dialogBox;
	}
	
}