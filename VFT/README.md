#Development guideline for VFT

##Structure of VFT project  
VFT project consists of parser, view, filter.  
Each parts match vft.parser, vft.filter, (vft, vft.views) packages.  
Each packages have following java sourcre code.  

###[parser]
+ vft.parser
    + parser.java

###[filter]
+ vft.filter
    + FilterWrapper.java => wrapper of filter
    + Filter.java => parent of each filter module
    + InterComponentFilter.java => filter for inter component filtering rule
    + FileFilter.java => filter for file filtering rule
    + TestCaseFilter.java => filter for test case filtering rule
    + TestMethodFilter.java => filter for test method filtering rule

###[view]
+ vft
    + Activator.java => eclipse plug-in activator
+ vft.views
    + VFTView.java => all for view part consist of graph & tree view & select bottons
    + VFTGraph.java => graph view part
    + VFTTree.java => texture tree view part

##Parsing module
The main file of Parsing module is "parser.java" file.  
The main class is "parser" defined in "parser.java".  

###[First]
When parser's construct call "parser()" is created in filter's construct call,   "Filter()", "xml_parsing()" and "log_parsing()" functions are performed.  
These two functions are received log files in "logs" directory.  
The input log files are located in "logs".  
Each logs path is mathced "ArchitectureFileName" and "LogFileName" variable.  

###[Second]
"xml_parsing()" sets "parsed_Arch" variable with xml parser in java library & architecture file.  
"parsed_Arch" consists of "Arch_channel" classes.  
The "Arch_Channel" class is for Architecture Spec file.  
So the "parsed_Arch" variable in type of "ArrayList of class Arch_Channel" is filled by parsing Archtecture Spec file.  

"log_parsing()" sets "parsed_LogData" variable with pattern & matcher in java library & log file.  
"parsed_LogData" consists of "LogData" classes.  
The "LogData" class is for log file.  
So the "parsed_LogData" variable in type of "ArrayList of class LogData" is filled by parsing log file.  

###[Third]
If you want to change the log files, you should set *ArchitectureFileName* and *LogFileName* variable.  
If you want to change parsing rule, you should change "xml_parsing()" and "log_parsing()" function.  

"xml_parsing()" function just use java xml parsing library.  
So, If you change your architecture file's rule. you should change attribute's & tag's name  
```
		
		// Example of architecture parser in "xml_parsing()" function
		parsed_Arch = new ArrayList<Arch_Channel>();
		
		NodeList all = xml_doc.getElementsByTagName("channel");
		for(int i=0; i < all.getLength(); i++){
			Node node = all.item(i);
			if (node instanceof Element) {
				Arch_Channel ac = new Arch_Channel();
				Element e = (Element) node;
				
				ac.name = node.getNodeName();
				ac.start = e.getAttribute("start");
				ac.end = e.getAttribute("end");
				
				NodeList events = e.getElementsByTagName("event");
				ac.event = new ArrayList<String>();
				
				for(int j = 0; j < events.getLength(); j++){
					Node event = events.item(j);
					if(event instanceof Element){
						Element event_e = (Element) event;
						ac.event.add(event_e.getAttribute("name"));
					}
				}

				parsed_Arch.add(ac);
			}
		}
```
"log_parsing()" function use java pattern & matcher library.  
Each parsing rules are in "set_edge" & "patternMatcher" function.  
So, If you change your log file's rule. you should change "patternMatcher" function's parsing rule.  
```
		
		 // Example of log parser in "log_parsing()" function
		 while(!lines.isEmpty()){ 
			 if(line.matches("^test.*$")){ 
				 String testSuiteName = line;				  
				  
				 line = lines.remove(0); 
				 while(!line.matches("^PO.*$")){ 
					 line = lines.remove(0); 
				 } 
				  
				 while(!line.matches("^test.*$") && !lines.isEmpty()){ 
					 if(line.matches("^PO.*$")){ 
						 sig = new LogData(testSuiteName); 
						  
						 String node = line;					  
						  
						 line = lines.remove(0); 
						 String edge = line; 
						 sig.set_edge(node, edge); 
						 parsed_LogData.add(sig); 
					 } 
					 if(!lines.isEmpty()){ 
						 line = lines.remove(0); 
					 } 
				 }				 
			 }else{ 
				 line = lines.remove(0); 
			 } 
		 } 
```

