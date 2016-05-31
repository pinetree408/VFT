package vft.filter;
 
import java.util.ArrayList;


public class FilterWrapper extends Filter {

	private int filterRule;
	private int viewMode;
	private String interfaceName;
	private String packageName1;
	private String packageName2;
	
	public FilterWrapper() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public ArrayList<String> setFilterRule(int rule) {
		filterRule = rule;
		if (rule == INTER_COMPONENT_FILTER) {
			InterComponentFilter ICF = new InterComponentFilter();
			return ICF.getPackageList();
		} else if (rule == FILE_FILTER) {
			FileFilter FF = new FileFilter();
			return FF.getFileList();
		} else if (rule == TEST_CASE_FILTER) {
			TestCaseFilter TC = new TestCaseFilter();
			return TC.getTestCaseList();
		} else if (rule == TEST_METHOD_FILTER) {
			TestMethodFilter TM = new TestMethodFilter();
			return TM.getTestMethodList();
		} 		
		return null;
	}

	public boolean selectComponent(int rule, String package1, String package2) {
		filterRule = rule;
		if (rule == INTER_COMPONENT_FILTER) {
			InterComponentFilter ICF = new InterComponentFilter();
			packageName1 = package1;
			packageName2 = package2;
			return ICF.setFilterRule(package1, package2);
		} else if (rule == FILE_FILTER) {
			FileFilter FF = new FileFilter();
			packageName1 = package1;
			return FF.setFilterRule(packageName1);
		} else if (rule == TEST_CASE_FILTER) {
			TestCaseFilter TC = new TestCaseFilter();
			packageName1 = package1;
			return TC.setFilterRule(packageName1);
		} else if (rule == TEST_METHOD_FILTER) {
			TestMethodFilter TM = new TestMethodFilter();
			packageName1 = package1;
			return TM.setFilterRule(packageName1);
		} 		
		return false;
	}
	
	public boolean setInterface (String selectedInterface) {
		interfaceName = selectedInterface;
		return super.setInterface(interfaceName);			
	}

	public void prePareLogData () {
		super.prePareLogData();			
	}

	public ArrayList<ErrorInfo> getErrorInfo() {
		return super.getErrorInfo();
	}

	public ArrayList<GraphNode> getGraphNode() {
		return super.getGraphNode();
	}

	public ArrayList<TextualNode> getTextualNode() {
		return super.getTextualNode();
	} 

}
