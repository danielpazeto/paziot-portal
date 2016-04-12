package com.pazeto.iot.server.dao;

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
public class DeviceDAO {

	public static String persistDevice(Device dev) throws Exception {

		Session session = HibernateUtil.getCurrentSession();
		try {
			session.beginTransaction();
			System.out.println(1);
			DeviceDTO results = (DeviceDTO) session.get(DeviceDTO.class,
					new String(dev.getChipId()));
			System.out.println(2);
			if (results != null) {
				System.out.println(3);
				session.merge(new DeviceDTO(dev));
				System.out.println(4);
				return dev.getChipId();
			}
			System.out.println(5);
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
			Query query = session.createQuery(hql);
			query.setLong("userId", user.getId());
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
			String hql = "FROM DeviceDTO d WHERE d.chipId = :chipId";
			Query query = session.createQuery(hql);
			query.setString("chipId", chipId);
			query.setMaxResults(1);
			List results = query.list();
			System.out.println(results.size());
			if (results.size() >= 1) {
				for (Object object : results) {
					System.out.println(object);
				}
			}
			return results.size() >= 1;
		} finally {
			session.getTransaction().commit();
		}

	}

}
