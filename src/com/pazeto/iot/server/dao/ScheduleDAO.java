package com.pazeto.iot.server.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.pazeto.iot.shared.HibernateUtil;
import com.pazeto.iot.shared.dto.IoPortDTO;
import com.pazeto.iot.shared.dto.ScheduleDTO;
import com.pazeto.iot.shared.vo.Device;
import com.pazeto.iot.shared.vo.IoPort;
import com.pazeto.iot.shared.vo.Schedule;

public class ScheduleDAO {

	public long persistSchedule(Schedule schedule) {
		Session session = HibernateUtil.getCurrentSession();
		try {
			session.beginTransaction();
			if (schedule != null && schedule.getId() > 0) {
				ScheduleDTO results = (ScheduleDTO) session.get(
						ScheduleDTO.class, schedule.getId());
				if (results != null) {
					session.merge(new ScheduleDTO(schedule));
					return schedule.getId();
				}
			}
			return (Long) session.save(new ScheduleDTO(schedule));
		} finally {
			session.getTransaction().commit();
		}
	}

	public ArrayList<Schedule> listAllSchedulesByDevice(Device currentDev) {
		Session session = HibernateUtil.getCurrentSession();
		try {
			if (currentDev == null)
				return null;
			session.beginTransaction();
			String hql = "FROM ScheduleDTO s WHERE s.chipId = :chipId";
			Query query = session.createQuery(hql);
			query.setString("chipId", currentDev.getChipId());
			List results = query.list();
			if (results.size() >= 1) {
				ArrayList<Schedule> schedules = new ArrayList<>();
				for (Object object : results) {
					schedules.add(new Schedule((ScheduleDTO) object));
				}
				return schedules;
			}
			return null;
		} finally {
			session.getTransaction().commit();
		}
	}

}
