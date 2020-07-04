package io.perfwise.onfly.rest;

import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.init;
import static spark.Spark.path;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.put;

import java.net.InetAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import io.perfwise.onfly.model.Element;
import io.perfwise.onfly.model.Property;
import io.perfwise.onfly.model.VariableModel;
import io.perfwise.onfly.service.ElementService;
import io.perfwise.onfly.service.PropertyService;
import io.perfwise.onfly.service.TestService;
import io.perfwise.onfly.service.ThreadGroupService;
import io.perfwise.onfly.service.VariableService;
import io.perfwise.utils.Credentials;
import spark.Spark;

public class RestController {

	private static final Logger LOGGER = LoggerFactory.getLogger(RestController.class);
	private static String UriPath;

	public RestController(String UriPath) {
		RestController.UriPath = UriPath;
	}

	public static void startRestServer(String port) {

		int serverPort = Integer.parseInt(port);
		try {
			port(serverPort);
			init();
			LOGGER.info(String.format("On-Fly-Updater REST services started :: http://%s:%s%s/",
					InetAddress.getLocalHost().getHostAddress(), port, UriPath));
		} catch (Exception e) {
			LOGGER.error("On-Fly-Updater REST services failed to start", e);
		}
	}

	public static void stopRestServer() {
		Spark.stop();
	}

	public static void loadServices() {
		LOGGER.info("Loading REST Services");

		path(UriPath, () -> {
			before("/*", (q, a) -> LOGGER.info("Received an API call :: " + q.uri()));
			

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

					Property props = new Gson().fromJson(req.body(), Property.class);

					return new Gson().toJson(PropertyService.updateProperty(props));
				}
				return new Gson().toJson(new StandardResponse(StatusResponse.AUTHERROR, "Invalid Credentials"));
			});
			
			
			put("/logger/:loglevel", (req, res) -> {
				res.type("application/json");
				if (Credentials.validate(req.headers("password"))) {
					return new Gson().toJson(TestService.setLoggerLevel(req.params(":loglevel")));
				}
				return new Gson().toJson(new StandardResponse(StatusResponse.AUTHERROR, "Invalid Credentials"));
			});
			

			get("/threads", (req, res) -> {
				res.type("application/json");
				if (Credentials.validate(req.headers("password"))) {
					return new Gson().toJson(ThreadGroupService.getAllThreads());
				}
				return new Gson().toJson(new StandardResponse(StatusResponse.AUTHERROR, "Invalid Credentials"));
			});

			
			put("/threads", (req, res) -> {
				res.type("application/json");
				if (Credentials.validate(req.headers("password"))) {
					JsonParser jsonParser = new JsonParser();
					JsonElement jsonElement = jsonParser.parse(req.body());
					JsonArray jsonArray = jsonElement.getAsJsonArray();
					return new Gson().toJson(ThreadGroupService.updateThreads(jsonArray));
				}
				return new Gson().toJson(new StandardResponse(StatusResponse.AUTHERROR, "Invalid Credentials"));
			});

			
			get("/threadgroups", (req, res) -> {
				res.type("application/json");
				if (Credentials.validate(req.headers("password"))) {
					return new Gson().toJson(ThreadGroupService.getAllThreadGroupsInfo());
				}
				return new Gson().toJson(new StandardResponse(StatusResponse.AUTHERROR, "Invalid Credentials"));
			});

			
			put("/threadgroups", (req, res) -> {
				res.type("application/json");
				if (Credentials.validate(req.headers("password"))) {
					// new Gson().toJson(ThreadGroupService.getAllThreadGroupsInfo());
					return new Gson().toJson(ThreadGroupService.getAllThreadGroupsInfo());
				}
				return new Gson().toJson(new StandardResponse(StatusResponse.AUTHERROR, "Invalid Credentials"));
			});
			

			get("/elements", (req, res) -> {
				res.type("application/json");
				if (Credentials.validate(req.headers("password"))) {
					return new Gson().toJson(ElementService.getTestElementsInfo());
				}
				return new Gson().toJson(new StandardResponse(StatusResponse.AUTHERROR, "Invalid Credentials"));
			});

			
			put("/element", (req, res) -> {
				res.type("application/json");
				if (Credentials.validate(req.headers("password"))) {
					Element element = new Gson().fromJson(req.body(), Element.class);
					return new Gson().toJson(ElementService.updateTestElement(element));
				}
				return new Gson().toJson(new StandardResponse(StatusResponse.AUTHERROR, "Invalid Credentials"));
			});
			

			get("/vars", (req, res) -> {
				res.type("application/json");
				if (Credentials.validate(req.headers("password"))) {
					return new Gson().toJson(VariableService.getVars());
				}
				return new Gson().toJson(new StandardResponse(StatusResponse.AUTHERROR, "Invalid Credentials"));
			});

			
			put("/vars", (req, res) -> {
				res.type("application/json");
				
				if (Credentials.validate(req.headers("password"))) {
					VariableModel vars	= new Gson().fromJson(req.body(), VariableModel.class);
					
					return new Gson().toJson(VariableService.setVars(vars));
				}
				return new Gson().toJson(new StandardResponse(StatusResponse.AUTHERROR, "Invalid Credentials"));
			});

			
			get("/slaves", (req, res) -> {
				res.type("application/json");
				if (Credentials.validate(req.headers("password"))) {
					return new Gson().toJson(PropertyService.getSlavesInfo());
				}
				return new Gson().toJson(new StandardResponse(StatusResponse.AUTHERROR, "Invalid Credentials"));
			});
			

			post("/stoptest", (req, res) -> {
				res.type("application/json");

				if (req.queryParams("action") != null && Credentials.validate(req.headers("password"))) {
					return new Gson().toJson(TestService.stopTest(req.queryParams("action")));
				}
				return new Gson().toJson(new StandardResponse(StatusResponse.AUTHERROR, "Invalid Credentials"));
			});
			
			
			post("/slaves/stoptest", (req, res) -> {
				res.type("application/json");

				if (req.queryParams("action") != null && Credentials.validate(req.headers("password"))) {
					return new Gson().toJson(TestService.stopTestSlaves(req.queryParams("action"), req.queryParams("target-slave")));
				}
				return new Gson().toJson(new StandardResponse(StatusResponse.AUTHERROR, "Invalid Credentials"));
			});

		});

	}

}
