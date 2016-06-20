package com.pazeto.iot.shared.dto;

import com.pazeto.iot.shared.vo.Schedule;

public class ScheduleDTO {

	public ScheduleDTO(Schedule schedule) {
		setId(schedule.getId());
		setPortId(schedule.getPortId());
		setChipId(schedule.getChipId());
		setHour(schedule.getHour());
		setMinute(schedule.getMinute());
		setFrequency(schedule.getFrequency().name());
	}

	public ScheduleDTO() {
		// TODO Auto-generated constructor stub
	}
	
	long id;
	String chipId;
	String portId;
	int hour;
	int minute;
	String frequency;
	String value;
	
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

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	public String getValue() {
		return value;
	}	

}
