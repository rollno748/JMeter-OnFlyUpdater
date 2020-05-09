package io.perfwise.rest;

import static spark.Spark.get;
import static spark.Spark.init;
import static spark.Spark.port;
import static spark.Spark.stop;

import org.apache.jmeter.threads.JMeterVariables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestServices {

	private static final Logger LOGGER = LoggerFactory.getLogger(RestServices.class);
	private String URIPATH;
	private JMeterVariables vars;

	public RestServices(String uriPath) {
		this.URIPATH = uriPath;

	}

	public void status() {
		get(URIPATH+"/status", (req, res) -> "Hello World");
	}

	public void healthCheck() {
	}

	public static void startRestServer(String port) {
		port(Integer.parseInt(port));
		
		try {
			init();
			LOGGER.info("On-Fly-Updater REST services started:: 0.0.0.0:"+port);
		} catch (Exception e) {
			LOGGER.error("On-Fly-Updater REST services failed to start", e);
		}
	}

	public static void stopRestServer() {
		stop();
	}

}
