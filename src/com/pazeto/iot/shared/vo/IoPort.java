package com.pazeto.iot.shared.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.pazeto.iot.shared.dto.IoPortDTO;

public class IoPort implements Serializable {

	public static enum PORT_TYPE {
		DIGITAL_INPUT, DIGITAL_OUTPUT, ANALOG_OUTPUT, ANALOG_INPUT;

		public static List<String> getTypeNames() {
			List<String> categoryNames = new ArrayList<String>();
			for (PORT_TYPE category : PORT_TYPE.values()) {
				categoryNames.add(category.name());
			}
			return categoryNames;
		}
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

	public IoPort(IoPort port) {
		this.id = port.getId();
		this.iONumber = port.getiONumber();
		this.type = port.getType();
		this.deviceId = port.getDeviceId();
		setDescription(port.getDescription());
	}

	public IoPort() {
	}

	public String getId() {
		return getDeviceId() + "-" + getiONumber();
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
