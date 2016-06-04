package vft.views;

import org.jgrapht.ListenableGraph;
import org.jgrapht.graph.ListenableDirectedGraph;
import org.jgrapht.graph.DirectedMultigraph;

import java.util.ArrayList;

import vft.filter.FilterWrapper;
import vft.filter.Filter.GraphNode; 

public class VFTGraph {
	
    public static ListenableGraph<String, String> init(int filteringRule, ArrayList<String> options)
    {
        // create a JGraphT graph
        ListenableGraph<String, String> g =
            new ListenableDirectedGraph<String, String>(
               String.class);
    	//DirectedMultigraph<String, String> g =
        //     new DirectedMultigraph<String, String>(
		//   String.class);
    	ArrayList<GraphNode> graphNode = new ArrayList<GraphNode>();
    	ArrayList<String> componentList;

    	/*
    	String vertex = String.valueOf(option);
        String v1 = vertex;
        String v2 = vertex + "1";
        String v3 = vertex + "2";
        String v4 = vertex + "3";

        // add some sample data (graph manipulated via JGraphX)
        g.addVertex(v1);
        g.addVertex(v2);
        g.addVertex(v3);
        g.addVertex(v4);

        g.addEdge(v1, v2);
        g.addEdge(v2, v3);
        g.addEdge(v3, v1);
        g.addEdge(v4, v3);
    	 */
		
        /////////////////////////////////////////       
        long start = System.currentTimeMillis();
        
        FilterWrapper Filter = new FilterWrapper();        
        int i;
        String tmpList;          
        Filter.prepareLogData();
 
        // 1st step : set filter rule and get list
        componentList = Filter.setFilterRule(Filter.INTER_COMPONENT_FILTER);
        for(i = 0; i < componentList.size(); i++) {
        	tmpList = componentList.get(i);
            System.out.println("VFTGraph : Package list " + tmpList);
        }
        
        componentList = Filter.setFilterRule(Filter.FILE_FILTER);
        for(i = 0; i < componentList.size(); i++) {
        	tmpList = componentList.get(i);
            System.out.println("VFTGraph : File list " + tmpList);
        }

        componentList = Filter.setFilterRule(Filter.TEST_CASE_FILTER);
        for(i = 0; i < componentList.size(); i++) {
        	tmpList = componentList.get(i);
            System.out.println("VFTGraph : Test Case list " + tmpList);
        }

        componentList = Filter.setFilterRule(Filter.TEST_METHOD_FILTER);
        for(i = 0; i < componentList.size(); i++) {
        	tmpList = componentList.get(i);
            System.out.println("VFTGraph : Test Method list " + tmpList);
        }
        
        // 2nd step : select package or file or test case
        if (filteringRule == Filter.INTER_COMPONENT_FILTER) {
        	
        	if (options.size() != 0) {
		        Filter.selectComponent(Filter.INTER_COMPONENT_FILTER, options.get(0), options.get(1));
		        graphNode = Filter.getGraphNode();
		
		        System.out.println("VFTGraph : ##### Interface between  Atm <-> Simulation #####");
		        GraphNode gNodeToDebug =  Filter.new GraphNode(); 
		        for (i = 0; i < graphNode.size(); i++) {
		            gNodeToDebug = graphNode.get(i);
		            g.addVertex(gNodeToDebug.caller);
		            g.addVertex(gNodeToDebug.callee);
		            g.addEdge(gNodeToDebug.caller, gNodeToDebug.callee, gNodeToDebug.functionName + ":" + gNodeToDebug.param);
		            System.out.println("VFTGraph :  " + gNodeToDebug.caller + " -> " + gNodeToDebug.functionName + " -> " + gNodeToDebug.callee);
		        }
        	} else {
        		componentList = Filter.setFilterRule(Filter.INTER_COMPONENT_FILTER);
        		for(int j = 0; j < componentList.size(); j++) {
        			if (j != componentList.size() - 1) {
        				Filter.selectComponent(Filter.INTER_COMPONENT_FILTER, componentList.get(j),componentList.get(j+1));
        			} else {
        				Filter.selectComponent(Filter.INTER_COMPONENT_FILTER, componentList.get(j),componentList.get(0));
        			}
			        graphNode = Filter.getGraphNode();
			
			        System.out.println("VFTGraph : ##### Interface between  Atm <-> Simulation #####");
			        GraphNode gNodeToDebug =  Filter.new GraphNode(); 
			        for (i = 0; i < graphNode.size(); i++) {
			            gNodeToDebug = graphNode.get(i);
			            g.addVertex(gNodeToDebug.caller);
			            g.addVertex(gNodeToDebug.callee);
			            g.addEdge(gNodeToDebug.caller, gNodeToDebug.callee, gNodeToDebug.functionName + ":" + gNodeToDebug.param);
			            System.out.println("VFTGraph :  " + gNodeToDebug.caller + " -> " + gNodeToDebug.functionName + " -> " + gNodeToDebug.callee);
			        }
        		}
        	}
	        
        } else if (filteringRule == Filter.FILE_FILTER) {
        	
        	if (options.size() != 0) {
		        Filter.selectComponent(Filter.FILE_FILTER, options.get(0), null);
		        graphNode = Filter.getGraphNode();
		
		        System.out.println("VFTGraph : ##### Interface with this file #####");
		        GraphNode gNodeToDebug =  Filter.new GraphNode(); 
		        for (i = 0; i < graphNode.size(); i++) {
		            gNodeToDebug = graphNode.get(i);
		            g.addVertex(gNodeToDebug.caller);
		            g.addVertex(gNodeToDebug.callee);
		            g.addEdge(gNodeToDebug.caller, gNodeToDebug.callee, gNodeToDebug.functionName + ":" + gNodeToDebug.param);
		            System.out.println("VFTGraph :  " + gNodeToDebug.caller + " -> " + gNodeToDebug.functionName + " -> " + gNodeToDebug.callee);
		        }
        	} else {
        		componentList = Filter.setFilterRule(Filter.FILE_FILTER);
        		for(int j = 0; j < componentList.size(); j++) {
			        Filter.selectComponent(Filter.FILE_FILTER, componentList.get(j), null);
			        graphNode = Filter.getGraphNode();
			
			        System.out.println("VFTGraph : ##### Interface with this file #####");
			        GraphNode gNodeToDebug =  Filter.new GraphNode(); 
			        for (i = 0; i < graphNode.size(); i++) {
			            gNodeToDebug = graphNode.get(i);
			            g.addVertex(gNodeToDebug.caller);
			            g.addVertex(gNodeToDebug.callee);
			            g.addEdge(gNodeToDebug.caller, gNodeToDebug.callee, gNodeToDebug.functionName + ":" + gNodeToDebug.param);
			            System.out.println("VFTGraph :  " + gNodeToDebug.caller + " -> " + gNodeToDebug.functionName + " -> " + gNodeToDebug.callee);
			        }
        		}
        	}
	        
        } else if (filteringRule == Filter.TEST_CASE_FILTER) {
        	
	        Filter.selectComponent(Filter.TEST_CASE_FILTER, options.get(0), null);
	        graphNode = Filter.getGraphNode();
	
	        System.out.println("VFTGraph : ##### call graph involved with this test case #####");
	        GraphNode gNodeToDebug =  Filter.new GraphNode(); 
	        for (i = 0; i < graphNode.size(); i++) {
	            gNodeToDebug = graphNode.get(i);
	            g.addVertex(gNodeToDebug.caller);
	            g.addVertex(gNodeToDebug.callee);
	            g.addEdge(gNodeToDebug.caller, gNodeToDebug.callee, gNodeToDebug.functionName + ":" + gNodeToDebug.param);
	            System.out.println("VFTGraph :  " + gNodeToDebug.caller + " -> " + gNodeToDebug.functionName + " -> " + gNodeToDebug.callee);
	        }
        } else if (filteringRule == Filter.TEST_METHOD_FILTER) {
        	
	        Filter.selectComponent(Filter.TEST_METHOD_FILTER, options.get(0), null);
	        graphNode = Filter.getGraphNode();
	
	        System.out.println("VFTGraph : ##### call graph involved with this test method #####");
	        GraphNode gNodeToDebug =  Filter.new GraphNode(); 
	        for (i = 0; i < graphNode.size(); i++) {
	            gNodeToDebug = graphNode.get(i);
	            g.addVertex(gNodeToDebug.caller);
	            g.addVertex(gNodeToDebug.callee);
	            g.addEdge(gNodeToDebug.caller, gNodeToDebug.callee, gNodeToDebug.functionName + ":" + gNodeToDebug.param);
	            System.out.println("VFTGraph :  " + gNodeToDebug.caller + " -> " + gNodeToDebug.functionName + " -> " + gNodeToDebug.callee);
	        }
        	
        } else {
        	/*
        	String vertex = String.valueOf(filteringRule);
            String v1 = vertex;
            String v2 = vertex + "1";
            String v3 = vertex + "2";
            String v4 = vertex + "3";

            // add some sample data (graph manipulated via JGraphX)
            g.addVertex(v1);
            g.addVertex(v2);
            g.addVertex(v3);
            g.addVertex(v4);

            g.addEdge(v1, v2, v1 + v2);
            g.addEdge(v1, v2, v1 + v1);
            g.addEdge(v2, v3, v2 + v3);
            g.addEdge(v3, v1, v3 + v4);
            g.addEdge(v4, v3, v4 + v3);
            */
        }

        long end = System.currentTimeMillis();	     
		System.out.println("VFTGraph : Filter  (ms) " +  (end - start)); 
        
        return g;

    }
}
