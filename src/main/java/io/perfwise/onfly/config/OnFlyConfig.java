package io.perfwise.onfly.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.jmeter.config.ConfigElement;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.engine.event.LoopIterationListener;
import org.apache.jmeter.testbeans.TestBean;
import org.apache.jmeter.testbeans.TestBeanHelper;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.testelement.TestStateListener;
import org.apache.jmeter.testelement.ThreadListener;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterThread;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.jmeter.threads.ThreadGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.perfwise.onfly.rest.RestController;
import io.perfwise.utils.Credentials;

public class OnFlyConfig extends AbstractTestElement implements ConfigElement, Serializable, Cloneable, ThreadListener,
		TestStateListener, LoopIterationListener, TestBean {

	private static final long serialVersionUID = 3031594799580611171L;
	private static final Logger LOGGER = LoggerFactory.getLogger(OnFlyConfig.class);

	private String port;
	private String uriPath;
	private String password;
	private static boolean threadUpdateBoolean;
	private static StandardJMeterEngine jmeterEngine;
	private static JMeterContext context;
	private static JMeterThread jmeterThread;
	private static HashSet<ThreadGroup> threadGroupsList = new HashSet<>();
	private static List<String> jmeterThreadNames = new ArrayList<>();
	private static ThreadGroup threadGroups;
	private static JMeterVariables vars;

	public void testStarted() {
		this.setRunningVersion(true);
		TestBeanHelper.prepare(this);
		new Credentials(getPassword());
		new RestController(getUriPath());

		// Start Spark REST services
		RestController.startRestServer(port);
		RestController.loadServices();

	}

	public void testStarted(String host) {
		testStarted();
	}

	public void testEnded() {
		synchronized (this) {
			try {
				RestController.stopRestServer();
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

		if (iterEvent.getIteration() == 1) {
			jmeterEngine = JMeterContextService.getContext().getEngine();
			context = JMeterContextService.getContext();
			threadGroups = (ThreadGroup) JMeterContextService.getContext().getThreadGroup();
			threadGroups.getProperty(NAME);
			jmeterThread = JMeterContextService.getContext().getThread();
			jmeterThreadNames.add(JMeterContextService.getContext().getThread().getThreadName());
			vars = JMeterContextService.getContext().getVariables();
			OnFlyConfig.threadGroupsList.add(threadGroups);
		}
	}

	@Override
	public void threadStarted() {
		// TODO Auto-generated method stub

	}

	@Override
	public void threadFinished() {
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

	public static ThreadGroup getThreadGroups() {
		return threadGroups;
	}

	public static void setThreadGrp(ThreadGroup threadGroups) {
		OnFlyConfig.threadGroups = threadGroups;
	}

	public static JMeterVariables getVars() {
		return vars;
	}

	public static void setVars(JMeterVariables vars) {
		OnFlyConfig.vars = vars;
	}

	public static boolean isThreadUpdateBoolean() {
		return threadUpdateBoolean;
	}

	public static void setThreadUpdateBoolean(boolean threadUpdateBoolean) {
		OnFlyConfig.threadUpdateBoolean = threadUpdateBoolean;
	}

	public static JMeterContext getContext() {
		return context;
	}

	public static void setContext(JMeterContext context) {
		OnFlyConfig.context = context;
	}

	public static JMeterThread getJmeterThread() {
		return jmeterThread;
	}

	public static void setJmeterThread(JMeterThread jmeterThread) {
		OnFlyConfig.jmeterThread = jmeterThread;
	}

	public static List<String> getJmeterThreadNames() {
		return jmeterThreadNames;
	}

	public static void setJmeterThreadNames(List<String> jmeterThreadNames) {
		OnFlyConfig.jmeterThreadNames = jmeterThreadNames;
	}

	public static void setThreadGroups(ThreadGroup threadGroups) {
		OnFlyConfig.threadGroups = threadGroups;
	}

	public static void removeThreadNamesFromList(String threadName) {
		OnFlyConfig.jmeterThreadNames.remove(threadName);
	}

	public static HashSet<ThreadGroup> getThreadGroupsList() {
		return threadGroupsList;
	}

	public static void setThreadGroupsList(HashSet<ThreadGroup> threadGroupsList) {
		OnFlyConfig.threadGroupsList = threadGroupsList;
	}

	

}
