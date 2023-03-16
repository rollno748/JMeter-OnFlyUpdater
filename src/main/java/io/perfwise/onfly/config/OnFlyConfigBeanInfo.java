package io.perfwise.onfly.config;

import org.apache.jmeter.testbeans.BeanInfoSupport;
import org.apache.jmeter.testbeans.gui.TypeEditor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.stream.Collectors;

public class OnFlyConfigBeanInfo extends BeanInfoSupport {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OnFlyConfigBeanInfo.class);

	public OnFlyConfigBeanInfo() {
		super(OnFlyConfig.class);
		createPropertyGroup("connection", new String[] { "uriPath", "port", "password" });

		PropertyDescriptor propertyDescriptor = property("uriPath");
		propertyDescriptor.setValue(NOT_UNDEFINED, Boolean.TRUE);
		propertyDescriptor.setValue(DEFAULT, "/on-fly");

		propertyDescriptor = property("port");
		propertyDescriptor.setValue(NOT_UNDEFINED, Boolean.TRUE);
		propertyDescriptor.setValue(DEFAULT, "1304");

		propertyDescriptor = property("password", TypeEditor.PasswordEditor);
		propertyDescriptor.setValue(NOT_UNDEFINED, Boolean.TRUE);
		propertyDescriptor.setValue(DEFAULT, "Upd@t3M3");
		
		if(LOGGER.isDebugEnabled()) {
			String descriptorsAsString = Arrays.stream(getPropertyDescriptors())
					.map(pd -> pd.getName() + "=" + pd.getDisplayName())
					.collect(Collectors.joining(" ,"));
			LOGGER.debug(descriptorsAsString);
		}
	}

}
