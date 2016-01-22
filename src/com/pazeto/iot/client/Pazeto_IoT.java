package com.pazeto.iot.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;
import com.pazeto.iot.client.ui.LoginPage;
import com.pazeto.iot.client.ui.MainRootScreen;
import com.pazeto.iot.client.ui.UiViewHandler;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Pazeto_IoT implements EntryPoint {

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		MainRootScreen mainScreen = MainRootScreen.getInstance();

		RootPanel.get().add(mainScreen);
		RootPanel.get().setStyleName("main-div");

		if (LoginPage.isUserLogged()) {
			UiViewHandler.getInstance().openMainPage();
		} else {
			UiViewHandler.getInstance().openLoginPage();
		}

		//
		// User u1 = new User();
		// u1.setEmail("e@e");
		// u1.setName("d");
		// u1.setPwd("123");
		//
		// User u2 = new User();
		// u2.setEmail("2@2");
		// u2.setName("222");
		// u2.setPwd("adad");
		//
		//
		//
		// Device d1 = new Device();
		// d1.setChipId(123);
		// d1.setCreateDate(new Date());
		// d1.setUser(u1);
		//
		// Device d2 = new Device();
		// d2.setChipId(23);
		// d2.setCreateDate(new Date());
		// d2.setUser(u1);
		//
		// Device d3 = new Device();
		// d3.setChipId(12);
		// d3.setCreateDate(new Date());
		// d3.setUser(u2);
		//
		//
		//
		//
		// final Button sendButton = new Button("Send");
		// final TextBox nameField = new TextBox();
		// nameField.setText("GWT User");
		// final Label errorLabel = new Label();
		//
		// // We can add style names to widgets
		// sendButton.addStyleName("sendButton");
		//
		// // Add the nameField and sendButton to the RootPanel
		// // Use RootPanel.get() to get the entire body element
		// RootPanel.get("nameFieldContainer").add(nameField);
		// RootPanel.get("sendButtonContainer").add(sendButton);
		// RootPanel.get("errorLabelContainer").add(errorLabel);
		//
		// // Focus the cursor on the name field when the app loads
		// nameField.setFocus(true);
		// nameField.selectAll();
		//
		// // Create the popup dialog box
		// final DialogBox dialogBox = new DialogBox();
		// dialogBox.setText("Remote Procedure Call");
		// dialogBox.setAnimationEnabled(true);
		// final Button closeButton = new Button("Close");
		// // We can set the id of a widget by accessing its Element
		// closeButton.getElement().setId("closeButton");
		// final Label textToServerLabel = new Label();
		// final HTML serverResponseLabel = new HTML();
		// VerticalPanel dialogVPanel = new VerticalPanel();
		// dialogVPanel.addStyleName("dialogVPanel");
		// dialogVPanel.add(new HTML("<b>Sending name to the server:</b>"));
		// dialogVPanel.add(textToServerLabel);
		// dialogVPanel.add(new HTML("<br><b>Server replies:</b>"));
		// dialogVPanel.add(serverResponseLabel);
		// dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		// dialogVPanel.add(closeButton);
		// dialogBox.setWidget(dialogVPanel);
		//
		// // Add a handler to close the DialogBox
		// closeButton.addClickHandler(new ClickHandler() {
		// public void onClick(ClickEvent event) {
		// dialogBox.hide();
		// sendButton.setEnabled(true);
		// sendButton.setFocus(true);
		// }
		// });
	}
}
