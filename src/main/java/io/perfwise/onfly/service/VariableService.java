package io.perfwise.onfly.service;

import java.util.Map.Entry;

import org.apache.jmeter.threads.JMeterVariables;

import com.google.gson.JsonObject;

import io.perfwise.onfly.config.OnFlyConfig;
import io.perfwise.onfly.rest.StandardResponse;
import io.perfwise.onfly.rest.StatusResponse;

public class VariableService {

	private static JMeterVariables jVars;

	public static StandardResponse getVars(String threadName) {
		JsonObject variableObj = new JsonObject();

		try {
			jVars = OnFlyConfig.getContext().getVariables();
			for (Entry<String, Object> temp : jVars.entrySet()) {
				if (!temp.getKey().equalsIgnoreCase("JMeterThread.pack")) {
					variableObj.addProperty(temp.getKey(), temp.getValue().toString());
				}
			}
			return new StandardResponse(StatusResponse.SUCCESS, variableObj);
		} catch (Exception e) {
			e.printStackTrace();
			return new StandardResponse(StatusResponse.ERROR, "Error in retrieving Jmeter variables :: " + e);
		}
	}

	@SuppressWarnings("unlikely-arg-type")
	public static StandardResponse setVars(String threadname, JsonObject json) {

		try {
			JMeterVariables jVars = OnFlyConfig.getContext().getVariables();

			jVars.entrySet().parallelStream().forEach(entry -> {
				if (json.has(entry.getKey())) {
					if (!(json.get(entry.getKey()).equals(entry.getValue().toString()))) {
						jVars.put(entry.getKey(), json.get(entry.getKey()).toString());
					}
				}
			});
			
			OnFlyConfig.setVariables(jVars);
			
			return new StandardResponse(StatusResponse.SUCCESS, "Variable update success");

		} catch (Exception e) {
			return new StandardResponse(StatusResponse.ERROR, "Error in updating Jmeter variables :: " + e);
		}

	}

}
