package com.pazeto.iot.server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.pazeto.iot.shared.vo.MonitoredValues;

public class MonitoredValueServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	DAO db = new DAO();

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		System.out.println(req.getParameter("iot_device_info"));

		JSONObject json = null;
		String chipId = null;
		try {
			json = new JSONObject(req.getParameter("iot_device_info"));
			chipId = json.getString("chipId");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		if (db.isValidDevice(chipId)) {
			MonitoredValues value = new MonitoredValues(json);
			db.saveMonitoredValue(value);
		} else {

		}

		PrintWriter out = resp.getWriter();
		out.write("1");
		super.doPost(req, resp);
	}

}
