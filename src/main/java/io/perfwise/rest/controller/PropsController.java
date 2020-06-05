package io.perfwise.rest.controller;

import org.apache.jmeter.util.JMeterUtils;

import io.perfwise.utils.JsonHelper;

public class PropsController {

	public static Object getProperty(String type) {

		if (type.equalsIgnoreCase("system")) {
			return JsonHelper.getProperties(System.getProperties());
		} else if (type.equalsIgnoreCase("jmeter")) {
			return JsonHelper.getProperties(JMeterUtils.getJMeterProperties());
		} else {
			return "{\"message\":\"Invalid property type in query param\"}";
		}

	}

}
