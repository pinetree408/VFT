package vft.filter;

import java.io.IOException;
import java.util.ArrayList;

import org.xml.sax.SAXException;

import vft.parser.parser;
import vft.parser.parser.Arch_Channel;
import vft.parser.parser.LogData;

public class Filter {
	
    public final int INTER_COMPONENT_FILTER = 1;
    public final int FILE_FILTER = 2;
    public final int TEST_CASE_FILTER = 3;
    public final int TEST_METHOD_FILTER = 4;
    
    public static ArrayList<String> packageList = new ArrayList<String>(); //for Inter-package filter
    public static ArrayList<String> fileList = new ArrayList<String>(); //for File filter
    public static ArrayList<String> testCaseList = new ArrayList<String>(); //for Test Case filter
    public static ArrayList<String> functionListForTC = new ArrayList<String>(); //for Test Case filter
    public static ArrayList<String> testMethodList = new ArrayList<String>(); //for Test Method filter

    public static String interfaceName;
    public static ArrayList<ErrorInfo> errorInfo = new ArrayList<ErrorInfo>();
    public static ArrayList<GraphNode> graphNode = new ArrayList<GraphNode>();
    public static ArrayList<TextualNode> textualNode = new ArrayList<TextualNode>();
    public static ArrayList<MethodListForTC> methodListForTC = new ArrayList<MethodListForTC>();
    private ArrayList<Arch_Channel> pArchitectureData = new ArrayList<Arch_Channel>();	// parsed architecture data
    private ArrayList<LogData> pLogData = new ArrayList<LogData>();	// parsed log data
    parser parsedData = null;
	
