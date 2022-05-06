package io.perfwise.onfly.config;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.jmeter.config.ConfigElement;
import org.apache.jmeter.engine.DistributedRunner;
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
	private static StandardJMeterEngine jmeterEngine;
	private static DistributedRunner distributedRunner;
	private static JMeterContext context;
	private static JMeterThread jmeterThread;
	private static HashSet<ThreadGroup> jmeterThreadGroups = new HashSet<>();
	private static List<String> jmeterThreadNames = new ArrayList<String>();
	private static ThreadGroup threadGroups;
	private static JMeterVariables variables;
	private static boolean addThread;
	private static boolean isResultsViewer;
	private static int count;
	private static Field testPlan;


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
		jmeterThreadGroups.clear();
		jmeterThreadNames.clear();

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
			
			context = JMeterContextService.getContext();
			jmeterEngine = context.getEngine();
			jmeterThreadNames.add(context.getThread().getThreadName());
			jmeterThreadGroups.add((ThreadGroup) context.getThreadGroup());
			
			if(testPlan == null) {
				try {
					testPlan = context.getEngine().getClass().getDeclaredField("test");
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				}
				testPlan.setAccessible(true);
			}
		}
		
		if (isAddThread()) {
			addThreads(count);
		}
		
		if(true) {
			//add( res);
			//isResultsViewer()
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
	
	public static void addThreads(int count) {
		setAddThread(false);
		for (int i = 0; i < count; i++) {
			threadGroups.addNewThread(0, jmeterEngine);
		}
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

	public static StandardJMeterEngine getJmeterEngine() {
		return jmeterEngine;
	}

	public static void setJmeterEngine(StandardJMeterEngine jmeterEngine) {
		OnFlyConfig.jmeterEngine = jmeterEngine;
	}

	public static DistributedRunner getDistributedRunner() {
		return distributedRunner;
	}
	public static void setDistributedRunner(DistributedRunner distributedRunner) {
		OnFlyConfig.distributedRunner = distributedRunner;
	}


	public static ThreadGroup getThreadGroups() {
		return threadGroups;
	}

	public static void setThreadGrp(ThreadGroup threadGroups) {
		OnFlyConfig.threadGroups = threadGroups;
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

	public static void updateThreadNameList(String threadName) {
		OnFlyConfig.jmeterThreadNames.remove(new String(threadName));
	}

	public static HashSet<ThreadGroup> getJmeterThreadGroups() {
		return jmeterThreadGroups;
	}

	public static void setJmeterThreadGroups(HashSet<ThreadGroup> jmeterThreadGroups) {
		OnFlyConfig.jmeterThreadGroups = jmeterThreadGroups;
	}

	public static boolean isAddThread() {
		return addThread;
	}

	public static void setAddThread(boolean addThread) {
		OnFlyConfig.addThread = addThread;
	}
	
	public static int getCount() {
		return count;
	}

	public static void setCount(int count) {
		OnFlyConfig.count = count;
	}
	
	public static JMeterVariables getVariables() {
		return variables;
	}

	public static void setVariables(JMeterVariables vars) {
		OnFlyConfig.variables = vars;
	}

	public static Field getTestPlan() {
		return testPlan;
	}

	public static void setTestPlan(Field testPlan) {
		OnFlyConfig.testPlan = testPlan;
	}
	
	public static boolean isResultsViewer() {
		return isResultsViewer;
	}

	public static void setResultsViewer(boolean isResultsViewer) {
		OnFlyConfig.isResultsViewer = isResultsViewer;
	}

	
}
