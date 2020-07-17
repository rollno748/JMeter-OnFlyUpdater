package io.perfwise.onfly.service;

import java.util.ArrayList;
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
import io.perfwise.onfly.model.TGModel;
import io.perfwise.onfly.model.ThreadGroupsModel;
import io.perfwise.onfly.model.ThreadModel;
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
	
	
	public static StandardResponse updateThreadGroups(JsonArray jsonArray) {

		try {

			for (JsonElement jsonElement : jsonArray) {
				TGModel tgModel = new Gson().fromJson(jsonElement, TGModel.class);
				threadGroup = getAppropriateThreadGroupfromList(tgModel.getThreadGroupName());
				
				if (!(threadGroup == null)) {
					try {
						toggleThreadGroup(tgModel.isSetActive());
					}catch (Exception e) {
						LOGGER.info(String.format("ThreadGroup %s toggled successfully !!", tgModel.getThreadGroupName()));
					}
					
					threadGroup = null;
				}
			}

			return new StandardResponse(StatusResponse.SUCCESS, "Threadgroup toggled successfully");
		} catch (Exception e) {
			return new StandardResponse(StatusResponse.ERROR, "Error in Toggling ThreadGroup Element");
		}

	}

	
	private static void toggleThreadGroup(boolean setActive) {
		OnFlyConfig.setThreadGrp(threadGroup);
		
		if(setActive) {
			OnFlyConfig.setCount(1);
			OnFlyConfig.setAddThread(true);
		}else {
			while (threadGroup.numberOfActiveThreads() != 0) {
				threadGroup.stopThread(getRandomThreadNames(OnFlyConfig.getJmeterThreadNames(), threadGroup.getName()),
						true);
			}
		}
	}


	public static StandardResponse updateThreads(JsonArray jsonArray) {

		context = OnFlyConfig.getContext();

		try {

			for (JsonElement jsonElement : jsonArray) {
				User user = new Gson().fromJson(jsonElement, User.class);

				threadGroup = getAppropriateThreadGroupfromList(user.getThreadGroup());
				if (!(threadGroup == null)) {
					if (user.getAction().equalsIgnoreCase("add")) {
						addUsers(user.getThreadCount());
					} else if (user.getAction().equalsIgnoreCase("remove")) {
						removeUsers(user.getThreadCount(), user.getThreadGroup());
					}
					threadGroup = null;
				}
			}

			return new StandardResponse(StatusResponse.SUCCESS, "User count amended successfully");

		} catch (Exception e) {
			return new StandardResponse(StatusResponse.ERROR, "Error updating user count");
		}

	}

