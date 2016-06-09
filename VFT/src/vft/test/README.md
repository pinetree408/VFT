#Test guideline for VFT

##Test for parsing module
parsing module test case consists of 5 test methods.  
+ "testParser()" tests "parse()" function
    + these test method checks that whether "parse()" function return "parser" class.
+ "testXml_parsing()" tests "xml_parsing()" function
    + these test method checks that whether "xml_parsing()" function return "parsed_Arch".
+ "testLog_parsing()" tests "log_parsing()" function
    + these test method checks that whether "log_parsing()" function return "parsed_LogData".
+ "testGet_parsed_LogData()" tests "get_parsed_LogData()" function
    + these test method checks that whether "get_parsed_LogData()" function return "parsed_LogData".
+ "testGet_pared_Arch()" tests "get_pared_arch()" function
    + these test method checks that whether "get_pared_arch()" function return "parsed_Arch".

##Test for filtering module
filtering module test case consists of 11 test methods.  
+ testPrePareLogData() tests "prePareLogData()" functions.
    + these test method checks that whether "prePareLogData()" function sets "packageList" variable.
+ testSetInterface() tests "setInterface()" functions.
    + these test method checks that whether "setInterface()" function sets interface and return "true".
+ testPrePareTestCaseInfo() tests "prePareTestCaseInfo()" functions.
    + these test method checks that whether "prePareTestCaseInfo()" function sets test case and return "true".
+ testPrePareTextTreeData() tests "prePareTextTreeData()" functions.
    + these test method checks that whether "prePareTextTreeData()" function sets test case and return "true".
+ testGetGraphNode() tests "getGraphNode()" functions.
    + these test method checks that whether "getGraphNode()" function return "graphNode".
+ testGetTextualNode() tests "getTextualNode()" functions.
    + these test method checks that whether "getTextualNode()" function return "graphNode" variable.
+ testGetMethodListForTC() tests "getMethodListForTC()" functions.
    + these test method checks that whether "getMethodListForTC()" function sets "methodListForTC" variable.
+ testGetFunctionListForTC() tests "getFunctionListForTC()" functions.
    + these test method checks that whether "getFunctionListForTC()" function sets "functionListForTC" variable.
+ testFilterWrapper() tests "FilterWrapper()" functions.
    + these test method checks that whether "FilterWrapper()" function returns "FilterWrapper()" class.
+ testSetFilterRule() tests "setFilterRule()" functions.
    + these test method checks that whether "setFilterRule()" function sets "filterRule" variable and return results.
+ testSelectComponent() tests "selectComponent()" functions.
    + these test method checks that whether "selectComponent()" function sets each options and return results.

##Test for viewing module
###Test for Performance
1. Please guess where this line of log came from.
2. Use VFT at guess again.
    + Q. Was it helpful to you to understand cause of log message? (0 ~ 5)
    + Q. Did it provided smooth experience when you using it? (0 ~ 5)

###Test for function.
1. Does filtering rule select button shows 5 filtering rule correctly? (0 ~ 5)
2. When you set filtering rule, does specific option buttons made correctly? (0 ~ 5)
3. When you set all of options & filtering rule, does graph show correctly? (0 ~ 5)
4. When you set all of options & filtering rule, does texture tree show correctly? (0 ~ 5)
5. When you change all of options & filtering rule, does graph change correctly? (0 ~ 5)
6. When you change all of options & filtering rule, does texture tree change correctly? (0 ~ 5)

###How to check viewing module passes these test.
Total sum of test is 40 point.
The passing score is 30 points or more if.  

