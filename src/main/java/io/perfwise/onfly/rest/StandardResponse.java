package io.perfwise.onfly.rest;

import java.util.Map;

import javax.xml.transform.Transformer;

import org.apache.jmeter.threads.JMeterVariables;
import org.apache.jmeter.threads.JMeterContextService.ThreadCounts;

import com.google.gson.JsonArray;

public class StandardResponse {

	private StatusResponse status;
	private String message;
	private Object vars;
	private JsonArray threadsInfo;
	private JMeterVariables variables;
	private ThreadCounts threadCounts;
	private Transformer newTransformer;

	public StandardResponse(StatusResponse status) {
		this.status=status;
	}

	public StandardResponse(StatusResponse status, String message) {
		this.status=status;
		this.message=message;
	}

	public StandardResponse(StatusResponse status, JsonArray data) {
		this.status=status;
		this.threadsInfo=data;
	}
	
	public StandardResponse(StatusResponse status, ThreadCounts threadCounts) {
		this.status=status;
		this.threadCounts=threadCounts;
	}
	
	public StandardResponse(StatusResponse status, Transformer newTransformer) {
		this.status=status;
		this.newTransformer=newTransformer;
	}
	
	public StandardResponse(StatusResponse status, JMeterVariables variables) {
		this.status=status;
		this.variables=variables;
	}
	
	public StandardResponse(StatusResponse status, Object object) {
		this.status=status;
		this.vars=object;
	}
	
	public StandardResponse(StatusResponse status, Map<String, String> vars) {
		this.status=status;
		this.vars=vars;
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

	public JsonArray getThreadsInfo() {
		return threadsInfo;
	}

	public void setThreadsInfo(JsonArray threadsInfo) {
		this.threadsInfo = threadsInfo;
	}

	public JMeterVariables getVariables() {
		return variables;
	}

	public void setVariables(JMeterVariables variables) {
		this.variables = variables;
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

	public Object getVars() {
		return vars;
	}

	public void setVars(Object vars) {
		this.vars = vars;
	}
	
}
