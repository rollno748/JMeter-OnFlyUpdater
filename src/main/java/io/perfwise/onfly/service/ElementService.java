package io.perfwise.onfly.service;

import com.google.gson.JsonArray;
import io.perfwise.onfly.config.OnFlyConfig;
import io.perfwise.onfly.rest.StandardResponse;
import io.perfwise.onfly.rest.StatusResponse;
import org.apache.jmeter.gui.tree.JMeterTreeModel;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jorphan.collections.HashTree;
import org.apache.jorphan.collections.HashTreeTraverser;
import org.apache.jorphan.collections.SearchByClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.HashSet;

public class ElementService extends JMeterTreeModel implements HashTreeTraverser, ActionListener {

	private static final long serialVersionUID = 4325902366689818078L;

	private static final Logger LOGGER = LoggerFactory.getLogger(ElementService.class);

	private static ThreadGroup threadGroup;
	private static JMeterContext context;
	private static Field testPlan;
	private static HashSet<ResultCollector> elementResult;

	public static StandardResponse getTestElementsInfo(String type) {
		try {
			context = OnFlyConfig.getContext();
			testPlan = OnFlyConfig.getTestPlan();
			HashTree testPlanTreeRC = (HashTree) testPlan.get(context.getEngine());
			if(type.equalsIgnoreCase("listeners")){
				SearchByClass<ResultCollector> search= new SearchByClass<>(ResultCollector.class);
				testPlanTreeRC.traverse(search);
				elementResult = new HashSet<>(search.getSearchResults());
			}

			return new StandardResponse(StatusResponse.SUCCESS, elementResult);

		} catch (Exception e) {
			LOGGER.info("Exception occurred on getting element info ::"+ e);
			return new StandardResponse(StatusResponse.ERROR, e.toString());
		}

	}

	public static StandardResponse updateTestElement(JsonArray jsonArray) {
		try {
			context = OnFlyConfig.getContext();
			testPlan = OnFlyConfig.getTestPlan();
			HashTree testPlanTreeRC = (HashTree) testPlan.get(context.getEngine());
			SearchByClass<ResultCollector> search= new SearchByClass<>(ResultCollector.class);
			testPlanTreeRC.traverse(search);
//			Collection<ResultCollector> listeners = search.getSearchResults();
//
//			listeners.forEach(tmpList -> {
//				tmpList.setEnabled(false);
//				});

			return new StandardResponse(StatusResponse.SUCCESS, "Element status updated in Jmeter");

		} catch (Exception e) {
			return new StandardResponse(StatusResponse.ERROR, e.toString());
		}
	}


	@Override
	public void addNode(Object node, HashTree subTree) {
		// TODO Auto-generated method stub
	}

	@Override
	public void subtractNode() {
		// TODO Auto-generated method stub

	}

	@Override
	public void processPath() {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

	// Getters and setters

	public static ThreadGroup getThreadGroup() {
		return threadGroup;
	}

	public static void setThreadGroup(ThreadGroup threadGroup) {
		ElementService.threadGroup = threadGroup;
	}

	public static JMeterContext getContext() {
		return context;
	}

	public static void setContext(JMeterContext context) {
		ElementService.context = context;
	}


}
