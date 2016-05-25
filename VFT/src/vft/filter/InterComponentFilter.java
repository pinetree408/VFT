package vft.filter;

import java.util.ArrayList;

public class InterComponentFilter extends Filter {

	private static String ComponentName1;
	private static String ComponentName2;
	
	protected InterComponentFilter() {
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

	private ArrayList<String> findJointPoint() {
		throw new UnsupportedOperationException("The method is not implemented yet.");
	}

}