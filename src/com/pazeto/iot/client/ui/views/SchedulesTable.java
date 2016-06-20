package com.pazeto.iot.client.ui.views;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.SelectionCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.pazeto.iot.client.services.CustomAsyncCall;
import com.pazeto.iot.client.services.IoPortService;
import com.pazeto.iot.client.services.IoPortServiceAsync;
import com.pazeto.iot.client.services.ScheduleService;
import com.pazeto.iot.client.services.ScheduleServiceAsync;
import com.pazeto.iot.client.ui.BaseComposite;
import com.pazeto.iot.client.ui.DevicePage;
import com.pazeto.iot.client.ui.DynamicSelectionCell;
import com.pazeto.iot.shared.vo.IoPort;
import com.pazeto.iot.shared.vo.Schedule;
import com.pazeto.iot.shared.vo.Schedule.FREQUENCIES;

public class SchedulesTable extends BaseComposite {

	static SchedulesTable uniqueInstance;
	private static IoPortServiceAsync portService = GWT
			.create(IoPortService.class);
	private static ScheduleServiceAsync scheduleService = GWT
			.create(ScheduleService.class);
	private static ListDataProvider<Schedule> scheduleDataProvider;

	public static SchedulesTable getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new SchedulesTable();
		}
		refresh();
		return uniqueInstance;
	}

	private static Map<String, String> portNumbers = new HashMap<>();

	private static void refresh() {
		if (DevicePage.getCurrentDev() != null) {
			// list availables ports by device
			new CustomAsyncCall<ArrayList<IoPort>>() {

				@Override
				public void onSuccess(ArrayList<IoPort> result) {
					if (result != null) {
						for (IoPort ioPort : result) {
							portNumbers.put(ioPort.getId(),
									ioPort.getiONumber());
						}
						portCategoryCell.addOption(portNumbers.values(), 0);
					}
				}

				@Override
				protected void callService(AsyncCallback<ArrayList<IoPort>> cb) {
					portService.listAllPortsByDevice(
							DevicePage.getCurrentDev(), cb);
				}
			}.executeWithoutSpinner();

			// list all schedules by device
			new CustomAsyncCall<ArrayList<Schedule>>() {

				@Override
				public void onSuccess(ArrayList<Schedule> result) {
					if (result != null && scheduleDataProvider != null) {
						scheduleDataProvider.getList().clear();
						scheduleDataProvider.setList(result);
					}
				}

				@Override
				protected void callService(AsyncCallback<ArrayList<Schedule>> cb) {
					scheduleService.listSchedulesByDevice(
							DevicePage.getCurrentDev(), cb);
				}
			}.executeWithoutSpinner();
		}
	}

	public static final ProvidesKey<Schedule> KEY_PROVIDER = new ProvidesKey<Schedule>() {
		@Override
		public Object getKey(Schedule item) {
			return item == null ? null : item.getId();
		}
	};

	private CellTable<Schedule> table;
	private Button addNewScheduleButton;

	public SchedulesTable() {
		GWT.log("iniciando");
		table = new CellTable<Schedule>(KEY_PROVIDER);
		table.setWidth("100%", true);

		// Add a selection model so we can select cells.
		// final SelectionModel<Schedule> selectionModel = new
		// MultiSelectionModel<Schedule>(
		// KEY_PROVIDER);
		// table.setSelectionModel(selectionModel,
		// DefaultSelectionEventManager.<Schedule> createCheckboxManager());

		// Attach a column sort handler to the ListDataProvider to sort the
		// list.
		scheduleDataProvider = new ListDataProvider<>();
		// Initialize the columns.
		GWT.log("iniciando tabela colunas");
		initTableColumns();

		// Add the CellList to the adapter in the database.
		scheduleDataProvider.addDataDisplay(table);

		addNewScheduleButton = new Button("Novo Agendamento");
		addNewScheduleButton.addClickHandler(AddNewBtnHandler);

		VerticalPanel vp = new VerticalPanel();
		vp.add(addNewScheduleButton);
		vp.add(table);
		GWT.log("finalizando construtot");
		initWidget(vp);
	}

	private ClickHandler AddNewBtnHandler = new ClickHandler() {

		@Override
		public void onClick(ClickEvent event) {
			Schedule schdl = new Schedule(DevicePage.getCurrentDev());
			scheduleDataProvider.getList().add(schdl);

		}
	};

	private void refreshSchedule(final int index, final Schedule newSchedule,
			final Schedule oldSchedule) {
		new CustomAsyncCall<Long>() {
			String oldValue;

			@Override
			public void onSuccess(Long result) {
				GWT.log("Sucesso, mas está como " + result);
				newSchedule.setId(result);
				scheduleDataProvider.getList().add(index, newSchedule);
				scheduleDataProvider.refresh();
			}

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("era pra ser -1, mas está como " + oldValue);
				scheduleDataProvider.getList().add(index, oldSchedule);
				scheduleDataProvider.refresh();
			}

			@Override
			protected void callService(AsyncCallback<Long> cb) {
				scheduleService.saveSchedule(newSchedule, cb);
			}
		}.executeWithoutSpinner(0);
	}

	//
	// private void refreshIoDescriptionPort(final IoPort port,
	// final String newPortName) {
	// new CustomIotPazetoAsyncCall<String>() {
	// String oldValue;
	//
	// @Override
	// public void onSuccess(String result) {
	// GWT.log("Sucesso, mas está como " + result);
	// }
	//
	// @Override
	// public void onFailure(Throwable caught) {
	// ((IoPort) scheduleDataProvider.getKey(port))
	// .setDescription(oldValue);
	// scheduleDataProvider.refresh();
	// }
	//
	// @Override
	// protected void callService(AsyncCallback<String> cb) {
	// oldValue = port.getDescription();
	// port.setDescription(newPortName);
	// portService.savePort(port, cb);
	// }
	// }.executeWithoutSpinner(0);
	// }
	//
	static DynamicSelectionCell portCategoryCell = new DynamicSelectionCell();

	private void initTableColumns() {
		Column<Schedule, String> portColumn = new Column<Schedule, String>(
				portCategoryCell) {
			@Override
			public String getValue(Schedule object) {
				return portNumbers.get(object.getPortId());
			}
		};
		portColumn.setFieldUpdater(new FieldUpdater<Schedule, String>() {
			@Override
			public void update(int index, Schedule object, String value) {
				Schedule newSchedule = new Schedule(object);
				for (Entry<String, String> item : portNumbers.entrySet()) {
					if (item.getValue().equals(value)) {
						newSchedule.setPortId(item.getKey());
						break;
					}
				}
				refreshSchedule(index, newSchedule, object);
			}
		});
		table.setColumnWidth(portColumn, 10, Unit.PCT);
		table.addColumn(portColumn, "Porta");

		// Frequency
		final FREQUENCIES[] frequecies = FREQUENCIES.values();
		List<String> frequenciesNames = new ArrayList<String>();
		for (FREQUENCIES category : frequecies) {
			frequenciesNames.add(category.name());
		}
		SelectionCell frequencyCell = new SelectionCell(frequenciesNames);
		Column<Schedule, String> feqColumn = new Column<Schedule, String>(
				frequencyCell) {
			@Override
			public String getValue(Schedule object) {
				if (object.getFrequency() != null)
					return object.getFrequency().name();
				return null;
			}
		};
		feqColumn.setFieldUpdater(new FieldUpdater<Schedule, String>() {
			@Override
			public void update(int index, Schedule object, String value) {
				Schedule newSchedule = new Schedule(object);
				for (FREQUENCIES category : frequecies) {
					if (category.name().equals(value)) {
						object.setFrequency(category);
					}
				}
				refreshSchedule(index, newSchedule, object);
			}
		});
		table.addColumn(feqColumn, "Frequência");
		table.setColumnWidth(feqColumn, 25, Unit.PCT);

		// hour
		List<String> hours = new ArrayList<String>();
		for (int i = 0; i < 24; i++) {
			hours.add(String.valueOf(i));
		}
		Cell<String> hourCell = new SelectionCell(hours);
		Column<Schedule, String> hourColumn = new Column<Schedule, String>(
				hourCell) {
			@Override
			public String getValue(Schedule object) {
				return String.valueOf(object.getHour());
			}
		};
		hourColumn.setFieldUpdater(new FieldUpdater<Schedule, String>() {
			@Override
			public void update(int index, Schedule object, String value) {
				Schedule newSchedule = new Schedule(object);
				newSchedule.setHour(Integer.valueOf(value));
				refreshSchedule(index, newSchedule, object);
			}
		});
		table.addColumn(hourColumn, "Hora");
		table.setColumnWidth(hourColumn, 5, Unit.PCT);

		// minute
		List<String> mins = new ArrayList<String>();
		for (int i = 0; i < 60; i++) {
			mins.add(String.valueOf(i));
		}
		Cell<String> minuteCell = new SelectionCell(mins);
		Column<Schedule, String> minuteColumn = new Column<Schedule, String>(
				minuteCell) {
			@Override
			public String getValue(Schedule object) {
				return String.valueOf(object.getHour());
			}
		};
		hourColumn.setFieldUpdater(new FieldUpdater<Schedule, String>() {
			@Override
			public void update(int index, Schedule object, String value) {
				Schedule newSchedule = new Schedule(object);
				newSchedule.setMinute(Integer.valueOf(value));
				refreshSchedule(index, newSchedule, object);
			}
		});
		table.addColumn(hourColumn, "Minuto");
		table.setColumnWidth(hourColumn, 5, Unit.PCT);

		
		// value
		Cell<String> valueCell = new SelectionCell(Arrays.asList("0","1"));
		Column<Schedule, String> valueColunm = new Column<Schedule, String>(
				valueCell) {
			@Override
			public String getValue(Schedule object) {
				return object.getValue();
			}
		};
		valueColunm.setFieldUpdater(new FieldUpdater<Schedule, String>() {
			@Override
			public void update(int index, Schedule object, String value) {
				Schedule newSchedule = new Schedule(object);
				newSchedule.setValue(value);
				refreshSchedule(index, newSchedule, object);
			}
		});
		table.addColumn(valueColunm, "Valor");
		table.setColumnWidth(valueColunm, 5, Unit.PCT);

		//
		// // IOnumber name.
		// Column<Schedule, String> ioNumberColumn = new Column<Schedule,
		// String>(
		// new EditTextCell()) {
		// @Override
		// public String getValue(Schedule object) {
		// return object.getPortDescription();
		// }
		// };
		// ioNumberColumn.setSortable(true);
		// // sortHandler.setComparator(ioNumberColumn, new Comparator<IoPort>()
		// {
		// // @Override
		// // public int compare(IoPort o1, IoPort o2) {
		// // return o1.getiONumber().compareTo(o2.getiONumber());
		// // }
		// // });
		// table.addColumn(ioNumberColumn, "Nome");
		// ioNumberColumn.setFieldUpdater(new FieldUpdater<IoPort, String>() {
		// @Override
		// public void update(int index, IoPort object, String value) {
		// refreshSchedule(index, object, value);
		// }
		// });
		// table.setColumnWidth(ioNumberColumn, 20, Unit.PCT);
		//
		// // IOport name.
		// Column<IoPort, String> nameColumn = new Column<IoPort, String>(
		// new EditTextCell()) {
		// @Override
		// public String getValue(IoPort object) {
		// return object.getDescription();
		// }
		// };
		// nameColumn.setSortable(true);
		// sortHandler.setComparator(nameColumn, new Comparator<IoPort>() {
		// @Override
		// public int compare(IoPort o1, IoPort o2) {
		// return o1.getDescription().compareTo(o2.getDescription());
		// }
		// });
		// table.addColumn(nameColumn, "Name");
		// nameColumn.setFieldUpdater(new FieldUpdater<IoPort, String>() {
		// @Override
		// public void update(int index, IoPort object, String value) {
		// refreshIoDescriptionPort(object, value);
		// }
		// });
		// table.setColumnWidth(nameColumn, 40, Unit.PCT);
		//
		// // port type.
		// final PORT_TYPE[] categories = PORT_TYPE.values();
		// List<String> categoryNames = new ArrayList<String>();
		// for (PORT_TYPE category : categories) {
		// categoryNames.add(category.name());
		// }
		// SelectionCell categoryCell = new SelectionCell(categoryNames);
		// Column<IoPort, String> categoryColumn = new Column<IoPort, String>(
		// categoryCell) {
		// @Override
		// public String getValue(IoPort object) {
		// return object.getType();
		// }
		// };
		// table.addColumn(categoryColumn, "Type");
		// categoryColumn.setFieldUpdater(new FieldUpdater<IoPort, String>() {
		// @Override
		// public void update(int index, IoPort object, String value) {
		// for (PORT_TYPE category : categories) {
		// if (category.name().equals(value)) {
		// object.setType(category.name());
		// }
		// }
		// // ContactDatabase.get().refreshDisplays();
		// }
		// });
		// table.setColumnWidth(categoryColumn, 40, Unit.PCT);

	}
}