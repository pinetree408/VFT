package vft.test;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.xml.sax.SAXException;

import vft.parser.*;

public class paserTest {

	@Test
	public void testParser() throws SAXException, IOException, ParserConfigurationException {
		parser parsedData = new parser();
		assertNotEquals(null, parsedData);
	}

	@Test
	public void testXml_parsing() throws SAXException, IOException, ParserConfigurationException{
		parser parsedData = new parser();
		parsedData.xml_parsing("/logs/ATMSimulationSystemXML_2016_05_10.txt");
		assertNotEquals(null, parsedData.get_pared_Arch());
	}

	@Test
	public void testLog_parsing() throws SAXException, IOException, ParserConfigurationException {
		parser parsedData = new parser();
		parsedData.log_parsing("/logs/PO_log_20160510_2024.txt");
		assertNotEquals(null, parsedData.get_parsed_LogData());
	}

	@Test
	public void testGet_parsed_LogData() throws SAXException, IOException, ParserConfigurationException {
		parser parsedData = new parser();
		parsedData.log_parsing("/logs/PO_log_20160510_2024.txt");
		assertNotEquals(null, parsedData.get_parsed_LogData());
	}

	@Test
	public void testGet_pared_Arch() throws SAXException, IOException, ParserConfigurationException {
		parser parsedData = new parser();
		parsedData.xml_parsing("/logs/ATMSimulationSystemXML_2016_05_10.txt");
		assertNotEquals(null, parsedData.get_pared_Arch());
	}

	@Test
	public void testPrint_parsed_Arch() throws SAXException, IOException, ParserConfigurationException {
		parser parsedData = new parser();
		assertNotEquals(null, parsedData);
	}

}
