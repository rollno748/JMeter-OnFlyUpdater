package io.perfwise.model;

import com.google.gson.JsonObject;

public class Property {

	private String propType;
	private JsonObject props;
	

	public String getPropType() {
		return propType;
	}

	public void setPropType(String propType) {
		this.propType = propType;
	}

	public JsonObject getProps() {
		return props;
	}

	public void setProps(JsonObject props) {
		this.props = props;
	}

}
