package com.pazeto.iot.client.ui.views;

import gwt.material.design.client.constants.IconPosition;
import gwt.material.design.client.constants.IconType;

import java.util.ArrayList;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.NoSelectionModel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.pazeto.iot.client.services.CustomAsyncCall;
import com.pazeto.iot.client.services.IoPortService;
import com.pazeto.iot.client.services.IoPortServiceAsync;
import com.pazeto.iot.client.ui.BaseComposite;
import com.pazeto.iot.client.ui.DevicePage;
import com.pazeto.iot.client.ui.widgets.IotMaterialButton;
import com.pazeto.iot.shared.vo.IoPort;

public class ListIoPortView extends BaseComposite {

	static ListIoPortView uniqueInstance;
	private static IoPortServiceAsync portService = GWT
			.create(IoPortService.class);
	private static ListDataProvider<IoPort> ioPortDataProvider;

	public static ListIoPortView getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new ListIoPortView();
		}
		refresh();
		return uniqueInstance;
	}

	public static void refresh() {
		if (ioPortDataProvider != null) {
			ioPortDataProvider.getList().clear();
		}
		if (DevicePage.getCurrentDev() != null) {
			new CustomAsyncCall<ArrayList<IoPort>>() {

				@Override
				public void onSuccess(ArrayList<IoPort> result) {
					if (result != null) {
						ioPortDataProvider.setList(result);
					}
				}

				@Override
				protected void callService(AsyncCallback<ArrayList<IoPort>> cb) {
					GWT.log("AQUI ID" + DevicePage.getCurrentDev().getChipId());
					portService.listAllPortsByDevice(
							DevicePage.getCurrentDev(), cb);
				}
			}.executeWithoutSpinner();
		}
	}

	public static final ProvidesKey<IoPort> KEY_PROVIDER = new ProvidesKey<IoPort>() {
		@Override
		public Object getKey(IoPort item) {
			return item.getId();
		}
	};

	private static CellTable<IoPort> table;

	private IotMaterialButton addIoPortButton;

	public ListIoPortView() {

		table = new CellTable<IoPort>(KEY_PROVIDER);
		table.setWidth("100%");
		table.setHeight("40vh");
		table.addStyleName("type striped");
		final NoSelectionModel<IoPort> selectionModel = new NoSelectionModel<IoPort>();

		selectionModel
				.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

					@Override
					public void onSelectionChange(SelectionChangeEvent event) {
						IoPort port = selectionModel.getLastSelectedObject();
						IoPortPopupForm.getInstance(port).center();
					}
				});
		table.setSelectionModel(selectionModel);

		ioPortDataProvider = new ListDataProvider<>();
		initTableColumns();

		ioPortDataProvider.addDataDisplay(table);

		addIoPortButton = new IotMaterialButton("Nova porta",
				IconType.ADD_CIRCLE, IconPosition.RIGHT);
		addIoPortButton.addClickHandler(btnNewPortClickHandler);

		VerticalPanel vp = new VerticalPanel();
		vp.add(addIoPortButton);
		vp.add(table);

		initBaseWidget(vp);
	}

	ClickHandler btnNewPortClickHandler = new ClickHandler() {

		@Override
		public void onClick(ClickEvent event) {
			IoPortPopupForm.getInstance().center();

			// IoPort port = new IoPort();
			// // ioPortDataProvider.refresh();
			// ioPortDataProvider.getList().add(port);
			//
			// port.setDeviceId(DevicePage.getCurrentDev().getChipId());
			// port.setIONumber("");
			// GWT.log(port.getId());
			// GWT.log(port.getiONumber());
			//
			// for (IoPort iterable_element : ioPortDataProvider.getList()) {
			// GWT.log("ionumer=" + iterable_element.getiONumber());
			// }
		}
	};

	/**
	 * // * Method to update a value for IoPort list // * // * @param index // * @param
	 * port // * @param oldPort //
	 */
	// private void refreshPort(final int index, final IoPort port,
	// final IoPort oldPort) {
	// new CustomAsyncCall<String>() {
	//
	// @Override
	// public void onSuccess(String result) {
	// GWT.log("Sucesso, id: " + result);
	// port.setId(result);
	// ioPortDataProvider.getList().set(index, port);
	// ioPortDataProvider.refresh();
	// }
	//
	// @Override
	// public void onFailure(Throwable caught) {
	// GWT.log("Falha, id:" + oldPort.getId());
	// ioPortDataProvider.getList().set(index, oldPort);
	// ioPortDataProvider.refresh();
	// }
	//
	// @Override
	// protected void callService(AsyncCallback<String> cb) {
	// portService.savePort(port, cb);
	// }
	// }.executeWithoutSpinner(0);
	// }

	private void initTableColumns() {

		// IOnumber name.
		Column<IoPort, String> ioNumberColumn = new Column<IoPort, String>(
				new TextCell()) {
			@Override
			public String getValue(IoPort object) {
				return object.getiONumber();
			}
		};
		// ioNumberColumn.setFieldUpdater(new FieldUpdater<IoPort, String>() {
		// @Override
		// public void update(int index, IoPort object, String value) {
		// GWT.log("object.getiONumber()" + object.getiONumber());
		// IoPort newPort = new IoPort(object);
		// newPort.setIONumber(value);
		// refreshPort(index, newPort, object);
		// }
		// });
		table.addColumn(ioNumberColumn, "Número");
		table.setColumnWidth(ioNumberColumn, 15, Unit.PCT);

		// IOport name.
		Column<IoPort, String> nameColumn = new Column<IoPort, String>(
				new TextCell()) {
			@Override
			public String getValue(IoPort object) {
				return object.getDescription() != null ? object
						.getDescription() : "";
			}
		};
		// nameColumn.setFieldUpdater(new FieldUpdater<IoPort, String>() {
		// @Override
		// public void update(int index, IoPort object, String value) {
		// IoPort newPort = new IoPort(object);
		// newPort.setDescription(value);
		// refreshPort(index, newPort, object);
		// }
		// });
		table.addColumn(nameColumn, "Descrição");
		table.setColumnWidth(nameColumn, 40, Unit.PCT);

		// port type.
		// SelectionCell categoryCell = new
		// SelectionCell(PORT_TYPE.getTypeNames());
		Column<IoPort, String> categoryColumn = new Column<IoPort, String>(
				new TextCell()) {
			@Override
			public String getValue(IoPort object) {
				return object.getType();
			}
		};
		// categoryColumn.setFieldUpdater(new FieldUpdater<IoPort, String>() {
		// @Override
		// public void update(int index, IoPort object, String value) {
		// IoPort newPort = new IoPort(object);
		// for (PORT_TYPE category : PORT_TYPE.values()) {
		// if (category.name().equals(value)) {
		// // object.setType(category.name());
		// newPort.setType(value);
		// }
		// }
		// refreshPort(index, newPort, object);
		// }
		// });
		table.addColumn(categoryColumn, "Tipo");
		table.setColumnWidth(categoryColumn, 40, Unit.PCT);

	}

	@Override
	protected String getModalTitle() {
		return "Porta";
	}

}