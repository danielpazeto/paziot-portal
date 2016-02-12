package com.pazeto.iot.shared.vo;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

public class Value implements Serializable {

	public enum VALUE_TYPE {
		MONITORED, SETED_BY_USER;
	}

	@Id
	@Index
	String id;
	@Index
	String chipId;
	@Index
	long ioPortId;
	String value;
	@Index
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

	public String getType() {
		return type;
	}

	public void setType(VALUE_TYPE monitored) {
		this.type = monitored.toString();
	}

}
