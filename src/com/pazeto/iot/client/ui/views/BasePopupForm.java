package com.pazeto.iot.client.ui.views;

import gwt.material.design.client.ui.MaterialButton;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.PopupPanel;
import com.googlecode.mgwt.ui.client.widget.panel.Panel;

public abstract class BasePopupForm extends PopupPanel {

	Panel vPanel = new Panel();
	private MaterialButton saveBtn;
	private MaterialButton closeBtn;

	public BasePopupForm() {
		// TODO Auto-generated constructor stub
	}

	Panel getBasePanel() {
		return vPanel;
	}

	void addDefaultButtons() {
		saveBtn = new MaterialButton();
		saveBtn.addClickHandler(getSaveClickHandler());
		saveBtn.setText("Salvar");

		closeBtn = new MaterialButton();
		closeBtn.addClickHandler(getCloseClickHandler());
		closeBtn.setText("Cancelar");

		vPanel.add(saveBtn);
		vPanel.add(closeBtn);
		this.add(vPanel);
	}

	abstract ClickHandler getSaveClickHandler();

	abstract ClickHandler getCloseClickHandler();
	
	protected MaterialButton getSaveBtn() {
		return saveBtn;
	}
}
