package com.pazeto.iot.shared.dto;

import com.pazeto.iot.shared.vo.MonitoredValue;

public class MonitoredValueDTO extends ValueDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3387891800736936513L;

	public MonitoredValueDTO() {
		// TODO Auto-generated constructor stub
	}

	public MonitoredValueDTO(MonitoredValue monitoredValue) {
			setType(monitoredValue.getType());
			setChipId(monitoredValue.getChipId());
			setIoPortId(monitoredValue.getIoPortId());
			setValue(monitoredValue.getValue());
			setDate(monitoredValue.getDate());
	}
}
