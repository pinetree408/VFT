###Development guideline for VFT

##Structure of VFT project  
VFT project consists of parser, view, filter.
Each parts match vft.parser, vft.filter, (vft, vft.views) packages
Each packages have following java sourcre code.

#[parser]
+ vft.parser
    + parser.java

#[filter]
+ vft.filter
    + FilterWrapper.java => wrapper of filter
    + Filter.java => parent of each filter module
    + InterComponentFilter.java => filter for inter component filtering rule
    + FileFilter.java => filter for file filtering rule
    + TestCaseFilter.java => filter for test case filtering rule
    + TestMethodFilter.java => filter for test method filtering rule

#[view]
+ vft
    + Activator.java => eclipse plug-in activator
+ vft.views
    + VFTView.java => all for view part consist of graph & tree view & select bottons
    + VFTGraph.java => graph view part
    + VFTTree.java => texture tree view part

##Parsing module
The main file of Parsing module is "parser.java" file.
The main class is "parser" defined in "parser.java".

#[First]
When parser's construct call "parser()" is created in filter's construct call "Filter()", "xml_parsing()" and "log_parsing()" functions are performed.
These two functions are received log files in "logs" directory.
The input log files are located in "logs".
Each logs path is mathced "ArchitectureFileName" and "LogFileName" variable.

#[Second]
"xml_parsing()" sets "parsed_Arch" variable with xml parser in java library & architecture file.
"parsed_Arch" consists of "Arch_channel" classes.
The "Arch_Channel" class is for Architecture Spec file.
So the "parsed_Arch" variable in type of "ArrayList of class Arch_Channel" is filled by parsing Archtecture Spec file.  

"log_parsing()" sets "parsed_LogData" variable with pattern & matcher in java library & log file.
"parsed_LogData" consists of "LogData" classes.
The "LogData" class is for log file.
So the "parsed_LogData" variable in type of "ArrayList of class LogData" is filled by parsing log file.

#[Third]
If you want to change the log files, you should set *ArchitectureFileName* and *LogFileName* variable.
If you want to change parsing rule, you should change "xml_parsing()" and "log_parsing()" function.

"xml_parsing()" function just use java xml parsing library.
So, If you change your architecture file's rule. you should change attribute's & tag's name
"log_parsing()" function use java pattern & matcher library.
Each parsing rules are in "set_edge" & "patternMatcher" function.
So, If you change your log file's rule. you should change "patternMatcher" function's parsing rule.

##Filtering module

##Drawing module