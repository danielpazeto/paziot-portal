package com.pazeto.iot.shared.dto;

import java.io.Serializable;

import com.pazeto.iot.shared.vo.Value.VALUE_TYPE;


public class ValueDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5411500087502057089L;

	String id;
	String chipId;
	long ioPortId;
	String value;
	long date;
	String type;

	public String getId() {
		return id;
	}

	public String getChipId() {
		return chipId;
	}

	public void setChipId(String chipId) {
		id = this.chipId + ioPortId;
		this.chipId = chipId;
	}

	public long getIoPortId() {
		return ioPortId;
	}

	public void setIoPortId(long ioPortId) {
		id = this.chipId + ioPortId;
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

	public VALUE_TYPE getType() {
		return VALUE_TYPE.valueOf(this.type);
	}

	public void setType(VALUE_TYPE monitored) {
		this.type = monitored.toString();
	}

}
