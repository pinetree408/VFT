package vft.test;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.xml.sax.SAXException;

import vft.filter.FilterWrapper;

public class filterTest {

	@Test
	public void testPrePareLogData() throws SAXException, IOException, ParserConfigurationException {
		FilterWrapper Filter = new FilterWrapper();
		Filter.prePareLogData();
		assertNotEquals(null, Filter.packageList);
	}

	@Test
	public void testSetInterface() throws SAXException, IOException, ParserConfigurationException {
		FilterWrapper Filter = new FilterWrapper();
		Filter.prePareLogData();
		assertEquals(true, Filter.setInterface("com.atmsimulation.simulation.Simulation.getInstance"));
	}

	@Test
	public void testPrePareTestCaseInfo() throws SAXException, IOException, ParserConfigurationException {
		FilterWrapper Filter = new FilterWrapper();
		Filter.prePareLogData();
		assertEquals(true, Filter.prePareTestCaseInfo("testSomething()"));
	}

	@Test
	public void testPrePareTextTreeData() throws SAXException, IOException, ParserConfigurationException {
		FilterWrapper Filter = new FilterWrapper();
		Filter.prePareLogData();
		assertEquals(true, Filter.prePareTextTreeData("testSomething()"));
	}

	@Test
	public void testGetGraphNode() throws SAXException, IOException, ParserConfigurationException {
		FilterWrapper Filter = new FilterWrapper();
		Filter.prePareLogData();
		Filter.selectComponent(Filter.FILE_FILTER, "ATM.java", null);
		assertNotEquals(null, Filter.getGraphNode());
	}

	@Test
	public void testGetTextualNode() throws SAXException, IOException, ParserConfigurationException {
		FilterWrapper Filter = new FilterWrapper();
		Filter.prePareLogData();
		Filter.prePareTextTreeData("testBankName()");
		assertNotEquals(null, Filter.getTextualNode());
	}

	@Test
	public void testGetMethodListForTC() throws SAXException, IOException, ParserConfigurationException {
		FilterWrapper Filter = new FilterWrapper();
		Filter.prePareLogData();
		assertNotEquals(null, Filter.getMethodListForTC());
	}

	@Test
	public void testGetFunctionListForTC() throws SAXException, IOException, ParserConfigurationException {
		FilterWrapper Filter = new FilterWrapper();
		Filter.prePareLogData();
		assertNotEquals(null, Filter.getFunctionListForTC());
	}

	@Test
	public void testFilterWrapper() throws SAXException, IOException, ParserConfigurationException {
		FilterWrapper Filter = new FilterWrapper();
		Filter.prePareLogData();
		assertNotEquals(null, Filter.getFunctionListForTC());
	}

	@Test
	public void testSetFilterRule() throws SAXException, IOException, ParserConfigurationException {
		FilterWrapper Filter = new FilterWrapper();
		Filter.prePareLogData();
		assertNotEquals(null, Filter.setFilterRule(Filter.TEST_METHOD_FILTER));
	}

	@Test
	public void testSelectComponent() throws SAXException, IOException, ParserConfigurationException {
		FilterWrapper Filter = new FilterWrapper();
		Filter.prePareLogData();
		assertNotEquals(null, Filter.selectComponent(Filter.FILE_FILTER, "ATM.java", null));
	}

}
