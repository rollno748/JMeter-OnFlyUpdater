package io.perfwise.onfly.service;

import io.perfwise.onfly.config.OnFlyConfig;


public class ThreadGroupHandler extends OnFlyConfig {

	private static final long serialVersionUID = -989418731318270980L;
	//private static final Logger LOGGER = LoggerFactory.getLogger(ThreadGroupHandler.class);
/*
	public static Object update(User user) {

		if (user.getType().equalsIgnoreCase("add")) {
			addUsers(user);
		} else if (user.getType().equalsIgnoreCase("remove")) {
			removeUsers(user);

		} else {
			return new StandardResponse(StatusResponse.AUTHERROR, "Invalid Credentials");
		}
		return user;
	}

	public static Object addUsers(User user) {
		
		for(int i=1; i<=user.getUsersCount(); i++) {
			AbstractThreadGroup tg = OnFlyConfig.getThreadGrp();
			tg.addNewThread(0, OnFlyConfig.getJmeterEngine());
			LOGGER.info("Adding thread");
		}
		return "Success";
	}
	

	public static Object removeUsers(User user) {
		
		for(int i=user.getUsersCount(); i<=1; i--) {
			AbstractThreadGroup tg = OnFlyConfig.getThreadGrp();
			tg.addNewThread(0, OnFlyConfig.getJmeterEngine());
			LOGGER.info("Removing thread");
		}
		return "Success";
	}
	
	*/

}