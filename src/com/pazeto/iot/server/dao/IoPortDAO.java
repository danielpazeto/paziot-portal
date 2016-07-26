package com.pazeto.iot.server.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.pazeto.iot.shared.HibernateUtil;
import com.pazeto.iot.shared.dto.IoPortDTO;
import com.pazeto.iot.shared.vo.Device;
import com.pazeto.iot.shared.vo.IoPort;

public class IoPortDAO {

	public ArrayList<IoPort> listAllPortsByDevice(Device dev) {
		Session session = HibernateUtil.getCurrentSession();
		try {
			if (dev == null)
				return null;
			session.beginTransaction();
			String hql = "FROM IoPortDTO as p WHERE p.deviceId = :deviceId";
			Query query = session.createQuery(hql);
			System.out.println("filtering by : " + dev.getChipId());
			query.setString("deviceId", dev.getChipId());
			List<?> results = query.list();
			if (results.size() >= 1) {
				ArrayList<IoPort> ports = new ArrayList<>();
				for (Object object : results) {
					ports.add(new IoPort((IoPortDTO) object));
				}
				return ports;
			}
			return null;
		} finally {
			session.getTransaction().commit();
		}
	}

	public IoPort getPortById(String id) {
		Session session = HibernateUtil.getCurrentSession();
		try {
			session.beginTransaction();
			String hql = "FROM IoPortDTO as p WHERE p.id = :id";
			Query query = session.createQuery(hql);
			System.out.println("filtering by : " + id);
			query.setString("id", id);
			List<?> results = query.list();
			if (results.size() >= 1) {
				return new IoPort((IoPortDTO) results.get(0));
			}
			return null;
		} finally {
			session.getTransaction().commit();
		}
	}

	public String persistIoPort(IoPort port) {

		Session session = HibernateUtil.getCurrentSession();
		try {
			session.beginTransaction();
			if (port != null && port.getId() != null) {
				IoPortDTO results = (IoPortDTO) session.get(IoPortDTO.class,
						new String(port.getId()));
				if (results != null) {
					session.merge(new IoPortDTO(port));
					return port.getId();
				}
			}
			return (String) session.save(new IoPortDTO(port));
		} finally {
			session.getTransaction().commit();
		}

	}
	
	
	/**
	 * BIG WORKAROUND there is the same method in the client side, because the libs JSON 
	 * @param v
	 * @return
	 * @throws JSONException
	 */
	public String makeMessage(IoPort port, String v) throws JSONException {
		JSONObject responseJson = new JSONObject();
		JSONObject value = new JSONObject();
		value.put("ioNumber", port.getiONumber());
		value.put("value", v);
		JSONArray arrayValues = new JSONArray();
		arrayValues.put(0, value);
		responseJson.put("values", arrayValues);
		responseJson.put("chipId", port.getDeviceId());
		return responseJson.toString();
	}

}
