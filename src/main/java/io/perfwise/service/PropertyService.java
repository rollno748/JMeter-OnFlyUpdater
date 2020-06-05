package io.perfwise.service;

import org.apache.jmeter.util.JMeterUtils;

import com.google.gson.Gson;
import com.healthmarketscience.jackcess.PropertyMap.Property;

import io.perfwise.rest.StandardResponse;
import io.perfwise.rest.StatusResponse;

public class PropertyService {

	public static Object getProperty(String propertyType) {

		if (propertyType.equalsIgnoreCase("system")) {
			return new StandardResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(System.getProperties()));

		} else if (propertyType.equalsIgnoreCase("jmeter")) {
			return new StandardResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(JMeterUtils.getJMeterProperties()));

		}

		return new StandardResponse(StatusResponse.ERROR, "Invalid Property type received");
	}

	public static Object updateProperty(String body, Class<Property> property) {

		if (body.equalsIgnoreCase("system")) {
			new StandardResponse(StatusResponse.ERROR, "Update is possible only for Jmeter properties");

		} else if (body.equalsIgnoreCase("jmeter")) {
			new StandardResponse(StatusResponse.SUCCESS, new Gson().toJson(JMeterUtils.getJMeterProperties()));

		}

		return new StandardResponse(StatusResponse.ERROR, new Gson().toJson("Update failed!!"));
	}

}
