package com.pazeto.iot.client.ui;

import com.google.gwt.user.client.ui.FlexTable;
import com.pazeto.iot.client.ui.views.MenuView;

public class HomePage extends BaseComposite {
	
	private static HomePage mainPageInstance;
	public static HomePage getInstance(){
		if(mainPageInstance==null){
			mainPageInstance = new HomePage();
		}
		return mainPageInstance;
	}
	
	public HomePage() {
		super();
		
		final FlexTable flexTable = new FlexTable();
		
		initBaseWidget(flexTable);
	}

}
