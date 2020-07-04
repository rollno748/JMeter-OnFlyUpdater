package io.perfwise.onfly.service;

import java.util.Map.Entry;

import org.apache.jmeter.threads.JMeterVariables;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import io.perfwise.onfly.config.OnFlyConfig;
import io.perfwise.onfly.model.VariableModel;
import io.perfwise.onfly.rest.StandardResponse;
import io.perfwise.onfly.rest.StatusResponse;

public class VariableService {

	private static JMeterVariables jVars;

	public static StandardResponse getVars() {
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

	public static StandardResponse setVars(VariableModel vars) {

		JMeterVariables jVars = OnFlyConfig.getContext().getVariables();

		try {
			JsonObject json = (JsonObject) new Gson().toJsonTree(vars);

			json.entrySet().parallelStream().forEach(entry -> {
				jVars.put(entry.getKey(), entry.getValue().getAsString());
			});

			return new StandardResponse(StatusResponse.SUCCESS, "Variable update success");

		} catch (Exception e) {
			return new StandardResponse(StatusResponse.ERROR, "Error in updating Jmeter variables :: " + e);
		}

	}

}
