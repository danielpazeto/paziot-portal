package com.pazeto.iot.server.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.pazeto.iot.shared.HibernateUtil;
import com.pazeto.iot.shared.dto.MonitoredValueDTO;

public class MonitoredValueDAO {

	public static void saveMonitoredValue(List<MonitoredValueDTO> valuesToSave) {

		Session session = HibernateUtil.getCurrentSession();
		try {
			session.beginTransaction();
			for (MonitoredValueDTO monitoredValueDTO : valuesToSave) {
				session.save(monitoredValueDTO);
			}
		} finally {
			session.getTransaction().commit();
		}

	}

	/**
	 * Method to handle monitored value json to this object. { "pt":"0",
	 * "vl":"0", "dt":"1415455646" }
	 * 
	 * @param json
	 */
	public static MonitoredValueDTO jsonToObject(JSONObject json) {
		try {
			MonitoredValueDTO obj = new MonitoredValueDTO();
			obj.setPortId(json.getLong("chipId") + "-" + json.getLong("pt"));
			obj.setValue(json.getString("vl"));
			obj.setDate(new Date(json.getLong("dt")));
			return obj;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

}
