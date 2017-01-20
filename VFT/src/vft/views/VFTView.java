package vft.views;

import vft.views.VFTGraph;
import vft.views.VFTTree;
import vft.filter.FilterWrapper;
import vft.filter.Filter.TextualNode;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.*;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.SWT;

// Import Lib for Graph
import org.jgrapht.ListenableGraph;
import org.jgrapht.ext.JGraphXAdapter;
import org.xml.sax.SAXException;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;

import java.util.ArrayList;

/**
 * This sample class demonstrates how to plug-in a new workbench view. The view shows data obtained
 * from the model. The sample creates a dummy model on the fly, but a real implementation would
 * connect to the model available either in this or another plug-in (e.g. the workspace). The view
 * is connected to the model using a content provider.
 * <p>
 * The view uses a label provider to define how model objects should be presented in the view. Each
 * view can present the same model objects using different labels and icons, if needed.
 * Alternatively, a single label provider can be shared between views in order to ensure that
 * objects of the same type are presented in the same way everywhere.
 * <p>
 */

public class VFTView extends ViewPart {

  /**
   * The ID of the view as specified by the extension.
   */
  public static final String ID = "vft.views.VFTView";

  private Frame frame;

  private FilterWrapper Filter;

  private JPanel graphPanel;
  private JPanel treePanel;
  private JPanel selectPane;
  private JTabbedPane tabPane;
  private Integer filterRule;
  private String[] filterRules =
      {"NONE", "INTER_COMPONENT_FILTER", "FILE_FILTER", "TEST_CASE_FILTER", "TEST_METHOD_FILTER"};

  private ArrayList<String> options;
  private String packageFrom;
  private String packageTo;
  private String file;
  private String testCase;
  private String testCaseMethod;
  private String testMethod;

  /**
   * The constructor.
   */
  public VFTView() {}

  /**
   * This is a callback that will allow us to create the viewer and initialize it.
   */
  public void createPartControl(Composite parent) {

    // Initialize
    filterRule = 0;
    options = new ArrayList<String>();
    Filter = null;
    try {
      Filter = new FilterWrapper();
    } catch (SAXException | IOException | ParserConfigurationException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }
    Filter.prePareLogData();

    // Add JFrame in plug-in view
    Composite composite = new Composite(parent, SWT.EMBEDDED | SWT.NO_BACKGROUND);
    frame = SWT_AWT.new_Frame(composite);

    // Add Panel for graph
    graphPanel = new JPanel(new BorderLayout());
    // draw Graph
    drawGraph(filterRule, options);

    // Add Panel for Tree
    treePanel = new JPanel(new BorderLayout());
    // draw Tree
    drawTree(filterRule, options);

    // Add Panel for tab
    tabPane = new JTabbedPane();
    tabPane.addTab("VFT Graph", new JScrollPane(graphPanel));
    tabPane.addTab("VFT Tree", new JScrollPane(treePanel));

    // Add Panel for select
    selectPane = new JPanel();
    drawSelectPane();

    JSplitPane splitPaneV = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    splitPaneV.setDividerLocation(500);
    splitPaneV.setTopComponent(tabPane);
    splitPaneV.setBottomComponent(selectPane);

    frame.add(splitPaneV);

  }

