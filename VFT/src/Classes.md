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
		public void print_parsed_Arch()
		 	 
		