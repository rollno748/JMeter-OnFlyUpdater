package io.perfwise.onfly.service;

import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterVariables;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import io.perfwise.onfly.config.OnFlyConfig;
import io.perfwise.onfly.rest.StandardResponse;
import io.perfwise.onfly.rest.StatusResponse;

public class VariableService {
	
	private static JMeterContext context;
	private static JMeterVariables jVars;
	

	public static StandardResponse getVars() {
		context = OnFlyConfig.getContext();
		try {
			jVars = (JMeterVariables) context.getVariables().entrySet();
			return new StandardResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(jVars));

		} catch (Exception e) {
			return new StandardResponse(StatusResponse.ERROR, "Error in retrieving Jmeter variables \n"+e);
		}
	}


	public static JsonElement setVars(String body) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
