package vft.views;

import java.util.ArrayList;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import vft.filter.FilterWrapper;
import vft.filter.Filter.GraphNode;
import vft.filter.Filter.TextualNode;

public class VFTTree {

    public static JTree init(int filteringRule, ArrayList<String> options)
    {
    	JTree tree;
    	
    	/*
        //create the root node
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
        //create the child nodes
        DefaultMutableTreeNode vegetableNode = new DefaultMutableTreeNode("Vegetables");
        DefaultMutableTreeNode fruitNode = new DefaultMutableTreeNode("Fruits");

        //add the child nodes to the root node
        root.add(vegetableNode);
        root.add(fruitNode);
         
        //create the tree by passing in the root node
        tree = new JTree(root);
        */
    	
    	DefaultMutableTreeNode root;
    	DefaultMutableTreeNode caller;
    	DefaultMutableTreeNode functionName;
    	DefaultMutableTreeNode callee;
    	
    	ArrayList<GraphNode> graphNode = new ArrayList<GraphNode>();
    	ArrayList<TextualNode> textualNode = new ArrayList<TextualNode>();
    	ArrayList<String> componentList;
        FilterWrapper Filter = new FilterWrapper();        
        int i;
        String tmpList;          
        Filter.prePareLogData();
        
        // 2nd step : select package or file or test case
        if (filteringRule == Filter.INTER_COMPONENT_FILTER) {
        	
        	root = new DefaultMutableTreeNode("INTER_COMPONENT_FILTER");
        	
	        Filter.selectComponent(Filter.INTER_COMPONENT_FILTER, options.get(0), options.get(1));
	        graphNode = Filter.getGraphNode();
	
	        System.out.println("VFTGraph : ##### Interface between  Atm <-> Simulation #####");
	        GraphNode gNodeToDebug =  Filter.new GraphNode(); 
	        for (i = 0; i < graphNode.size(); i++) {
	            gNodeToDebug = graphNode.get(i);
	            caller = new DefaultMutableTreeNode(gNodeToDebug.caller);
	            callee = new DefaultMutableTreeNode(gNodeToDebug.callee);
	            functionName = new DefaultMutableTreeNode(gNodeToDebug.functionName);            
	            functionName.add(callee);
	            caller.add(functionName);
	            root.add(caller);
	            System.out.println("VFTGraph :  " + gNodeToDebug.caller + " -> " + gNodeToDebug.functionName + " -> " + gNodeToDebug.callee);
	        }
	        
        } else if (filteringRule == Filter.FILE_FILTER) {
        	
        	root = new DefaultMutableTreeNode("FILE_FILTER");
        	
	        Filter.selectComponent(Filter.FILE_FILTER, options.get(0), null);
	        graphNode = Filter.getGraphNode();
	
	        System.out.println("VFTGraph : ##### Interface with this file #####");
	        GraphNode gNodeToDebug =  Filter.new GraphNode(); 
	        for (i = 0; i < graphNode.size(); i++) {
	            gNodeToDebug = graphNode.get(i);
	            caller = new DefaultMutableTreeNode(gNodeToDebug.caller);
	            callee = new DefaultMutableTreeNode(gNodeToDebug.callee);
	            functionName = new DefaultMutableTreeNode(gNodeToDebug.functionName);            
	            functionName.add(callee);
	            caller.add(functionName);
	            root.add(caller);
	            System.out.println("VFTGraph :  " + gNodeToDebug.caller + " -> " + gNodeToDebug.functionName + " -> " + gNodeToDebug.callee);
	        } 
	        
        } else if (filteringRule == Filter.TEST_CASE_FILTER) {
        	
        	root = new DefaultMutableTreeNode("TEST_CASE_FILTER");
        	
	        Filter.selectComponent(Filter.TEST_CASE_FILTER, options.get(0), null);
	        
	    	DefaultMutableTreeNode call;
	    	DefaultMutableTreeNode inner;

	        System.out.println("VFTGraph : ##### call text tree involved with this test case #####");
	        Filter.prePareTextTreeData(options.get(0));
	        textualNode = Filter.getTextualNode();
	        TextualNode gTextualTemp = null;
	        TextualNode gTextualInnerTemp = null;
	        for (i = 0; i < textualNode.size(); i++) {
	        	int j;
	        	gTextualTemp = textualNode.get(i);
	        	call = new DefaultMutableTreeNode(gTextualTemp.action + "  " + gTextualTemp.caller + "  " + gTextualTemp.lineNumber + "  " 
    	        		+ gTextualTemp.callee + "  " + gTextualTemp.functionName + "  " + gTextualTemp.param);
	        	System.out.println("VFTGraph :  call  " + gTextualTemp.action + "  " + gTextualTemp.caller + "  " + gTextualTemp.lineNumber + "  " 
    	        		+ gTextualTemp.callee + "  " + gTextualTemp.functionName + "  " + gTextualTemp.param);	
	        	if(gTextualTemp.innerAction.size() > 0) {
    				for(j = 0; j < gTextualTemp.innerAction.size(); j++) {
    					gTextualInnerTemp = gTextualTemp.innerAction.get(j);
    					inner = new DefaultMutableTreeNode(gTextualInnerTemp.action + "  " 
    			        		+ gTextualInnerTemp.caller + "  " + gTextualInnerTemp.lineNumber + "  "  
    			        		+ gTextualInnerTemp.callee + "  " + gTextualInnerTemp.functionName + "  " + gTextualInnerTemp.param);
    					call.add(inner);
    			        System.out.println("VFTGraph :  inner  " + gTextualInnerTemp.action + "  " 
    			        		+ gTextualInnerTemp.caller + "  " + gTextualInnerTemp.lineNumber + "  "  
    			        		+ gTextualInnerTemp.callee + "  " + gTextualInnerTemp.functionName + "  " + gTextualInnerTemp.param);	
    				}
    	        }
	        	root.add(call);
	        }
	        
        } else if (filteringRule == Filter.TEST_METHOD_FILTER) {
        	
        	root = new DefaultMutableTreeNode("TEST_METHOD_FILTER");
        	
	        Filter.selectComponent(Filter.TEST_METHOD_FILTER, options.get(0), null);
	        graphNode = Filter.getGraphNode();
	
	        System.out.println("VFTGraph : ##### call graph involved with this test method #####");
	        GraphNode gNodeToDebug =  Filter.new GraphNode(); 
	        for (i = 0; i < graphNode.size(); i++) {
	            gNodeToDebug = graphNode.get(i);
	            caller = new DefaultMutableTreeNode(gNodeToDebug.caller);
	            callee = new DefaultMutableTreeNode(gNodeToDebug.callee);
	            functionName = new DefaultMutableTreeNode(gNodeToDebug.functionName);            
	            functionName.add(callee);
	            caller.add(functionName);
	            root.add(caller);
	            System.out.println("VFTGraph :  " + gNodeToDebug.caller + " -> " + gNodeToDebug.functionName + " -> " + gNodeToDebug.callee);
	        }
        	
        } else {
        	
            //create the root node
            root = new DefaultMutableTreeNode("Root");
            //create the child nodes
            DefaultMutableTreeNode vegetableNode = new DefaultMutableTreeNode("Vegetables");
            DefaultMutableTreeNode fruitNode = new DefaultMutableTreeNode("Fruits");

            //add the child nodes to the root node
            root.add(vegetableNode);
            root.add(fruitNode);
            //create the tree by passing in the root node
            
        }
        
        tree = new JTree(root);
        return tree;

    }
}
