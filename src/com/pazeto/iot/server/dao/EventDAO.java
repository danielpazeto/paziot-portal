package com.pazeto.iot.server.dao;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.pazeto.iot.shared.HibernateUtil;
import com.pazeto.iot.shared.dto.EventDTO;
import com.pazeto.iot.shared.vo.Event.ENTITIES;
import com.pazeto.iot.shared.vo.Event.TYPE_EVENT;

public class EventDAO {

	public static void saveEvent(List<EventDTO> eventsToSave) {

		Session session = HibernateUtil.getCurrentSession();
		try {
			session.beginTransaction();
			for (EventDTO eventDTO : eventsToSave) {
				session.save(eventDTO);
			}
		} finally {
			session.getTransaction().commit();
		}

	}

	/**
	 * Method to handle event json to this object. { "code":"0", "value":"0",
	 * "time":"1415455646" , "ioNumber":2} <br>
	 * Method used for handle Json from device
	 * 
	 * @param json
	 */
	public static EventDTO jsonToObject(JSONObject json, String chipId) {
		try {
			IoPortDAO portDAO = new IoPortDAO();
			EventDTO obj = new EventDTO();
			obj.setValue(json.getString("value"));
			obj.setDate(new Date(json.getLong("time")));

			for (TYPE_EVENT evType : TYPE_EVENT.values()) {
				if (json.getString("code").equals(
						String.valueOf(evType.getCode()))) {
					obj.setType(evType.name());
					switch (evType) {
					case CHANGE_PINTOUT_VALUE:
						obj.setRequester(ENTITIES.IO_PORT.name());
						obj.setOwnId(chipId + "-" + json.getString("ioNumber"));
						break;
					case CONNECTED:
					case DISCONNECTED:
						break;
					default:
						break;
					}
				}
			}
			obj.setDate(new Date(json.getLong("time")));

			return obj;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void saveConnectionEvent(String id, ENTITIES who) {
		EventDTO evConnection = new EventDTO();
		evConnection.setRequester(who.name());
		evConnection.setOwnId(id);
		evConnection.setType(TYPE_EVENT.CONNECTED.name());
		evConnection.setDate(new Date());
		saveEvent(Arrays.asList(evConnection));
	}

	public static void saveDisconnectionEvent(String id, ENTITIES who) {
		EventDTO evConnection = new EventDTO();
		evConnection.setRequester(who.name());
		evConnection.setOwnId(id);
		evConnection.setType(TYPE_EVENT.DISCONNECTED.name());
		evConnection.setDate(new Date());
		saveEvent(Arrays.asList(evConnection));
	}
}
