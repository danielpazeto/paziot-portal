package com.pazeto.iot.shared.dto;

import java.io.Serializable;
import java.util.List;

import com.pazeto.iot.shared.vo.IoPort;
import com.pazeto.iot.shared.vo.MonitoredValue;

/**
 * Class to persist Io Ports in db
 * 
 * @author Daniel
 * 
 */
public class IoPortDTO implements Serializable {

	private String id;
	private String iONumber;
	private String deviceId;
	private String type;
	private String description;
	private List<MonitoredValue> monitoredValues;

	public IoPortDTO() {
		// TODO Auto-generated constructor stub
	}

	public IoPortDTO(IoPort port) {
		this.id = port.getId();
		this.iONumber = port.getiONumber();
		this.deviceId = port.getDeviceId();
		this.type = port.getType();
		setDescription(port.getDescription());
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

	public void setiONumber(String iONumber) {
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

	public List<MonitoredValue> getMonitoredValues() {
		return monitoredValues;
	}

	public void setMonitoredValues(List<MonitoredValue> monitoredValues) {
		this.monitoredValues = monitoredValues;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
