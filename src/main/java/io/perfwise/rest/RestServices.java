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

import io.perfwise.model.User;
import io.perfwise.service.PropertyService;
import io.perfwise.service.TestService;
import io.perfwise.service.ThreadGroupHandler;
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
				return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS, "On-Fly-Updater Running"));
			});

			get("/status", (req, res) -> {
				res.type("application/json");
				if (Credentials.validate(req.headers("password"))) {
					return new Gson().toJson(TestService.getStatus());
				}
				return new Gson().toJson(new StandardResponse(StatusResponse.AUTHERROR, "Invalid Credentials"));
			});

			get("/properties", (req, res) -> {
				res.type("application/json");
				if (req.queryParams("type") != null && Credentials.validate(req.headers("password"))) {
					return new Gson().toJson(PropertyService.getProperty(req.queryParams("type")));
				}
				return new Gson().toJson(new StandardResponse(StatusResponse.AUTHERROR, "Invalid Credentials"));
			});

			put("/properties", (req, res) -> {
				res.type("application/json");
				if (Credentials.validate(req.headers("password"))) {
					return new Gson().toJson(PropertyService.updateProperty(req.body(), Property.class));
				}
				return new Gson().toJson(new StandardResponse(StatusResponse.AUTHERROR, "Invalid Credentials"));
			});

			get("/testinfo", (req, res) -> {
				res.type("application/json");
				if (Credentials.validate(req.headers("password"))) {
					return new Gson().toJson(TestService.getOverallTestInfo());
				}
				return new Gson().toJson(new StandardResponse(StatusResponse.AUTHERROR, "Invalid Credentials"));
			});

			put("/threads", (req, res) -> {
				res.type("application/json");
				User user = JsonHelper.fromJson(req.body(), User.class);
				// --- Add/remove users from specific threadgroup ---
				if (Credentials.validate(req.headers("password"))) {
					return new Gson().toJson(ThreadGroupService.update(user));
				}
				return new Gson().toJson(new StandardResponse(StatusResponse.AUTHERROR, "Invalid Credentials"));

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

				if (Credentials.validate(req.headers("password"))) {
					return null;
				}
				return new Gson().toJson(new StandardResponse(StatusResponse.AUTHERROR, "Invalid Credentials"));
			});

			// --- Get list of threadgroups info from testplan---
			put("/threadgroups", (req, res) -> {
				res.type("application/json");
				if (Credentials.validate(req.headers("password"))) {
					return JsonHelper.toJson(JMeterContextService.getContext().getThreadGroup().getSamplerController());
				}
				return new Gson().toJson(new StandardResponse(StatusResponse.AUTHERROR, "Invalid Credentials"));
			});

			// -- Enable/Disable element
			put("/element", (req, res) -> {
				res.type("application/json");
				if (Credentials.validate(req.headers("password"))) {
					return null;
				}
				return new Gson().toJson(new StandardResponse(StatusResponse.AUTHERROR, "Invalid Credentials"));
			});

			post("/stoptest", (req, res) -> {
				res.type("application/json");

				if (req.queryParams("action") != null && Credentials.validate(req.headers("password"))) {
					return ThreadGroupService.stopTest(req.queryParams("action"));
				}
				// Stop Test -- Applies to all threadgroups
				return new Gson().toJson(new StandardResponse(StatusResponse.AUTHERROR, "Invalid Credentials"));
			});

		});

	}

	public static void stopRestServer() {
		Spark.stop();
	}

}
