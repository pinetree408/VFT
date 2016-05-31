package vft.filter;

import java.util.ArrayList;

public class FileFilter  extends Filter {
	
	private static String fileName;
	
	protected FileFilter() {
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