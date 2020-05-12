package io.perfwise.rest;

import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.init;
import static spark.Spark.path;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.put;
import static spark.Spark.stop;

import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.util.JMeterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.perfwise.onfly.config.OnFlyConfig;
import io.perfwise.utils.Credentials;
import io.perfwise.utils.JsonHelper;
import io.perfwise.rest.controller.User;
import io.perfwise.rest.controller.UsersHandler;

public class RestServices extends OnFlyConfig {

	private static final long serialVersionUID = -6517381958520629644L;

	private static final Logger LOGGER = LoggerFactory.getLogger(RestServices.class);

	private static String UriPath;
	private static StandardJMeterEngine engine;

	public RestServices(String UriPath) {
		RestServices.UriPath = UriPath;
	}

	public void healthCheck() {
	}

	public static void startRestServer(String port) {

		int serverPort = Integer.parseInt(port);
		try {
			port(serverPort);
			init();
			LOGGER.info("On-Fly-Updater REST services started:: 0.0.0.0:" + port);
		} catch (Exception e) {
			LOGGER.error("On-Fly-Updater REST services failed to start", e);
		}
	}

	public static void loadServices() {

		path(UriPath, () -> {
			before("/*", (q, a) -> LOGGER.info("Received api call"));

			get("/status", (req, res) -> {
				res.type("application/json");
				return "{\"status\":\"On-Fly-Updater Running\"}";
			});

			get("/props", (req, res) -> {
				res.type("application/json");
				if(req.queryParams("type").equalsIgnoreCase("system")) {
					return JsonHelper.getProperties(System.getProperties());
				}else if(req.queryParams("type").equalsIgnoreCase("jmeter")){
					//return JsonHelper.getProperties(JMeterContextService.getContext().getProperties());
					return JsonHelper.getProperties(JMeterUtils.getJMeterProperties());
				}else {
					return "{\"message\":\"Property type required in query param\"}";
				}
				
			});
			
			
			get("/threadinfo", (req, res) -> {
				res.type("application/json");
				return JsonHelper.toJson(JMeterContextService.getThreadCounts());
			});
			

			get("/jmetervars", (req, res) -> {
				res.type("application/json");
				return JsonHelper.toJson(JMeterContextService.getContext().getVariables().entrySet());
			});

			
			put("/users", (req, res) -> {
				res.type("application/json");
				User user = JsonHelper.fromJson(req.body(), User.class);
				return UsersHandler.handle(user);
			});

			
			

			post("/stoptest", (req, res) -> {
				res.type("application/json");
				engine = JMeterContextService.getContext().getEngine();
				//JMeterContextService.getContext().getEngine();
				if(Credentials.validate(req.headers("password"))) {
					//engine.stopTest();
					engine.askThreadsToStop();
					return true;
				}
				
				return false;
			});

			put("/update", (req, res) -> {
				res.type("application/json");

				return JsonHelper.getProperties(JMeterContextService.getContext().getProperties());
			});

		});

	}


	public static void stopRestServer() {
		stop();
	}

}
