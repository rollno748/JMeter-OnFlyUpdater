package io.perfwise.onfly.config;

import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.path;
import static spark.Spark.post;
import static spark.Spark.put;

import java.util.Properties;

import org.apache.jmeter.config.ConfigElement;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.testbeans.TestBean;
import org.apache.jmeter.testbeans.TestBeanHelper;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.testelement.TestStateListener;
import org.apache.jmeter.threads.JMeterContextService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.perfwise.rest.RestServices;
import io.perfwise.utils.Credentials;
import io.perfwise.utils.JsonHelper;


public class OnFlyConfig extends AbstractTestElement implements ConfigElement, TestStateListener, TestBean {

	private static final long serialVersionUID = 3031594799580611171L;
	private static final Logger LOGGER = LoggerFactory.getLogger(OnFlyConfig.class);
	
	private String port;
	private String uriPath;
	private String password;
	private static StandardJMeterEngine engine;
	

	public void testStarted() {
		this.setRunningVersion(true);
		TestBeanHelper.prepare(this);
		Properties props = JMeterContextService.getContext().getProperties();
		engine = JMeterContextService.getContext().getEngine();
		new Credentials(getPassword());
		new RestServices(getUriPath());
		//Start Spark REST services
		RestServices.startRestServer(getPort());
		
		
		
		path(getUriPath(), () -> {
		    before("/*", (q, a) -> LOGGER.info("Received api call"));
		    
		    get("/status", (req, res) -> {	
		    	res.type("application/json");
			    return "{\"status\":\"On-Fly-Updater Running\"}";
		    });
		    
		    get("/jmeterprops", (req, res) -> {	
		    	res.type("application/json");
		    	
			    return JsonHelper.getJmeterProperties(props);
		    }); 
		    
		    get("/jmetervars", (req, res) -> {	
		    	res.type("application/json");
			    return JsonHelper.toJson(JMeterContextService.getClientSideVariables());
		    }); 
		    
		    get("/threadinfo", (req, res) -> {	
		    	res.type("application/json");
			    return JsonHelper.toJson(JMeterContextService.getThreadCounts());
		    }); 
		    
		    post("/stoptest", (req, res) -> {
		    	res.type("application/json");
		    	if(Credentials.validate(req.headers("password"))) {
		    		engine.askThreadsToStop();
		    	}
		    	return true;
		    });
		    
		    
		    put("/update", (req, res) -> {	
		    	res.type("application/json");
		    	
			    return JsonHelper.getJmeterProperties(props);
		    }); 
		    
		});
		
	}



	public void testStarted(String host) {
		testStarted();
	}

	public void testEnded() {
		synchronized (this) {
			try {
				RestServices.stopRestServer();
			}catch (Exception e) {
				LOGGER.error("On-Fly-Updater REST services failed to stop", e);
			}
            
        }
	}

	public void testEnded(String host) {
		testEnded();
	}

	public void addConfigElement(ConfigElement config) {
		// TODO Auto-generated method stub
	}

	public boolean expectsModification() {
		return false;
	}

	// Getter and Setters 
	
	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		
		if(port.isEmpty()) {
			this.port="4567";
		}	
		this.port = port;
	}

	public String getUriPath() {
		return uriPath;
	}

	public void setUriPath(String uriPath) {
		this.uriPath = uriPath;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setVariables(String vars) {
		
	}
	
	

}
