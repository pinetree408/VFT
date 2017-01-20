package vft.filter;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class TestMethodFilter extends Filter {

  private static String testMethodName;

  protected TestMethodFilter() throws SAXException, IOException, ParserConfigurationException {
    super();
    // TODO Auto-generated constructor stub
  }

  protected boolean setFilterRule(String packageName) {
    testMethodName = packageName;
    return collectErrorInfo();
  }

  private boolean collectErrorInfo() {
    return super.setArchitectureNode(TEST_METHOD_FILTER, testMethodName, null);
  }

  protected ArrayList<String> getTestMethodList() {
    return super.getTestMethodList();
  }

}
