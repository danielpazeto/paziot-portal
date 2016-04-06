package com.pazeto.iot.shared.vo;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

public class MonitoredValue extends Value {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3387891800736936513L;

	public MonitoredValue() {
		// TODO Auto-generated constructor stub
	}

	public MonitoredValue(JSONObject json) {

		// "cid":"123",
		// "pt":"0",
		// "vl":"0",
		// "dt":"1415455646"

		try {
			setType(VALUE_TYPE.MONITORED);
			setChipId(json.getString("cid"));
			setIoPortId(json.getLong("pt"));
			setValue(json.getString("vl"));
			setDate(json.getLong("dt"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
