package io.perfwise.onfly.service;

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

	public ThreadGroupService() {
		ThreadGroupService.threadGroup = OnFlyConfig.getThreadGroups();
	}

	public static StandardResponse updateThreads(JsonArray jsonArray) {
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

		try {
			for (int i=0; i<user.getThreadCount() ; i++) {
				if(OnFlyConfig.getJmeterThreadNames().size()>i) {
					threadGroup.stopThread(OnFlyConfig.getJmeterThreadNames().get(i), true);
					OnFlyConfig.removeThreadNamesFromList(OnFlyConfig.getJmeterThreadNames().get(i));
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exception occurred while removing the threads");
		}
	}

	public static void addUsers(User user) {

		if (threadGroup == null) {
			threadGroup = OnFlyConfig.getThreadGroups();
		}

		for (int i = 0; i < user.getThreadCount(); i++) {
			threadGroup.addNewThread(0, OnFlyConfig.getJmeterEngine());
		}
	}

	public static StandardResponse getAllThreadGroupsInfo() {
		try {
			System.out.println("Test");
			return new StandardResponse(StatusResponse.SUCCESS, "User count amended successfully");
		} catch (Exception e) {
			return new StandardResponse(StatusResponse.ERROR, "Error updating user count");
		}
	}

	// Getters and Setters

	public ThreadGroup getThreadGroup() {
		return threadGroup;
	}

	public void setThreadGroup(ThreadGroup threadGroup) {
		ThreadGroupService.threadGroup = threadGroup;
	}

}
