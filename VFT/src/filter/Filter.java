package filter;

import java.util.ArrayList; 

public class Filter {
	
	protected final int INTER_COMPONENT_FILTER = 1;
	protected final int FILE_FILTER = 2;
    protected final int TEST_CASE_FILTER = 3;
    protected final int TEST_METHOD_FILTER = 4;
    
	protected static String interfaceName;
	protected static ArrayList<ErrorInfo> errorInfo = new ArrayList<ErrorInfo>();
	protected static ArrayList<GraphNode> graphNode = new ArrayList<GraphNode>();
	protected static ArrayList<TextualNode> textualNode = new ArrayList<TextualNode>();
	private ArrayList<ArchData> pArchitectureData = new ArrayList<ArchData>();	// parsed architecture data
	private ArrayList<LogData> pLogData = new ArrayList<LogData>();	// parsed log data

	protected Filter() {  
		//collect basic information here -> architecture data(architectureData) and log data				
				
	}
	
	private void setArchData() {
    	ArchData temp = new ArchData();
    	temp.event = new ArrayList<String>();
    	temp.name = "chAS";
    	temp.start = "CAtm";
    	temp.end = "CSimulation";
    	temp.event.add(new String("acceptEnvelope"));
    	temp.event.add(new String("clearDisplay"));
    	temp.event.add(new String("dispenseCash"));
    	temp.event.add(new String("display"));
    	temp.event.add(new String("ejectCard"));
    	temp.event.add(new String("getInitialCash"));
    	temp.event.add(new String("getInstance"));
    	temp.event.add(new String("printLogLine"));
    	temp.event.add(new String("printReceiptLine"));
    	temp.event.add(new String("readCard"));
    	temp.event.add(new String("readInput"));
    	temp.event.add(new String("retainCard"));
    	temp.event.add(new String("sendMessage"));    	
    	pArchitectureData.add(temp);
        
    	temp = new ArchData();
    	temp.event = new ArrayList<String>();
    	temp.name = "chSA";
    	temp.start = "CSimulation";
    	temp.end = "CAtm";
    	temp.event.add(new String("cardInserted"));
    	temp.event.add(new String("switchOn"));
    	temp.event.add(new String("switchOff"));	
    	pArchitectureData.add(temp);

    	temp = new ArchData();
    	temp.event = new ArrayList<String>();
    	temp.name = "chSB";
    	temp.start = "CSimulation";
    	temp.end = "CBanking";
    	temp.event.add(new String("setBalance"));
    	temp.event.add(new String("Card"));
    	temp.event.add(new String("getNumber"));
    	temp.event.add(new String("getAmount"));
    	temp.event.add(new String("getCard"));
    	temp.event.add(new String("getFromAccount"));
    	temp.event.add(new String("getMessageCode"));
    	temp.event.add(new String("getPin"));
    	temp.event.add(new String("getToAccount"));
    	pArchitectureData.add(temp);

    	temp = new ArchData();
    	temp.event = new ArrayList<String>();
    	temp.name = "chAB";
    	temp.start = "CAtm";
    	temp.end = "CBanking";
    	temp.event.add(new String("ACCOUNT_ABBREVIATIONS"));
    	temp.event.add(new String("ACCOUNT_NAMES"));
    	temp.event.add(new String("Balances"));
    	temp.event.add(new String("Message"));
    	temp.event.add(new String("Message.setPIN"));
    	temp.event.add(new String("Message.toString"));
    	temp.event.add(new String("Receipt.getLines"));
    	temp.event.add(new String("Status.getMessage"));
    	temp.event.add(new String("Status.inInvalidPIN"));
    	temp.event.add(new String("Status.isSuccess"));
    	temp.event.add(new String("Status.toString"));
    	pArchitectureData.add(temp);

    	temp = new ArchData();
    	temp.event = new ArrayList<String>();
    	temp.name = "chBA";
    	temp.start = "CBanking";
    	temp.end = "CAtm";
    	temp.event.add(new String("Transaction.getSerialNumber"));
    	temp.event.add(new String("ATM.getBankName"));
    	temp.event.add(new String("ATM.getID"));
    	temp.event.add(new String("ATM.getPlace"));
    	pArchitectureData.add(temp);
    }

	protected boolean setArchitectureNode(int filterRule, String inputParam1, String inputParam2) {
		int i, j;
		boolean ret = false;

        long start = System.currentTimeMillis();
        
		setArchData();
		graphNode.clear();
		textualNode.clear();
		ArchData temp;
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

        long end = System.currentTimeMillis();	        
		System.out.println("Filter : time for setArchitectureNode (ms) " +  (end - start));		
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
		String functionName;
		String errorDescription;
		String contentsInfo;
	}
	public class GraphNode{
		String functionName;
		String caller;
		String callee;
	}
	public class TextualNode{
		String functionName;
		String contentsInfo;
		TextualNode textualNode;
	}	
	public class ArchData{
		String name;
		String start;
		String end;
		ArrayList<String> event;
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
