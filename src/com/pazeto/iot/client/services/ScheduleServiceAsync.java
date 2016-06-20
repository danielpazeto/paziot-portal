package com.pazeto.iot.client.services;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.pazeto.iot.shared.vo.Device;
import com.pazeto.iot.shared.vo.Schedule;

public interface ScheduleServiceAsync {

	void listSchedulesByDevice(Device currentDev,
			AsyncCallback<ArrayList<Schedule>> cb);

	void saveSchedule(Schedule schedule, AsyncCallback<Long> callback);

}
