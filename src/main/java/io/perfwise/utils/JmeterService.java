package io.perfwise.utils;


import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.ThreadGroup;

public class JmeterService {
	
	private ThreadGroup tg;
	
	public boolean addNewThread (int delay, StandardJMeterEngine jEngine) {
		jEngine = JMeterContextService.getContext().getEngine();
		JMeterContextService.addTotalThreads(1);
		tg = (ThreadGroup) JMeterContextService.getContext().getThreadGroup();
		tg.addNewThread(delay, jEngine);
		return true;
	}
    

}
