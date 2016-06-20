package com.pazeto.iot.client.services;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.pazeto.iot.shared.vo.Device;
import com.pazeto.iot.shared.vo.Schedule;

@RemoteServiceRelativePath("schedule")
public interface ScheduleService extends RemoteService {

	ArrayList<Schedule> listSchedulesByDevice(Device currentDev);

	long saveSchedule(Schedule schedule);

}
