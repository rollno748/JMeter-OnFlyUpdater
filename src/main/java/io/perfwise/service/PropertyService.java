package io.perfwise.service;

import org.apache.jmeter.util.JMeterUtils;

import com.google.gson.Gson;

import io.perfwise.model.Property;
import io.perfwise.rest.StandardResponse;
import io.perfwise.rest.StatusResponse;

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

		if (props.getPropType().equalsIgnoreCase("jmeter")) {
			try {
				
				for(int i=0; i<props.getProps().size(); i++) {
					
					//JMeterUtils.setProperty(props.getPropType(), propValue);
				}

				return new StandardResponse(StatusResponse.SUCCESS, "Property updated successfully");
			} catch (Exception e) {

				return new StandardResponse(StatusResponse.ERROR, "Error occurred while updating the property");
			}

		} else {
			return new StandardResponse(StatusResponse.ERROR, "Update is possible only for Jmeter properties");
		}

	}

}