    protected Filter() {  			
		try {
			parsedData = new parser();
			pArchitectureData = parsedData.get_pared_Arch();
			pLogData = parsedData.get_parsed_LogData();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}


	public void prePareLogData () {		
		//collect basic information here -> architecture data(architectureData) and log data
		collectFilterInfoForFirstPage();
	}
		
	private void collectFilterInfoForFirstPage() {
		int i, j;
		LogData tempLogData;
		Arch_Channel tempArch;
		String temp;
		
		// Package list
        for(i = 0; i < pArchitectureData.size(); i++) {
        	tempArch = pArchitectureData.get(i);
        	
        	if (packageList.size() == 0) {        		
        		packageList.add(new String(tempArch.start.substring(1)));
        		packageList.add(new String(tempArch.end.substring(1)));
        	} else {
	            for(j = 0; j < packageList.size(); j++) {
	            	temp = packageList.get(j);
	            	if (tempArch.start.contains(temp)) {
	            		break;
	            	}
	            }
	            if (packageList.size() == j)
	        		packageList.add(new String(tempArch.start.substring(1)));
	            
	            for(j = 0; j < packageList.size(); j++) {
	            	temp = packageList.get(j);
	            	if (tempArch.end.contains(temp)) {
	            		break;
	            	}
	            }
	            if (packageList.size() == j)
	        		packageList.add(new String(tempArch.end.substring(1)));
        	}
        }

        for(i = 0; i < pLogData.size(); i++) {
        	tempLogData = pLogData.get(i);
        	
        	// File list
        	if (tempLogData.fileName.equals("null")) {
	            System.out.println("Filter : Log Err - The fileName is null. Don't add this log to File list.");
        	} else {
	        	if (fileList.size() == 0) {        		
	        		fileList.add(new String(tempLogData.fileName));
	        	} else {
		            for(j = 0; j < fileList.size(); j++) {
		            	temp = fileList.get(j);
		            	if (tempLogData.fileName.equals(temp)) {
		            		break;
		            	}
		            }
		            if (fileList.size() == j)
		            	fileList.add(new String(tempLogData.fileName));	            
	        	}
        	}
        	
        	
        	// Test case list
        	if (tempLogData.testSuiteName.equals("null")) {
	            System.out.println("Filter : Log Err - The testSuiteName is null. Don't add this log to Test case list.");
        	} else {
	        	if (testCaseList.size() == 0) {        		
	        		testCaseList.add(new String(tempLogData.testSuiteName));
	        	} else {
		            for(j = 0; j < testCaseList.size(); j++) {
		            	temp = testCaseList.get(j);
		            	if (tempLogData.testSuiteName.equals(temp)) {
		            		break;
		            	}
		            }
		            if (testCaseList.size() == j) {
		            	testCaseList.add(new String(tempLogData.testSuiteName));
		            }
	        	}
        	}
        	
        	// Test method list
        	if (testMethodList.size() == 0) {
	            if (tempLogData.action.equals("call") &&
	            		tempLogData.calledClass.startsWith("com.atmsimulation")) {
	    			if (tempLogData.functionName.equals("<init>")) { //constructor call
	            		testMethodList.add(new String(tempLogData.calledClass));
	    			} else if (tempLogData.functionName.equals("null")) { //
	    	            System.out.println("Filter : Log Err - The functionName is null. Don't add this log to Test Method list.");
	    			} else {
	            		testMethodList.add(new String(tempLogData.calledClass + "." + tempLogData.functionName));
	    			}
	            }
    			
        	} else {
    			String mfunctionName = "null";	            	
    			String[] splitText;
    			splitText = tempLogData.calledClass.split("[.]");
    			
	        	if (!tempLogData.calledClass.equals("null") && tempLogData.functionName.equals("<init>"))
	        		mfunctionName =  splitText[splitText.length - 1];
	        	else 
        			mfunctionName =  tempLogData.functionName;	        		
	        	
	            for(j = 0; j < testMethodList.size(); j++) {
	            	temp = testMethodList.get(j);
	            	if (temp.endsWith(mfunctionName)) {
	            		break;
	            	}
	            } 
	            if (testMethodList.size() == j && 
	            		tempLogData.action.equals("call") && 
	            		tempLogData.calledClass.startsWith("com.atmsimulation")) {
	    			if (tempLogData.functionName.equals("<init>")) { //constructor call
	            		testMethodList.add(new String(tempLogData.calledClass));
	    			} else if (tempLogData.functionName.equals("null") || mfunctionName.equals("null")) { //
	    	            System.out.println("Filter : Log Err - The functionName is null. Don't add this log to Test Method list.");
	    			} else {
	            		testMethodList.add(new String(tempLogData.calledClass + "." + mfunctionName));    			
	    			}   
	            }            				          
        	}
        	
        }		
	}
	
	private void collectFunctionListForTC(String testCaseName) {
		int i, j;
		LogData tempLogData;
		String temp;
		
        for(i = 0; i < pLogData.size(); i++) {
        	tempLogData = pLogData.get(i);

        	if (tempLogData.testSuiteName.equals(testCaseName)) { 
		    	if (functionListForTC.size() == 0) {
		            if (tempLogData.action.equals("call")) {
		    			if (tempLogData.functionName.equals("<init>")) { //constructor call
		    				functionListForTC.add(new String(tempLogData.calledClass));
		    			} else if (tempLogData.functionName.equals("null")) { //
		    	            System.out.println("Filter : Log Err - The functionName is null. Don't add this log to Test Method list.");
		    			} else {
		    				functionListForTC.add(new String(tempLogData.calledClass + "." + tempLogData.functionName));
		    			}
		            }					
		    	} else {
					String mfunctionName = "null";	            	
					String[] splitText;
					splitText = tempLogData.calledClass.split("[.]");
					
					if (!tempLogData.calledClass.equals("null") && tempLogData.functionName.equals("<init>"))
						mfunctionName =  splitText[splitText.length - 1];
					else
						mfunctionName =  tempLogData.functionName;	        		
		        	
		            for(j = 0; j < functionListForTC.size(); j++) {
		            	temp = functionListForTC.get(j);
		            	if (temp.endsWith(mfunctionName)) {
		            		break;
		            	}
		            } 
		            if (functionListForTC.size() == j && 
		            		tempLogData.action.equals("call")) {
		    			if (tempLogData.functionName.equals("<init>")) { //constructor call
		    				functionListForTC.add(new String(tempLogData.calledClass));
		    			} else if (tempLogData.functionName.equals("null") || mfunctionName.equals("null")) {
		    	            System.out.println("Filter : Log Err - The functionName is null. Don't add this log to Test Method list.");
		    			} else {
		    				functionListForTC.add(new String(tempLogData.calledClass + "." + mfunctionName));    			
		    			}   
		            }            				          
		    	}
        	}
        }
	}
	
	protected boolean setArchitectureNode(int filterRule, String inputParam1, String inputParam2) {
		int i, j;
		boolean ret = false;
 
		graphNode.clear();
		Arch_Channel tempArch;
		LogData tempLogData;
		GraphNode gNodeTemp;
		
		//set graphNode based on parsed architecture data
		if (filterRule == INTER_COMPONENT_FILTER) {
	        for(i = 0; i < pArchitectureData.size(); i++) {
	        	tempArch = pArchitectureData.get(i); 

	        	//Graph node
	        	if (tempArch.start.contains(inputParam1) && tempArch.end.contains(inputParam2)) { // package1 -> package2
	                for(j = 0; j < tempArch.event.size(); j++) {
	    	        	gNodeTemp = new GraphNode();
	    	        	gNodeTemp.caller = inputParam1;
	    	        	gNodeTemp.callee = inputParam2;
	    	        	gNodeTemp.functionName = tempArch.event.get(j);
	            		graphNode.add(gNodeTemp);
	            		ret = true;
	                }
	        	}
	        	if (tempArch.end.contains(inputParam1) && tempArch.start.contains(inputParam2)) { // package2 -> package1
	                for(j = 0; j < tempArch.event.size(); j++) {
	    	        	gNodeTemp = new GraphNode();
	    	        	gNodeTemp.caller = inputParam2;
	    	        	gNodeTemp.callee = inputParam1;
	                	gNodeTemp.functionName = tempArch.event.get(j);
	            		graphNode.add(gNodeTemp);
	            		ret = true;
	                }
	        	}	        	 
	        }	
		}
		else if (filterRule == FILE_FILTER) {
			String mCalledClassName;
			String mfunctionName;

			// selected file is caller or callee	            
	        for(i = 0; i < pLogData.size(); i++) {
	        	tempLogData = pLogData.get(i);
	        	String[] splitText = tempLogData.calledClass.split("[.]");
				mCalledClassName = splitText[splitText.length - 1]+".java";
	        	if (tempLogData.functionName.equals("<init>"))
	        		mfunctionName =  splitText[splitText.length - 1];
	        	else 
	        		mfunctionName =  tempLogData.functionName;	        		
	             
	        	if (tempLogData.fileName.equals(inputParam1) || mCalledClassName.equals(inputParam1)) { 
		            if (tempLogData.action.equals("call") && tempLogData.calledClass.startsWith("com.atmsimulation")) {
			            for(j = 0; j < graphNode.size(); j++) {
			            	gNodeTemp = graphNode.get(j);
			            	if (tempLogData.fileName.equals(gNodeTemp.caller) &&
			            			mfunctionName.equals(gNodeTemp.functionName) &&
			            			mCalledClassName.equals(gNodeTemp.callee)) {
			            		break;
			            	}
			            }
			            if (graphNode.size() == j) {
			        		gNodeTemp = new GraphNode();
			        		gNodeTemp.caller = tempLogData.fileName;
			        		gNodeTemp.callee = mCalledClassName;
		                	gNodeTemp.functionName = mfunctionName;
		                	gNodeTemp.param = tempLogData.inputParams;
		            		graphNode.add(gNodeTemp);
		            		ret = true;
			            }			            
		            }		            
	        	}
	        }	        	
		}
		else if (filterRule == TEST_CASE_FILTER) {  

			String mCalledClassName;
			String mfunctionName;

			// selected file is caller or callee	            
	        for(i = 0; i < pLogData.size(); i++) {
	        	tempLogData = pLogData.get(i);
	        	String[] splitText = tempLogData.calledClass.split("[.]");
				mCalledClassName = splitText[splitText.length - 1]+".java";
	        	if (tempLogData.functionName.equals("<init>"))
	        		mfunctionName =  splitText[splitText.length - 1];
	        	else 
	        		mfunctionName =  tempLogData.functionName;	        		

	        	//Graph node 
	        	if (tempLogData.testSuiteName.equals(inputParam1)) { 
		            if (tempLogData.action.equals("call") && tempLogData.calledClass.startsWith("com.atmsimulation")) {
			            for(j = 0; j < graphNode.size(); j++) {
			            	gNodeTemp = graphNode.get(j);
			            	if (tempLogData.fileName.equals(gNodeTemp.caller) &&
			            			mfunctionName.equals(gNodeTemp.functionName) &&
			            			mCalledClassName.equals(gNodeTemp.callee)) {
			            		break;
			            	}
			            }
			            if (graphNode.size() == j) {
		    	        	gNodeTemp = new GraphNode();
		    	        	gNodeTemp.caller = tempLogData.fileName;
		    	        	gNodeTemp.callee = mCalledClassName;
		    	        	gNodeTemp.functionName = mfunctionName;
		    	        	gNodeTemp.param = tempLogData.inputParams;
		    	        	graphNode.add(gNodeTemp);
		            		ret = true;
			            }			            
		            }		            
	        	}	        	
	        }
		}
		else if (filterRule == TEST_METHOD_FILTER) { 
			setInterfaceNode(inputParam1);			
		}	
        return ret;
	}	 
	
	private boolean setInterfaceNode(String interfaceName) {
		
		int i,j,k;
		boolean ret = false;
		LogData tempLogData;
		GraphNode gNodeTemp;
		
		String mCalledClassName;
		String mfunctionName;
		
        for(k = 0; k < fileList.size(); k++) {
        	String fileNmae = fileList.get(k);
        	
	        for(i = 0; i < pLogData.size(); i++) {
	        	tempLogData = pLogData.get(i);
	        	String[] splitText = tempLogData.calledClass.split("[.]");
				mCalledClassName = splitText[splitText.length - 1]+".java";
	        	if (tempLogData.functionName.equals("<init>"))
	        		mfunctionName =  splitText[splitText.length - 1];
	        	else 
        			mfunctionName =  tempLogData.functionName;	        		 
	        	
	        	if (tempLogData.fileName.equals(fileNmae) || mCalledClassName.equals(fileNmae)) { 
		            if (tempLogData.action.equals("call") && tempLogData.calledClass.startsWith("com.atmsimulation")) {
			            for(j = 0; j < graphNode.size(); j++) {
			            	gNodeTemp = graphNode.get(j);
			            	if (tempLogData.fileName.equals(gNodeTemp.caller) &&
			            			mfunctionName.equals(gNodeTemp.functionName) &&
			            			mCalledClassName.equals(gNodeTemp.callee)) {
			            		break;
			            	}
			            }
			            if (graphNode.size() == j) {
			        		gNodeTemp = new GraphNode();
			        		gNodeTemp.caller = tempLogData.fileName;
			        		gNodeTemp.callee = mCalledClassName;
		                	gNodeTemp.functionName = mfunctionName;
		                	gNodeTemp.param = tempLogData.inputParams;
		            		graphNode.add(gNodeTemp);
		            		ret = true;
			            }			            
		            }		            
	        	}     	
	        	
	        }	        	
        }	        
        return ret;		
	}
	
	protected boolean setInterface (String selectedInterface) {		
		return setInterfaceNode(selectedInterface);		
	}

	protected boolean prePareTestCaseInfo(String testCaseName) {
		boolean ret = false;
		
        collectFunctionListForTC(testCaseName);
        prePareMethodListForTC(testCaseName);
        if (functionListForTC.size() > 0 && methodListForTC.size() > 0)
        	ret = true;

		return ret;
	}
	
	protected boolean prePareTextTreeData(String testCaseName) {
		boolean ret = false;
		boolean mGroupOpenClose = false; 
		int i;
		LogData tempLogData;
		LogData tempLogDataBackup = parsedData.new LogData(testCaseName);
		TextualNode gTextualTemp = null;
		TextualNode gTextualInnerTemp = null;
		String mCalledClassName;
		String mfunctionName;
		
		textualNode.clear(); 
		for(i = 0; i < pLogData.size(); i++) {
			tempLogData = pLogData.get(i);
			if (tempLogData.testSuiteName.equals(testCaseName)) {
				if (tempLogData.action.equals(tempLogDataBackup.action) && 
						tempLogData.lineNumber.equals(tempLogDataBackup.lineNumber) &&
						tempLogData.fileName.equals(tempLogDataBackup.fileName) && 
						tempLogData.inputParams.equals(tempLogDataBackup.inputParams) &&
						tempLogData.functionName.equals(tempLogDataBackup.functionName)) { //skip duplicated log
					continue;
				} else {
					String[] splitText = tempLogData.calledClass.split("[.]");
					mCalledClassName = splitText[splitText.length - 1]+".java";
					if (tempLogData.functionName.equals("<init>"))
						mfunctionName =  splitText[splitText.length - 1];
					else
						mfunctionName =  tempLogData.functionName;
	
					if (tempLogData.action.equals("call")) {// && tempLogData.calledClass.startsWith("com.atmsimulation")
						if (gTextualTemp != null && mGroupOpenClose == true) {
							textualNode.add(gTextualTemp);
							mGroupOpenClose = false;
							ret = true;
						}
						gTextualTemp = new TextualNode();
						gTextualTemp.action = tempLogData.action;
						gTextualTemp.caller = tempLogData.fileName;
						gTextualTemp.callee = mCalledClassName;
						gTextualTemp.lineNumber = tempLogData.lineNumber;
						gTextualTemp.functionName = mfunctionName;
						gTextualTemp.param = tempLogData.inputParams;
						gTextualTemp.innerAction = new ArrayList<TextualNode>();
						mGroupOpenClose = true;
					} else if (!tempLogData.action.equals("null")) {// && tempLogData.calledClass.startsWith("com.atmsimulation")
						if (gTextualTemp != null && mGroupOpenClose == true) {
							gTextualInnerTemp = new TextualNode();
							gTextualInnerTemp.action = tempLogData.action;
							gTextualInnerTemp.caller = tempLogData.fileName;
							gTextualInnerTemp.callee = mCalledClassName;
							gTextualInnerTemp.lineNumber = tempLogData.lineNumber;
							gTextualInnerTemp.functionName = mfunctionName;
							gTextualInnerTemp.param = tempLogData.inputParams;
							gTextualTemp.innerAction.add(gTextualInnerTemp);
						}
					} else {
	    	            System.out.println("Filter : Log Err - The action is null. Don't add this log to text tree.");					
					}				
				}
				tempLogDataBackup.action = tempLogData.action;
				tempLogDataBackup.lineNumber = tempLogData.lineNumber;
				tempLogDataBackup.fileName = tempLogData.fileName;
				tempLogDataBackup.inputParams = tempLogData.inputParams;
				tempLogDataBackup.functionName = tempLogData.functionName;
        	}
				
		}
		if (gTextualTemp != null && mGroupOpenClose == true) {
			textualNode.add(gTextualTemp);
		}		 
		return ret;
	}

	private void prePareMethodListForTC(String testCaseName) {
		boolean mGroupOpenClose = false; 
		int i;
		LogData tempLogData;
		LogData tempLogDataBackup = parsedData.new LogData(testCaseName);
		MethodListForTC gTextualTemp = null;
		MethodListForTC gTextualInnerTemp = null;
		String mCalledClassName;
		String mfunctionName;
		
		methodListForTC.clear(); 
		for(i = 0; i < pLogData.size(); i++) {
			tempLogData = pLogData.get(i);
			if (tempLogData.testSuiteName.equals(testCaseName)) {
				if (tempLogData.action.equals(tempLogDataBackup.action) && 
						tempLogData.lineNumber.equals(tempLogDataBackup.lineNumber) &&
						tempLogData.fileName.equals(tempLogDataBackup.fileName) && 
						tempLogData.inputParams.equals(tempLogDataBackup.inputParams) &&
						tempLogData.functionName.equals(tempLogDataBackup.functionName)) { //skip duplicated log
					continue;
				} else {
					String[] splitText = tempLogData.calledClass.split("[.]");
					mCalledClassName = splitText[splitText.length - 1]+".java";
					if (tempLogData.functionName.equals("<init>"))
						mfunctionName =  splitText[splitText.length - 1];
					else
						mfunctionName =  tempLogData.functionName;
	
					if (tempLogData.action.equals("call")) {// && tempLogData.calledClass.startsWith("com.atmsimulation")
						if (gTextualTemp != null && mGroupOpenClose == true) {
							methodListForTC.add(gTextualTemp);
							mGroupOpenClose = false;
						}
						gTextualTemp = new MethodListForTC();
						gTextualTemp.action = tempLogData.action;
						gTextualTemp.caller = tempLogData.fileName;
						gTextualTemp.callee = mCalledClassName;
						gTextualTemp.lineNumber = tempLogData.lineNumber;
						gTextualTemp.functionName = mfunctionName;
						gTextualTemp.param = tempLogData.inputParams;
						gTextualTemp.innerAction = new ArrayList<MethodListForTC>();
						mGroupOpenClose = true;
					} else if (!tempLogData.action.equals("null")) {// && tempLogData.calledClass.startsWith("com.atmsimulation")
						if (gTextualTemp != null && mGroupOpenClose == true) {
							gTextualInnerTemp = new MethodListForTC();
							gTextualInnerTemp.action = tempLogData.action;
							gTextualInnerTemp.caller = tempLogData.fileName;
							gTextualInnerTemp.callee = mCalledClassName;
							gTextualInnerTemp.lineNumber = tempLogData.lineNumber;
							gTextualInnerTemp.functionName = mfunctionName;
							gTextualInnerTemp.param = tempLogData.inputParams;
							gTextualTemp.innerAction.add(gTextualInnerTemp);
						}
					} else {
	    	            System.out.println("Filter : Log Err - The action is null. Don't add this log to text tree.");					
					}				
				}
				tempLogDataBackup.action = tempLogData.action;
				tempLogDataBackup.lineNumber = tempLogData.lineNumber;
				tempLogDataBackup.fileName = tempLogData.fileName;
				tempLogDataBackup.inputParams = tempLogData.inputParams;
				tempLogDataBackup.functionName = tempLogData.functionName;
        	}
				
		}
		if (gTextualTemp != null && mGroupOpenClose == true) {
			methodListForTC.add(gTextualTemp);
		}	
		
	}
	protected boolean setErrorInfo() {
		throw new UnsupportedOperationException("The method is not implemented yet.");
	}
	
	ArrayList<ErrorInfo> getErrorInfo() {
		return errorInfo;
	}

	ArrayList<GraphNode> getGraphNode() {
		return graphNode;
	}

	ArrayList<TextualNode> getTextualNode() {
		return textualNode;
	}

	ArrayList<MethodListForTC> getMethodListForTC() {
		return methodListForTC;
	}

	protected ArrayList<String> getPackageList() {
		return packageList;
	}	
	
	protected ArrayList<String> getFileList() {
		return fileList;
	}	

	protected ArrayList<String> getTestCaseList() {		
		return testCaseList;
	}

	protected ArrayList<String> getFunctionListForTC() {
		return functionListForTC;
	}

	protected ArrayList<String> getTestMethodList() {		
		return testMethodList;
	}
	
	public class ErrorInfo{
		public String functionName;
		public String errorDescription;
		public String contentsInfo;
	}
	public class GraphNode{
		public String functionName;
		public String caller;
		public String callee;
		public String param;
	}
	public class MethodListForTC{
		public String action; 
		public String functionName; 
		public String caller;
		public String callee;
		public String lineNumber;
		public String param; 
		public ArrayList<MethodListForTC> innerAction;
	}
	public class TextualNode{
		public String action; 
		public String functionName; 
		public String caller;
		public String callee;
		public String lineNumber;
		public String param; 
		public ArrayList<TextualNode> innerAction;
	}
}
