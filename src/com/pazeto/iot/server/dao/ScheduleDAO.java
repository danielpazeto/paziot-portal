package com.pazeto.iot.server.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.pazeto.iot.shared.HibernateUtil;
import com.pazeto.iot.shared.dto.ScheduleDTO;
import com.pazeto.iot.shared.vo.Device;
import com.pazeto.iot.shared.vo.Schedule;
import com.pazeto.iot.shared.vo.Schedule.FREQUENCIES;

public class ScheduleDAO {

	public long persistSchedule(Schedule schedule) {
		System.out.println("Saving schedule: "+schedule.toString());
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
	
	/**
	 * get all schedules with DAILY and weekday frequency
	 * @param date - to get de week day
	 * @return
	 */
	public ArrayList<Schedule> getSchedulesForTodayAndDaily(Date d) {
		Session session = HibernateUtil.getCurrentSession();
		try {
			
			String weekDay = FREQUENCIES.values()[d.getDay()].name();
			
			session.beginTransaction();
			String hql = "FROM ScheduleDTO s WHERE s.frequency = :weekDay or s.frequency = 'DAILY'";
			Query query = session.createQuery(hql);
			query.setString("weekDay", weekDay);
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
