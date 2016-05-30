package vft.views;

import org.jgrapht.ListenableGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.ListenableDirectedGraph;

import java.util.ArrayList;
import vft.filter.FilterWrapper;
import vft.filter.Filter.GraphNode; 

public class VFTGraph {
	
    public static ListenableGraph<String, DefaultEdge> init()
    {
        // create a JGraphT graph
        ListenableGraph<String, DefaultEdge> g =
            new ListenableDirectedGraph<String, DefaultEdge>(
                DefaultEdge.class);
    	ArrayList<GraphNode> graphNode = new ArrayList<GraphNode>();
    	ArrayList<String> componentList;

        String v1 = "v1";
        String v2 = "v2";
        String v3 = "v3";
        String v4 = "v4";

        // add some sample data (graph manipulated via JGraphX)
        g.addVertex(v1);
        g.addVertex(v2);
        g.addVertex(v3);
        g.addVertex(v4);

        g.addEdge(v1, v2);
        g.addEdge(v2, v3);
        g.addEdge(v3, v1);
        g.addEdge(v4, v3);

        /////////////////////////////////////////       
        long start = System.currentTimeMillis();
        
        FilterWrapper Filter = new FilterWrapper();        
        int i;
        String tmpList;          
        Filter.prePareLogData();
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
        Filter.selectComponent(Filter.INTER_COMPONENT_FILTER, "Atm", "Simulation");
        graphNode = Filter.getGraphNode();

        System.out.println("VFTGraph : ##### Interface between  Atm <-> Simulation #####");
        GraphNode gNodeToDebug =  Filter.new GraphNode(); 
        for (i = 0; i < graphNode.size(); i++) {
            gNodeToDebug = graphNode.get(i);
            System.out.println("VFTGraph :  " + gNodeToDebug.caller + " -> " + gNodeToDebug.functionName + " -> " + gNodeToDebug.callee);
        }
                
        Filter.selectComponent(Filter.FILE_FILTER, "Simulation.java", null);
        graphNode = Filter.getGraphNode();

        System.out.println("VFTGraph : ##### Interface with this file #####");
        gNodeToDebug =  Filter.new GraphNode(); 
        for (i = 0; i < graphNode.size(); i++) {
            gNodeToDebug = graphNode.get(i);
            System.out.println("VFTGraph :  " + gNodeToDebug.caller + " -> " + gNodeToDebug.functionName + " -> " + gNodeToDebug.callee);
        } 
        
        Filter.selectComponent(Filter.TEST_CASE_FILTER, "testBankName", null);
        graphNode = Filter.getGraphNode();

        System.out.println("VFTGraph : ##### call graph involved with this test case #####");
        gNodeToDebug =  Filter.new GraphNode(); 
        for (i = 0; i < graphNode.size(); i++) {
            gNodeToDebug = graphNode.get(i);
            System.out.println("VFTGraph :  " + gNodeToDebug.caller + " -> " + gNodeToDebug.functionName + " -> " + gNodeToDebug.callee);
        }


        long end = System.currentTimeMillis();	     
		System.out.println("VFTGraph : Filter  (ms) " +  (end - start)); 
        
        
        return g;

    }
}