//	public static StandardResponse updateThreadGroups(JsonArray jsonArray) {
//
//		try {
//
//			String returnStatement = null;
//
//			for (JsonElement jsonElement : jsonArray) {
//				TGModel tgModel = new Gson().fromJson(jsonElement, TGModel.class);
//
//				threadGroup = getAppropriateThreadGroupfromList(tgModel.getThreadGroupName());
//				
//				if (!(threadGroup == null)) {
//					threadGroup.setEnabled(tgModel.isSetActive());
//					returnStatement = String.format("ThreadGroup %s Disabled Successfully",
//								tgModel.getThreadGroupName());
//					threadGroup = null;
//				}
//			}
//
//			return new StandardResponse(StatusResponse.SUCCESS, returnStatement);
//
//		} catch (Exception e) {
//			return new StandardResponse(StatusResponse.ERROR, "Error in updating ThreadGroup Element");
//		}
//
//	}

	public static StandardResponse getAllThreads() {
		context = OnFlyConfig.getContext();
		ThreadGroup tg = null;
		JsonArray threadInfoJsonArray = new JsonArray();
		HashSet<ThreadGroup> threadGroupList = OnFlyConfig.getJmeterThreadGroups();

		try {
			Iterator<ThreadGroup> tmp = threadGroupList.iterator();
			while (tmp.hasNext()) {
				ThreadModel threadModel = new ThreadModel();
				tg = tmp.next();
				threadModel.setThreadGroupComment(tg.getComment());
				threadModel.setThreadGroupName(tg.getName());
				threadModel.setThreadNames(getThreadNamesForThreadGroup(tg.getName()));
				threadInfoJsonArray.add(new Gson().toJsonTree(threadModel));
			}
			threadGroupList = null;
			return new StandardResponse(StatusResponse.SUCCESS, threadInfoJsonArray);

		} catch (Exception e) {
			return new StandardResponse(StatusResponse.ERROR, "Error while retrieving the Thread Info");
		}
	}

	private static List<String> getThreadNamesForThreadGroup(String name) {
		List<String> tNames = new ArrayList<String>();

		for (int i = 0; i < OnFlyConfig.getJmeterThreadNames().size(); i++) {
			if (OnFlyConfig.getJmeterThreadNames().get(i).contains(name)) {
				tNames.add(OnFlyConfig.getJmeterThreadNames().get(i));
			}
		}

		return tNames;
	}

	private static void removeUsers(int userCount, String threadGroupName) {

		try {
			for (int i = 0; i < userCount; i++) {
				if (threadGroup.numberOfActiveThreads() >= userCount) {
					threadGroup.stopThread(getRandomThreadNames(OnFlyConfig.getJmeterThreadNames(), threadGroupName),
							true);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exception occurred while removing the threads");
		}
	}

	public static void addUsers(int userCount) {
		OnFlyConfig.setThreadGrp(threadGroup);
		try {
			OnFlyConfig.setCount(userCount);
			OnFlyConfig.setAddThread(true);
		} catch (Exception e) {
			LOGGER.error("Exception occurred while adding the threads");
		}
	}

	public static StandardResponse getAllThreadGroupsInfo() {
		ThreadGroup tg = null;
		int i = 0;
		JsonArray threadGroupsJsonArray = new JsonArray();
		HashSet<ThreadGroup> threadGroupList = OnFlyConfig.getJmeterThreadGroups();
		int size = threadGroupList.size();

		try {
			Iterator<ThreadGroup> tmp = threadGroupList.iterator();
			while (tmp.hasNext()) {
				ThreadGroupsModel tgModel = new ThreadGroupsModel();
				tg = tmp.next();
				tgModel.setThreadGroupComment(tg.getComment());
				tgModel.setThreadGroupDelay(tg.getDelay());
				tgModel.setThreadGroupName(tg.getName());
				tgModel.setNumberOfThreads(tg.getNumberOfThreads());
				tgModel.setThreadGroupRampUp(tg.getRampUp());
				tgModel.setSchedularEnabled(tg.getScheduler());
				tgModel.setThreadGroupNumber(i);

				if (tg.getOnErrorStartNextLoop()) {
					tgModel.setThreadGroupTestAction("StartNextLoop");
				} else if (tg.getOnErrorStopTest()) {
					tgModel.setThreadGroupTestAction("StopTest");
				} else if (tg.getOnErrorStopTestNow()) {
					tgModel.setThreadGroupTestAction("StopTestNow");
				} else if (tg.getOnErrorStopThread()) {
					tgModel.setThreadGroupTestAction("StopThread");
				} else {
					tgModel.setThreadGroupTestAction("Continue");
				}

				threadGroupsJsonArray.add(new Gson().toJsonTree(tgModel));
				i += 1;
			}

			threadGroupList = null;

			return new StandardResponse(StatusResponse.SUCCESS, Integer.toString(size), threadGroupsJsonArray);
		} catch (Exception e) {
			return new StandardResponse(StatusResponse.ERROR, "Error updating user count");
		}
	}

	// returns a random thread name from the associated threadgroup.
	private static String getRandomThreadNames(List<String> threadNames, String threadGroupName) {
		Random rand = new Random();
		String threadName = null;

		do {
			threadName = threadNames.get(rand.nextInt(threadNames.size()));
		} while (!threadName.contains(threadGroupName));

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
