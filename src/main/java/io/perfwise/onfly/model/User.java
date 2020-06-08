package io.perfwise.onfly.model;

public class User {

	private String threadGroup;
	private String action;
	private int threadCount;

	public String getThreadGroup() {
		return threadGroup;
	}

	public void setThreadGroup(String threadGroup) {
		this.threadGroup = threadGroup;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public int getThreadCount() {
		return threadCount;
	}

	public void setThreadCount(int threadCount) {
		this.threadCount = threadCount;
	}

}