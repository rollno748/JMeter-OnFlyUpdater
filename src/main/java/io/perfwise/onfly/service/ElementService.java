package io.perfwise.onfly.service;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.Collection;

import org.apache.jmeter.control.Controller;
import org.apache.jmeter.control.GenericController;
import org.apache.jmeter.gui.tree.JMeterTreeModel;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jorphan.collections.HashTree;
import org.apache.jorphan.collections.HashTreeTraverser;
import org.apache.jorphan.collections.SearchByClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.perfwise.onfly.config.OnFlyConfig;
import io.perfwise.onfly.model.Element;
import io.perfwise.onfly.rest.StandardResponse;
import io.perfwise.onfly.rest.StatusResponse;

public class ElementService extends JMeterTreeModel implements HashTreeTraverser, ActionListener {

	private static final long serialVersionUID = 4325902366689818078L;

	private static final Logger LOGGER = LoggerFactory.getLogger(ElementService.class);

	private static ThreadGroup threadGroup;
	private static JMeterContext context;
	private static Field testPlan;

	// GuiPackage
	// JMeterGUIComponent getGui

	public static StandardResponse getTestElementsInfo() {
		try {
			context = OnFlyConfig.getContext();
			testPlan = OnFlyConfig.getTestPlan();
			HashTree testPlanTreeRC = (HashTree) testPlan.get(context.getEngine());
			SearchByClass<ResultCollector> search= new SearchByClass<>(ResultCollector.class);
			testPlanTreeRC.traverse(search);
			Collection<ResultCollector> listeners = search.getSearchResults();

			return new StandardResponse(StatusResponse.SUCCESS, listeners);

		} catch (Exception e) {
			return new StandardResponse(StatusResponse.ERROR, e.toString());
		}

	}

	public static StandardResponse updateTestElement(Element element) {
		try {

			context = OnFlyConfig.getContext();
			JMeterTreeModel jTreeModel = new JMeterTreeModel();
			HashTree jMeterTestPlanModel = jTreeModel.getTestPlan();

			LOGGER.info("Tree model :: " + jMeterTestPlanModel);

			JMeterContextService.getContext().getThreadGroup().setEnabled(false);

			Controller controller = new GenericController();
			controller.addTestElement(controller);

			return new StandardResponse(StatusResponse.SUCCESS, "Element status updated in Jmeter");

		} catch (Exception e) {
			return new StandardResponse(StatusResponse.ERROR, e.toString());
		}
	}

	public void disableElement() {

	}

	public void enableElement() {

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
