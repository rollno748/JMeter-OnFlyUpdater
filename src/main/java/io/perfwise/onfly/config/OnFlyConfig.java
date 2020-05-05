package io.perfwise.onfly.config;

import org.apache.jmeter.config.ConfigElement;
import org.apache.jmeter.testbeans.TestBean;
import org.apache.jmeter.testbeans.TestBeanHelper;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.testelement.TestStateListener;
import org.apache.jmeter.threads.JMeterVariables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.perfwise.rest.RestServices;


public class OnFlyConfig extends AbstractTestElement implements ConfigElement, TestStateListener, TestBean {

	private static final long serialVersionUID = 3031594799580611171L;
	private static final Logger LOGGER = LoggerFactory.getLogger(OnFlyConfig.class);
	
	private String port;
	private String uriPath;
	private String password;
	

	public void testStarted() {
		this.setRunningVersion(true);
		TestBeanHelper.prepare(this);
		JMeterVariables variables = getThreadContext().getVariables();
		new RestServices(getUriPath(), variables);
		//Start Spark REST services
		RestServices.startRestServer(getPort());
	}


	public void testStarted(String host) {
		testStarted();
	}

	public void testEnded() {
		synchronized (this) {
			try {
				RestServices.stopRestServer();
			}catch (Exception e) {
				LOGGER.error("On-Fly-Updater REST services failed to stop", e);
			}
            
        }
	}

	public void testEnded(String host) {
		testEnded();
	}

	public void addConfigElement(ConfigElement config) {
		// TODO Auto-generated method stub
	}

	public boolean expectsModification() {
		return false;
	}

	// Getter and Setters 
	
	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		
		if(port.isEmpty()) {
			this.port="4567";
		}	
		this.port = port;
	}

	public String getUriPath() {
		return uriPath;
	}

	public void setUriPath(String uriPath) {
		this.uriPath = uriPath;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	

}
