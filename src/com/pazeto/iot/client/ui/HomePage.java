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
//		MenuView.getInstance().setVisible(true);
//		final Button sellButton = new Button("Vendas");
//		final Button stockButton = new Button("Estoque");
//		final Button clientButton = new Button("Clientes");
//		final Button productsButton = new Button("Produtos");
		
		final FlexTable flexTable = new FlexTable();
		
//		flexTable.setWidget(0, 0, sellButton);
//	    flexTable.setWidget(0, 1, stockButton);
//	    flexTable.setWidget(1, 0, clientButton);
//	    flexTable.setWidget(1, 1, productsButton);

		

//		productsButton.addClickHandler(new ProductButtonHandler());
//		clientButton.addClickHandler(new CompanyButtonHandler());
		
		flexTable.setStyleName("mainMenu");
		initWidget(flexTable);
	}

	
	//handle to product btn
//	class ProductButtonHandler implements ClickHandler {
//		public void onClick(ClickEvent event) {
//			uiHandler.openProductsPage();
//		}
//	}
//	
//	class CompanyButtonHandler implements ClickHandler {
//		public void onClick(ClickEvent event) {
//			uiHandler.openCompanysPage();
//		}
//	}
}
