package com.pazeto.iot.server.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.pazeto.iot.shared.HibernateUtil;
import com.pazeto.iot.shared.dto.DeviceDTO;
import com.pazeto.iot.shared.vo.Device;
import com.pazeto.iot.shared.vo.User;

/**
 * class to access/persist devices in database
 * 
 * @author Daniel
 * 
 */
public class DeviceDAO implements Serializable {

	public static String persistDevice(Device dev) throws Exception {

		Session session = HibernateUtil.getCurrentSession();
		try {
			session.beginTransaction();
			String hql = "FROM DeviceDTO d WHERE d.chipId = :chipId";
			Query query = session.createQuery(hql).setParameter("chipId",
					dev.getChipId());
			List results = query.list();
			if (results.size() > 0) {
				throw new Exception("Device already exists!");
			}
			return (String) session.save(new DeviceDTO(dev));
		} finally {
			session.getTransaction().commit();
		}

	}

	public static ArrayList<Device> listDeviceByUser(User user) {

		Session session = HibernateUtil.getCurrentSession();
		try {
			session.beginTransaction();
			String hql = "FROM DeviceDTO d WHERE d.userId = :userId";
			System.out.println(2);
			Query query = session.createQuery(hql);
			System.out.println(3);
			query.setLong("userId", user.getId());
			System.out.println(query.getQueryString());
			System.out.println(5.6);
			List results = query.list();
			if (results.size() >= 1) {
				ArrayList<Device> devices = new ArrayList<>();
				for (Object object : results) {
					devices.add(new Device((DeviceDTO) object));
				}
				return devices;
			}
			return null;
		} finally {
			session.getTransaction().commit();
		}
	}

	public static boolean isValidDevice(String chipId) {
		Session session = HibernateUtil.getCurrentSession();
		try {
			session.beginTransaction();
			System.out.println(1);
			String hql = "FROM DeviceDTO d WHERE d.chipId = :chipId";
			System.out.println(2);
			Query query = session.createQuery(hql);
			System.out.println(3);
			query.setString("chipId", chipId);
			System.out.println(5);
			query.setMaxResults(1);
			System.out.println(5.5);
			System.out.println(query.getQueryString());
			System.out.println(5.6);
			List results = query.list();
			System.out.println(results.size());
			if (results.size() >= 1) {
				for (Object object : results) {
					System.out.println(object);
				}

			}
			return true;
		} finally {
			session.getTransaction().commit();
		}

	}

}
