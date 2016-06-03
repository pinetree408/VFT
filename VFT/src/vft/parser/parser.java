package vft.parser; 


 import java.io.BufferedReader; 
import java.io.File; 
import java.io.FileNotFoundException; 
import java.io.FileReader; 
import java.io.IOException; 
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL; 
import java.nio.file.Paths; 
import java.util.ArrayList; 
import java.util.Enumeration; 
import java.util.List; 
import java.util.Properties; 
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
 	 private DocumentBuilderFactory dbf; 
 	 private DocumentBuilder xml_parser; 
 	 private Document xml_doc; 
 	 private Element xml_root; 
 	 private ArrayList<Arch_Channel> parsed_Arch; 
 	 private ArrayList<LogData> parsed_LogData; 
 	 private String ArchitectureFileName = "ATMSimulationSystemXML_2016_05_10.txt";
 	 private String LogFileName = "PO_log_20160510_2024.txt";
 	 
 	 public parser() throws SAXException, IOException{ 
 		  
 		 //URL classURL = getClass().getProtectionDomain().getCodeSource().getLocation(); 
 		 //String path = classURL.toString(); 
 		 //path = path.replace("file:/", ""); 
 		 		  
 		 //xml parsing 
 		 try{ 
 			  
 			 dbf = DocumentBuilderFactory.newInstance(); 
 	 
 			 xml_parser = dbf.newDocumentBuilder(); 
 	 
 			 xml_doc = xml_parser.parse(parser.class.getResourceAsStream("/logs/ATMSimulationSystemXML_2016_05_10.txt")); 
 	 
 			 xml_root = xml_doc.getDocumentElement(); 
 			  
 		 }catch(ParserConfigurationException e){ 
 			  
 		 }catch(IOException e) { 
 			  
 		 }  
 		 parsed_Arch = new ArrayList<Arch_Channel>(); 
 		  
 		 xml_parsing(); 
 		  
 		  
 		 //log parsing 
 		 InputStream input = parser.class.getResourceAsStream("/logs/PO_log_20160510_2024.txt");
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
 				 //System.out.println(line); 
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
 					 //System.out.println(line); 
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
 		// for debugging  
 		  /*
 		 for(int i = 0; i < parsed_LogData.size(); i++){ 
 			 LogData temp = parsed_LogData.get(i); 
 			 temp.print_LogData(); 
 		 } 
 		  */
 		 reader.close(); 
 	 } 
 	  
 	 private static String replaceLast(String string, String toReplace, String replacement) {     
 		   int pos = string.lastIndexOf(toReplace);      
 		   if (pos > -1) {         
 		   return string.substring(0, pos)+ replacement + string.substring(pos +   toReplace.length(), string.length());      
 		   } else {  
 			return string;      
 		   }  
 		} 
 	  
 	 public ArrayList<LogData> get_parsed_LogData(){ 
 		 return parsed_LogData; 
 	 } 
 	  
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
 		   
 		 public LogData(String tsName){ 
 			 testSuiteName = tsName; 
 		 } 
 		  
 		 public void set_edge(String node, String edge){ 
 			 //System.out.println(edge); 
 			 int index; 
 			 String[] elements; 
 			  
 			 String[] array;  //start, end node 
 			 array = node.split("::"); 
 			  
 			 String transition; 
 			 transition = array[1]; 
 			 array = transition.split("->"); 
 			 start = array[0]; 
 			 end = array[1]; 
 			  
 			 Pattern p = Pattern.compile("\\[(.*?)\\]"); //input parameter 
 			 Matcher m = p.matcher(edge); 
 			 if(m.find()) { 
 				 //System.out.println(m.group(1)); 
 				 inputParams = m.group(1); 
 		     }else{ 
 		    	 inputParams = "null"; 
 		     } 
 			  
 			 p = Pattern.compile(",(.*?)\\.java"); //filename 
 			 m = p.matcher(edge); 
 			 if(m.find()) { 
 				 //System.out.println(m.group(1)); 
 				 fileName = m.group(1) + ".java"; 
 		     }else{ 
 		    	 fileName = "null"; 
 		     } 
 			  
 			 p = Pattern.compile("\\),(.*?),\\["); //functionName 
 			 m = p.matcher(edge); 
 			  
 			 if(m.find()) { 
 				 array = m.group(1).split(","); 
 				 if(array.length == 3){ 
 					//System.out.println(array[2]); 
 				 	functionName = array[2]; 
 				 }else{ 
 					functionName = "null";  
 				 } 
 		     }else{ 
 		    	 functionName = "null"; 
 		     } 
 			  
 			 p = Pattern.compile(",((java|com).*?),"); //calledClass 
 			 m = p.matcher(edge); 
 			 if(m.find()) { 
 				 //System.out.println(m.group(1)); 
 				 calledClass = m.group(1); 
 		     }else{ 
 		    	 calledClass = "null"; 
 		     } 
 			  
 			 elements = edge.split(","); 
 			 if(elements.length > 3){ 
 				 p = Pattern.compile("(.*?)\\("); //action 
 				 m = p.matcher(elements[3]); 
 				 if(m.find()) { 
 					 //System.out.println(m.group(1)); 
 					 action = m.group(1); 
 			     }else{ 
 			    	 action = "null"; 
 			     } 
 			 }else{ 
 				 action = "null"; 
 			 } 
 			  
 			 if(elements.length > 2){				  
 				 lineNumber = elements[2]; 
 				 //System.out.println(lineNumber); 
 			 }else{ 
 				 lineNumber = "null"; 
 			 } 
 		 } 
 		  
 		 public void print_LogData(){ 
 			 System.out.println("testSuiteName : " + testSuiteName); 
 			 System.out.println("start : " + start); 
 			 System.out.println("end : " + end); 
 			 System.out.println("fileName : " + fileName); 
 			 System.out.println("lineNumber : " + lineNumber); 
 			 System.out.println("functionName : " + functionName); 
 			 System.out.println("calledClass : " + calledClass); 
 			 System.out.println("action : " + action); 
 			 System.out.println("inputParams : " + inputParams); 
 			 System.out.println("-----------------------------------"); 
 		 } 
 	 } 
 	  
 	 public void log_parsing(){ 
 	 } 
 	  
 	 public class Arch_Channel{ 
 		 public String name; 
 		 public String start; 
 		 public String end; 
 		 public ArrayList<String> event; 
 	 } 
 	  
 	 public ArrayList<Arch_Channel> get_pared_Arch(){ 
 		 return parsed_Arch; 
 	 } 
 	  
 	 public void xml_parsing(){ 
 		 Node n = xml_root.getFirstChild(); 
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
 	 public void print_parsed_Arch(){ 
 		  
 		 for(int i = 0; i < parsed_Arch.size(); i++){ 
 			 Arch_Channel ac = parsed_Arch.get(i); 
 			 System.out.println("channel name = " + ac.name); 
 			 System.out.println(" start = " + ac.start); 
 			 System.out.println(" end = " + ac.end); 
 			  
 			 for(int j = 0; j < ac.event.size(); j++){				  
 				 System.out.println("  event = " + ac.event.get(j));	  
 			 } 
 		 } 
 	 } 
 } 
