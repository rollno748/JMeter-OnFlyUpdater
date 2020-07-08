package io.perfwise.onfly.rest;

import java.util.Collection;
import java.util.Map;

import javax.xml.transform.Transformer;

import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.threads.JMeterContextService.ThreadCounts;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class StandardResponse {

	private StatusResponse status;
	private String message;
	private String TotalThreadGroups;
	private Map<String, String> vars;
	private Object slaves;
	private JsonArray threadsInfo;
	private JsonArray threadGroups;
	private JsonObject variables;
	private ThreadCounts threadCounts;
	private Transformer newTransformer;
	private Collection<ResultCollector> listeners;
	


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
	
	public StandardResponse(StatusResponse status, String size, JsonArray threadGroupsJsonArray) {
		this.status=status;
		this.TotalThreadGroups=size;
		this.threadGroups=threadGroupsJsonArray;
	}
	
	public StandardResponse(StatusResponse status, JsonObject variableObj) {
		this.status=status;
		this.variables=variableObj;
	}
	
	public StandardResponse(StatusResponse status, Object object) {
		this.status=status;
		this.slaves=object;
	}
	
	public StandardResponse(StatusResponse status, Collection<ResultCollector> listeners) {
		this.status=status;
		this.listeners=listeners;
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

	public Map<String, String> getVars() {
		return vars;
	}

	public void setVars(Map<String, String> vars) {
		this.vars = vars;
	}

	public Object getSlaves() {
		return slaves;
	}

	public void setSlaves(Object slaves) {
		this.slaves = slaves;
	}

	public JsonArray getThreadsInfo() {
		return threadsInfo;
	}

	public void setThreadsInfo(JsonArray threadsInfo) {
		this.threadsInfo = threadsInfo;
	}

	public JsonArray getThreadGroups() {
		return threadGroups;
	}

	public void setThreadGroups(JsonArray threadGroups) {
		this.threadGroups = threadGroups;
	}

	public JsonObject getVariables() {
		return variables;
	}

	public void setVariables(JsonObject variables) {
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

	public String getTotalThreadGroups() {
		return TotalThreadGroups;
	}

	public void setTotalThreadGroups(String totalThreadGroups) {
		TotalThreadGroups = totalThreadGroups;
	}
	
	public Collection<ResultCollector> getListeners() {
		return listeners;
	}

	public void setListeners(Collection<ResultCollector> listeners) {
		this.listeners = listeners;
	}
	
}
