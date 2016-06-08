package vft.views;

import org.jgrapht.ListenableGraph;
import org.jgrapht.graph.ListenableDirectedGraph;

import java.util.ArrayList;

import vft.filter.FilterWrapper;
import vft.filter.Filter.GraphNode;

public class VFTGraph {

	public static ListenableGraph<String, String> init(FilterWrapper Filter, int filteringRule, ArrayList<String> options) {
		// create a JGraphT graph
		ListenableGraph<String, String> g = new ListenableDirectedGraph<String, String>(String.class);
		ArrayList<GraphNode> graphNode = new ArrayList<GraphNode>();
		ArrayList<String> componentList;

		/////////////////////////////////////////
		long start = System.currentTimeMillis();

		int i;

		// select package or file or test case
		if (filteringRule == Filter.INTER_COMPONENT_FILTER) {

			if (options.size() != 0) {
				Filter.selectComponent(Filter.INTER_COMPONENT_FILTER, options.get(0), options.get(1));
				graphNode = Filter.getGraphNode();

				GraphNode gNodeToDebug = Filter.new GraphNode();
				for (i = 0; i < graphNode.size(); i++) {
					gNodeToDebug = graphNode.get(i);
					g.addVertex(gNodeToDebug.caller);
					g.addVertex(gNodeToDebug.callee);
					g.addEdge(gNodeToDebug.caller, gNodeToDebug.callee,
							gNodeToDebug.functionName + ":" + gNodeToDebug.param);
				}
			} else {
				componentList = Filter.setFilterRule(Filter.INTER_COMPONENT_FILTER);
				for (int j = 0; j < componentList.size(); j++) {
					if (j != componentList.size() - 1) {
						Filter.selectComponent(Filter.INTER_COMPONENT_FILTER, componentList.get(j),
								componentList.get(j + 1));
					} else {
						Filter.selectComponent(Filter.INTER_COMPONENT_FILTER, componentList.get(j),
								componentList.get(0));
					}
					graphNode = Filter.getGraphNode();

					GraphNode gNodeToDebug = Filter.new GraphNode();
					for (i = 0; i < graphNode.size(); i++) {
						gNodeToDebug = graphNode.get(i);
						g.addVertex(gNodeToDebug.caller);
						g.addVertex(gNodeToDebug.callee);
						g.addEdge(gNodeToDebug.caller, gNodeToDebug.callee,
								gNodeToDebug.functionName + ":" + gNodeToDebug.param);
					}
				}
			}

		} else if (filteringRule == Filter.FILE_FILTER) {

			if (options.size() != 0) {
				Filter.selectComponent(Filter.FILE_FILTER, options.get(0), null);
				graphNode = Filter.getGraphNode();

				GraphNode gNodeToDebug = Filter.new GraphNode();
				for (i = 0; i < graphNode.size(); i++) {
					gNodeToDebug = graphNode.get(i);
					g.addVertex(gNodeToDebug.caller);
					g.addVertex(gNodeToDebug.callee);
					g.addEdge(gNodeToDebug.caller, gNodeToDebug.callee,
							gNodeToDebug.functionName + ":" + gNodeToDebug.param);
				}
			} else {
				componentList = Filter.setFilterRule(Filter.FILE_FILTER);
				for (int j = 0; j < componentList.size(); j++) {
					Filter.selectComponent(Filter.FILE_FILTER, componentList.get(j), null);
					graphNode = Filter.getGraphNode();

					GraphNode gNodeToDebug = Filter.new GraphNode();
					for (i = 0; i < graphNode.size(); i++) {
						gNodeToDebug = graphNode.get(i);
						g.addVertex(gNodeToDebug.caller);
						g.addVertex(gNodeToDebug.callee);
						g.addEdge(gNodeToDebug.caller, gNodeToDebug.callee,
								gNodeToDebug.functionName + ":" + gNodeToDebug.param);
					}
				}
			}

		} else if (filteringRule == Filter.TEST_CASE_FILTER) {

			if (options.get(1) == null) {
				Filter.selectComponent(Filter.TEST_CASE_FILTER, options.get(0), null);
				graphNode = Filter.getGraphNode();

				GraphNode gNodeToDebug = Filter.new GraphNode();
				for (i = 0; i < graphNode.size(); i++) {
					gNodeToDebug = graphNode.get(i);
					g.addVertex(gNodeToDebug.caller);
					g.addVertex(gNodeToDebug.callee);
					g.addEdge(gNodeToDebug.caller, gNodeToDebug.callee,
							gNodeToDebug.functionName + ":" + gNodeToDebug.param);
				}
			} else {
				Filter.selectComponent(Filter.TEST_METHOD_FILTER, options.get(1), null);
				graphNode = Filter.getGraphNode();
				
				GraphNode gNodeToDebug = Filter.new GraphNode();
				for (i = 0; i < graphNode.size(); i++) {
					gNodeToDebug = graphNode.get(i);
					g.addVertex(gNodeToDebug.caller);
					g.addVertex(gNodeToDebug.callee);
					g.addEdge(gNodeToDebug.caller, gNodeToDebug.callee,
							gNodeToDebug.functionName + ":" + gNodeToDebug.param);
				}
			}
		} else if (filteringRule == Filter.TEST_METHOD_FILTER) {

			Filter.selectComponent(Filter.TEST_METHOD_FILTER, options.get(0), null);
			graphNode = Filter.getGraphNode();

			GraphNode gNodeToDebug = Filter.new GraphNode();
			for (i = 0; i < graphNode.size(); i++) {
				gNodeToDebug = graphNode.get(i);
				g.addVertex(gNodeToDebug.caller);
				g.addVertex(gNodeToDebug.callee);
				g.addEdge(gNodeToDebug.caller, gNodeToDebug.callee,
						gNodeToDebug.functionName + ":" + gNodeToDebug.param);
			}

		}

		long end = System.currentTimeMillis();
		System.out.println("VFTGraph : Filter  (ms) " + (end - start));

		return g;

	}
}
