package io.perfwise.onfly.service;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.ThreadGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import io.perfwise.onfly.config.OnFlyConfig;
import io.perfwise.onfly.model.User;
import io.perfwise.onfly.rest.StandardResponse;
import io.perfwise.onfly.rest.StatusResponse;

public class ThreadGroupService extends ThreadGroup {

	private static final long serialVersionUID = 2380671011782469721L;
	private static final Logger LOGGER = LoggerFactory.getLogger(ThreadGroupService.class);

	private static ThreadGroup threadGroup;
	private static JMeterContext context;

	public ThreadGroupService() {
		this.setContext(OnFlyConfig.getContext());
	}

	public static StandardResponse updateThreads(JsonArray jsonArray) {

		context = OnFlyConfig.getContext();

		try {

			for (JsonElement jsonElement : jsonArray) {
				User user = new Gson().fromJson(jsonElement, User.class);

				threadGroup = getAppropriateThreadGroupfromList(user.getThreadGroup());
				if (!(threadGroup == null)) {
					if (user.getAction().equalsIgnoreCase("add")) {
						addUsers(user);
					} else if (user.getAction().equalsIgnoreCase("remove")) {
						removeUsers(user);
					}
					threadGroup = null;
				}
			}

			return new StandardResponse(StatusResponse.SUCCESS, "User count amended successfully");

		} catch (Exception e) {
			return new StandardResponse(StatusResponse.ERROR, "Error updating user count");
		}

	}

	private static void removeUsers(User user) {

		try {
			for (int i = 0; i < user.getThreadCount(); i++) {
				if (threadGroup.numberOfActiveThreads() >= user.getThreadCount()) {
					threadGroup.stopThread(getRandomThreadNames(OnFlyConfig.getJmeterThreadNames(), user), true);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exception occurred while removing the threads");
		}
	}

	public static void addUsers(User user) {
		OnFlyConfig.setThreadGrp(threadGroup);
		try {
			OnFlyConfig.setCount(user.getThreadCount());
			OnFlyConfig.setAddThread(true);
			//OnFlyConfig.addThreads(user.getThreadCount());
		} catch (Exception e) {
			LOGGER.error("Exception occurred while adding the threads");
		}
	}

	public static StandardResponse getAllThreadGroupsInfo() {
		try {

			ThreadGroup threadGroups = (ThreadGroup) OnFlyConfig.getContext().getThreadGroup();
			return new StandardResponse(StatusResponse.SUCCESS, threadGroups.getName());
		} catch (Exception e) {
			return new StandardResponse(StatusResponse.ERROR, "Error updating user count");
		}
	}

	// returns a random thread name from the associated threadgroup.
	private static String getRandomThreadNames(List<String> threadNames, User user) {
		Random rand = new Random();
		String threadName = null;

		do {
			threadName = threadNames.get(rand.nextInt(threadNames.size()));
		} while (!threadName.contains(user.getThreadGroup()));

		OnFlyConfig.updateThreadNameList(threadName);
		LOGGER.info(String.format("Thread going to be stopped is %s", threadName));
		return threadName;
	}

	private static ThreadGroup getAppropriateThreadGroupfromList(String tgName) {
		threadGroup = null;
		ThreadGroup tg;
		HashSet<ThreadGroup> threadGroupList = OnFlyConfig.getJmeterThreadGroups();

		Iterator<ThreadGroup> tmp = threadGroupList.iterator();
		while (tmp.hasNext()) {
			tg = tmp.next();

			if (tg.getName().toLowerCase().equals(tgName.toLowerCase())) {
				threadGroup = tg;
				tg = null;
			}
		}

		return threadGroup;
	}

	// Getters and Setters

	public JMeterContext getContext() {
		return context;
	}

	public void setContext(JMeterContext context) {
		ThreadGroupService.context = OnFlyConfig.getContext();
	}

}
