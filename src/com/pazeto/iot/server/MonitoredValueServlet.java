package com.pazeto.iot.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.pazeto.iot.shared.vo.MonitoredValue;

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

			if (db.isValidDevice(chipId)) {
				// verifica se há algum valor para ser salva na base
				if (json.has("monitored_values")) {
					JSONArray values = json.getJSONArray("monitored_values");
					List<MonitoredValue> valuesToSave = new ArrayList<MonitoredValue>();
					for (int i = 0; i < values.length(); i++) {
						values.getJSONObject(i);
						valuesToSave.add(new MonitoredValue(json));
					}
					db.saveMonitoredValue(valuesToSave);
				}
				
				
				
				

				
			} else {

			}

			PrintWriter out = resp.getWriter();
			out.write("1");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		super.doPost(req, resp);
	}

}
