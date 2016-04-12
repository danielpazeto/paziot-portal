package com.pazeto.iot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
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
		try {
            // open websocket
            final WebsocketEndPointClient clientEndPoint = new WebsocketEndPointClient(new URI("ws://localhost:8025/dev_con"));
            // add listener
            clientEndPoint.addMessageHandler(new WebsocketEndPointClient.MessageHandler() {
                public void handleMessage(String message) {
                    System.out.println(message);
                }
            });
            // send message to websocket
            clientEndPoint.sendMessage("{'event':'addChannel','channel':'ok_btccny_ticker'}");

            // wait 5 seconds for messages from websocket
            Thread.sleep(5000);

        } catch (InterruptedException ex) {
            System.err.println("InterruptedException exception: " + ex.getMessage());
        } catch (URISyntaxException ex) {
            System.err.println("URISyntaxException exception: " + ex.getMessage());
        }
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
