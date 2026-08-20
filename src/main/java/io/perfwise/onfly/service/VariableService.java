package io.perfwise.onfly.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.perfwise.onfly.config.OnFlyConfig;
import io.perfwise.onfly.rest.StandardResponse;
import io.perfwise.onfly.rest.StatusResponse;
import org.apache.jmeter.threads.JMeterVariables;

import java.util.Map.Entry;

public class VariableService {

	public static StandardResponse getVars(String threadName) {
		JsonObject variableObj = new JsonObject();

		try {
			JMeterVariables jVars = resolveVars(threadName);
			if (jVars == null) {
				return new StandardResponse(StatusResponse.ERROR, "Thread not found: " + threadName);
			}
			for (Entry<String, Object> temp : jVars.entrySet()) {
				if (!temp.getKey().equalsIgnoreCase("JMeterThread.pack")) {
					variableObj.addProperty(temp.getKey(), temp.getValue().toString());
				}
			}
			return new StandardResponse(StatusResponse.SUCCESS, variableObj);
		} catch (Exception e) {
			return new StandardResponse(StatusResponse.ERROR, "Error retrieving Jmeter variables :: " + e);
		}
	}

	public static StandardResponse setVars(String threadName, JsonObject json) {
		try {
			JMeterVariables jVars = resolveVars(threadName);
			if (jVars == null) {
				return new StandardResponse(StatusResponse.ERROR, "Thread not found: " + threadName);
			}
			for (Entry<String, JsonElement> entry : json.entrySet()) {
				jVars.put(entry.getKey(), entry.getValue().getAsString());
			}
			return new StandardResponse(StatusResponse.SUCCESS, "Variable update success");
		} catch (Exception e) {
			return new StandardResponse(StatusResponse.ERROR, "Error updating Jmeter variables :: " + e);
		}
	}

	private static JMeterVariables resolveVars(String threadName) {
		JMeterVariables jVars = OnFlyConfig.getThreadVariables().get(threadName);
		if (jVars == null) {
			// fall back to shared context for backward compatibility
			jVars = OnFlyConfig.getContext() != null ? OnFlyConfig.getContext().getVariables() : null;
		}
		return jVars;
	}

}
