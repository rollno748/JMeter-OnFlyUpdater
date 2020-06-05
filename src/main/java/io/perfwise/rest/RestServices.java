package io.perfwise.rest;

import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.init;
import static spark.Spark.path;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.put;

import org.apache.jmeter.threads.JMeterContextService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.healthmarketscience.jackcess.PropertyMap.Property;

import io.perfwise.rest.controller.ThreadGroupHandler;
import io.perfwise.rest.controller.User;
import io.perfwise.service.PropertyService;
import io.perfwise.service.ThreadGroupService;
import io.perfwise.service.VariableService;
import io.perfwise.utils.Credentials;
import io.perfwise.utils.JsonHelper;
import spark.Spark;

public class RestServices extends ThreadGroupHandler {

	private static final long serialVersionUID = 3776795558086590868L;

	private static final Logger LOGGER = LoggerFactory.getLogger(RestServices.class);

	private static String UriPath;

	public RestServices(String UriPath) {
		RestServices.UriPath = UriPath;
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
		LOGGER.info("Loading REST Services");

		path(UriPath, () -> {
			before("/*", (q, a) -> LOGGER.info("Received api call"));

			get("/ping", (req, res) -> {
				res.type("application/json");
				// --- Get Plugin Running Status ---
				return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS, "On-Fly-Updater Running"));
			});

			
			get("/properties", (req, res) -> {
				res.type("application/json");
				// --- Get Properties (System/Jmeter)---
				return new Gson().toJson(PropertyService.getProperty(req.queryParams("type")));
			});

			
			
			put("/update", (req, res) -> {
				res.type("application/json");
				// --- Update Jmeter properties --- Not Completed
				return new Gson().toJson(PropertyService.updateProperty(req.body(), Property.class));
			});

			
			get("/status", (req, res) -> {
				res.type("application/json");
				// --- Get thread Running Status (Active, started, running)---
				return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS, JMeterContextService.getThreadCounts()));
			});

			
			get("/testinfo", (req, res) -> {
				res.type("application/json");
				if (req.queryParams("type") != null && Credentials.validate(req.headers("password"))) {
					return null;
				}
				// --- Get the complete test informations (Threads, threadgroups, etc)
				return new Gson().toJson("AuthError");
			});

			
			put("/threads", (req, res) -> {
				res.type("application/json");
				User user = JsonHelper.fromJson(req.body(), User.class);
				// --- Add/remove users from specific threadgroup ---
				return ThreadGroupService.update(user);
			});

			
			get("/vars", (req, res) -> {
				res.type("application/json");
				// return JsonHelper.getJmeterVars(true);
				// --- Get Jmeter variables info ---
				return VariableService.getVars();
			});

			// --- Get Jmeter variables info ---
			put("/vars", (req, res) -> {
				res.type("application/json");
				// return JsonHelper.getJmeterVars(true);
				return true;
			});

			// --- Get list of threadgroups info from testplan---
			put("/threadgroups", (req, res) -> {
				res.type("application/json");
				return JsonHelper.toJson(JMeterContextService.getContext().getThreadGroup().getSamplerController());
			});

			// -- Enable/Disable element
			put("/element", (req, res) -> {
				res.type("application/json");
				return null;
			});

			
			post("/stoptest", (req, res) -> {
				res.type("application/json");
				
				if (req.queryParams("action") != null && Credentials.validate(req.headers("password"))) {
					return ThreadGroupService.stopTest(req.queryParams("action"));
				}
				// --- Get Plugin Running Status ---
				return new Gson().toJson("AuthError");
			});

		});

	}

	public static void stopRestServer() {
		Spark.stop();
	}

}
