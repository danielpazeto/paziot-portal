package com.pazeto.iot.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.pazeto.iot.client.ui.views.DeviceEditorView;
import com.pazeto.iot.shared.vo.Device;

public class DevicePage extends BaseComposite {

	public enum DeviceTabs {
		PROFILE, STATUS;
	}

	private static DevicePage uniqueInstance;
	private static Device currentDev;

	public static DevicePage getInstance() {
		GWT.log("entrei "+uniqueInstance);
		if (uniqueInstance == null) {
			uniqueInstance = new DevicePage();
		}
		return uniqueInstance;
	}

	public static DevicePage getInstance(Device dev) {
		currentDev = dev;
		return getInstance();
	}

	private TabLayoutPanel deviceTabPanel;

	public DevicePage() {
		super();
		deviceTabPanel = new TabLayoutPanel(2.5, Unit.EM);
		deviceTabPanel.add(DeviceEditorView.getInstance(currentDev),
				"Profile Device");
		deviceTabPanel.add(new HTML("TODO "), "Device Status");
		deviceTabPanel.add(new HTML("TODO "), "Device Status");
		deviceTabPanel.getElement().setId("TESTE");
		deviceTabPanel.setVisible(true);
		deviceTabPanel.setWidth("100%");
		deviceTabPanel.setHeight("100%");
		deviceTabPanel.setStyleName("footer");
		initWidget(deviceTabPanel);
	}

	public void openStatus() {
		DeviceEditorView.getInstance(currentDev);
		GWT.log("status " + DeviceTabs.STATUS.ordinal());
		deviceTabPanel.selectTab(DeviceTabs.STATUS.ordinal());
	}

	public void openDeficeProfile() {
		DeviceEditorView.getInstance(currentDev);
		GWT.log("status" + DeviceTabs.PROFILE.ordinal());
		deviceTabPanel.selectTab(DeviceTabs.PROFILE.ordinal());
	}

}
