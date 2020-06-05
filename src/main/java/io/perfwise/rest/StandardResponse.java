package io.perfwise.rest;

import org.apache.jmeter.threads.JMeterContextService.ThreadCounts;

import com.google.gson.JsonElement;

public class StandardResponse {

	private StatusResponse status;
	private String message;
	private JsonElement data;
	private ThreadCounts threadCounts;

	public StandardResponse(StatusResponse status) {
		this.status=status;
		//return new Gson().toJsonTree(StatusResponse.SUCCESS, message);
	}

	public StandardResponse(StatusResponse status, String message) {
		this.status=status;
		this.message=message;
		//return new Gson().toJsonTree(StatusResponse.SUCCESS, message);
	}

	public StandardResponse(StatusResponse status, JsonElement data) {
		this.status=status;
		this.data=data;
	}

	// Getters and setters

	public StandardResponse(StatusResponse status, ThreadCounts threadCounts) {
		this.status=status;
		this.threadCounts=threadCounts;
	}

	public StatusResponse getStatus() {
		return status;
	}

	public void setStatus(StatusResponse status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public JsonElement getData() {
		return data;
	}

	public void setData(JsonElement data) {
		this.data = data;
	}

	public ThreadCounts getThreadCounts() {
		return threadCounts;
	}

	public void setThreadCounts(ThreadCounts threadCounts) {
		this.threadCounts = threadCounts;
	}
	

}
