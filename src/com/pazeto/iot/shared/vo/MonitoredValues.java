package com.pazeto.iot.shared.vo;

import java.io.Serializable;

public class MonitoredValues implements Serializable {

	long ioPortId;
	String value;
	long date;

	public long getIoPortId() {
		return ioPortId;
	}

	public void setIoPortId(long ioPortId) {
		this.ioPortId = ioPortId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

}
