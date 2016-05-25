package vft.views;

import java.util.ArrayList;

import org.jgrapht.ListenableGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.ListenableDirectedGraph;

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

        FilterWrapper Filter = new FilterWrapper();
        Filter.setFilterRule(1, "Atm", "Simulation"); // 1 means INTER_COMPONENT_FILTER
        graphNode = Filter.getGraphNode();
        for(int i = 0; i < graphNode.size(); i++) {
        }
        
        return g;

    }
}
