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
			System.out.println("Trying save device with chip id:"+dev.getChipId());
			DeviceDTO results = (DeviceDTO) session.get(DeviceDTO.class,
					new String(dev.getChipId()));
			if (results != null) {
				System.out.println("Has already a device with chip id:"+dev.getChipId());
				session.merge(new DeviceDTO(dev));
				return dev.getChipId();
			}
			System.out.println("new saved device with chip id:"+dev.getChipId());
			return (String) session.save(new DeviceDTO(dev));
		} finally {
			session.getTransaction().commit();
		}

	}

	public static ArrayList<Device> listDeviceByUser(User user) {

		Session session = HibernateUtil.getCurrentSession();
		try {
			session.beginTransaction();
			System.out.println("Listing device with user id:"+user.getId());
			String hql = "FROM DeviceDTO d WHERE d.userId = :userId";
			Query query = session.createQuery(hql);
			query.setLong("userId", user.getId());
			List results = query.list();
			if (results.size() >= 1) {
				System.out.println("There is "+results.size()+" devices with user id:"+user.getId());
				ArrayList<Device> devices = new ArrayList<>();
				for (Object object : results) {
					devices.add(new Device((DeviceDTO) object));
				}
				return devices;
			}
			System.out.println("There isn't devices with user id:"+user.getId());
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
			return query.list().size() >= 1;
		} finally {
			session.getTransaction().commit();
		}

	}

}
