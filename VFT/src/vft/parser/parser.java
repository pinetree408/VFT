package vft.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
	 
	 public parser() throws SAXException, IOException{
		 
		 //xml parsing
		 try{
			 dbf = DocumentBuilderFactory.newInstance();
	
			 xml_parser = dbf.newDocumentBuilder();
	
			 xml_doc = xml_parser.parse("C:\\input\\ATMSimulationSystemXML_2016_05_10.txt");
	
			 xml_root = xml_doc.getDocumentElement();
			 
		 }catch(ParserConfigurationException e){
			 
		 }catch(IOException e) {
			 
		 } 
		 parsed_Arch = new ArrayList<Arch_Channel>();
		 
		 xml_parsing();
		 
		 
		 //log parsing
		 File logFile = new File("C:\\input\\PO_log_20160510_2024.txt");
		 FileReader fileReader = new FileReader(logFile);
		 BufferedReader reader = new BufferedReader(fileReader);
		 
		 String line = null;
		 ArrayList<log_signiture> log = new ArrayList<log_signiture>();
		 ArrayList<String> lines = new ArrayList<String>();
	 
		 log_signiture sig;
		 
		 while((line = reader.readLine()) != null){
			 line = line.replaceFirst("\t", "");
			 line = line.replaceFirst("<", "");
			 line = replaceLast(line, ">", "");
			 
			 if(!line.equals("setUp()") && !line.equals("/tearDown()")){
				 lines.add(line);
			 }			 
		 }
		 
		 while(!lines.isEmpty()){
			 line = lines.remove(0);
			 if(line.matches("^PO.*$")){
				 sig = new log_signiture(line);				 
				 do{
					line = lines.remove(0);
					sig.set_edge(line);
				 }while(!lines.isEmpty() && !lines.get(0).matches("^PO.*$"));
				 log.add(sig);
			 }
		 }
		// for debugging 
		 /*
		 for(int i = 0; i < log.size(); i++){
			log_signiture temp = log.get(i);
		 
			if(temp.inf_list.size() != 1){
				System.out.println(temp.POS_name);
				System.out.println(temp.transition);
				System.out.println(" " + temp.inf_list.get(0)[0]);
				System.out.println(" " + temp.inf_list.get(1)[0]);
			}
		 	
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
	 
	 public class log_signiture{
		 private String POS_name;
		 private String transition;
		 private ArrayList<String[]> inf_list;
		 
		 public log_signiture(String POS){
			 String[] array; 
			 array = POS.split("::");
			 POS_name = array[0];
			 transition = array[1];
			 inf_list = new ArrayList<String[]>();
		 }
		 
		 public void set_edge(String edge){
			 inf_list.add(edge.split(","));
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
