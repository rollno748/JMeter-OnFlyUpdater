package io.perfwise.onfly.service;

import java.util.Arrays;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.jmeter.control.Controller;
import org.apache.jmeter.control.GenericController;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.gui.action.SchematicView;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.logging.log4j.core.config.Configurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.perfwise.onfly.config.OnFlyConfig;
import io.perfwise.onfly.model.Element;
import io.perfwise.onfly.rest.StandardResponse;
import io.perfwise.onfly.rest.StatusResponse;

public class TestService extends SchematicView {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TestService.class);

	public static StandardResponse getOverallTestInfo() throws Exception {
		
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
				//OnFlyConfig.getJmeterEngine().stopEngine();
				return new StandardResponse(StatusResponse.SUCCESS, "Jmeter is Shutting down !!");
			}else {
				engine.stopTest(true);
				//OnFlyConfig.getJmeterEngine().stopEngineNow();
				return new StandardResponse(StatusResponse.SUCCESS, "Jmeter Stopped abrubtly !!");
			}
		} catch (Exception e) {
			return new StandardResponse(StatusResponse.ERROR, e.toString());
		}
	}

	public static StandardResponse updateTestElement(Element element) {
		try {
			JMeterContextService.getContext().getThreadGroup().getSamplerController();
			JMeterContextService.getContext().getThreadGroup().setEnabled(false);
			
			Controller controller = new GenericController();
			controller.addTestElement(controller);
			
			return new StandardResponse(StatusResponse.SUCCESS, "Element status updated in Jmeter");
			
		}catch (Exception e) {
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

}