##Filtering module
The main file of Filtering module is "Filter.java" file.  
The main class is "Filter" defined in "Filter.java".  
"FilterWrapper.java"'s "FilterWrapper" class is extended by "Filter" class.  
And Setting options to other filtering rule class.  
"InterComponentFilter.java"'s "InterComponentFilter" class,  
"FileFilter.java"'s "FileFilter" class,  
"TestCaseFilter.java"'s "TestCaseFilter" class,  
"TestMethodFilter.java"'s "TestMethodFilter" class  
are also extended by "Filter" class and have their own setting funtions by filtering Rule.  

###[First]
When FilterWrapper's construct call "FilterWrapper()" is created in Viewer's construct call "createPartControl()",  
"get_parsed_LogData()" and "get_pared_Arch()" functions are performed and set result to "pLogData", "pArchitectureData" variable.  

###[Second]
If FilterWrapper's construct call was executed, Viewer call "prePareLogData()" function.  
"prePareLogData()" collect basic information from "pLogData", "pArchitectureData" variable.  
When "prePareLogData()" function was executed,  
Filter class has "packageList", "fileList", "testCaseList", "testMethodList" variable perfectly.  

###[Third]
In UI, If user set filtering Rule and specific component,  
Viewer call "selectComponent()" function.  
"selectComponent()" function call each Filter class's "setFilterRule" function matched by filtering rule.  
Filter class's "setFilterRule" function call "Filter" class's "setArchitectureNode" function.  
"setArchitectureNode" function set "graphNode" variable matched by filtering rule.  

###[Forth]
If "selectComponent()" function was executed, Viewer can draw graph & texture with "graphNode" variable.  

###[Fifth]
If you want to change filtering rule, you should change filtering rule in "Filter" class.  
```
	
	// Filtering rule
	public final int INTER_COMPONENT_FILTER = 1;
	public final int FILE_FILTER = 2;
	public final int TEST_CASE_FILTER = 3;
	public final int TEST_METHOD_FILTER = 4;
```
If you want to change filter, you should change "collectFilterInfoForFirstPage()" function and "setArchitectureNode()" function.  
```
	
			// Example of set "packageList", "fileList", "testCaseList", "testMethodList" in "collectFilterInfoForFirstPage()" class
			// Test case list
			if (tempLogData.testSuiteName.equals("null")) {
				System.out.println("Filter : Log Err - The testSuiteName is null. Don't add this log to Test case list.");
			} else {
				if (testCaseList.size() == 0) {
					testCaseList.add(new String(tempLogData.testSuiteName));
				} else {
					for (j = 0; j < testCaseList.size(); j++) {
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

```
```
	
			// Example of set "graphNode" in "setArchitectureNode()" class
			// selected file is caller or callee
			for (i = 0; i < pLogData.size(); i++) {
				tempLogData = pLogData.get(i);
				String[] splitText = tempLogData.calledClass.split("[.]");
				mCalledClassName = splitText[splitText.length - 1] + ".java";
				if (tempLogData.functionName.equals("<init>")) {
					mfunctionName = splitText[splitText.length - 1];
				} else {
					mfunctionName = tempLogData.functionName;
				}

				if (tempLogData.fileName.equals(inputParam1) || mCalledClassName.equals(inputParam1)) {
					if (tempLogData.action.equals("call") && tempLogData.calledClass.startsWith("com.atmsimulation")) {
						for (j = 0; j < graphNode.size(); j++) {
							gNodeTemp = graphNode.get(j);
							if (tempLogData.fileName.equals(gNodeTemp.caller)
									&& mfunctionName.equals(gNodeTemp.functionName)
									&& mCalledClassName.equals(gNodeTemp.callee)) {
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

```