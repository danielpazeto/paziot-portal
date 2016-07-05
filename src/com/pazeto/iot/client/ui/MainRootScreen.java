package com.pazeto.iot.client.ui;

import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.googlecode.mgwt.ui.client.widget.panel.Panel;
import com.pazeto.iot.client.ui.views.MenuView;

public class MainRootScreen extends BaseComposite {

	private Panel rootPanel;

	private LayoutPanel bodyView;
	private LayoutPanel contentView;

	private static MainRootScreen uniqueInstance;

	public static MainRootScreen getInstance() {

		if (uniqueInstance == null) {
			uniqueInstance = new MainRootScreen();
		}
		return uniqueInstance;
	}

	public MainRootScreen() {
		super();
		rootPanel = new Panel();
		rootPanel.addStyleName("root-panel-style");
		rootPanel.setHeight("100%");
		rootPanel.add(MenuView.getInstance());
		contentView = new LayoutPanel();
		
		contentView.setHeight("80%");
//		contentView.setWidth("100%");
		rootPanel.add(contentView);
		initBaseWidget(rootPanel);
		RootPanel.get().add(this);
		
	}


	public LayoutPanel getContentView() {
		return contentView;
	}

	public LayoutPanel getBodyView() {
		return bodyView;
	}

	/**
	 * To squeeze menu view
	 * 
	 * @param isFullScreen
	 */
	public void setContentViewFullScreen(boolean isFullScreen) {
		if (isFullScreen) {
			// rootPanel.remove(HeaderView.getInstance());
			// bodyView.setWidgetLeftWidth(MenuView.getInstance(), 0, Unit.PCT,
			// 0,
			// Unit.PCT);
			// bodyView.setWidgetRightWidth(contentView, 0, Unit.PCT, 100,
			// Unit.PCT);
		} else {
			// if (Window.getClientWidth() > 500) {
			// bodyView.setWidgetLeftWidth(MenuView.getInstance(), 0,
			// Unit.PCT, 20, Unit.PCT);
			// bodyView.setWidgetRightWidth(contentView, 0, Unit.PCT, 80,
			// Unit.PCT);
			// } else {
			// // rootPanel.remove(HeaderView.getInstance());
			// bodyView.setWidgetTopHeight(MenuView.getInstance(), 0, Unit.PX,
			// 250, Unit.PX);
			// bodyView.setWidgetTopHeight(contentView, 250, Unit.PX, 250,
			// Unit.PX);
			// }
		}
	}

	private String title;

	public void setMainModalTitle(String title) {
		this.title = title;
	}

	public void setMainModalText(String text) {
		super.setModalText(text);
	}

	@Override
	protected String getModalTitle() {
		return title;
	}
}
