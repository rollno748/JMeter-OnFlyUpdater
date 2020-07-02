package io.perfwise.onfly.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gson.JsonObject;

import io.perfwise.onfly.config.OnFlyConfig;
import io.perfwise.onfly.rest.StandardResponse;
import io.perfwise.onfly.rest.StatusResponse;

public class VariableService {
	
	private static Set<Entry<String, Object>> jVars;
	
	

	public static StandardResponse getVars() {
		JsonObject variableObj = new JsonObject();
		
		try {
			OnFlyConfig.setThreadVars(true);
			jVars = OnFlyConfig.getVariables().entrySet();
			
			for (Entry<String, Object> temp : jVars) {
				if(!temp.getKey().equalsIgnoreCase("JMeterThread.pack")) {
					variableObj.addProperty(temp.getKey(), temp.getValue().toString());
				}
		     }
			
			return new StandardResponse(StatusResponse.SUCCESS, variableObj);

		} catch (Exception e) {
			return new StandardResponse(StatusResponse.ERROR, "Error in retrieving Jmeter variables :: "+e);
		}
	}


	public static StandardResponse setVars(String body) {
		Map<String, String> variables = new HashMap<String, String>();
		try {
			OnFlyConfig.setThreadVars(true);
			//jVars = OnFlyConfig.setVariables(variables);
			return new StandardResponse(StatusResponse.SUCCESS, variables);

		} catch (Exception e) {
			return new StandardResponse(StatusResponse.ERROR, "Error in updating Jmeter variables :: "+e);
		}
		
	}

	

}
