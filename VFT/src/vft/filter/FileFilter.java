package vft.filter;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class FileFilter extends Filter {

  private static String fileName;

  protected FileFilter() throws SAXException, IOException, ParserConfigurationException {
    super();
    // TODO Auto-generated constructor stub
  }

  protected boolean setFilterRule(String packageName) {
    fileName = packageName;
    return collectErrorInfo();
  }

  private boolean collectErrorInfo() {
    return super.setArchitectureNode(FILE_FILTER, fileName, null);
  }

  protected ArrayList<String> getFileList() {
    return super.getFileList();
  }

}
