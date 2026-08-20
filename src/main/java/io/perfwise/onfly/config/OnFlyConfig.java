package io.perfwise.onfly.config;

import io.perfwise.onfly.rest.RestController;
import io.perfwise.utils.Credentials;
import org.apache.jmeter.config.ConfigElement;
import org.apache.jmeter.engine.DistributedRunner;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.engine.event.LoopIterationListener;
import org.apache.jmeter.testbeans.TestBean;
import org.apache.jmeter.testbeans.TestBeanHelper;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.testelement.TestStateListener;
import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jmeter.threads.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class OnFlyConfig extends AbstractTestElement implements ConfigElement, Serializable, TestStateListener, LoopIterationListener, TestBean {

	private static final long serialVersionUID = 3031594799580611171L;
	private static final Logger LOGGER = LoggerFactory.getLogger(OnFlyConfig.class);

	private String port;
	private String uriPath;
	private String password;
	private static StandardJMeterEngine jmeterEngine;
	private static DistributedRunner distributedRunner;
	private static JMeterContext context;
	private static JMeterThread jmeterThread;
	private RestController restController;
	private static Set<ThreadGroup> jmeterThreadGroups = ConcurrentHashMap.newKeySet();
	private static List<String> jmeterThreadNames = new CopyOnWriteArrayList<>();
	private static ConcurrentHashMap<String, JMeterVariables> threadVariables = new ConcurrentHashMap<>();
	private static ThreadGroup threadGroups;
	private static JMeterVariables variables;
	private static boolean addThread;
	private static int count;
	private static volatile Field testPlan;  // volatile required for double-checked locking (Fix 10)
	private Credentials credentials;


	public void testStarted() {
		this.setRunningVersion(true);
		TestBeanHelper.prepare(this);
		credentials = new Credentials(getPassword());
		restController = new RestController(getUriPath());
		restController.startRestServer(port); // Start REST services APIs
	}

	@Override
	public void iterationStart(LoopIterationEvent loopIterationEvent) {
		JMeterContext currentCtx = JMeterContextService.getContext();
		String threadName = currentCtx.getThread().getThreadName();

		// Fix 4: register thread names on every first-iteration, covers dynamically added threads
		if (!jmeterThreadNames.contains(threadName)) {
			jmeterThreadNames.add(threadName);
		}

		// Fix 3: keep per-thread variable references up to date each iteration
		threadVariables.put(threadName, currentCtx.getVariables());

		if (loopIterationEvent.getIteration() == 1) {
			this.setInitialContext(loopIterationEvent);
		}
		if (isAddThread()) { addThreads(count); }
	}

	private void setInitialContext(LoopIterationEvent loopIterationEvent) {
		context = JMeterContextService.getContext();
		jmeterEngine = context.getEngine();
		jmeterThreadGroups.add((ThreadGroup) context.getThreadGroup());

		if (testPlan == null) {
			synchronized (OnFlyConfig.class) {
				if (testPlan == null) {  // double-checked locking — safe because field is volatile
					try {
						Field f = context.getEngine().getClass().getDeclaredField("test");
						f.setAccessible(true);
						testPlan = f;  // publish after fully initialised
					} catch (NoSuchFieldException | SecurityException e) {
						LOGGER.error("Failed to access JMeter test plan field", e);
					}
				}
			}
		}
	}

	public void testEnded() {
		jmeterThreadGroups.clear();
		jmeterThreadNames.clear();
		threadVariables.clear();
		synchronized (this) {
			try {
				restController.stopRestServer();
			} catch (Exception e) {
				LOGGER.error("On-Fly-Updater REST services failed to stop", e);
			}
		}
	}

	public void testStarted(String host) {
		testStarted();
	}

	public void testEnded(String host) {
		testEnded();
	}

	public void addConfigElement(ConfigElement config) {

	}

	public boolean expectsModification() {
		return false;
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

	public static Set<ThreadGroup> getJmeterThreadGroups() {
		return jmeterThreadGroups;
	}

	public static void setJmeterThreadGroups(Set<ThreadGroup> jmeterThreadGroups) {
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

	public static ConcurrentHashMap<String, JMeterVariables> getThreadVariables() {
		return threadVariables;
	}

}
