package com.pazeto.iot.shared.vo;

import org.apache.jasper.compiler.JspUtil.ValidAttribute;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

public class MonitoredValues extends Value {

	public MonitoredValues(JSONObject json) {
		try {
			setType(VALUE_TYPE.MONITORED);
			setChipId(json.getString("cid"));
			setIoPortId(json.getLong("pt"));
			setValue(json.getString("vl"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
