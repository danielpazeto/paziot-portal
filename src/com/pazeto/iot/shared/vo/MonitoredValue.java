package com.pazeto.iot.shared.vo;

import java.util.Date;

import com.pazeto.iot.shared.dto.MonitoredValueDTO;

public class MonitoredValue {

	private static final long serialVersionUID = -3387891800736936513L;

	public MonitoredValue(MonitoredValueDTO monitoredValueDTO) {
		setId(monitoredValueDTO.getId());
		setPortId(monitoredValueDTO.getPortId());
		setValue(monitoredValueDTO.getValue());
		setDate(monitoredValueDTO.getDate());
	}

	private long id;
	private String portId; //chipid - ionumber
	private String value;
	private Date date;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPortId() {
		return portId;
	}

	public void setPortId(String ioPortId) {
		this.portId = ioPortId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
