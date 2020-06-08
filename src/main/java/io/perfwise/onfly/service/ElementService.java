package io.perfwise.onfly.service;

import java.util.Collection;

import javax.swing.JPopupMenu;

import org.apache.jmeter.gui.AbstractJMeterGuiComponent;
import org.apache.jmeter.gui.JMeterGUIComponent;
import org.apache.jmeter.gui.UndoHistory;
import org.apache.jmeter.gui.UndoHistory.HistoryListener;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.LocaleChangeEvent;
import org.apache.jmeter.util.LocaleChangeListener;
import org.apache.jmeter.visualizers.Printable;

public class ElementService extends AbstractJMeterGuiComponent implements LocaleChangeListener, HistoryListener, Printable {

	private static final long serialVersionUID = -951934843062248990L;

	@Override
	public String getLabelResource() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TestElement createTestElement() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void modifyTestElement(TestElement element) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public JPopupMenu createPopupMenu() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<String> getMenuCategories() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void notifyChangeInHistory(UndoHistory history) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void localeChanged(LocaleChangeEvent event) {
		// TODO Auto-generated method stub
		
	}

	

}
