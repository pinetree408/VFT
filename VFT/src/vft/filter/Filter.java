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
    public static ArrayList<String> testMethodList = new ArrayList<String>(); //for Test Method filter

    public static String interfaceName;
	public static ArrayList<ErrorInfo> errorInfo = new ArrayList<ErrorInfo>();
	public static ArrayList<GraphNode> graphNode = new ArrayList<GraphNode>();
	public static ArrayList<TextualNode> textualNode = new ArrayList<TextualNode>();
	private ArrayList<Arch_Channel> pArchitectureData = new ArrayList<Arch_Channel>();	// parsed architecture data
	private ArrayList<LogData> pLogData = new ArrayList<LogData>();	// parsed log data
	parser parsedArch = null;
	
	protected Filter() {  			
		try {
			parsedArch = new parser();
			pArchitectureData = parsedArch.get_pared_Arch();
			pLogData = parsedArch.get_parsed_LogData();
			//setlogdata();
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
	
	protected ArrayList<String> getPackageList() {
		return packageList;
	}	
	
	protected ArrayList<String> getFileList() {
		return fileList;
	}	

	protected ArrayList<String> getTestCaseList() {		
		return testCaseList;
	}
	
	protected ArrayList<String> getTestMethodList() {		
		return testMethodList;
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
		            if (testCaseList.size() == j)
		            	testCaseList.add(new String(tempLogData.testSuiteName));	            
	        	}
        	}
        	
        	// Test method list
        	if (testMethodList.size() == 0) {
	            if (tempLogData.action.equals("call") &&
	            		tempLogData.calledClass.startsWith("com.atmsimulation")) {
	            	String[] splitText = tempLogData.calledClass.split("[.]");
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
	
	protected boolean setArchitectureNode(int filterRule, String inputParam1, String inputParam2) {
		int i, j;
		boolean ret = false;

        //long start = System.currentTimeMillis();
        
		//setArchData();
		graphNode.clear();
		textualNode.clear();
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
	        	//TO DO : Text-tree node
	        	
	        	
	        	
	        	
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

	            //System.out.println("Filter FILE_FILTER : calledClass " + tempLogData.calledClass + " function " + mfunctionName+ " action "+tempLogData.action);
	            
	        	//Graph node 
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
		            		graphNode.add(gNodeTemp);
			            }			            
		            }		            
	        	}
	        	
	        	//TO DO : Text-tree node        	
	        	
	        	
	        }	        	
		}
		else if (filterRule == TEST_CASE_FILTER) {  

			String mCalledClassName;

			// selected file is caller or callee	            
	        for(i = 0; i < pLogData.size(); i++) {
	        	tempLogData = pLogData.get(i);
	        	String[] splitText = tempLogData.calledClass.split("[.]");
				mCalledClassName = splitText[splitText.length - 1]+".java";

	        	//Graph node 
	        	if (tempLogData.testSuiteName.equals(inputParam1)) { 
		            if (tempLogData.action.equals("call") && tempLogData.calledClass.startsWith("com.atmsimulation")) {
			            for(j = 0; j < graphNode.size(); j++) {
			            	gNodeTemp = graphNode.get(j);
			            	if (tempLogData.fileName.equals(gNodeTemp.caller) &&
			            			tempLogData.functionName.equals(gNodeTemp.functionName) &&
			            			mCalledClassName.equals(gNodeTemp.callee)) {
			            		break;
			            	}
			            }
			            if (graphNode.size() == j) {
			        		gNodeTemp = new GraphNode();
			        		gNodeTemp.caller = tempLogData.fileName;
			        		gNodeTemp.callee = mCalledClassName;
		                	gNodeTemp.functionName = tempLogData.functionName;
		            		graphNode.add(gNodeTemp);
			            }			            
		            }		            
	        	}
	        	
	        	//TO DO : Text-tree node        	
	        	
	        	
	        }	        	
		}
		else if (filterRule == TEST_METHOD_FILTER) {
			// 1.package and interface  
			
			
			
			
		}
		/* for debugging
        long end = System.currentTimeMillis();	      
		GraphNode gNodeToDebug =  new GraphNode(); 
        for(i = 0; i < graphNode.size(); i++) {
        	gNodeToDebug = graphNode.get(i);
        	System.out.println("Filter :  " + gNodeToDebug.caller + " -> " + gNodeToDebug.functionName + " -> " + gNodeToDebug.callee);
        }
		System.out.println("Filter : time for setArchitectureNode (ms) " +  (end - start));
		 */		
        return ret;
	}	 
	
	protected boolean setInterface (String selectedInterface) {
		int i;
		boolean ret = false;
		interfaceName = selectedInterface;
		LogData temp;
		GraphNode gNodeTemp;
		
		//set graphNode based on parsed log data
		graphNode.clear();
		textualNode.clear();
        for(i = 0; i < pLogData.size(); i++) {
        	temp = pLogData.get(i);
        	
        	if (temp.functionName.equals(selectedInterface)) {
        		gNodeTemp = new GraphNode();
        		gNodeTemp.caller = temp.start;
        		gNodeTemp.callee = temp.end;
        		gNodeTemp.functionName = temp.functionName;
        		graphNode.add(gNodeTemp);
        		ret = true;
        	}
        }
		

		//set TextualNode based on parsed log data
		
		
		return ret;		
	}

	protected boolean setTextualNode() {
		throw new UnsupportedOperationException("The method is not implemented yet.");
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
/*
	private void setlogdata() {
		LogData tempLogData;
		tempLogData = new LogData();
		tempLogData.testSuiteName = new String("testBankName");
		tempLogData.start = new String("CSimulation");
		tempLogData.end = new String("CAtm");
		tempLogData.fileName = new String("ATMApplet.java");
		tempLogData.lineNumber = new String("31");
		tempLogData.functionName = new String("ATM");
		tempLogData.calledClass = new String("com.atmsimulation.atm.ATM");
		tempLogData.action = new String("call");
		tempLogData.inputParams = new String("42, Gordon College, First National Bank of Podunk, null");
		tempLogData.errorMsg = new String("null");
		pLogData.add(tempLogData);
		
		tempLogData = new LogData();
		tempLogData.testSuiteName = new String("testBankName");
		tempLogData.start = new String("CSimulation");
		tempLogData.end = new String("CAtm");
		tempLogData.fileName = new String("ATM.java");
		tempLogData.lineNumber = new String("40");
		tempLogData.functionName = new String("ATM");
		tempLogData.calledClass = new String("com.atmsimulation.atm.ATM");
		tempLogData.action = new String("preinitialization");
		tempLogData.inputParams = new String("42, Gordon College, First National Bank of Podunk, null");
		tempLogData.errorMsg = new String("null");
		pLogData.add(tempLogData);

		tempLogData = new LogData();
		tempLogData.testSuiteName = new String("testBankName");
		tempLogData.start = new String("CSimulation");
		tempLogData.end = new String("CAtm");
		tempLogData.fileName = new String("ATM.java");
		tempLogData.lineNumber = new String("40");
		tempLogData.functionName = new String("ATM");
		tempLogData.calledClass = new String("com.atmsimulation.atm.ATM");
		tempLogData.action = new String("initialization");
		tempLogData.inputParams = new String("42, Gordon College, First National Bank of Podunk, null");
		tempLogData.errorMsg = new String("null");
		pLogData.add(tempLogData);
		 
		tempLogData = new LogData();
		tempLogData.testSuiteName = new String("testBankName");
		tempLogData.start = new String("CSimulation");
		tempLogData.end = new String("CAtm");
		tempLogData.fileName = new String("ATM.java");
		tempLogData.lineNumber = new String("40");
		tempLogData.functionName = new String("Runnable");
		tempLogData.calledClass = new String("java.lang.Runnable");
		tempLogData.action = new String("initialization");
		tempLogData.inputParams = new String("null");
		tempLogData.errorMsg = new String("null");
		pLogData.add(tempLogData);
		
		tempLogData = new LogData();
		tempLogData.testSuiteName = new String("testBankName");
		tempLogData.start = new String("CSimulation");
		tempLogData.end = new String("CAtm");
		tempLogData.fileName = new String("ATM.java");
		tempLogData.lineNumber = new String("45");
		tempLogData.functionName = new String("bankAddress");
		tempLogData.calledClass = new String("com.atmsimulation.atm.ATM");
		tempLogData.action = new String("set");
		tempLogData.inputParams = new String("null");
		tempLogData.errorMsg = new String("null");
		pLogData.add(tempLogData);
		
		tempLogData = new LogData();
		tempLogData.testSuiteName = new String("testBankName");
		tempLogData.start = new String("CSimulation");
		tempLogData.end = new String("CAtm");
		tempLogData.fileName = new String("ATM.java");
		tempLogData.lineNumber = new String("49");
		tempLogData.functionName = new String("Log");
		tempLogData.calledClass = new String("com.atmsimulation.atm.physical.Log");
		tempLogData.action = new String("call");
		tempLogData.inputParams = new String("null");
		tempLogData.errorMsg = new String("null");
		pLogData.add(tempLogData);

		tempLogData = new LogData();
		tempLogData.testSuiteName = new String("testBankName");
		tempLogData.start = new String("CBanking");
		tempLogData.end = new String("CAtm");
		tempLogData.fileName = new String("CashDispenser.java");
		tempLogData.lineNumber = new String("28");
		tempLogData.functionName = new String("Money");
		tempLogData.calledClass = new String("com.atmsimulation.banking.Money");
		tempLogData.action = new String("call");
		tempLogData.inputParams = new String("null");
		tempLogData.errorMsg = new String("null");
		pLogData.add(tempLogData);	 


		tempLogData = new LogData();
		tempLogData.testSuiteName = new String("testBankName");
		tempLogData.start = new String("CSimulation");
		tempLogData.end = new String("CAtm");
		tempLogData.fileName = new String("Money.java");
		tempLogData.lineNumber = new String("48");
		tempLogData.functionName = new String("append");
		tempLogData.calledClass = new String("java.lang.StringBuilder");
		tempLogData.action = new String("call");
		tempLogData.inputParams = new String("null");
		tempLogData.errorMsg = new String("null");
		pLogData.add(tempLogData);	 
		
		tempLogData = new LogData();
		tempLogData.testSuiteName = new String("testBankName");
		tempLogData.start = new String("CAtm");
		tempLogData.end = new String("CSimulation");
		tempLogData.fileName = new String("CustomerConsole.java");
		tempLogData.lineNumber = new String("35");
		tempLogData.functionName = new String("getInstance");
		tempLogData.calledClass = new String("com.atmsimulation.simulation.Simulation");
		tempLogData.action = new String("call");
		tempLogData.inputParams = new String("null");
		tempLogData.errorMsg = new String("null");
		pLogData.add(tempLogData);	 
		
		tempLogData = new LogData();
		tempLogData.testSuiteName = new String("testBankName");
		tempLogData.start = new String("CAtm");
		tempLogData.end = new String("CSimulation");
		tempLogData.fileName = new String("CustomerConsole.java");
		tempLogData.lineNumber = new String("35");
		tempLogData.functionName = new String("display");
		tempLogData.calledClass = new String("com.atmsimulation.simulation.Simulation");
		tempLogData.action = new String("call");
		tempLogData.inputParams = new String("Not currently available");
		tempLogData.errorMsg = new String("null");
		pLogData.add(tempLogData);	 
		
		tempLogData = new LogData();
		tempLogData.testSuiteName = new String("testBankName");
		tempLogData.start = new String("CAtm");
		tempLogData.end = new String("CSimulation");
		tempLogData.fileName = new String("Simulation.java");
		tempLogData.lineNumber = new String("129");
		tempLogData.functionName = new String("display");
		tempLogData.calledClass = new String("com.atmsimulation.simulation.SimDisplay");
		tempLogData.action = new String("call");
		tempLogData.inputParams = new String("Not currently available");
		tempLogData.errorMsg = new String("null");
		pLogData.add(tempLogData);	 
		

	}
	*/
	
	public class ErrorInfo{
		public String functionName;
		public String errorDescription;
		public String contentsInfo;
	}
	public class GraphNode{
		public String functionName;
		public String caller;
		public String callee;
	}
	public class TextualNode{
		public String functionName;
		public String contentsInfo;
		public TextualNode textualNode;
	}	
	/*
	public class LogData{
		 public String testSuiteName; 
		 public String start; 
		 public String end; 
		 public String fileName;  
		 public String lineNumber;  
		 public String functionName;  
		 public String calledClass;  
		 public String action;           
		 public String inputParams;   
		 public String errorMsg; 
	}*/	
}
