package com.pazeto.iot.shared.vo;

import java.io.Serializable;

import com.pazeto.iot.shared.dto.IoPortDTO;

public class IoPort implements Serializable {

	public static enum PORT_TYPE {
		OUTPUT, OUTPUT_ON_OFF;
	}

	private String id;
	private String iONumber;
	private String deviceId;
	private String type;
	private String description;

	public IoPort(IoPortDTO port) {
		this.id = port.getId();
		this.iONumber = port.getiONumber();
		this.type = port.getType();
		this.deviceId = port.getDeviceId();
		setDescription(port.getDescription());
	}
	public IoPort() {
	}

	public String getId() {
		return getDeviceId() + "-" +getiONumber();
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getiONumber() {
		return iONumber;
	}

	public void setIONumber(String iONumber) {
		this.iONumber = iONumber;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
