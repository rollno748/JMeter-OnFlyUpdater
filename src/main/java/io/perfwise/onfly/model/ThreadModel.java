package io.perfwise.onfly.model;

import java.util.List;

public class ThreadModel {

	String ThreadGroupName;
	String ThreadGroupComment;
	List<String> threadNames;

	public String getThreadGroupName() {
		return ThreadGroupName;
	}

	public void setThreadGroupName(String threadGroupName) {
		ThreadGroupName = threadGroupName;
	}

	public List<String> getThreadNames() {
		return threadNames;
	}

	public void setThreadNames(List<String> threadNames) {
		this.threadNames = threadNames;
	}

	public String getThreadGroupComment() {
		return ThreadGroupComment;
	}

	public void setThreadGroupComment(String threadGroupComment) {
		ThreadGroupComment = threadGroupComment;
	}
	
}