  private void drawGraph(int filterRule, ArrayList<String> options) {

    ListenableGraph<String, String> g = VFTGraph.init(Filter, filterRule, options);
    JGraphXAdapter<String, String> graphAdapter = changeGraphToAdapter(g);

    mxHierarchicalLayout layout = new mxHierarchicalLayout(graphAdapter);
    layout.setInterHierarchySpacing(5.0);
    layout.setInterRankCellSpacing(100.0);
    layout.setIntraCellSpacing(15.0);
    layout.execute(graphAdapter.getDefaultParent());

    mxGraphComponent grpahComponent = new mxGraphComponent(graphAdapter);
    grpahComponent.getGraphControl().addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        Object cell = grpahComponent.getCellAt(e.getX(), e.getY());
        if (cell instanceof mxCell) {
          String cellTitle = ((mxCell) cell).getValue().toString();
          drawModal(e, cellTitle, filterRule);
        }
      }
    });

    graphPanel.add(grpahComponent);

  }

  private void drawModal(MouseEvent e, String cellTitle, int filterRule) {
    JDialog infoDia = new JDialog();
    infoDia.setLocation(100 + e.getX(), 100 + e.getY());
    infoDia.setTitle(filterRules[filterRule]);
    options.clear();
    options.add(cellTitle);
    JPanel dialogPannel = new JPanel(new BorderLayout());
    dialogPannel.add(VFTTree.init(Filter, Filter.TEST_METHOD_FILTER, options));
    infoDia.add(new JScrollPane(dialogPannel));
    infoDia.pack();
    infoDia.setModal(true);
    infoDia.setVisible(true);
  }

  private JGraphXAdapter<String, String> changeGraphToAdapter(ListenableGraph<String, String> g) {
    JGraphXAdapter<String, String> graphAdapter = new JGraphXAdapter<String, String>(g);
    Object[] edgeCellArray = new Object[graphAdapter.getEdgeToCellMap().size()];
    for (int i = 0; i < graphAdapter.getEdgeToCellMap().size(); ++i) {
      edgeCellArray[i] = (Object) (graphAdapter.getEdgeToCellMap().get(g.edgeSet().toArray()[i]));
    }

    // Set Style to graph
    graphAdapter.setCellStyle("fontSize=3", edgeCellArray);
    graphAdapter.setEnabled(false);
    graphAdapter.setConnectableEdges(false);
    graphAdapter.setCellsMovable(false);
    graphAdapter.setCellsResizable(false);
    graphAdapter.setCellsEditable(false);
    graphAdapter.setAllowDanglingEdges(false);

    return graphAdapter;
  }

  private void drawTree(int filterRule, ArrayList<String> options) {
    treePanel.add(VFTTree.init(Filter, filterRule, options));
  }

  private void drawSelectPane() {

    JComboBox<String> comboBox = new JComboBox<String>(filterRules);
    comboBox.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        filterRule = comboBox.getSelectedIndex();

        if (selectPane.getComponentCount() == 4) {
          selectPane.remove(selectPane.getComponentCount() - 1);
          selectPane.remove(selectPane.getComponentCount() - 1);
        } else if (selectPane.getComponentCount() == 3) {
          selectPane.remove(selectPane.getComponentCount() - 1);
        }

        if (filterRule == Filter.INTER_COMPONENT_FILTER) {
          ArrayList<String> componentList = Filter.setFilterRule(Filter.INTER_COMPONENT_FILTER);
          JComboBox<String> packageBoxFrom =
              new JComboBox<String>(componentList.toArray(new String[componentList.size()]));
          JComboBox<String> packageBoxTo =
              new JComboBox<String>(componentList.toArray(new String[componentList.size()]));
          packageBoxFrom.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              packageFrom = packageBoxFrom.getSelectedItem().toString();
            }
          });
          packageBoxTo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              packageTo = packageBoxTo.getSelectedItem().toString();
            }
          });
          options.clear();
          if (packageFrom == null && packageTo == null) {
            rePaintGraph();
          }
          selectPane.add(packageBoxFrom);
          selectPane.add(packageBoxTo);
        } else if (filterRule == Filter.FILE_FILTER) {
          ArrayList<String> componentList = Filter.setFilterRule(Filter.FILE_FILTER);
          JComboBox<String> fileBox =
              new JComboBox<String>(componentList.toArray(new String[componentList.size()]));
          fileBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              file = fileBox.getSelectedItem().toString();
            }
          });
          options.clear();
          if (file == null) {
            rePaintGraph();
          }
          selectPane.add(fileBox);
        } else if (filterRule == Filter.TEST_CASE_FILTER) {
          ArrayList<String> componentList = Filter.setFilterRule(Filter.TEST_CASE_FILTER);
          JComboBox<String> testCaseBox =
              new JComboBox<String>(componentList.toArray(new String[componentList.size()]));
          testCaseBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              testCase = testCaseBox.getSelectedItem().toString();

              if (selectPane.getComponentCount() == 4) {
                selectPane.remove(selectPane.getComponentCount() - 1);
              }

              Filter.prePareTextTreeData(testCase);
              ArrayList<String> test = new ArrayList<String>();
              ArrayList<TextualNode> textualNode = Filter.getTextualNode();

              ArrayList<String> methodComponentList =
                  Filter.setFilterRule(Filter.TEST_METHOD_FILTER);
              TextualNode gTextualTemp = null;
              for (int i = 0; i < textualNode.size(); i++) {
                gTextualTemp = textualNode.get(i);
                for (int j = 0; j < methodComponentList.size(); j++) {
                  if (methodComponentList.get(j).contains(gTextualTemp.functionName)) {
                    if (!test.contains(methodComponentList.get(j))) {
                      test.add(methodComponentList.get(j));
                    }
                  }
                }
              }
              JComboBox<String> testCaseMethodBox =
                  new JComboBox<String>(test.toArray(new String[test.size()]));
              testCaseMethodBox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                  testCaseMethod = testCaseMethodBox.getSelectedItem().toString();
                }
              });
              selectPane.add(testCaseMethodBox);
              selectPane.revalidate();
              selectPane.repaint();
            }
          });
          selectPane.add(testCaseBox);
        } else if (filterRule == Filter.TEST_METHOD_FILTER) {
          ArrayList<String> componentList = Filter.setFilterRule(Filter.TEST_METHOD_FILTER);
          JComboBox<String> testMethodBox =
              new JComboBox<String>(componentList.toArray(new String[componentList.size()]));
          testMethodBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              testMethod = testMethodBox.getSelectedItem().toString();
            }
          });
          selectPane.add(testMethodBox);
        }

        selectPane.revalidate();
        selectPane.repaint();
      }
    });

    Button drawButton = new Button("draw");
    drawButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        setOptions();
        rePaintGraph();
        rePaintTree();
      }
    });

    selectPane.add(drawButton);
    selectPane.add(comboBox);
  }

  private void setOptions() {
    ArrayList<String> componentList = Filter.setFilterRule(filterRule);
    options.clear();

    if (filterRule == Filter.INTER_COMPONENT_FILTER) {
      if (packageFrom == null) {
        packageFrom = componentList.get(0);
      }
      if (packageTo == null) {
        packageTo = componentList.get(0);
      }
      options.add(packageFrom);
      options.add(packageTo);
    } else if (filterRule == Filter.FILE_FILTER) {
      if (file == null) {
        file = componentList.get(0);
      }
      options.add(file);
    } else if (filterRule == Filter.TEST_CASE_FILTER) {
      if (testCase == null) {
        testCase = componentList.get(0);
      }
      options.add(testCase);
      options.add(testCaseMethod);
    } else if (filterRule == Filter.TEST_METHOD_FILTER) {
      if (testMethod == null) {
        testMethod = componentList.get(0);
      }
      options.add(testMethod);
    }
  }

  private void rePaintGraph() {
    graphPanel.removeAll();
    drawGraph(filterRule, options);
    graphPanel.revalidate();
    graphPanel.repaint();
    resetSelectedItems();
  }

  private void rePaintTree() {
    treePanel.removeAll();
    drawTree(filterRule, options);
    treePanel.revalidate();
    treePanel.repaint();
    resetSelectedItems();
  }

  private void resetSelectedItems() {
    packageFrom = null;
    packageTo = null;
    file = null;
    testCase = null;
    testCaseMethod = null;
    testMethod = null;
  }

  /**
   * Passing the focus request to the viewer's control.
   */
  public void setFocus() {}

}
