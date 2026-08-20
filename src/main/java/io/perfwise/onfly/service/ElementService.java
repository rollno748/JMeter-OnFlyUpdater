package io.perfwise.onfly.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.perfwise.onfly.config.OnFlyConfig;
import io.perfwise.onfly.model.ElementModel;
import io.perfwise.onfly.rest.StandardResponse;
import io.perfwise.onfly.rest.StatusResponse;
import org.apache.jmeter.assertions.Assertion;
import org.apache.jmeter.config.ConfigElement;
import org.apache.jmeter.gui.tree.JMeterTreeModel;
import org.apache.jmeter.processor.PostProcessor;
import org.apache.jmeter.processor.PreProcessor;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jmeter.timers.Timer;
import org.apache.jorphan.collections.HashTree;
import org.apache.jorphan.collections.HashTreeTraverser;
import org.apache.jorphan.collections.SearchByClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ElementService extends JMeterTreeModel implements HashTreeTraverser, ActionListener {

	private static final long serialVersionUID = 4325902366689818078L;

	private static final Logger LOGGER = LoggerFactory.getLogger(ElementService.class);

	private static JMeterContext context;
	private static Field testPlan;

	public static StandardResponse getTestElementsInfo(String type) {
		try {
			context = OnFlyConfig.getContext();
			testPlan = OnFlyConfig.getTestPlan();
			HashTree testPlanTree = (HashTree) testPlan.get(context.getEngine());
			JsonArray result = new JsonArray();

			switch (type.toLowerCase()) {
				case "listeners":
					collectElementInfo(testPlanTree, ResultCollector.class, "listener", result);
					break;
				case "threadgroups":
					collectElementInfo(testPlanTree, ThreadGroup.class, "threadgroup", result);
					break;
				case "timers":
					collectElementInfo(testPlanTree, Timer.class, "timer", result);
					break;
				case "assertions":
					collectElementInfo(testPlanTree, Assertion.class, "assertion", result);
					break;
				case "config":
					collectElementInfo(testPlanTree, ConfigElement.class, "config", result);
					break;
				case "preprocessors":
					collectElementInfo(testPlanTree, PreProcessor.class, "preprocessor", result);
					break;
				case "postprocessors":
					collectElementInfo(testPlanTree, PostProcessor.class, "postprocessor", result);
					break;
				default:
					return new StandardResponse(StatusResponse.ERROR,
							"Unknown type: " + type + ". Supported: listeners, threadgroups, timers, assertions, config, preprocessors, postprocessors");
			}

			return new StandardResponse(StatusResponse.SUCCESS, result);

		} catch (Exception e) {
			LOGGER.error("Exception occurred on getting element info: {}", e.toString());
			return new StandardResponse(StatusResponse.ERROR, e.toString());
		}
	}

	private static <T> void collectElementInfo(HashTree testPlanTree, Class<T> clazz,
			String typeName, JsonArray result) {
		SearchByClass<T> search = new SearchByClass<>(clazz);
		testPlanTree.traverse(search);
		for (T element : search.getSearchResults()) {
			if (element instanceof TestElement) {
				TestElement te = (TestElement) element;
				JsonObject obj = new JsonObject();
				obj.addProperty("name", te.getName());
				obj.addProperty("type", typeName);
				obj.addProperty("enabled", te.isEnabled());
				result.add(obj);
			}
		}
	}

	public static StandardResponse updateTestElement(JsonArray jsonArray) {
		try {
			context = OnFlyConfig.getContext();
			testPlan = OnFlyConfig.getTestPlan();
			HashTree testPlanTree = (HashTree) testPlan.get(context.getEngine());

			List<String> updated = new ArrayList<>();
			List<String> notFound = new ArrayList<>();

			for (JsonElement jsonElement : jsonArray) {
				ElementModel model = new Gson().fromJson(jsonElement, ElementModel.class);
				if (model.getName() == null || model.getType() == null) continue;

				boolean found = applyToMatchingElement(testPlanTree, model);
				(found ? updated : notFound).add(model.getName());
			}

			JsonObject result = new JsonObject();
			result.add("updated", new Gson().toJsonTree(updated));
			if (!notFound.isEmpty()) {
				result.add("notFound", new Gson().toJsonTree(notFound));
			}
			return new StandardResponse(StatusResponse.SUCCESS, result);

		} catch (Exception e) {
			LOGGER.error("Exception in updateTestElement: {}", e.toString());
			return new StandardResponse(StatusResponse.ERROR, e.toString());
		}
	}

	private static boolean applyToMatchingElement(HashTree testPlanTree, ElementModel model) {
		switch (model.getType().toLowerCase()) {
			case "listener":
				return setEnabledByName(testPlanTree, ResultCollector.class, model);
			case "threadgroup":
				return setEnabledByName(testPlanTree, ThreadGroup.class, model);
			case "timer":
				return setEnabledByName(testPlanTree, Timer.class, model);
			case "assertion":
				return setEnabledByName(testPlanTree, Assertion.class, model);
			case "config":
				return setEnabledByName(testPlanTree, ConfigElement.class, model);
			case "preprocessor":
				return setEnabledByName(testPlanTree, PreProcessor.class, model);
			case "postprocessor":
				return setEnabledByName(testPlanTree, PostProcessor.class, model);
			default:
				LOGGER.warn("Unknown element type: {}", model.getType());
				return false;
		}
	}

	private static <T> boolean setEnabledByName(HashTree testPlanTree, Class<T> clazz,
			ElementModel model) {
		SearchByClass<T> search = new SearchByClass<>(clazz);
		testPlanTree.traverse(search);
		boolean found = false;
		for (T element : search.getSearchResults()) {
			if (element instanceof TestElement) {
				TestElement te = (TestElement) element;
				if (te.getName().equalsIgnoreCase(model.getName())) {
					te.setEnabled(model.isEnabled());
					LOGGER.info("Element '{}' ({}) set enabled={}", model.getName(), model.getType(), model.isEnabled());
					found = true;
				}
			}
		}
		return found;
	}

	@Override
	public void addNode(Object node, HashTree subTree) {}

	@Override
	public void subtractNode() {}

	@Override
	public void processPath() {}

	@Override
	public void actionPerformed(ActionEvent e) {}

}
