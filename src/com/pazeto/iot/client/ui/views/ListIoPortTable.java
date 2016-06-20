package com.pazeto.iot.client.ui.views;

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
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionModel;
import com.pazeto.iot.client.services.CustomAsyncCall;
import com.pazeto.iot.client.services.IoPortService;
import com.pazeto.iot.client.services.IoPortServiceAsync;
import com.pazeto.iot.client.ui.BaseComposite;
import com.pazeto.iot.client.ui.DevicePage;
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
					portService.listAllPortsByDevice(DevicePage.getCurrentDev(), cb);
				}
			}.executeWithoutSpinner();
		}
	}

	public static final ProvidesKey<IoPort> KEY_PROVIDER = new ProvidesKey<IoPort>() {
		@Override
		public Object getKey(IoPort item) {
			return item == null ? null : item.getId();
		}
	};

	CellTable<IoPort> table;

	private Button addIoPortButton;

	public ListIoPortTable() {

		table = new CellTable<IoPort>(KEY_PROVIDER);
		table.setWidth("100%", true);

		// Add a selection model so we can select cells.
		final SelectionModel<IoPort> selectionModel = new MultiSelectionModel<IoPort>(
				KEY_PROVIDER);
		table.setSelectionModel(selectionModel,
				DefaultSelectionEventManager.<IoPort> createCheckboxManager());

		// Attach a column sort handler to the ListDataProvider to sort the
		// list.
		ioPortDataProvider = new ListDataProvider<>();
		ListHandler<IoPort> sortHandler = new ListHandler<IoPort>(
				ioPortDataProvider.getList());
		table.addColumnSortHandler(sortHandler);
		// Initialize the columns.
		initTableColumns(selectionModel, sortHandler);

		// Add the CellList to the adapter in the database.
		ioPortDataProvider.addDataDisplay(table);

		addIoPortButton = new Button("Nova porta");
		addIoPortButton.addClickHandler(addNewBtnHandler);

		VerticalPanel vp = new VerticalPanel();
		vp.add(addIoPortButton);
		vp.add(table);

		initWidget(vp);
	}

	ClickHandler addNewBtnHandler = new ClickHandler() {

		@Override
		public void onClick(ClickEvent event) {
			IoPort port = new IoPort();
			port.setDeviceId(DevicePage.getCurrentDev().getChipId());
			port.setIONumber("");
			GWT.log(port.getId());
			GWT.log(port.getiONumber());

			ioPortDataProvider.refresh();
			ioPortDataProvider.getList().add(port);
			for (IoPort iterable_element : ioPortDataProvider.getList()) {
				GWT.log("ionumer=" + iterable_element.getiONumber());
			}
		}
	};

	private void refreshIoNumberPort(final IoPort port,
			final String newIoNumberValue) {
		new CustomAsyncCall<String>() {
			String oldValue;

			@Override
			public void onSuccess(String result) {
				GWT.log("Sucesso, mas est� como " + result);
			}

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("era pra ser -1, mas est� como " + oldValue);
				((IoPort) ioPortDataProvider.getKey(port))
						.setIONumber(oldValue);
				ioPortDataProvider.refresh();
			}

			@Override
			protected void callService(AsyncCallback<String> cb) {
				if (isValidIOnumber(newIoNumberValue)) {
					oldValue = new String(port.getiONumber());
					port.setIONumber(newIoNumberValue);
					portService.savePort(port, cb);
				}
			}

			private boolean isValidIOnumber(final String newIoNumberValue) {
				Integer i;
				try {
					i = Integer.valueOf(newIoNumberValue);
					return i > -1;
				} catch (Exception e) {
					return false;
				}
			}
		}.executeWithoutSpinner(0);
	}

	private void refreshIoDescriptionPort(final IoPort port,
			final String newPortName) {
		new CustomAsyncCall<String>() {
			String oldValue;

			@Override
			public void onSuccess(String result) {
				GWT.log("Sucesso, mas est� como " + result);
			}

			@Override
			public void onFailure(Throwable caught) {
				((IoPort) ioPortDataProvider.getKey(port))
						.setDescription(oldValue);
				ioPortDataProvider.refresh();
			}

			@Override
			protected void callService(AsyncCallback<String> cb) {
				oldValue = port.getDescription();
				port.setDescription(newPortName);
				portService.savePort(port, cb);
			}
		}.executeWithoutSpinner(0);
	}

	private void initTableColumns(final SelectionModel<IoPort> selectionModel,
			ListHandler<IoPort> sortHandler) {

		// IOnumber name.
		Column<IoPort, String> ioNumberColumn = new Column<IoPort, String>(
				new EditTextCell()) {
			@Override
			public String getValue(IoPort object) {
				return object.getiONumber();
			}
		};
		// ioNumberColumn.setSortable(true);
		// sortHandler.setComparator(ioNumberColumn, new Comparator<IoPort>() {
		// @Override
		// public int compare(IoPort o1, IoPort o2) {
		// return o1.getiONumber().compareTo(o2.getiONumber());
		// }
		// });
		ioNumberColumn.setFieldUpdater(new FieldUpdater<IoPort, String>() {
			@Override
			public void update(int index, IoPort object, String value) {
				refreshIoNumberPort(object, value);
			}
		});
		table.addColumn(ioNumberColumn, "N�mero");
		table.setColumnWidth(ioNumberColumn, 15, Unit.PCT);

		// IOport name.
		Column<IoPort, String> nameColumn = new Column<IoPort, String>(
				new EditTextCell()) {
			@Override
			public String getValue(IoPort object) {
				return object.getDescription();
			}
		};
		// nameColumn.setSortable(true);
		// sortHandler.setComparator(nameColumn, new Comparator<IoPort>() {
		// @Override
		// public int compare(IoPort o1, IoPort o2) {
		// return o1.getDescription().compareTo(o2.getDescription());
		// }
		// });
		table.addColumn(nameColumn, "Descri��o");
		nameColumn.setFieldUpdater(new FieldUpdater<IoPort, String>() {
			@Override
			public void update(int index, IoPort object, String value) {
				refreshIoDescriptionPort(object, value);
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
				for (PORT_TYPE category : categories) {
					if (category.name().equals(value)) {
						object.setType(category.name());
					}
				}
				// ContactDatabase.get().refreshDisplays();
			}
		});
		table.setColumnWidth(categoryColumn, 40, Unit.PCT);

	}

}