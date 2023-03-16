package io.perfwise.onfly.service;

import io.perfwise.onfly.config.OnFlyConfig;
import io.perfwise.onfly.rest.StandardResponse;
import io.perfwise.onfly.rest.StatusResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.jmeter.engine.DistributedRunner;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.gui.GuiPackage;
import org.apache.jmeter.gui.action.SchematicView;
import org.apache.jmeter.gui.tree.JMeterTreeListener;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.logging.log4j.core.config.Configurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;
import java.util.Arrays;
import java.util.List;

public class TestService extends SchematicView {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TestService.class);

	public static StandardResponse getOverallTestInfo() throws Exception {
		
		JMeterTreeListener testPlanTree = GuiPackage.getInstance().getTreeListener();
		testPlanTree.getCurrentNode().getParent();
		
		TransformerFactory factory = TransformerFactory.newInstance("net.sf.saxon.BasicTransformerFactory", Thread.currentThread().getContextClassLoader());
		try {
			factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
	        Source xslt;
	        if (!StringUtils.isEmpty(JMeterUtils.getProperty("docgeneration.schematic_xsl"))) {
	            LOGGER.info("Will use file {} for Schematic View generation", JMeterUtils.getProperty("docgeneration.schematic_xsl"));
	            xslt = new StreamSource(JMeterUtils.getProperty("docgeneration.schematic_xsl"));
	        } else {
	            xslt = new StreamSource(SchematicView.class.getResourceAsStream("/org/apache/jmeter/gui/action/schematic.xsl"));
	        }
	        //Transformer transformer = factory.newTransformer(xslt);
	        return new StandardResponse(StatusResponse.SUCCESS, factory.newTransformer(xslt));
		} catch(Exception e) {
			return new StandardResponse(StatusResponse.ERROR, e.toString());
		}
	}
	

	public static StandardResponse getStatus() {
		try {
			return new StandardResponse(StatusResponse.SUCCESS, JMeterContextService.getThreadCounts());
		} catch (Exception e) {
			return new StandardResponse(StatusResponse.ERROR, e.toString());
		}
	}

	public static StandardResponse stopTest(String action) {
		try {
			StandardJMeterEngine engine = OnFlyConfig.getJmeterEngine();

			if (action.toLowerCase().equals("shutdown")) {
				engine.askThreadsToStop();
				return new StandardResponse(StatusResponse.SUCCESS, "Jmeter is Shutting down !!");
			}else {
				engine.stopTest(true);
				return new StandardResponse(StatusResponse.SUCCESS, "Jmeter Stopped abruptly !!");
			}
		} catch (Exception e) {
			return new StandardResponse(StatusResponse.ERROR, e.toString());
		}
	}

	
	public static StandardResponse setLoggerLevel(String logLevel) {
		String[] loggerTypes = new String[] {"off","fatal","error","warn","info","debug","trace","all"};
		
		if (!Arrays.asList(loggerTypes).contains(logLevel)) {
			return new StandardResponse(StatusResponse.ERROR, "The Log Level should be either one of this type: (OFF, FATAL, ERROR, WARN, INFO, DEBUG, TRACE, ALL)");
			
		}else {
			try {
				Configurator.setRootLevel(org.apache.logging.log4j.Level.toLevel(logLevel));
				return new StandardResponse(StatusResponse.SUCCESS, String.format("The Log Level has been changed to %s", logLevel));
			} catch (Exception e) {
				return new StandardResponse(StatusResponse.ERROR, e.toString());
			}
		}
		
	}
	
	
	public static StandardResponse getLoggerLevel() {
		String logLevel = Configurator.class.getName();
		try {
			return new StandardResponse(StatusResponse.SUCCESS, String.format("The Log Level in Jmeter is %s", logLevel));
		} catch (Exception e) {
			return new StandardResponse(StatusResponse.ERROR, e.toString());
		}		
	}
	
	
	public static StandardResponse stopTestSlaves(String action, String targettedSlaves) {

		if(targettedSlaves.isEmpty()){
			return new StandardResponse(StatusResponse.ERROR, "LGs list should not be empty or null -> try checking /slaves to get the list of LGs to control");
		}

		List<String> slavesList = Arrays.asList(targettedSlaves.split(","));
		DistributedRunner distributedRunner = OnFlyConfig.getDistributedRunner();

		try {
			if (action.toLowerCase().equals("shutdown")) {
				distributedRunner.shutdown(slavesList);
				return new StandardResponse(StatusResponse.SUCCESS, "JMeter LG(s) Shutting down !!");
			}else {
				distributedRunner.stop(slavesList);
				return new StandardResponse(StatusResponse.SUCCESS, "JMeter LG(s) Stopped abruptly !!");
			}
		} catch (Exception e) {
			return new StandardResponse(StatusResponse.ERROR, e.toString());
		}

		/*
			2020-06-30 23:14:25,375 INFO o.a.j.e.DistributedRunner: Failed to configure 127.0.0.1
			2020-06-30 23:14:25,375 INFO o.a.j.e.DistributedRunner: Stopping remote engines
			2020-06-30 23:14:25,375 INFO o.a.j.e.DistributedRunner: Remote engines have been stopped
			2020-06-30 23:14:25,375 ERROR o.a.j.g.a.ActionRouter: Error processing org.apache.jmeter.gui.action.RemoteStart@15d0d6c9
		 */
	}

}
