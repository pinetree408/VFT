Development guideline for VFT
=============================

Structure of VFT project  
--------------------------
VFT project consists of vft, vft.parser, vft.filter and vft.views packages.  
Each packages have following java sourcre code.
* vft : Activator.java
* vft.parser : parser.java
* vft.filter : FileFilter.java, Filter.java, FilterWrapper.java, InterComponentFilter.java, TestCaseFilter.java, TestMethodFilter.java
* vft.views : VFTGraph.java, VFTTree.java, VFTView.java


Parsing module
--------------------------
The main file of Parsing module is *parser.java* file. And the main class is *class parser* defined in *parser.java*.  
When *class parser* is created, xml parsing and log parsing are performed. The input log files are located in *VFT/logs*.
If you want to change the log file, you should set *ArchitectureFileName* and *LogFileName* variable. 
* *class Arch_Channel*  
In the part of xml parsing, the *parsed_Arch* array list in type of *ArrayList<Arch_Channel>* is filled by parsing archtecture file.

* **class LogData**

Filtering module
--------------------------
VFT project includes vft, vft.parser, vft.filter and vft.views package.

Drawing module
--------------------------
VFT project includes vft, vft.parser, vft.filter and vft.views package.
