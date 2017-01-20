package vft.views;

import java.util.ArrayList;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import vft.filter.FilterWrapper;
import vft.filter.Filter.GraphNode;
import vft.filter.Filter.TextualNode;

public class VFTTree {

  public static JTree init(FilterWrapper Filter, int filteringRule, ArrayList<String> options) {
    JTree tree;

    DefaultMutableTreeNode root = null;
    DefaultMutableTreeNode caller;
    DefaultMutableTreeNode functionName;
    DefaultMutableTreeNode callee;

    ArrayList<GraphNode> graphNode = new ArrayList<GraphNode>();
    ArrayList<TextualNode> textualNode = new ArrayList<TextualNode>();
    ArrayList<String> componentList;

    int i;

    // select package or file or test case
    if (filteringRule == Filter.INTER_COMPONENT_FILTER) {

      root = new DefaultMutableTreeNode("INTER_COMPONENT_FILTER");

      Filter.selectComponent(Filter.INTER_COMPONENT_FILTER, options.get(0), options.get(1));
      graphNode = Filter.getGraphNode();

      GraphNode gNodeToDebug = Filter.new GraphNode();
      for (i = 0; i < graphNode.size(); i++) {
        gNodeToDebug = graphNode.get(i);
        caller = new DefaultMutableTreeNode(gNodeToDebug.caller);
        callee = new DefaultMutableTreeNode(gNodeToDebug.callee);
        functionName = new DefaultMutableTreeNode(gNodeToDebug.functionName);
        functionName.add(callee);
        caller.add(functionName);
        root.add(caller);
      }

    } else if (filteringRule == Filter.FILE_FILTER) {

      root = new DefaultMutableTreeNode("FILE_FILTER");

      Filter.selectComponent(Filter.FILE_FILTER, options.get(0), null);
      graphNode = Filter.getGraphNode();

      GraphNode gNodeToDebug = Filter.new GraphNode();
      for (i = 0; i < graphNode.size(); i++) {
        gNodeToDebug = graphNode.get(i);
        caller = new DefaultMutableTreeNode(gNodeToDebug.caller);
        callee = new DefaultMutableTreeNode(gNodeToDebug.callee);
        functionName = new DefaultMutableTreeNode(gNodeToDebug.functionName);
        functionName.add(callee);
        caller.add(functionName);
        root.add(caller);
      }

    } else if (filteringRule == Filter.TEST_CASE_FILTER) {

      root = new DefaultMutableTreeNode("TEST_CASE_FILTER");

      Filter.selectComponent(Filter.TEST_CASE_FILTER, options.get(0), null);

      DefaultMutableTreeNode call;
      DefaultMutableTreeNode inner;

      Filter.prePareTextTreeData(options.get(0));
      textualNode = Filter.getTextualNode();
      TextualNode gTextualTemp = null;
      TextualNode gTextualInnerTemp = null;
      for (i = 0; i < textualNode.size(); i++) {
        int j;
        gTextualTemp = textualNode.get(i);
        call = new DefaultMutableTreeNode(gTextualTemp.action + "  " + gTextualTemp.caller + "  "
            + gTextualTemp.lineNumber + "  " + gTextualTemp.callee + "  "
            + gTextualTemp.functionName + "  " + gTextualTemp.param);
        if (gTextualTemp.innerAction.size() > 0) {
          for (j = 0; j < gTextualTemp.innerAction.size(); j++) {
            gTextualInnerTemp = gTextualTemp.innerAction.get(j);
            inner = new DefaultMutableTreeNode(
                gTextualInnerTemp.action + "  " + gTextualInnerTemp.caller + "  "
                    + gTextualInnerTemp.lineNumber + "  " + gTextualInnerTemp.callee + "  "
                    + gTextualInnerTemp.functionName + "  " + gTextualInnerTemp.param);
            call.add(inner);
          }
        }
        root.add(call);
      }

    } else if (filteringRule == Filter.TEST_METHOD_FILTER) {

      root = new DefaultMutableTreeNode(options.get(0));

      String[] cellTitleFinal = options.get(0).split(":");

      Filter.selectComponent(Filter.TEST_METHOD_FILTER, cellTitleFinal[0], null);

      DefaultMutableTreeNode call;

      componentList = Filter.setFilterRule(Filter.TEST_CASE_FILTER);
      for (int k = 0; k < componentList.size(); k++) {
        Filter.prePareTextTreeData(componentList.get(k));
        textualNode = Filter.getTextualNode();
        TextualNode gTextualTemp = null;
        TextualNode gTextualInnerTemp = null;
        for (i = 0; i < textualNode.size(); i++) {
          int j;
          gTextualTemp = textualNode.get(i);
          if (cellTitleFinal[0].equals(gTextualTemp.functionName)) {
            if (gTextualTemp.innerAction.size() > 0) {
              for (j = 0; j < gTextualTemp.innerAction.size(); j++) {
                gTextualInnerTemp = gTextualTemp.innerAction.get(j);
                call = new DefaultMutableTreeNode(
                    gTextualInnerTemp.action + "  " + gTextualInnerTemp.caller + "  "
                        + gTextualInnerTemp.lineNumber + "  " + gTextualInnerTemp.callee + "  "
                        + gTextualInnerTemp.functionName + "  " + gTextualInnerTemp.param);
                root.add(call);
              }
            }
          }
        }
      }
    }

    tree = new JTree(root);
    return tree;

  }
}
