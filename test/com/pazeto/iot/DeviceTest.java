package com.pazeto.iot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import java.net.URL;

public class DeviceTest {

	private static final String POST_URL = "http://127.0.0.1:8888/pazeto_iot/add_monitored_value";


	private static void sendPOST() throws IOException {
		URL obj = new URL(POST_URL);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("POST");
//		con.setRequestProperty("User-Agent", USER_AGENT);

		// For POST only - START
		con.setDoOutput(true);
		OutputStream os = con.getOutputStream();
		os.write(makeDiferentValues().getBytes());
		os.flush();
		os.close();
		// For POST only - END

		int responseCode = con.getResponseCode();
		System.out.println("POST Response Code :: " + responseCode);

		if (responseCode == HttpURLConnection.HTTP_OK) { // success
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// print result
			System.out.println(response.toString());
		} else {
			System.out.println("POST request not worked");
		}
	}
	
	public static void main(String[] args) {
		Integer oi = 0;
		oi= oi >> 1;
//		while(true){
//			
//			
//			
//		}
//		
//		
//		
//		try {
//			sendPOST();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	
	static String makeDiferentValues(){
		String monitoredValue = "{\"cid\":\"123\"," +
		"\"pt\":\"0\","+
		"\"vl\":\"0\","+
		"\"dt\":\"1415455646\"}";
		
		String chipId= "iot_device_info={\"chipId\":\"123\"," +
				"\"monitored_values\":[" +
				monitoredValue +
				"]" +
				"}";
		return chipId;
		
	}

}
