package io.perfwise.onfly.service;

import org.apache.jmeter.util.JMeterUtils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import io.perfwise.onfly.model.Property;
import io.perfwise.onfly.rest.StandardResponse;
import io.perfwise.onfly.rest.StatusResponse;

public class PropertyService {

	public static StandardResponse getProperty(String propertyType) {

		if (propertyType.equalsIgnoreCase("system")) {
			return new StandardResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(System.getProperties()));

		} else if (propertyType.equalsIgnoreCase("jmeter")) {
			return new StandardResponse(StatusResponse.SUCCESS,
					new Gson().toJsonTree(JMeterUtils.getJMeterProperties()));

		}

		return new StandardResponse(StatusResponse.ERROR, "Invalid Property type received");
	}

	public static StandardResponse updateProperty(Property props) {

		if (props.getType().equalsIgnoreCase("jmeter")) {
			try {

				JsonObject json = props.getProperties();

				json.entrySet().parallelStream().forEach(entry -> {
					JMeterUtils.setProperty(entry.getKey(), entry.getValue().getAsString());
				});

				return new StandardResponse(StatusResponse.SUCCESS, "Jmeter Properties updated successfully");
			} catch (Exception e) {

				return new StandardResponse(StatusResponse.ERROR, "Error occurred while updating the property to Jmeter");
			}

		} else if(props.getType().equalsIgnoreCase("system")){
			try {

				JsonObject json = props.getProperties();

				json.entrySet().parallelStream().forEach(entry -> {
					System.setProperty(entry.getKey(), entry.getValue().getAsString());
				});

				return new StandardResponse(StatusResponse.SUCCESS, "System Properties updated successfully");
			} catch (Exception e) {

				return new StandardResponse(StatusResponse.ERROR, "Error occurred while updating the property to System");
			}
		} else {
			return new StandardResponse(StatusResponse.ERROR, "Invalid type defined.. Update is possible for tyoe jmeter and system");
		}

	}
}
