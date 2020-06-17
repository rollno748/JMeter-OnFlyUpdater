package io.perfwise.onfly.rest;

import javax.xml.transform.Transformer;

import org.apache.jmeter.threads.JMeterContextService.ThreadCounts;

import com.google.gson.JsonElement;

public class StandardResponse {

	private StatusResponse status;
	private String message;
	private JsonElement data;
	private ThreadCounts threadCounts;
	private Transformer newTransformer;

	public StandardResponse(StatusResponse status) {
		this.status=status;
	}

	public StandardResponse(StatusResponse status, String message) {
		this.status=status;
		this.message=message;
	}

	public StandardResponse(StatusResponse status, JsonElement data) {
		this.status=status;
		this.data=data;
	}
	
	public StandardResponse(StatusResponse status, ThreadCounts threadCounts) {
		this.status=status;
		this.threadCounts=threadCounts;
	}
	
	public StandardResponse(StatusResponse status, Transformer newTransformer) {
		this.status=status;
		this.newTransformer=newTransformer;
	}


	// Getters and setters

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
	
	public Transformer getNewTransformer() {
		return newTransformer;
	}

	public void setNewTransformer(Transformer newTransformer) {
		this.newTransformer = newTransformer;
	}

}
