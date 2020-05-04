package io.perfwise.onfly.config;

import org.apache.jmeter.config.ConfigElement;
import org.apache.jmeter.testbeans.TestBean;
import org.apache.jmeter.testbeans.TestBeanHelper;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.testelement.TestStateListener;
import org.apache.jmeter.threads.JMeterVariables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OnFlyConfig extends AbstractTestElement implements ConfigElement, TestStateListener, TestBean {

	private static final long serialVersionUID = 3031594799580611171L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OnFlyConfig.class);
	private String port;
	private String uriPath;
	private String password;

	public OnFlyConfig() {

	}

	public void testStarted() {
		this.setRunningVersion(true);
		TestBeanHelper.prepare(this);
		JMeterVariables variables = getThreadContext().getVariables();
		
		LOGGER.info(variables.toString());
//		if (variables.getObject(BOLT_CONNECTION) != null) {
//			log.error("Bolt connection already exists");
//		} else {
//			synchronized (this) {
//				driver = GraphDatabase.driver(getBoltUri(), AuthTokens.basic(getUsername(), getPassword()));
//				variables.putObject(BOLT_CONNECTION, driver);
//			}
//		}

	}

	public void testStarted(String host) {
		testStarted();
	}

	public void testEnded() {
		synchronized (this) {
            //kill api
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
