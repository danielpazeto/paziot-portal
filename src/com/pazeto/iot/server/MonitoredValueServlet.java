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
import com.pazeto.iot.server.dao.DeviceDAO;
import com.pazeto.iot.server.dao.MonitoredValueDAO;
import com.pazeto.iot.shared.dto.MonitoredValueDTO;
import com.pazeto.iot.shared.vo.MonitoredValue;

public class MonitoredValueServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	DAO db = new DAO();

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.err.println(req.getParameterMap());
		System.out.println(req.getParameter("iot_device_info"));

		JSONObject json = null;
		String chipId = null;
		PrintWriter out = resp.getWriter();
		try {
			json = new JSONObject(req.getParameter("iot_device_info"));
			chipId = json.getString("chipId");

			if (DeviceDAO.isValidDevice(chipId)) {
				// verifica se há algum valor para ser salva na base
				if (json.has("monitored_values")) {
					JSONArray values = json.getJSONArray("monitored_values");
					List<MonitoredValueDTO> valuesToSave = new ArrayList<MonitoredValueDTO>();
					for (int i = 0; i < values.length(); i++) {
						System.out.println(values.getJSONObject(i));
						valuesToSave.add(new MonitoredValueDTO(new MonitoredValue(values.getJSONObject(i))));
					}
					MonitoredValueDAO.saveMonitoredValue(valuesToSave);
				}

			} else {
				throw new Exception("Not valid ID");
			}

			
			out.write("1");
		} catch (JSONException e) {
			e.printStackTrace();
			out.write(e.toString());
		} catch (Exception e) {
			out.write(e.toString());
			e.printStackTrace();
		}
	}

	private JSONObject MonitoredValue(JSONObject jsonObject) {
		// TODO Auto-generated method stub
		return null;
	}

}
