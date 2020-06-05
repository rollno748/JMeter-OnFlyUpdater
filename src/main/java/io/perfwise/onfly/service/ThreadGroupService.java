package io.perfwise.onfly.service;

import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.threads.AbstractThreadGroup;
import org.apache.jmeter.threads.ThreadGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.perfwise.onfly.config.OnFlyConfig;
import io.perfwise.onfly.model.User;

public class ThreadGroupService extends ThreadGroup {

	private static final long serialVersionUID = -989418731318270980L;
	private static final Logger LOGGER = LoggerFactory.getLogger(ThreadGroupService.class);

	public static Object update(User user) {

		if (user.getType().equalsIgnoreCase("add")) {
			addUsers(user);
		} else if (user.getType().equalsIgnoreCase("remove")) {
			removeUsers(user);

		} else {
			return "{\"message\": \"request body error\"}";
		}
		return user;
	}

	public static Object addUsers(User user) {

		for (int i = 1; i <= user.getUsersCount(); i++) {
			AbstractThreadGroup tg = OnFlyConfig.getThreadGrp();
			tg.addNewThread(0, OnFlyConfig.getJmeterEngine());
			LOGGER.info("Adding thread");
		}
		return "Success";
	}

	public static Object removeUsers(User user) {

		for (int i = user.getUsersCount(); i <= 1; i--) {
			AbstractThreadGroup tg = OnFlyConfig.getThreadGrp();
			tg.addNewThread(0, OnFlyConfig.getJmeterEngine());
			LOGGER.info("Removing thread");
		}
		return "Success";
	}

	public static Object stopTest(String action) {
		try {
			StandardJMeterEngine engine = OnFlyConfig.getJmeterEngine();

			if (action.toLowerCase().equals("shutdown")) {
				engine.askThreadsToStop();
			}
			engine.stopTest(true);
			return true;
		} catch (Exception e) {
			LOGGER.info("Exception :: " + e);
			return false;
		}

	}

}
