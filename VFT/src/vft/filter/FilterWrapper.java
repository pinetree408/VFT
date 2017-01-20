package vft.filter;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;


public class FilterWrapper extends Filter {

  private int filterRule;
  private String interfaceName;
  private String packageName1;
  private String packageName2;

  public FilterWrapper() throws SAXException, IOException, ParserConfigurationException {
    super();
    // TODO Auto-generated constructor stub
  }

  public ArrayList<String> setFilterRule(int rule) {
    filterRule = rule;
    if (rule == INTER_COMPONENT_FILTER) {
      InterComponentFilter ICF = null;
      try {
        ICF = new InterComponentFilter();
      } catch (SAXException | IOException | ParserConfigurationException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      return ICF.getPackageList();
    } else if (rule == FILE_FILTER) {
      FileFilter FF = null;
      try {
        FF = new FileFilter();
      } catch (SAXException | IOException | ParserConfigurationException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      return FF.getFileList();
    } else if (rule == TEST_CASE_FILTER) {
      TestCaseFilter TC = null;
      try {
        TC = new TestCaseFilter();
      } catch (SAXException | IOException | ParserConfigurationException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      return TC.getTestCaseList();
    } else if (rule == TEST_METHOD_FILTER) {
      TestMethodFilter TM = null;
      try {
        TM = new TestMethodFilter();
      } catch (SAXException | IOException | ParserConfigurationException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      return TM.getTestMethodList();
    }
    return null;
  }

  public boolean selectComponent(int rule, String package1, String package2) {
    filterRule = rule;
    if (rule == INTER_COMPONENT_FILTER) {
      InterComponentFilter ICF = null;
      try {
        ICF = new InterComponentFilter();
      } catch (SAXException | IOException | ParserConfigurationException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      packageName1 = package1;
      packageName2 = package2;
      return ICF.setFilterRule(package1, package2);
    } else if (rule == FILE_FILTER) {
      FileFilter FF = null;
      try {
        FF = new FileFilter();
      } catch (SAXException | IOException | ParserConfigurationException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      packageName1 = package1;
      return FF.setFilterRule(packageName1);
    } else if (rule == TEST_CASE_FILTER) {
      TestCaseFilter TC = null;
      try {
        TC = new TestCaseFilter();
      } catch (SAXException | IOException | ParserConfigurationException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      packageName1 = package1;
      return TC.setFilterRule(packageName1);
    } else if (rule == TEST_METHOD_FILTER) {
      TestMethodFilter TM = null;
      try {
        TM = new TestMethodFilter();
      } catch (SAXException | IOException | ParserConfigurationException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      packageName1 = package1;
      return TM.setFilterRule(packageName1);
    }
    return false;
  }

  public boolean setInterface(String selectedInterface) {
    interfaceName = selectedInterface;
    return super.setInterface(interfaceName);
  }

  public boolean prePareTextTreeData(String testCaseName) {
    return super.prePareTextTreeData(testCaseName);
  }

  public boolean prePareTestCaseInfo(String testCaseName) {
    return super.prePareTestCaseInfo(testCaseName);
  }

  public void prePareLogData() {
    super.prePareLogData();
  }

  public ArrayList<GraphNode> getGraphNode() {
    return super.getGraphNode();
  }

  public ArrayList<TextualNode> getTextualNode() {
    return super.getTextualNode();
  }

  public ArrayList<MethodListForTC> getMethodListForTC() { // all action - call, set, execution, etc
    return super.getMethodListForTC();
  }

  public ArrayList<String> getFunctionListForTC() { // call
    return super.getFunctionListForTC();
  }

}
