package vft.views;


import vft.views.VFTGraph;
import vft.views.VFTTree;

import java.awt.Button;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.*;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.SWT;

// Import Lib for Graph
import org.jgrapht.ListenableGraph;
import org.jgrapht.ext.JGraphXAdapter;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.swing.mxGraphComponent;

import java.util.ArrayList;

import vft.filter.FilterWrapper;

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

	private JPanel graphPanel;
	private JPanel treePanel;
	private JPanel selectPane;
	private Integer filterRule;
	
	private ArrayList<String> options;
	private String packageFrom;
	private String packageTo;
	private String file;
	private String testCase;
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
		Frame frame = SWT_AWT.new_Frame(composite);
		JSplitPane splitPaneV = new JSplitPane( JSplitPane.VERTICAL_SPLIT);
		
		JTabbedPane tabPane = new JTabbedPane();
		
		// Add Panel for graph
		graphPanel = new JPanel();
		
		// Add Graph
		filterRule = 0;
		options = new ArrayList<String>();
		ListenableGraph<String, String> g = VFTGraph.init(filterRule, options);
		JGraphXAdapter<String, String> graphAdapter = 
				new JGraphXAdapter<String, String>(g);
		mxIGraphLayout layout = new mxCircleLayout(graphAdapter);
		layout.execute(graphAdapter.getDefaultParent());
		graphPanel.add(new mxGraphComponent(graphAdapter));
		
		// Add Panel for Tree
		treePanel = new JPanel();
		
		// Add Tree
		treePanel.add(VFTTree.init(filterRule, options));
		
		tabPane.addTab("VFT Graph", graphPanel);
		tabPane.addTab("VFT Tree", treePanel);
		
		selectPane = new JPanel();
		drawInitialSelectPane();
		
		splitPaneV.setLeftComponent(tabPane);
		splitPaneV.setRightComponent(selectPane);
		frame.add(splitPaneV);
		
	}
	
	private void drawInitialSelectPane() {
		
		String[] filterRules = { "NONE", "INTER_COMPONENT_FILTER", "FILE_FILTER", "TEST_CASE_FILTE", "TEST_METHOD_FILTER"};
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
	            	selectPane.add(fileBox);
            	} else if (filterRule == Filter.TEST_CASE_FILTER) {
	            	ArrayList<String> componentList = Filter.setFilterRule(Filter.TEST_CASE_FILTER);
	            	JComboBox<String> testCaseBox = new JComboBox<String>(componentList.toArray(new String[componentList.size()]));
	            	testCaseBox.addActionListener(new ActionListener() {
	                    @Override
	                    public void actionPerformed(ActionEvent e) {
	                    	testCase = testCaseBox.getSelectedItem().toString();
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
            	} else if (filterRule == Filter.TEST_METHOD_FILTER) {
            		if (testCase == null){
    	            	ArrayList<String> componentList = Filter.setFilterRule(Filter.TEST_METHOD_FILTER);
    	            	testMethod = componentList.get(0);
            		}
            		options.add(testMethod);
            	}
            	
            	graphPanel.removeAll();
        		ListenableGraph<String, String> g = VFTGraph.init(filterRule, options);
        		JGraphXAdapter<String, String> graphAdapter = 
        				new JGraphXAdapter<String, String>(g);
        		mxIGraphLayout layout = new mxCircleLayout(graphAdapter);
        		layout.execute(graphAdapter.getDefaultParent());
        		graphPanel.add(new mxGraphComponent(graphAdapter));
        		graphPanel.revalidate();
        		graphPanel.repaint();
        		
        		treePanel.removeAll();
        		treePanel.add(VFTTree.init(filterRule, options));
        		treePanel.revalidate();
        		treePanel.repaint();
        		
            }
        });
		
		selectPane.add(a);
		selectPane.add(comboBox);
	}
	
	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		//viewer.getControl().setFocus();
	}
}