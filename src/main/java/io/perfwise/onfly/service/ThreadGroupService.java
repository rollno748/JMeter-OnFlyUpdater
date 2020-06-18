package io.perfwise.onfly.service;

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
				if (user.getAction().equalsIgnoreCase("add")) {
					addUsers(user);
				} else if (user.getAction().equalsIgnoreCase("remove")) {
					removeUsers(user);
				}
			}

			return new StandardResponse(StatusResponse.SUCCESS, "User count amended successfully");

		} catch (Exception e) {
			return new StandardResponse(StatusResponse.ERROR, "Error updating user count");
		}
		
	}

	private static void removeUsers(User user) {

		if (threadGroup == null) {
			threadGroup = (ThreadGroup) context.getThreadGroup();
		}

		try {
			for (int i = 0; i < user.getThreadCount(); i++) {

				if (threadGroup.numberOfActiveThreads() >= user.getThreadCount()) {
					threadGroup.stopThread(getRandomThreadNames(OnFlyConfig.getJmeterThreadNames()), true);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exception occurred while removing the threads");
		}
		
		threadGroup=null;
	}

	public static void addUsers(User user) {

		if (threadGroup == null) {
			threadGroup = (ThreadGroup) context.getThreadGroup();
		}

		for (int i = 0; i < user.getThreadCount(); i++) {
			threadGroup.setNumThreads(user.getThreadCount());
			threadGroup.addNewThread(0, OnFlyConfig.getJmeterEngine());
		}
		threadGroup=null;
	}

	public static StandardResponse getAllThreadGroupsInfo() {
		try {

			ThreadGroup threadGroups = (ThreadGroup) OnFlyConfig.getContext().getThreadGroup();
			return new StandardResponse(StatusResponse.SUCCESS, threadGroups.getName());
		} catch (Exception e) {
			return new StandardResponse(StatusResponse.ERROR, "Error updating user count");
		}
	}

	public ThreadGroup getThreadGroupInfo() {

		return null;
	}

	private static String getRandomThreadNames(List<String> threadNames) {
		Random rand = new Random();
        String threadName = threadNames.get(rand.nextInt(threadNames.size()));
		OnFlyConfig.updateThreadNameList(threadName);
		return threadName;
	}

	// Getters and Setters

	public JMeterContext getContext() {
		return context;
	}

	public void setContext(JMeterContext context) {
		ThreadGroupService.context = OnFlyConfig.getContext();
	}

}
