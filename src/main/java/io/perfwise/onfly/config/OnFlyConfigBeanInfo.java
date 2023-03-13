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

		PropertyDescriptor p = property("uriPath");
		p.setValue(NOT_UNDEFINED, Boolean.TRUE);
		p.setValue(DEFAULT, "/on-fly");

		p = property("port");
		p.setValue(NOT_UNDEFINED, Boolean.TRUE);
		p.setValue(DEFAULT, "1304");

		p = property("password", TypeEditor.PasswordEditor);
		p.setValue(NOT_UNDEFINED, Boolean.TRUE);
		p.setValue(DEFAULT, "Upd@t3M3");
		
		 if(LOGGER.isDebugEnabled()) {
	            String descriptorsAsString = Arrays.stream(getPropertyDescriptors())
	                    .map(pd -> pd.getName() + "=" + pd.getDisplayName())
	                    .collect(Collectors.joining(" ,"));
	            LOGGER.debug(descriptorsAsString);
	        }
	}

}
