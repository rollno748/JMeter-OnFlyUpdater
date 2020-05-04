package io.perfwise.rest;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


public class RestServices {
	
	public static void setPersonData(String name, String birthYear, String about) throws IOException {
		HttpURLConnection connection = (HttpURLConnection) new URL("http://localhost:8080/people/" + name)
				.openConnection();

		connection.setRequestMethod("POST");

		String postData = "name=" + URLEncoder.encode(name);
		postData += "&about=" + URLEncoder.encode(about);
		postData += "&birthYear=" + birthYear;

		connection.setDoOutput(true);
		OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
		wr.write(postData);
		wr.flush();

		int responseCode = connection.getResponseCode();
		if (responseCode == 200) {
			System.out.println("POST was successful.");
		} else if (responseCode == 401) {
			System.out.println("Wrong password.");
		}
	}

}
