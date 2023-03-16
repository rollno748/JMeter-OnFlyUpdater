package io.perfwise.onfly.rest;

import com.google.gson.*;
import io.perfwise.onfly.model.Property;
import io.perfwise.onfly.service.*;
import io.perfwise.utils.Credentials;
import io.swagger.models.Contact;
import io.swagger.models.Info;
import io.swagger.models.License;
import io.swagger.models.Swagger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Spark;

import java.net.InetAddress;
import java.util.List;

import static spark.Spark.*;

public class RestController {

	private static final Logger LOGGER = LoggerFactory.getLogger(RestController.class);
	private static final double PLUGIN_VERSION=1.0;
	
	private static String UriPath;

	public RestController(String UriPath) {
		RestController.UriPath = UriPath;
	}

	public void startRestServer(String port) {
		int serverPort = Integer.parseInt(port);
		try {
			port(serverPort);
			init();
			String localHostAddress = InetAddress.getLocalHost().getHostAddress();
			String fullPath = "http://" + localHostAddress + ":" + port + UriPath;
			LOGGER.info("On-Fly-Updater REST services started :: {}", fullPath);
			this.routes();
		} catch (Exception e) {
			LOGGER.error("Failed to start On-Fly-Updater services", e);
		}
	}

	public void stopRestServer() {
		Spark.stop();
	}

	private void routes() {
		LOGGER.info("Loading REST Services");
		/* Swagger Config
		 * goes here
		 */
		Swagger swaggerConfig = new Swagger().info(
				new Info().title("On-Fly Updater Swagger")
						.description("desc")
						.version("1.0")
						.contact(new Contact().name("rollno748")
								.email("")
								.url("https://github.com/rollno748/JMeter-OnFlyUpdater"))
						.license(new License()
								.name("Apache 2.0")
								.url("http://www.apache.org/licenses/LICENSE-2.0.html")));

		//registering swagger
//		new SwaggerContextService().withSwaggerConfig((SwaggerConfig) swaggerConfig);

		path(UriPath, () -> {

			before("/*", (q, a) -> LOGGER.info("Received an API call : {} - {} ", q.ip(), q.uri()));

			get("/ping", (req, res) -> {
				res.type("application/json");
				return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS, PLUGIN_VERSION));
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
					JsonParser jsonParser = new JsonParser();
					JsonElement jsonElement = jsonParser.parse(req.body());
					JsonArray jsonArray = jsonElement.getAsJsonArray();
					return new Gson().toJson(ThreadGroupService.updateThreadGroups(jsonArray));
				}
				return new Gson().toJson(new StandardResponse(StatusResponse.AUTHERROR, "Invalid Credentials"));
			});

			get("/elements", (req, res) -> {
				res.type("application/json");
				if (req.queryParams("type") != null && Credentials.validate(req.headers("password"))) {
					return new Gson().toJson(ElementService.getTestElementsInfo(req.queryParams("type")));
				}
				return new Gson().toJson(new StandardResponse(StatusResponse.AUTHERROR, "Invalid Credentials"));
			});

			put("/elements", (req, res) -> {
				res.type("application/json");
				if (Credentials.validate(req.headers("password"))) {
					JsonParser jsonParser = new JsonParser();
					JsonElement jsonElement = jsonParser.parse(req.body());
					JsonArray jsonArray = jsonElement.getAsJsonArray();
					return new Gson().toJson(ElementService.updateTestElement(jsonArray));
				}
				return new Gson().toJson(new StandardResponse(StatusResponse.AUTHERROR, "Invalid Credentials"));
			});

			get("/vars/:threadname", (req, res) -> {
				res.type("application/json");
				if (Credentials.validate(req.headers("password"))) {
					return new Gson().toJson(VariableService.getVars(req.params(":threadname")));
				}
				return new Gson().toJson(new StandardResponse(StatusResponse.AUTHERROR, "Invalid Credentials"));
			});

			put("/vars/:threadname", (req, res) -> {
				res.type("application/json");
				
				if (Credentials.validate(req.headers("password"))) {
					JsonParser jsonParser = new JsonParser();
					JsonElement jsonElement = jsonParser.parse(req.body());
					JsonObject json = jsonElement.getAsJsonObject();
					return new Gson().toJson(VariableService.setVars(req.params(":threadname"), json));
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

			get("/slaves", (req, res) -> {
				res.type("application/json");
				if (Credentials.validate(req.headers("password"))) {
					return new Gson().toJson(PropertyService.getSlavesInfo());
				}
				return new Gson().toJson(new StandardResponse(StatusResponse.AUTHERROR, "Invalid Credentials"));
			});

			post("/slaves/stoptest", (req, res) -> {
				res.type("application/json");
				List<String> slaves = null;

				if (req.queryParams("action") != null && Credentials.validate(req.headers("password"))) {
					return new Gson().toJson(TestService.stopTestSlaves(req.queryParams("action"), req.headers("slaves")));
				}
				return new Gson().toJson(new StandardResponse(StatusResponse.AUTHERROR, "Invalid Credentials"));
			});
		});

	}

}
