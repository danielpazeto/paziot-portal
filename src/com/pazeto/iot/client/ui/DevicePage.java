package com.pazeto.iot.client.ui;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.pazeto.iot.client.ui.views.DeviceEditorView;
import com.pazeto.iot.client.ui.views.ListIoPortStatusView;
import com.pazeto.iot.client.ui.views.ListIoPortView;
import com.pazeto.iot.client.ui.views.ListSchedulesView;
import com.pazeto.iot.shared.vo.Device;

public class DevicePage extends BaseComposite {

	public enum DeviceTabs {
		PROFILE("Dispositivo"), PORTS("Portas"), STATUS("Estado"), SCHEDULE(
				"Agendamento");

		String tabName;

		private DeviceTabs(String name) {
			this.tabName = name;
		}

		public String getTabName() {
			return tabName;
		}

	}

	private static DevicePage uniqueInstance;
	private static Device currentDev;

	public static void setCurrentDev(Device currentDev) {
		DevicePage.currentDev = currentDev;
	}

	public static Device getCurrentDev() {
		return currentDev;
	}

	private static DevicePage getInstance() {
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
		initWidget(deviceTabPanel);
	}

	private static void addTabs() {
		deviceTabPanel.clear();

		addTab(DeviceTabs.PROFILE, DeviceEditorView.getInstance());
		addTab(DeviceTabs.PORTS, ListIoPortView.getInstance());
		addTab(DeviceTabs.STATUS, ListIoPortStatusView.getInstance());
		addTab(DeviceTabs.SCHEDULE, ListSchedulesView.getInstance());
	}

	private static void addTab(DeviceTabs tab, BaseComposite page) {
		deviceTabPanel.add(page, tab.getTabName());
		deviceTabPanel.getTabWidget(tab.ordinal()).getParent().getParent()
				.setWidth("100%");
		deviceTabPanel.getTabWidget(tab.ordinal()).getParent()
				.setWidth(100 / DeviceTabs.values().length + "%");
	}

	public void openStatus() {
		DeviceEditorView.getInstance();
		deviceTabPanel.selectTab(DeviceTabs.STATUS.ordinal());
	}

	public void openDeficeProfile() {
		DeviceEditorView.getInstance();
		deviceTabPanel.selectTab(DeviceTabs.PROFILE.ordinal());
	}

	@Override
	protected String getModalTitle() {
		return "Dispositivo";
	}

}
