[Parser]
class parser

	class
		public Arch_Channel
		public LogData
		
	variable
		private DocumentBuilderFactory dbf
		private DocumentBuilder xml_parser
		private Document xml_doc
		private ArrayList<Arch_Channel> parsed_Arch
		private ArrayList<LogData> parsed_LogData
		private String ArchitectureFile
		private String LogFile
		
	function
		public void xml_parsing(String arcitectureFile)
		public void log_parsing(String logFile)
		public ArrayList<LogData> get_parsed_LogData()
		public ArrayList<Arch_Channel> get_pared_Arch()
		private static String replaceLast(String string, String toReplace, String replacement)

[Filter]
class filter

	class
		public class ErrorInfo
		public class GraphNode
		public class MethodListForTC
		public class TextualNode
		
	variable
		public final int INTER_COMPONENT_FILTER
		public final int FILE_FILTER
		public final int TEST_CASE_FILTER
		public final int TEST_METHOD_FILTER
		
		public static ArrayList<String> packageList
		public static ArrayList<String> fileList
		public static ArrayList<String> testCaseList
		public static ArrayList<String> functionListForTC
		public static ArrayList<String> testMethodList
		
		public static String interfaceName
		public static ArrayList<GraphNode> graphNode
		public static ArrayList<TextualNode> textualNode
		public static ArrayList<MethodListForTC> methodListForTC
		private ArrayList<Arch_Channel> pArchitectureData
		private ArrayList<LogData> pLogData
		
	function
		public void prePareLogData()
		private void collectFilterInfoForFirstPage()
		private void collectFunctionListForTC(String testCaseName)
		protected boolean setArchitectureNode(int filterRule, String inputParam1, String inputParam2)
		protected boolean setInterface(String selectedInterface)
		protected boolean prePareTestCaseInfo(String testCaseName)
		protected boolean prePareTextTreeData(String testCaseName)
		private void prePareMethodListForTC(String testCaseName) 
		ArrayList<GraphNode> getGraphNode()
		ArrayList<TextualNode> getTextualNode()
		ArrayList<MethodListForTC> getMethodListForTC()
		protected ArrayList<String> getPackageList()
		protected ArrayList<String> getFileList()
		protected ArrayList<String> getTestCaseList()
		protected ArrayList<String> getFunctionListForTC()
		protected ArrayList<String> getTestMethodList() 
		