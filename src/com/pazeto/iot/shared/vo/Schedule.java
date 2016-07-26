package com.pazeto.iot.shared.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.pazeto.iot.shared.dto.ScheduleDTO;

public class Schedule implements Serializable {

	public enum FREQUENCIES {
		SUNDAY, MONDAY, TUERSDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, DAILY;

		public static List<String> getFrquenciesNames() {
			List<String> frequencyNames = new ArrayList<String>();
			for (FREQUENCIES category : FREQUENCIES.values()) {
				frequencyNames.add(category.name());
			}
			return frequencyNames;
		}
	}

	public enum SCHEDULE_RUN_STATUS {
		EXECUTED, DEVICE_DISCONNECTED, ERROR_ON_SERVER;

	}

	public Schedule() {
		// TODO Auto-generated constructor stub
	}

	public Schedule(ScheduleDTO schedule) {
		setId(schedule.getId());
		setChipId(schedule.getChipId());
		setPortId(schedule.getPortId());
		setHour(schedule.getHour());
		setMinute(schedule.getMinute());
		setFrequency(FREQUENCIES.valueOf(schedule.getFrequency()));
		setValue(schedule.getValue());
	}

	public Schedule(Device currentDev) {
		setChipId(currentDev.getChipId());
	}

	public Schedule(Schedule schedule) {
		setId(schedule.getId());
		setChipId(schedule.getChipId());
		setPortId(schedule.getPortId());
		setHour(schedule.getHour());
		setMinute(schedule.getMinute());
		setFrequency(schedule.getFrequency());
		setValue(schedule.getValue());
	}

	long id;
	String chipId;
	String portId;
	int hour;
	int minute;
	String value;

	String portDescription;

	FREQUENCIES frequency;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getChipId() {
		return chipId;
	}

	public void setChipId(String chipId) {
		this.chipId = chipId;
	}

	public String getPortId() {
		return portId;
	}

	public void setPortId(String portId) {
		this.portId = portId;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	public FREQUENCIES getFrequency() {
		return frequency;
	}

	public void setFrequency(FREQUENCIES frequency) {
		this.frequency = frequency;
	}

	public String getPortDescription() {
		return portDescription;
	}

	public void setPortDescription(String portDescription) {
		this.portDescription = portDescription;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
