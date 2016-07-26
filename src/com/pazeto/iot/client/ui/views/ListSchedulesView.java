package com.pazeto.iot.client.ui.views;

import gwt.material.design.client.constants.IconPosition;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.ui.MaterialButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.SelectionCell;
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
import com.google.gwt.view.client.SelectionChangeEvent;
import com.pazeto.iot.client.services.CustomAsyncCall;
import com.pazeto.iot.client.services.IoPortService;
import com.pazeto.iot.client.services.IoPortServiceAsync;
import com.pazeto.iot.client.services.ScheduleService;
import com.pazeto.iot.client.services.ScheduleServiceAsync;
import com.pazeto.iot.client.ui.BaseComposite;
import com.pazeto.iot.client.ui.DevicePage;
import com.pazeto.iot.client.ui.widgets.DynamicSelectionCell;
import com.pazeto.iot.client.ui.widgets.IotMaterialButton;
import com.pazeto.iot.shared.vo.IoPort;
import com.pazeto.iot.shared.vo.Schedule;
import com.pazeto.iot.shared.vo.Schedule.FREQUENCIES;

public class ListSchedulesView extends BaseComposite {

	static ListSchedulesView uniqueInstance;
	private static IoPortServiceAsync portService = GWT
			.create(IoPortService.class);
	private static ScheduleServiceAsync scheduleService = GWT
			.create(ScheduleService.class);
	private static ListDataProvider<Schedule> scheduleDataProvider;

