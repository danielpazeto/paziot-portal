package com.pazeto.iot.server;

import java.util.ArrayList;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.pazeto.iot.client.services.ScheduleService;
import com.pazeto.iot.server.dao.ScheduleDAO;
import com.pazeto.iot.shared.vo.Device;
import com.pazeto.iot.shared.vo.Schedule;

@SuppressWarnings("serial")
public class ScheduleServiceImpl extends RemoteServiceServlet implements
		ScheduleService {

	private ScheduleDAO scheduleDAO = new ScheduleDAO();

	@Override
	public ArrayList<Schedule> listSchedulesByDevice(Device currentDev) {
		return scheduleDAO.listAllSchedulesByDevice(currentDev);
	}
	
	@Override
	public long saveSchedule(Schedule schedule) {
		return scheduleDAO.persistSchedule(schedule);
	}

}
