package io.perfwise.utils;

import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.apache.jmeter.control.Controller;
import org.apache.jmeter.threads.JMeterContextService.ThreadCounts;
import org.apache.jmeter.threads.JMeterVariables;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.perfwise.rest.controller.User;

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

	public static Object toJson(JMeterVariables variables) {
		return gson.toJson(variables);
	}

	public static Object toJson(ThreadCounts threadCounts) {
		return gson.toJson(threadCounts);
	}

	public static User fromJson(String body, Class<User> user) {
		// TODO Auto-generated method stub
		return new Gson().fromJson(body, User.class);
	}

	public static Object toJson(Controller samplerController) {
		// TODO Auto-generated method stub
		return  gson.toJson(samplerController);
	}
	

}
