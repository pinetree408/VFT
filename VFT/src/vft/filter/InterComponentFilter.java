package vft.filter;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class InterComponentFilter extends Filter {

	private static String ComponentName1;
	private static String ComponentName2;
	
	protected InterComponentFilter() throws SAXException, IOException, ParserConfigurationException {
		super();
		// TODO Auto-generated constructor stub
	}

	protected boolean setFilterRule(String package1, String package2) {
		ComponentName1 = package1;
		ComponentName2 = package2;		
		return collectErrorInfo();
	}

	private boolean collectErrorInfo() {
		return super.setArchitectureNode(INTER_COMPONENT_FILTER, ComponentName1, ComponentName2);
	}

	protected ArrayList<String> getPackageList() {
		return super.getPackageList();
	}

}