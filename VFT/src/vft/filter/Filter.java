package vft.filter;

import java.io.IOException;
import java.util.ArrayList;

import org.xml.sax.SAXException;

import vft.parser.parser;
import vft.parser.parser.Arch_Channel;

public class Filter {
	
	protected final int INTER_COMPONENT_FILTER = 1;
	protected final int FILE_FILTER = 2;
    protected final int TEST_CASE_FILTER = 3;
    protected final int TEST_METHOD_FILTER = 4;
    
	protected static String interfaceName;
	protected static ArrayList<ErrorInfo> errorInfo = new ArrayList<ErrorInfo>();
	protected static ArrayList<GraphNode> graphNode = new ArrayList<GraphNode>();
	protected static ArrayList<TextualNode> textualNode = new ArrayList<TextualNode>();
	private ArrayList<Arch_Channel> pArchitectureData = new ArrayList<Arch_Channel>();	// parsed architecture data
	private ArrayList<LogData> pLogData = new ArrayList<LogData>();	// parsed log data

	protected Filter() {  
		//collect basic information here -> architecture data(architectureData) and log data				
		parser parsedArch = null;
		try {
			parsedArch = new parser();
			pArchitectureData = parsedArch.get_pared_Arch();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected boolean setArchitectureNode(int filterRule, String inputParam1, String inputParam2) {
		int i, j;
		boolean ret = false;

        //long start = System.currentTimeMillis();
        
		//setArchData();
		graphNode.clear();
		textualNode.clear();
		Arch_Channel temp;
		GraphNode gNodeTemp;
		
		//set graphNode based on parsed architecture data
		if (filterRule == INTER_COMPONENT_FILTER) {
	        for(i = 0; i < pArchitectureData.size(); i++) {
	        	temp = pArchitectureData.get(i);

	        	//Graph node
	        	if (temp.start.contains(inputParam1) && temp.end.contains(inputParam2)) { // package1 -> package2
	                for(j = 0; j < temp.event.size(); j++) {
	    	        	gNodeTemp = new GraphNode();
		        		gNodeTemp.caller = inputParam1;
		        		gNodeTemp.callee = inputParam2;
	                	gNodeTemp.functionName = temp.event.get(j);
	            		graphNode.add(gNodeTemp);
	            		ret = true;
	                }
	        	}
	        	if (temp.end.contains(inputParam1) && temp.start.contains(inputParam2)) { // package2 -> package1
	                for(j = 0; j < temp.event.size(); j++) {
	    	        	gNodeTemp = new GraphNode();
		        		gNodeTemp.caller = inputParam2;
		        		gNodeTemp.callee = inputParam1;
	                	gNodeTemp.functionName = temp.event.get(j);
	            		graphNode.add(gNodeTemp);
	            		ret = true;
	                }
	        	}	        	
	        	//TO DO : Text-tree node
	        	
	        	
	        	
	        }	
		}
		else if (filterRule == FILE_FILTER) {  	        	
			//TO DO : Need to list-up functions in selected source file, but this information is not given from XML file.
			//        So, we need to check it from log file, but , it can be incomplete...
		}
		else if (filterRule == TEST_CASE_FILTER) {  
			//TO DO : Need to list-up functions in selected test case file, but this information is not given from XML file.
			//        So, we need to check it from log file, but , it can be incomplete...
		}
		else if (filterRule == TEST_METHOD_FILTER) {  
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
	public class LogData{
		String start;
		String end;
		String fileName;
		String lineNumber;
		String action;
		String functionName;
		String inputValue;		
	}
}