	public static ListSchedulesView getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new ListSchedulesView();
		}
		refresh();
		return uniqueInstance;
	}

	/**
	 * Map to portId,portNumber
	 */
	private static Map<String, String> portNumbers = new HashMap<>();

	static void refresh() {
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

	// public static final ProvidesKey<Schedule> KEY_PROVIDER = new
	// ProvidesKey<Schedule>() {
	// @Override
	// public Object getKey(Schedule item) {
	// return item == null ? null : item.getId();
	// }
	// };

	private CellTable<Schedule> table;
	private MaterialButton addNewScheduleButton;

	public ListSchedulesView() {
		table = new CellTable<Schedule>();
		table.setWidth("100%");
		// table.setHeight("40vh");
		table.addStyleName("type striped");
		final NoSelectionModel<Schedule> selectionModel = new NoSelectionModel<Schedule>();

		selectionModel
				.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

					@Override
					public void onSelectionChange(SelectionChangeEvent event) {
						Schedule schdl = selectionModel.getLastSelectedObject();
						SchedulePopupForm.getInstance(schdl).center();
					}
				});
		table.setSelectionModel(selectionModel);

		scheduleDataProvider = new ListDataProvider<>();
		initTableColumns();
		scheduleDataProvider.addDataDisplay(table);

		addNewScheduleButton = new IotMaterialButton("Novo Agendamento",
				IconType.ALARM_ADD, IconPosition.RIGHT);
		addNewScheduleButton.addClickHandler(AddNewBtnHandler);

		VerticalPanel vp = new VerticalPanel();
		vp.add(addNewScheduleButton);
		vp.add(table);
		initWidget(vp);
	}

	private ClickHandler AddNewBtnHandler = new ClickHandler() {

		@Override
		public void onClick(ClickEvent event) {
			 refresh();
			if (portCategoryCell.optionsMap.get(0) != null) {
				GWT.log(portCategoryCell.optionsMap.get(0).toString());
				SchedulePopupForm.getInstance().center();
			} else {
				GWT.log("There isn't signed ports...");
			}
		}
	};

	private void refreshSchedule(final int index, final Schedule newSchedule,
			final Schedule oldSchedule) {
		new CustomAsyncCall<Long>() {

			@Override
			public void onSuccess(Long result) {
				GWT.log("Sucesso, mas est� como " + result);
				newSchedule.setId(result);
				scheduleDataProvider.getList().set(index, newSchedule);
				scheduleDataProvider.refresh();
			}

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Falha " + oldSchedule.getChipId());
				scheduleDataProvider.getList().set(index, oldSchedule);
				scheduleDataProvider.refresh();
			}

			@Override
			protected void callService(AsyncCallback<Long> cb) {
				GWT.log("SALVANDO " + newSchedule);
				scheduleService.saveSchedule(newSchedule, cb);
			}
		}.executeWithoutSpinner(0);
	}

	public Map<String, String> getPortNumberAvailables() {
		return portNumbers;
	}

	public String getPortIdByPortNumber(String portNumber) {
		for (Entry<String, String> item : portNumbers.entrySet()) {
			if (item.getValue().equals(portNumber)) {
				return item.getKey();
			}
		}
		return null;
	}

	static DynamicSelectionCell portCategoryCell = new DynamicSelectionCell();

	private void initTableColumns() {
		Column<Schedule, String> portColumn = new Column<Schedule, String>(
				portCategoryCell) {
			@Override
			public String getValue(Schedule object) {
				return portNumbers.get(object.getPortId());
			}
		};
		// portColumn.setFieldUpdater(new FieldUpdater<Schedule, String>() {
		// @Override
		// public void update(int index, Schedule object, String value) {
		// Schedule newSchedule = new Schedule(object);
		// newSchedule.setPortId(getPortIdByPortNumber(value));
		// refreshSchedule(index, newSchedule, object);
		// }
		// });
		table.setColumnWidth(portColumn, 10, Unit.PCT);
		table.addColumn(portColumn, "Porta");

		// Frequency
		SelectionCell frequencyCell = new SelectionCell(
				FREQUENCIES.getFrquenciesNames());
		Column<Schedule, String> feqColumn = new Column<Schedule, String>(
				frequencyCell) {
			@Override
			public String getValue(Schedule object) {
				if (object.getFrequency() != null)
					return object.getFrequency().name();
				return null;
			}
		};
		// feqColumn.setFieldUpdater(new FieldUpdater<Schedule, String>() {
		// @Override
		// public void update(int index, Schedule object, String value) {
		// Schedule newSchedule = new Schedule(object);
		// for (FREQUENCIES category : frequecies) {
		// if (category.name().equals(value)) {
		// object.setFrequency(category);
		// }
		// }
		// refreshSchedule(index, newSchedule, object);
		// }
		// });
		table.addColumn(feqColumn, "Frequ�ncia");
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
		// hourColumn.setFieldUpdater(new FieldUpdater<Schedule, String>() {
		// @Override
		// public void update(int index, Schedule object, String value) {
		// Schedule newSchedule = new Schedule(object);
		// newSchedule.setHour(Integer.valueOf(value));
		// refreshSchedule(index, newSchedule, object);
		// }
		// });
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
		// minuteColumn.setFieldUpdater(new FieldUpdater<Schedule, String>() {
		// @Override
		// public void update(int index, Schedule object, String value) {
		// Schedule newSchedule = new Schedule(object);
		// newSchedule.setMinute(Integer.valueOf(value));
		// refreshSchedule(index, newSchedule, object);
		// }
		// });
		table.addColumn(minuteColumn, "Minuto");
		table.setColumnWidth(minuteColumn, 5, Unit.PCT);

		// value
		Cell<String> valueCell = new SelectionCell(Arrays.asList("0", "1"));
		Column<Schedule, String> valueColunm = new Column<Schedule, String>(
				valueCell) {
			@Override
			public String getValue(Schedule object) {
				return object.getValue();
			}
		};
		// valueColunm.setFieldUpdater(new FieldUpdater<Schedule, String>() {
		// @Override
		// public void update(int index, Schedule object, String value) {
		// Schedule newSchedule = new Schedule(object);
		// newSchedule.setValue(value);
		// refreshSchedule(index, newSchedule, object);
		// }
		// });
		table.addColumn(valueColunm, "Valor");
		table.setColumnWidth(valueColunm, 5, Unit.PCT);

	}

	@Override
	protected String getModalTitle() {
		return "Agendamento";
	}
}