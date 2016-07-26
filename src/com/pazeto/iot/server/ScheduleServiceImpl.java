package com.pazeto.iot.server;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.pazeto.iot.client.services.ScheduleService;
import com.pazeto.iot.server.dao.ScheduleDAO;
import com.pazeto.iot.shared.vo.Device;
import com.pazeto.iot.shared.vo.Schedule;
import com.pazeto.iot.shared.vo.Schedule.FREQUENCIES;

@SuppressWarnings("serial")
public class ScheduleServiceImpl extends RemoteServiceServlet implements
		ScheduleService {

	private ScheduleDAO scheduleDAO = new ScheduleDAO();
	private HandleSchedules handleSchedules = new HandleSchedules();

	@Override
	public ArrayList<Schedule> listSchedulesByDevice(Device currentDev) {
		return scheduleDAO.listAllSchedulesByDevice(currentDev);
	}

	@Override
	public long saveSchedule(Schedule schedule) {
		Date now = new Date();
		schedule.getFrequency();

		//verify if is DAILY or the weekday is the same AND verify the time
		if ((schedule.getFrequency() == FREQUENCIES.DAILY || schedule
				.getFrequency().ordinal() == now.getDay())
				&& schedule.getHour() > now.getHours()
				&& schedule.getMinute() > now.getMinutes()) {
			handleSchedules.registerScheduleAction(schedule, now.getDate());
		}

		return scheduleDAO.persistSchedule(schedule);
	}
}
