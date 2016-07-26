package com.pazeto.iot.client.ui.views;

import gwt.material.design.addins.client.timepicker.MaterialTimePicker;
import gwt.material.design.client.ui.MaterialListBox;
import gwt.material.design.client.ui.MaterialNumberBox;
import gwt.material.design.client.ui.MaterialTextBox;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.pazeto.iot.client.services.CustomAsyncCall;
import com.pazeto.iot.client.services.ScheduleService;
import com.pazeto.iot.client.services.ScheduleServiceAsync;
import com.pazeto.iot.client.ui.DevicePage;
import com.pazeto.iot.client.ui.MainRootScreen;
import com.pazeto.iot.shared.vo.Schedule;
import com.pazeto.iot.shared.vo.Schedule.FREQUENCIES;

public class SchedulePopupForm extends BasePopupForm {

	private static SchedulePopupForm uniqueInstance;

	private static Schedule currentSchedule;

	public static SchedulePopupForm getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new SchedulePopupForm();
		}
		MainRootScreen.getInstance().setMainModalTitle("Agendamento");
		return uniqueInstance;
	}

	private final ScheduleServiceAsync scheduleService = GWT
			.create(ScheduleService.class);

	private static MaterialListBox ioNumberListBox = new MaterialListBox();
	private static MaterialListBox frequencyListBox = new MaterialListBox();
	private static MaterialTimePicker time = new MaterialTimePicker();
	private static MaterialNumberBox<Integer> hourBox = new MaterialNumberBox<>();
	private static MaterialNumberBox<Integer> minBox = new MaterialNumberBox<>();
	private static MaterialTextBox value = new MaterialTextBox();

	public SchedulePopupForm() {
		ioNumberListBox.setPlaceholder("Número da porta");
		frequencyListBox.setPlaceholder("Frequencia");
		value.setPlaceholder("Valor");
		time.setPlaceholder("Time");
		time.setEnabled(true);
		time.initTimePicker();
		for (String frq : FREQUENCIES.getFrquenciesNames()) {
			frequencyListBox.addItem(frq);
		}

		for (String number : ListSchedulesView.getInstance()
				.getPortNumberAvailables().values()) {
			ioNumberListBox.addItem(number);
		}

		getBasePanel().add(new Label("Cadastro de Porta"));
		getBasePanel().add(ioNumberListBox);
		getBasePanel().add(frequencyListBox);
//		getBasePanel().add(time);
		getBasePanel().add(hourBox);
		getBasePanel().add(minBox);
		getBasePanel().add(value);

		this.setModal(true);

		addDefaultButtons();
	}

	@Override
	ClickHandler getSaveClickHandler() {
		return new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				final Schedule schdl = new Schedule();
				schdl.setChipId(DevicePage.getCurrentDev().getChipId());
				schdl.setPortId(ListSchedulesView.getInstance()
						.getPortIdByPortNumber(
								ioNumberListBox.getSelectedItemText()));
				schdl.setFrequency(FREQUENCIES.valueOf(frequencyListBox
						.getSelectedItemText()));
//				schdl.setHour(time.getValue().getHours());
//				schdl.setMinute(time.getValue().getMinutes());
				schdl.setHour(hourBox.getValue());
				schdl.setMinute(minBox.getValue());
				schdl.setValue(value.getText());
				getSaveBtn().setEnabled(false);

				new CustomAsyncCall<Long>() {

					@Override
					public void onSuccess(Long result) {
						SchedulePopupForm.getInstance().hide();
						MainRootScreen
								.getInstance()
								.setModalText(
										"Schedule "
												// + ioNumberListBox.getText()
												+ " criado com sucesso no dispositivo"
												+ DevicePage.getCurrentDev()
														.getName()).openModal();
						ListSchedulesView.getInstance().refresh();
					}

					@Override
					public void onFailure(Throwable caught) {
						GWT.log("Error message: ", caught);
						MainRootScreen.getInstance()
								.setModalText("Erro ao criar agendamento")
								.openModal();
					}

					@Override
					protected void callService(AsyncCallback<Long> cb) {
						scheduleService.saveSchedule(schdl, cb);
					}
				}.execute(0);
			}
		};
	}

	@Override
	ClickHandler getCloseClickHandler() {
		return new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				SchedulePopupForm.this.hide();
			}
		};
	}
	
	public static SchedulePopupForm getInstance(Schedule schdl) {
		getInstance();
		currentSchedule = schdl;
		
		ioNumberListBox.setSelectedValue(ListSchedulesView.getInstance().getPortNumberAvailables().get(schdl.getPortId()));
		frequencyListBox.setSelectedValue(schdl.getFrequency().name());
		
//		time.setValue(time)
		value.setText(schdl.getValue());
		return uniqueInstance;
	}
}
