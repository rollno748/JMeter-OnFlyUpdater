package io.perfwise.service;

import org.apache.jmeter.threads.JMeterContextService;

import io.perfwise.rest.StandardResponse;
import io.perfwise.rest.StatusResponse;

public class TestService {

	public static StandardResponse getOverallTestInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	public static StandardResponse getStatus() {
		try {
			return new StandardResponse(StatusResponse.SUCCESS, JMeterContextService.getThreadCounts());
		} catch (Exception e) {
			return new StandardResponse(StatusResponse.ERROR, e.toString());
		}
	}
	
	

}
