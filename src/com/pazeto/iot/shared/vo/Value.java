package com.pazeto.iot.shared.vo;

import java.io.Serializable;


public class Value implements Serializable {

	public enum VALUE_TYPE {
		MONITORED, SETED_BY_USER;
	}

	
	long ioPortId;
	String value;
	long date;

	String type;

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
