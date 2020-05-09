package io.perfwise.utils;

import java.util.Properties;

import org.apache.jmeter.threads.JMeterContextService.ThreadCounts;
import org.apache.jmeter.threads.JMeterVariables;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonHelper {
	
	private static Gson gson = new GsonBuilder().create();
	
	public static Object getJmeterProperties(Properties props) {
		return gson.toJson(props);
	}

	public static Object getJmeterVars(JMeterVariables variables) {
		return gson.toJson(variables);
	}
	
	public static Object toJson(ThreadCounts threadInfo) {
		return gson.toJson(threadInfo);
	}

	public static Object toJson(JMeterVariables clientSideVariables) {
		return gson.toJson(clientSideVariables);
	}
	

}
