package vft.filter;

public class TestCaseFilter  extends Filter {
	
	private static String testCaseName;
	
	protected TestCaseFilter() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected boolean setFilterRule(String packageName) {
		testCaseName = packageName;
		return collectErrorInfo();
	}

	private boolean collectErrorInfo() {
		return super.setArchitectureNode(TEST_CASE_FILTER, testCaseName, null);
	}


}