package vft.views;


import vft.views.VFTGraph;
import vft.views.VFTTree;
import vft.filter.FilterWrapper;
import vft.filter.Filter.TextualNode;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.*;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.SWT;

// Import Lib for Graph
import org.jgrapht.ListenableGraph;
import org.jgrapht.event.GraphEdgeChangeEvent;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DirectedMultigraph;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventSource.mxIEventListener;

import java.util.ArrayList;

/**
 * This sample class demonstrates how to plug-in a new
 * workbench view. The view shows data obtained from the
 * model. The sample creates a dummy model on the fly,
 * but a real implementation would connect to the model
 * available either in this or another plug-in (e.g. the workspace).
 * The view is connected to the model using a content provider.
 * <p>
 * The view uses a label provider to define how model
 * objects should be presented in the view. Each
 * view can present the same model objects using
 * different labels and icons, if needed. Alternatively,
 * a single label provider can be shared between views
 * in order to ensure that objects of the same type are
 * presented in the same way everywhere.
 * <p>
 */

public class VFTView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "vft.views.VFTView";

	private Frame frame;
	
	private JPanel graphPanel;
	private JPanel treePanel;
	private JPanel selectPane;
	private JTabbedPane tabPane;
	private Integer filterRule;
	
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
	public VFTView() {
	}

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		
		// Add JFrame in plug-in view
		Composite composite = new Composite(parent, SWT.EMBEDDED | SWT.NO_BACKGROUND);
		frame = SWT_AWT.new_Frame(composite);
		JSplitPane splitPaneV = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPaneV.setDividerLocation(500);
		
		tabPane = new JTabbedPane();
		
		// Add Panel for graph
		graphPanel = new JPanel(new BorderLayout());
		
		// Add Graph
		filterRule = 0;
		options = new ArrayList<String>();
		drawGraph(filterRule, options);
		
		// Add Panel for Tree
		treePanel = new JPanel(new BorderLayout());
		
		// Add Tree
		treePanel.add(VFTTree.init(filterRule, options));
		
		tabPane.addTab("VFT Graph", new JScrollPane(graphPanel));
		tabPane.addTab("VFT Tree", treePanel);
		
		selectPane = new JPanel();
		drawInitialSelectPane();
		
		splitPaneV.setTopComponent(tabPane);
		splitPaneV.setBottomComponent(selectPane);
		
		frame.add(splitPaneV);
	
	}
	
	private void drawGraph(int filterRule, ArrayList<String> options) {
		
		ListenableGraph<String, String> g = VFTGraph.init(filterRule, options);
		JGraphXAdapter<String, String> graphAdapter = 
				new JGraphXAdapter<String, String>(g);

		Object[] edgeCellArray = new Object[graphAdapter.getEdgeToCellMap().size()];
		for (int i = 0; i < graphAdapter.getEdgeToCellMap().size(); ++i) {
		  edgeCellArray[i] = (Object)(graphAdapter.getEdgeToCellMap().get(g.edgeSet().toArray()[i]));
		}
		graphAdapter.setCellStyle("fontSize=3", edgeCellArray);
		graphAdapter.setEnabled(false);
		graphAdapter.setConnectableEdges(false);
		graphAdapter.setCellsMovable(false);
		graphAdapter.setCellsResizable(false);
		graphAdapter.setCellsEditable(false);
		graphAdapter.setAllowDanglingEdges(false);
		
		mxHierarchicalLayout layout = new mxHierarchicalLayout(graphAdapter);
		layout.setInterHierarchySpacing(5.0);
		layout.setInterRankCellSpacing(100.0);
		layout.setIntraCellSpacing(15.0);
		layout.execute(graphAdapter.getDefaultParent());
		
		mxGraphComponent test = new mxGraphComponent(graphAdapter);
		test.getGraphControl().addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e)
            {
            	Object cell = test.getCellAt(e.getX(), e.getY());
            	if (cell instanceof mxCell) {
            		String cellTitle = ((mxCell) cell).getValue().toString();

                	FilterWrapper Filter = new FilterWrapper();
            		JDialog infoDia = new JDialog();
            		infoDia.setLocation(100 + e.getX(), 100 + e.getY());
            		infoDia.setTitle(String.valueOf(filterRule));
            		options.clear();
            		options.add(cellTitle);
            		infoDia.add(VFTTree.init(Filter.TEST_METHOD_FILTER, options));
            		infoDia.pack();
            		infoDia.setModal(true);
            		infoDia.setVisible(true);

            	}
            }
        });

		graphPanel.add(test);
		
	}
	
	private void drawInitialSelectPane() {
		
		String[] filterRules = { "NONE", "INTER_COMPONENT_FILTER", "FILE_FILTER", "TEST_CASE_FILTER", "TEST_METHOD_FILTER"};
        JComboBox<String> comboBox = new JComboBox<String>(filterRules);
        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	filterRule = comboBox.getSelectedIndex();

            	FilterWrapper Filter = new FilterWrapper();
            	Filter.prePareLogData();

        		if (selectPane.getComponentCount() == 4) {
        			selectPane.remove(selectPane.getComponentCount() - 1);
        			selectPane.remove(selectPane.getComponentCount() - 1);
        		} else if (selectPane.getComponentCount() == 3) {
        			selectPane.remove(selectPane.getComponentCount() - 1);
        		}
        		
            	if (filterRule == Filter.INTER_COMPONENT_FILTER) {
	            	ArrayList<String> componentList = Filter.setFilterRule(Filter.INTER_COMPONENT_FILTER);
	            	JComboBox<String> packageBoxFrom = new JComboBox<String>(componentList.toArray(new String[componentList.size()]));
	            	JComboBox<String> packageBoxTo = new JComboBox<String>(componentList.toArray(new String[componentList.size()]));
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
	                	graphPanel.removeAll();
	                	drawGraph(filterRule, options);
	            		graphPanel.revalidate();
	            		graphPanel.repaint();
	            	}
	            	selectPane.add(packageBoxFrom);
	            	selectPane.add(packageBoxTo);
            	} else if (filterRule == Filter.FILE_FILTER) {
	            	ArrayList<String> componentList = Filter.setFilterRule(Filter.FILE_FILTER);
	            	JComboBox<String> fileBox = new JComboBox<String>(componentList.toArray(new String[componentList.size()]));
	            	fileBox.addActionListener(new ActionListener() {
	                    @Override
	                    public void actionPerformed(ActionEvent e) {
	                    	file = fileBox.getSelectedItem().toString();
	                    }
	                });
	            	options.clear();
	            	if (file == null) {
	                	graphPanel.removeAll();
	                	drawGraph(filterRule, options);
	            		graphPanel.revalidate();
	            		graphPanel.repaint();
	            	}
	            	selectPane.add(fileBox);
            	} else if (filterRule == Filter.TEST_CASE_FILTER) {
	            	ArrayList<String> componentList = Filter.setFilterRule(Filter.TEST_CASE_FILTER);
	            	JComboBox<String> testCaseBox = new JComboBox<String>(componentList.toArray(new String[componentList.size()]));
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

	    	            	ArrayList<String> methodComponentList = Filter.setFilterRule(Filter.TEST_METHOD_FILTER);
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
	                    	JComboBox<String> testCaseMethodBox = new JComboBox<String>(test.toArray(new String[test.size()]));
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
	            	JComboBox<String> testMethodBox = new JComboBox<String>(componentList.toArray(new String[componentList.size()]));
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
        
		Button a = new Button("draw");
		a.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
            	options.clear();
            	FilterWrapper Filter = new FilterWrapper();
            	Filter.prePareLogData();
            	if (filterRule == Filter.INTER_COMPONENT_FILTER) {
            		if (packageFrom == null){
    	            	ArrayList<String> componentList = Filter.setFilterRule(Filter.INTER_COMPONENT_FILTER);
    	            	packageFrom = componentList.get(0);
            		}
            		if (packageTo == null){
    	            	ArrayList<String> componentList = Filter.setFilterRule(Filter.INTER_COMPONENT_FILTER);
    	            	packageTo = componentList.get(0);
            		}
                	options.add(packageFrom);
                	options.add(packageTo);
            	} else if (filterRule == Filter.FILE_FILTER) {
            		if (file == null){
    	            	ArrayList<String> componentList = Filter.setFilterRule(Filter.FILE_FILTER);
    	            	file = componentList.get(0);
            		}
            		options.add(file);
            	} else if (filterRule == Filter.TEST_CASE_FILTER) {
            		if (testCase == null){
    	            	ArrayList<String> componentList = Filter.setFilterRule(Filter.TEST_CASE_FILTER);
    	            	testCase = componentList.get(0);
            		}
            		options.add(testCase);
            		options.add(testCaseMethod);
            	} else if (filterRule == Filter.TEST_METHOD_FILTER) {
            		if (testMethod == null){
    	            	ArrayList<String> componentList = Filter.setFilterRule(Filter.TEST_METHOD_FILTER);
    	            	testMethod = componentList.get(0);
            		}
            		options.add(testMethod);
            	}
            	
            	
            	graphPanel.removeAll();
            	drawGraph(filterRule, options);
        		graphPanel.revalidate();
        		graphPanel.repaint();
        		
        		treePanel.removeAll();
        		treePanel.add(VFTTree.init(filterRule, options));
        		treePanel.revalidate();
        		treePanel.repaint();
        		
            }
        });
		
		/*
		Button b = new Button("Zoom");
		b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	System.out.println(graphPanel.getWidth());
            }
        });
		Button c = new Button("Export");
		*/
		
		selectPane.add(a);
		selectPane.add(comboBox);
		//selectPane.add(b);
	}
	
	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		//viewer.getControl().setFocus();
	}
	
}