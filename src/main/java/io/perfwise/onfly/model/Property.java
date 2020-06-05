package io.perfwise.onfly.model;

import com.google.gson.JsonObject;

public class Property {

	private String type;
	private JsonObject properties;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public JsonObject getProperties() {
		return properties;
	}

	public void setProperties(JsonObject properties) {
		this.properties = properties;
	}

}
