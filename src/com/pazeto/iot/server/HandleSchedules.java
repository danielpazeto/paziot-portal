package com.pazeto.iot.server;

import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.collections.map.MultiKeyMap;

import com.google.gwt.dev.util.collect.HashMap;
import com.pazeto.iot.server.dao.EventDAO;
import com.pazeto.iot.server.dao.IoPortDAO;
import com.pazeto.iot.server.dao.ScheduleDAO;
import com.pazeto.iot.server.websocket.HandleConnectedDevices;
import com.pazeto.iot.shared.vo.IoPort;
import com.pazeto.iot.shared.vo.Schedule;
import com.pazeto.iot.shared.vo.Schedule.SCHEDULE_RUN_STATUS;

public class HandleSchedules {

	private IoPortDAO portDao = new IoPortDAO();
	private ScheduleDAO scheduleDao = new ScheduleDAO();

//	<schedule.getid, monthday> = Timer
	private MultiKeyMap schedules = new MultiKeyMap();

	
	public void registerScheduleAction(final Schedule schedule, int monthDay) {

		Timer t = new Timer();
		t.schedule(new TimerTask() {

			@Override
			public void run() {
				sendScheduledActionToDevice(schedule);
			}

		}, calculateScheduleTime(schedule));

		if (schedules.containsKey(schedule.getId(), monthDay)) {
			unregisterScheduleAction(schedule, monthDay);
		}

		schedules.put(schedule.getId(), monthDay, t);
	}

	public void unregisterScheduleAction(final Schedule schedule, int monthDay) {
		((Timer) schedules.get(schedule.getId(), monthDay)).cancel();
		schedules.remove(schedule.getId(), monthDay);
	}

	private void sendScheduledActionToDevice(final Schedule schedule) {
		if (HandleConnectedDevices.deviceIsConnected(schedule.getChipId())) {
			try {
				IoPort ioPort = portDao.getPortById(schedule.getPortId());
				String responseJson = portDao.makeMessage(ioPort,
						schedule.getValue());

				HandleConnectedDevices.sendMessageToDevice(
						schedule.getChipId(), responseJson);
				EventDAO.saveScheduledRunned(schedule,
						SCHEDULE_RUN_STATUS.EXECUTED);
			} catch (Exception e) {
				// exception do JSON PARSER
				EventDAO.saveScheduledRunned(schedule,
						SCHEDULE_RUN_STATUS.ERROR_ON_SERVER);
				e.printStackTrace();
			}
		} else {
			EventDAO.saveScheduledRunned(schedule,
					SCHEDULE_RUN_STATUS.DEVICE_DISCONNECTED);
		}
		schedules.remove(schedule.getId());
	}

	private long calculateScheduleTime(Schedule schedule) {
		Date d = new Date();
		d.setMinutes(schedule.getMinute());
		d.setHours(schedule.getHour());
		return d.getTime();
	}

	/**
	 * Method to every day on 11:30 schedule all for the next day
	 */
	private void scheduleForNextDay() {
		final Date d = new Date();
		d.setDate(d.getDate() + 1);
		d.setHours(11);
		d.setMinutes(30);

		Timer t = new Timer();
		t.schedule(new TimerTask() {

			@Override
			public void run() {
				for (Schedule schl : scheduleDao
						.getSchedulesForTodayAndDaily(d)) {
					registerScheduleAction(schl, d.getDate());
				}
				scheduleForNextDay();
			}

		}, d.getTime());

	}

}
