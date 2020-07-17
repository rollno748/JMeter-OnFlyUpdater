package io.perfwise.onfly.service;

import io.perfwise.onfly.rest.StandardResponse;
import io.perfwise.onfly.rest.StatusResponse;

public class AggregateService {

	
	public static StandardResponse currentResults() {

		try {

			return new StandardResponse(StatusResponse.SUCCESS, "ThreadGroup toggled successfully");

		} catch (Exception e) {
			return new StandardResponse(StatusResponse.ERROR, "Error in Toggling ThreadGroup Element");
		}

	}

}
