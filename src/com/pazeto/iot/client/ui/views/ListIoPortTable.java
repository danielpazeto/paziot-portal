package com.pazeto.iot.client.ui.views;

import gwt.material.design.client.constants.IconPosition;
import gwt.material.design.client.constants.IconType;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.SelectionCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.pazeto.iot.client.services.CustomAsyncCall;
import com.pazeto.iot.client.services.IoPortService;
import com.pazeto.iot.client.services.IoPortServiceAsync;
import com.pazeto.iot.client.ui.BaseComposite;
import com.pazeto.iot.client.ui.DevicePage;
import com.pazeto.iot.client.ui.widgets.IotMaterialButton;
import com.pazeto.iot.shared.vo.IoPort;
import com.pazeto.iot.shared.vo.IoPort.PORT_TYPE;

public class ListIoPortTable extends BaseComposite {

	static ListIoPortTable uniqueInstance;
	private static IoPortServiceAsync portService = GWT
			.create(IoPortService.class);
	private static ListDataProvider<IoPort> ioPortDataProvider;

	public static ListIoPortTable getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new ListIoPortTable();
		}
		refresh();
		return uniqueInstance;
	}

	private static void refresh() {
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

	// public static final ProvidesKey<IoPort> KEY_PROVIDER = new
	// ProvidesKey<IoPort>() {
	// @Override
	// public Object getKey(IoPort item) {
	// return item == null ? null : item.getId();
	// }
	// };

	private static CellTable<IoPort> table;

	private IotMaterialButton addIoPortButton;

	public ListIoPortTable() {

		table = new CellTable<IoPort>();
		table.setWidth("100%");
		table.setHeight("40vh");
		table.addStyleName("type striped");

		// Add a selection model so we can select cells.
		// final SelectionModel<IoPort> selectionModel = new
		// MultiSelectionModel<IoPort>(
		// KEY_PROVIDER);
		// table.setSelectionModel(selectionModel,
		// DefaultSelectionEventManager.<IoPort> createCheckboxManager());

		// Attach a column sort handler to the ListDataProvider to sort the
		// list.
		ioPortDataProvider = new ListDataProvider<>();
		ListHandler<IoPort> sortHandler = new ListHandler<IoPort>(
				ioPortDataProvider.getList());
		table.addColumnSortHandler(sortHandler);
		// Initialize the columns.
		initTableColumns(sortHandler);

		// Add the CellList to the adapter in the database.
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
			IoPort port = new IoPort();
			// ioPortDataProvider.refresh();
			ioPortDataProvider.getList().add(port);

			port.setDeviceId(DevicePage.getCurrentDev().getChipId());
			port.setIONumber("");
			GWT.log(port.getId());
			GWT.log(port.getiONumber());

			for (IoPort iterable_element : ioPortDataProvider.getList()) {
				GWT.log("ionumer=" + iterable_element.getiONumber());
			}
		}
	};

	/**
	 * Method to update a value for IoPort list
	 * 
	 * @param index
	 * @param port
	 * @param oldPort
	 */
	private void refreshPort(final int index, final IoPort port,
			final IoPort oldPort) {
		new CustomAsyncCall<String>() {
			String oldValue;

			@Override
			public void onSuccess(String result) {
				GWT.log("Sucesso, mas está como " + result);
				port.setId(result);
				// ioPortDataProvider.getList().getadd(port);
				ioPortDataProvider.refresh();
			}

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("era pra ser -1, mas está como " + oldValue);
				// ioPortDataProvider.getList().add(oldPort);
				ioPortDataProvider.refresh();
			}

			@Override
			protected void callService(AsyncCallback<String> cb) {
				portService.savePort(port, cb);
			}
		}.executeWithoutSpinner(0);
	}

	private void initTableColumns(ListHandler<IoPort> sortHandler) {

		// IOnumber name.
		Column<IoPort, String> ioNumberColumn = new Column<IoPort, String>(
				new EditTextCell()) {
			@Override
			public String getValue(IoPort object) {
				return object.getiONumber();
			}
		};
		ioNumberColumn.setFieldUpdater(new FieldUpdater<IoPort, String>() {
			@Override
			public void update(int index, IoPort object, String value) {
				GWT.log("				object.getiONumber()" + object.getiONumber());
				IoPort newPort = new IoPort(object);
				newPort.setIONumber(value);
				refreshPort(index, newPort, object);
			}
		});
		table.addColumn(ioNumberColumn, "Número");
		table.setColumnWidth(ioNumberColumn, 15, Unit.PCT);

		// IOport name.
		Column<IoPort, String> nameColumn = new Column<IoPort, String>(
				new EditTextCell()) {
			@Override
			public String getValue(IoPort object) {
				return object.getDescription();
			}
		};
		table.addColumn(nameColumn, "Descrição");
		nameColumn.setFieldUpdater(new FieldUpdater<IoPort, String>() {
			@Override
			public void update(int index, IoPort object, String value) {
				IoPort newPort = new IoPort(object);
				newPort.setDescription(value);
				refreshPort(index, newPort, object);
			}
		});
		table.setColumnWidth(nameColumn, 40, Unit.PCT);

		// port type.
		final PORT_TYPE[] categories = PORT_TYPE.values();
		List<String> categoryNames = new ArrayList<String>();
		for (PORT_TYPE category : categories) {
			categoryNames.add(category.name());
		}
		SelectionCell categoryCell = new SelectionCell(categoryNames);
		Column<IoPort, String> categoryColumn = new Column<IoPort, String>(
				categoryCell) {
			@Override
			public String getValue(IoPort object) {
				return object.getType();
			}
		};
		table.addColumn(categoryColumn, "Tipo");
		categoryColumn.setFieldUpdater(new FieldUpdater<IoPort, String>() {
			@Override
			public void update(int index, IoPort object, String value) {
				IoPort newPort = new IoPort(object);
				for (PORT_TYPE category : categories) {
					if (category.name().equals(value)) {
						// object.setType(category.name());
						newPort.setType(value);
					}
				}
				refreshPort(index, newPort, object);
			}
		});
		table.setColumnWidth(categoryColumn, 40, Unit.PCT);

	}

	@Override
	protected String getModalTitle() {
		return "Porta";
	}

}