package io.perfwise.onfly.rest;

import com.google.gson.*;
import io.perfwise.onfly.model.Property;
import io.perfwise.onfly.service.*;
import io.perfwise.utils.Credentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Spark;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static spark.Spark.*;

public class RestController {
	private static final Logger LOGGER = LoggerFactory.getLogger(RestController.class);
	private static final double PLUGIN_VERSION = 1.0;
	private static String UriPath;

	private String openApiSpec;

	public RestController(String UriPath) {
		RestController.UriPath = UriPath;
	}

	public void startRestServer(String port) {
		int serverPort = Integer.parseInt(port);
		try {
			openApiSpec = loadResource("/openapi.json");
			port(serverPort);
			this.routes();   // register routes before init so no request can arrive before they're ready
			init();
			Spark.awaitInitialization();  // Fix 7: block until Jetty is fully bound
			String localHostAddress = InetAddress.getLocalHost().getHostAddress();
			String fullPath = "http://" + localHostAddress + ":" + port + UriPath;
			LOGGER.info("On-Fly-Updater REST services started :: {}", fullPath);
		} catch (Exception e) {
			LOGGER.error("Failed to start On-Fly-Updater services", e);
		}
	}

	public void stopRestServer() {
		Spark.stop();
		Spark.awaitStop();  // Fix 7: block until Jetty has fully released the port, prevents restart races
	}

	private String loadResource(String path) {
		try (InputStream is = RestController.class.getResourceAsStream(path)) {
			if (is == null) return "{}";
			StringBuilder sb = new StringBuilder();
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
				String line;
				while ((line = reader.readLine()) != null) {
					sb.append(line).append('\n');
				}
			}
			return sb.toString();
		} catch (Exception e) {
			LOGGER.error("Failed to load resource {}", path, e);
			return "{}";
		}
	}

	private void routes() {
		LOGGER.info("Loading REST Services");

		path(UriPath, () -> {

			before("/*", (q, a) -> LOGGER.info("Received an API call : {} - {} ", q.ip(), q.uri()));

			// Fix 6: CORS header so online tools (Swagger Editor, Postman web) can reach the spec
			options("/*", (req, res) -> {
				res.header("Access-Control-Allow-Origin", "*");
				res.header("Access-Control-Allow-Methods", "GET,PUT,POST,OPTIONS");
				res.header("Access-Control-Allow-Headers", "password,Content-Type");
				return "OK";
			});

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
				if (req.queryParams("action") != null && Credentials.validate(req.headers("password"))) {
					return new Gson().toJson(TestService.stopTestSlaves(req.queryParams("action"), req.headers("slaves")));
				}
				return new Gson().toJson(new StandardResponse(StatusResponse.AUTHERROR, "Invalid Credentials"));
			});

			// Fix 6: serve OpenAPI spec with CORS so Swagger Editor / Postman web can import it
			get("/openapi.json", (request, response) -> {
				response.type("application/json");
				response.header("Access-Control-Allow-Origin", "*");
				return openApiSpec;
			});

			// Fix 6: serve Swagger UI (CDN) wired to the local spec
			get("/swagger", (request, response) -> {
				response.type("text/html");
				return buildSwaggerUiHtml(request.host());
			});

		});
	}

	private static String buildSwaggerUiHtml(String host) {
		String specUrl = "http://" + host + UriPath + "/openapi.json";
		return "<!DOCTYPE html>\n"
			+ "<html>\n"
			+ "<head>\n"
			+ "  <title>JMeter OnFly Updater API</title>\n"
			+ "  <meta charset=\"utf-8\"/>\n"
			+ "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n"
			+ "  <link rel=\"stylesheet\" href=\"https://unpkg.com/swagger-ui-dist@5.9.0/swagger-ui.css\">\n"
			+ "</head>\n"
			+ "<body>\n"
			+ "  <div id=\"swagger-ui\"></div>\n"
			+ "  <script src=\"https://unpkg.com/swagger-ui-dist@5.9.0/swagger-ui-bundle.js\"></script>\n"
			+ "  <script>\n"
			+ "    SwaggerUIBundle({\n"
			+ "      url: '" + specUrl + "',\n"
			+ "      dom_id: '#swagger-ui',\n"
			+ "      presets: [SwaggerUIBundle.presets.apis],\n"
			+ "      layout: 'BaseLayout'\n"
			+ "    });\n"
			+ "  </script>\n"
			+ "</body>\n"
			+ "</html>\n";
	}

}
