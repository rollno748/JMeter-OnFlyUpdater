package io.perfwise.onfly.service;

import org.apache.jmeter.gui.tree.JMeterTreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ElementService extends JMeterTreeNode {

	private static final long serialVersionUID = -951934843062248990L;
	private static final Logger LOGGER = LoggerFactory.getLogger(ElementService.class);
	
	//GuiPackage
	//JMeterGUIComponent getGui

	public ElementService() {
		LOGGER.info("ELementService");
	}

}
