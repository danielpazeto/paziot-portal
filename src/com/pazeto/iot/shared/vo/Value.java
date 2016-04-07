package com.pazeto.iot.shared.vo;

import java.io.Serializable;


public class Value implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5411500087502057089L;

	public enum VALUE_TYPE {
		MONITORED, SETED_BY_USER;
	}

	String id;
	long ioPortId;
	String value;
	long date;
	String type;

	public String getId() {
		return id;
	}

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
