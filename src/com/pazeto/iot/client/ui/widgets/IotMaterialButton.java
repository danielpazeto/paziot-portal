package com.pazeto.iot.client.ui.widgets;

import gwt.material.design.client.constants.IconPosition;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.ui.MaterialButton;

public class IotMaterialButton extends MaterialButton {
	
	public IotMaterialButton(String text,IconType iconType, IconPosition iconPos) {
		setText(text);
		setIconType(iconType);
		setIconPosition(iconPos);
	}

}
