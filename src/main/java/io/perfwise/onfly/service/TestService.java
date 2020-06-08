package io.perfwise.onfly.service;

import org.apache.jmeter.control.Controller;
import org.apache.jmeter.control.GenericController;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.threads.JMeterContextService;

import io.perfwise.onfly.config.OnFlyConfig;
import io.perfwise.onfly.model.Element;
import io.perfwise.onfly.rest.StandardResponse;
import io.perfwise.onfly.rest.StatusResponse;

public class TestService {

	public static StandardResponse getOverallTestInfo() {
		// TODO Auto-generated method stub
		return null;
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

}
