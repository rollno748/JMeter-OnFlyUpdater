package io.perfwise.utils;

import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.apache.jmeter.threads.JMeterContextService.ThreadCounts;
import org.apache.jmeter.threads.JMeterVariables;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonHelper {
	
	private static Gson gson = new GsonBuilder().create();
	
	public static Object getProperties(Properties props) {
		return gson.toJson(props);
	}

	public static Object getJmeterVars(JMeterVariables variables) {
		return gson.toJson(variables);
	}
	
	public static Object toJson(Set<Entry<String, Object>> set) {
		return gson.toJson(set);
	}

	public static Object toJson(JMeterVariables clientSideVariables) {
		return gson.toJson(clientSideVariables);
	}

	public static Object toJson(ThreadCounts threadCounts) {
		return gson.toJson(threadCounts);
	}
	

}
