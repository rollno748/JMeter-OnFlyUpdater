package io.perfwise.onfly.model;

public class ThreadGroupsModel {

	int threadGroupNumber;
	String threadGroupName;
	int numberOfThreads;
    String threadGroupComment;
    long threadGroupDelay;
    int threadGroupRampUp;
    boolean schedularEnabled;
    String testActionOnError;
    
	public int getThreadGroupNumber() {
		return threadGroupNumber;
	}
	public void setThreadGroupNumber(int threadGroupNumber) {
		this.threadGroupNumber = threadGroupNumber;
	}
	public String getThreadGroupName() {
		return threadGroupName;
	}
	public void setThreadGroupName(String threadGroupName) {
		this.threadGroupName = threadGroupName;
	}
	public int getNumberOfThreads() {
		return numberOfThreads;
	}
	public void setNumberOfThreads(int numberOfThreads) {
		this.numberOfThreads = numberOfThreads;
	}
	public String getThreadGroupComment() {
		return threadGroupComment;
	}
	public void setThreadGroupComment(String threadGroupComment) {
		this.threadGroupComment = threadGroupComment;
	}
	public long getThreadGroupDelay() {
		return threadGroupDelay;
	}
	public void setThreadGroupDelay(long delay) {
		this.threadGroupDelay = delay;
	}
	public int getThreadGroupRampUp() {
		return threadGroupRampUp;
	}
	public void setThreadGroupRampUp(int rampup) {
		this.threadGroupRampUp = rampup;
	}
	public boolean getSchedularEnabled() {
		return schedularEnabled;
	}
	public void setSchedularEnabled(boolean enabled) {
		this.schedularEnabled = enabled;
	}
	public String getThreadGroupTestAction() {
		return testActionOnError;
	}
	public void setThreadGroupTestAction(String threadGroupTestAction) {
		this.testActionOnError = threadGroupTestAction;
	}

}
