package vft.parser; 


import java.io.BufferedReader; 
import java.io.IOException; 
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.ArrayList;  
import java.util.regex.Matcher; 
import java.util.regex.Pattern; 
 
import javax.xml.parsers.DocumentBuilder; 
import javax.xml.parsers.DocumentBuilderFactory; 
import javax.xml.parsers.ParserConfigurationException; 
 
import org.w3c.dom.Document; 
import org.w3c.dom.Element; 
import org.w3c.dom.Node; 
import org.w3c.dom.NodeList; 

import org.xml.sax.SAXException; 
 

public class parser {
	// Architecture Class
	public class Arch_Channel {
		public String name;
		public String start;
		public String end;
		public ArrayList<String> event;
	}
	
	// Log Class
	public class LogData{
		public String testSuiteName;
		public String fileName;
		public String lineNumber;
		public String functionName;
		public String start;
		public String end;
		public String calledClass;
		public String action;
		public String inputParams;
		public String errorMsg;

		public LogData(String tsName){
			testSuiteName = tsName;
		}

		public void set_edge(String node, String edge){

			String[] array = node.split("::");  //start, end node
			String transition = array[1];
			
			array = transition.split("->");
			start = array[0];
			end = array[1];
			String[] elements = edge.split(",");
			
			patternMatcher("inputparameter", edge, array, elements);
			patternMatcher("fileName", edge, array, elements);
			patternMatcher("functionName", edge, array, elements);
			patternMatcher("calledClass", edge, array, elements);
			patternMatcher("action", edge, array, elements);
			patternMatcher("lineNumber", edge, array, elements);

		}
		
		private void patternMatcher(String type, String edge, String[] array, String[] elements){
			
			Pattern p;
			Matcher m;

			switch (type) {
				case "inputparameter":
					p = Pattern.compile("\\[(.*?)\\]"); //input parameter
					m = p.matcher(edge);
					if(m.find()) {
						inputParams = m.group(1);
					}else{
						inputParams = "null";
					}
					break;
					
				case "fileName":
					p = Pattern.compile(",(.*?)\\.java"); //filename
					m = p.matcher(edge);
					if(m.find()) {
						fileName = m.group(1) + ".java";
					}else{
						fileName = "null";
					}
					break;
					
				case "functionName":
					p = Pattern.compile("\\),(.*?),\\["); //functionName
					m = p.matcher(edge);
					if(m.find()) {
						array = m.group(1).split(",");
						if(array.length == 3){
							functionName = array[2];
						}else{
							functionName = "null";
						}
					} else {
						functionName = "null";
					}
					break;
				
				case "calledClass":
					p = Pattern.compile(",((java|com).*?),"); //calledClass
					m = p.matcher(edge);
					if(m.find()) {
						calledClass = m.group(1);
					}else{
						calledClass = "null";
					}
					break;
				
				case "action":
					if(elements.length > 3){
						p = Pattern.compile("(.*?)\\("); //action
						m = p.matcher(elements[3]);
						if(m.find()) {
							action = m.group(1);
						}else{
							action = "null";
						}
					}else{
						action = "null";
					}
					break;
				
				case "lineNumber":
					if(elements.length > 2){
						lineNumber = elements[2];
					}else{
						lineNumber = "null";
					}
					break;
			
			}
			
		}
	}
	
	private String ArchitectureFile = "/logs/ATMSimulationSystemXML_2016_05_10.txt";
	private String LogFile = "/logs/PO_log_20160510_2024.txt";
	
	private DocumentBuilderFactory dbf;
	private DocumentBuilder xml_parser;
	private Document xml_doc;
	
	private ArrayList<Arch_Channel> parsed_Arch;
	private ArrayList<LogData> parsed_LogData;
	
	public parser() throws SAXException, IOException, ParserConfigurationException{
		
		//xml parsing
		xml_parsing(ArchitectureFile);
		
		//log parsing
		log_parsing(LogFile);
	}
	
	public void xml_parsing(String arcitectureFile) throws SAXException, ParserConfigurationException, IOException {
		
		dbf = DocumentBuilderFactory.newInstance();
		xml_parser = dbf.newDocumentBuilder();
			
		InputStream input = parser.class.getResourceAsStream(arcitectureFile);
		if (input == null) {
			input = parser.class.getResourceAsStream("../test" + arcitectureFile);
		}
		
		xml_doc = xml_parser.parse(input);
		
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
	}
 	 
 	public void log_parsing(String logFile) throws IOException{
 
		 InputStream input = parser.class.getResourceAsStream(logFile);
			 if (input == null) {
 				 input = parser.class.getResourceAsStream("../test" + logFile);
 			 }
		 BufferedReader reader = new BufferedReader(new InputStreamReader(input)); 
		  
		 String line = null; 
		 parsed_LogData = new ArrayList<LogData>(); 
		 ArrayList<String> lines = new ArrayList<String>(); 
	  
		 LogData sig; 
		  
		 while((line = reader.readLine()) != null){ 
			 line = line.replaceFirst("\t", ""); 
			 line = line.replaceFirst("<", ""); 
			 line = replaceLast(line, ">", ""); 


			 if(!line.equals("setUp()") && !line.equals("/setUp()") && !line.equals("tearDown()") && !line.equals("/tearDown()")){ 
				 lines.add(line); 
			 }			  
		 } 
		  
		 line = lines.remove(0); 
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
		 
		 reader.close();

 	}
 	
 	public ArrayList<LogData> get_parsed_LogData(){
 		return parsed_LogData;
 	}
 	
 	public ArrayList<Arch_Channel> get_pared_Arch(){
 		return parsed_Arch;
 	}
 	
 	private static String replaceLast(String string, String toReplace, String replacement) {
 		int pos = string.lastIndexOf(toReplace);
 		if (pos > -1) {
 			return string.substring(0, pos)+ replacement + string.substring(pos +   toReplace.length(), string.length());
 		} else {
 			return string;
 		}
 	}
 } 
