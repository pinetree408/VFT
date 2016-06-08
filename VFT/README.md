#Development guideline for VFT

##Structure of VFT project  
VFT project consists of parser, view, filter.
Each parts match vft.parser, vft.filter, (vft, vft.views) packages
Each packages have following java sourcre code.

###[parser]
+ vft.parser
    + parser.java

###[filter]
+ vft.filter
    + FilterWrapper.java => wrapper of filter
    + Filter.java => parent of each filter module
    + InterComponentFilter.java => filter for inter component filtering rule
    + FileFilter.java => filter for file filtering rule
    + TestCaseFilter.java => filter for test case filtering rule
    + TestMethodFilter.java => filter for test method filtering rule

###[view]
+ vft
    + Activator.java => eclipse plug-in activator
+ vft.views
    + VFTView.java => all for view part consist of graph & tree view & select bottons
    + VFTGraph.java => graph view part
    + VFTTree.java => texture tree view part

##Parsing module
The main file of Parsing module is "parser.java" file.
The main class is "parser" defined in "parser.java".

###[First]
When parser's construct call "parser()" is created in filter's construct call "Filter()", "xml_parsing()" and "log_parsing()" functions are performed.
These two functions are received log files in "logs" directory.
The input log files are located in "logs".
Each logs path is mathced "ArchitectureFileName" and "LogFileName" variable.

###[Second]
"xml_parsing()" sets "parsed_Arch" variable with xml parser in java library & architecture file.
"parsed_Arch" consists of "Arch_channel" classes.
The "Arch_Channel" class is for Architecture Spec file.
So the "parsed_Arch" variable in type of "ArrayList of class Arch_Channel" is filled by parsing Archtecture Spec file.  

"log_parsing()" sets "parsed_LogData" variable with pattern & matcher in java library & log file.
"parsed_LogData" consists of "LogData" classes.
The "LogData" class is for log file.
So the "parsed_LogData" variable in type of "ArrayList of class LogData" is filled by parsing log file.

###[Third]
If you want to change the log files, you should set *ArchitectureFileName* and *LogFileName* variable.
If you want to change parsing rule, you should change "xml_parsing()" and "log_parsing()" function.

"xml_parsing()" function just use java xml parsing library.
So, If you change your architecture file's rule. you should change attribute's & tag's name
"log_parsing()" function use java pattern & matcher library.
Each parsing rules are in "set_edge" & "patternMatcher" function.
So, If you change your log file's rule. you should change "patternMatcher" function's parsing rule.

##Filtering module
The main file of Filtering module is "Filter.java" file.
The main class is "Filter" defined in "Filter.java".
"FilterWrapper.java"'s "FilterWrapper" class is extended by "Filter" class.
And Setting options to other filtering rule class.
"InterComponentFilter.java"'s "InterComponentFilter" class,
"FileFilter.java"'s "FileFilter" class,
"TestCaseFilter.java"'s "TestCaseFilter" class,
"TestMethodFilter.java"'s "TestMethodFilter" class
are also extended by "Filter" class and have their own setting funtions by filtering Rule.

###[First]
When FilterWrapper's construct call "FilterWrapper()" is created in Viewer's construct call "createPartControl()", "get_parsed_LogData()" and "get_pared_Arch()" functions are performed and set result to "pLogData", "pArchitectureData" variable.

###[Second]
If FilterWrapper's construct call was executed, Viewer call "prePareLogData()" function.
"prePareLogData()" collect basic information from "pLogData", "pArchitectureData" variable.
When "prePareLogData()" function was executed, Filter class has "packageList", "fileList", "testCaseList", "testMethodList" variable perfectly.

###[Third]
In UI, If user set filtering Rule and specific component, Viewer call "selectComponent()" function.
"selectComponent()" function call each Filter class's "setFilterRule" function matched by filtering rule.
Filter class's "setFilterRule" function call "Filter" class's "setArchitectureNode" function.
"setArchitectureNode" function set "graphNode" variable matched by filtering rule.

###[Forth]
If "selectComponent()" function was executed, Viewer can draw graph & texture with "graphNode" variable.

###[Fifth]
If you want to change filtering rule, you should change filtering rule in "Filter" class.
If you want to change filter, you should change "collectFilterInfoForFirstPage()" function and "setArchitectureNode" function.