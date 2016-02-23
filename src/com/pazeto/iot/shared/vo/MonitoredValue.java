package com.pazeto.iot.shared.vo;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.googlecode.objectify.annotation.Subclass;

@Subclass(index=true)
public class MonitoredValue extends Value {

	public MonitoredValue(JSONObject json) {
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
