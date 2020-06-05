package io.perfwise.onfly.config;

import org.apache.jmeter.config.ConfigElement;
import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.engine.event.LoopIterationListener;
import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.samplers.SampleListener;
import org.apache.jmeter.testbeans.TestBean;
import org.apache.jmeter.testbeans.TestBeanHelper;
import org.apache.jmeter.testelement.TestStateListener;
import org.apache.jmeter.threads.AbstractThreadGroup;
import org.apache.jmeter.threads.JMeterContextService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.perfwise.rest.RestServices;
import io.perfwise.utils.Credentials;

public class OnFlyConfig extends ConfigTestElement
		implements ConfigElement, TestStateListener, TestBean, SampleListener, LoopIterationListener {

	private static final long serialVersionUID = 3031594799580611171L;
	private static final Logger LOGGER = LoggerFactory.getLogger(OnFlyConfig.class);

	private String port;
	private String uriPath;
	private String password;
	private static StandardJMeterEngine jmeterEngine;
	private static AbstractThreadGroup threadGrp;

	public void testStarted() {
		this.setRunningVersion(true);
		TestBeanHelper.prepare(this);
		new Credentials(getPassword());
		new RestServices(getUriPath());

		// Start Spark REST services
		RestServices.startRestServer(port);
		RestServices.loadServices();

	}

	public void testStarted(String host) {
		testStarted();
	}

	public void testEnded() {
		synchronized (this) {
			try {
				RestServices.stopRestServer();
			} catch (Exception e) {
				LOGGER.error("On-Fly-Updater REST services failed to stop", e);
			}

		}
	}

	public void testEnded(String host) {
		testEnded();
	}

	public void addConfigElement(ConfigElement config) {

	}

	public boolean expectsModification() {
		return false;
	}

	@Override
	public void iterationStart(LoopIterationEvent iterEvent) {
		if(iterEvent.getIteration() <= 1) {
            jmeterEngine = JMeterContextService.getContext().getEngine();
        }
		
		threadGrp = JMeterContextService.getContext().getThreadGroup();
		

	}

	@Override
	public void sampleOccurred(SampleEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sampleStarted(SampleEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sampleStopped(SampleEvent e) {
		// TODO Auto-generated method stub

	}

	// Getter and Setters

	public String getPort() {
		return port;
	}

	public void setPort(String port) {

		if (port.isEmpty()) {
			this.port = "4567";
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

	public void setVariables(String vars) {

	}
	
	public static StandardJMeterEngine getJmeterEngine() {
		return jmeterEngine;
	}

	public static void setJmeterEngine(StandardJMeterEngine jmeterEngine) {
		OnFlyConfig.jmeterEngine = jmeterEngine;
	}

	public static AbstractThreadGroup getThreadGrp() {
		return threadGrp;
	}

	public static void setThreadGrp(AbstractThreadGroup threadGrp) {
		OnFlyConfig.threadGrp = threadGrp;
	}
	
	

}
