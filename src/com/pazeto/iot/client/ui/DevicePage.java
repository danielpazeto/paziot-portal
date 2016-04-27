package com.pazeto.iot.client.ui;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.pazeto.iot.client.ui.views.DeviceEditorView;
import com.pazeto.iot.client.ui.views.ListIoPortStatusTable;
import com.pazeto.iot.client.ui.views.ListIoPortTable;
import com.pazeto.iot.shared.vo.Device;

public class DevicePage extends BaseComposite {

	public enum DeviceTabs {
		PROFILE, STATUS;
	}

	private static DevicePage uniqueInstance;
	private static Device currentDev;

	public static void setCurrentDev(Device currentDev) {
		DevicePage.currentDev = currentDev;
	}

	public static Device getCurrentDev() {
		return currentDev;
	}

	public static DevicePage getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new DevicePage();
		}
		addTabs();
		return uniqueInstance;
	}

	public static DevicePage getInstance(Device dev) {
		currentDev = dev;
		return getInstance();
	}

	private static TabLayoutPanel deviceTabPanel;

	public DevicePage() {
		super();
		deviceTabPanel = new TabLayoutPanel(2.5, Unit.EM);
		deviceTabPanel.setWidth("100%");
		deviceTabPanel.setHeight("100%");
		deviceTabPanel.setStyleName("footer");
		initWidget(deviceTabPanel);
	}

	private static void addTabs() {
		deviceTabPanel.clear();
		deviceTabPanel.add(DeviceEditorView.getInstance(), "Profile Device");
		deviceTabPanel.add(ListIoPortTable.getInstance(), "IO Ports");
		deviceTabPanel.add(ListIoPortStatusTable.getInstance(), "Status");
	}

	public void openStatus() {
		DeviceEditorView.getInstance();
		deviceTabPanel.selectTab(DeviceTabs.STATUS.ordinal());
	}

	public void openDeficeProfile() {
		DeviceEditorView.getInstance();
		deviceTabPanel.selectTab(DeviceTabs.PROFILE.ordinal());
	}

}
