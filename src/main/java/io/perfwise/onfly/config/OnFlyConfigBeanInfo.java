package io.perfwise.onfly.config;

import java.beans.PropertyDescriptor;

import org.apache.jmeter.testbeans.BeanInfoSupport;
import org.apache.jmeter.testbeans.gui.TypeEditor;

public class OnFlyConfigBeanInfo extends BeanInfoSupport {

	public OnFlyConfigBeanInfo() {
		super(OnFlyConfig.class);

		createPropertyGroup("connection", new String[] { "uriPath", "port", "password" });

		PropertyDescriptor p = property("uriPath");
		p.setValue(NOT_UNDEFINED, Boolean.TRUE);
		p.setValue(DEFAULT, "/on-fly");

		p = property("port");
		p.setValue(NOT_UNDEFINED, Boolean.TRUE);
		p.setValue(DEFAULT, "1304");

		p = property("password", TypeEditor.PasswordEditor);
		p.setValue(NOT_UNDEFINED, Boolean.TRUE);
		p.setValue(DEFAULT, "Upd@t3M3");

	}

}
