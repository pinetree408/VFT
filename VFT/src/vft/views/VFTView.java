package vft.views;


import vft.views.VFTGraph;
import vft.views.VFTTree;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.*;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.jface.action.*;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.*;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.SWT;

// Import Lib for Graph
import org.jgrapht.ListenableGraph;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultEdge;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.swing.mxGraphComponent;

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

	private TableViewer viewer;
	private Action action1;
	private Action action2;
	private Action doubleClickAction;
	private Integer option;

	/*
	 * The content provider class is responsible for
	 * providing objects to the view. It can wrap
	 * existing objects in adapters or simply return
	 * objects as-is. These objects may be sensitive
	 * to the current input of the view, or ignore
	 * it and always show the same content 
	 * (like Task List, for example).
	 */
	 
	class ViewContentProvider implements IStructuredContentProvider {
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}
		public void dispose() {
		}
		public Object[] getElements(Object parent) {
			return new String[] { "One", "Two", "Three" };
		}
	}
	class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {
		public String getColumnText(Object obj, int index) {
			return getText(obj);
		}
		public Image getColumnImage(Object obj, int index) {
			return getImage(obj);
		}
		public Image getImage(Object obj) {
			return PlatformUI.getWorkbench().
					getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
		}
	}
	class NameSorter extends ViewerSorter {
	}

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
		//viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		//viewer.setContentProvider(new ViewContentProvider());
		//viewer.setLabelProvider(new ViewLabelProvider());
		//viewer.setSorter(new NameSorter());
		//viewer.setInput(getViewSite());
		
		// Add JFrame in plug-in view
		Composite composite = new Composite(parent, SWT.EMBEDDED | SWT.NO_BACKGROUND);
		Frame frame = SWT_AWT.new_Frame(composite);
		JSplitPane splitPaneV = new JSplitPane( JSplitPane.VERTICAL_SPLIT);
		
		JTabbedPane tabPane = new JTabbedPane();
		
		// Add Panel for graph
		JPanel graphPanel = new JPanel(new BorderLayout());
		JLabel graphLabel = new JLabel("VFT GRAPH");
		graphPanel.add(graphLabel,BorderLayout.NORTH);
		
		// Add Graph
		option = 1;
		ListenableGraph<String, String> g = VFTGraph.init(option);
		JGraphXAdapter<String, String> graphAdapter = 
				new JGraphXAdapter<String, String>(g);
		mxIGraphLayout layout = new mxCircleLayout(graphAdapter);
		layout.execute(graphAdapter.getDefaultParent());
		graphPanel.add(new mxGraphComponent(graphAdapter));
		
		// Add Panel for Tree
		JPanel treePanel = new JPanel(new BorderLayout());
		JLabel treeLabel = new JLabel("VFT Tree");
		treePanel.add(treeLabel,BorderLayout.NORTH);
		
		// Add Tree
		treePanel.add(VFTTree.init());
		
		tabPane.addTab("Graph", graphPanel);
		tabPane.addTab("Tree", treePanel);
		
		JPanel selectPane = new JPanel();
		
		String[] options = { "INTER_COMPONENT_FILTER", "FILE_FILTER", "TEST_CASE_FILTE", "TEST_METHOD_FILTER"};
        JComboBox comboBox = new JComboBox(options);
        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	option = comboBox.getSelectedIndex();
            	
            }
        });
        
		Button a = new Button("draw");
		a.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
            	System.out.println(option);
        		ListenableGraph<String, String> g = VFTGraph.init(option);
        		JGraphXAdapter<String, String> graphAdapter = 
        				new JGraphXAdapter<String, String>(g);
        		mxIGraphLayout layout = new mxCircleLayout(graphAdapter);
        		layout.execute(graphAdapter.getDefaultParent());
        		graphPanel.add(new mxGraphComponent(graphAdapter));
        		graphPanel.revalidate();
            }
        });
		
		selectPane.add(a);
		selectPane.add(comboBox);
		
		splitPaneV.setLeftComponent(tabPane);
		splitPaneV.setRightComponent(selectPane);
		frame.add(splitPaneV);

		// Create the help context id for the viewer's control
		//PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "VFT.viewer");
		//makeActions();
		//hookContextMenu();
		//hookDoubleClickAction();
		//contributeToActionBars();
	}

/*
	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				VFTView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(action1);
		manager.add(new Separator());
		manager.add(action2);
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(action1);
		manager.add(action2);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}
	
	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(action1);
		manager.add(action2);
	}

	private void makeActions() {
		action1 = new Action() {
			public void run() {
				showMessage("Action 1 executed");
			}
		};
		action1.setText("Action 1");
		action1.setToolTipText("Action 1 tooltip");
		action1.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
			getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		
		action2 = new Action() {
			public void run() {
				showMessage("Action 2 executed");
			}
		};
		action2.setText("Action 2");
		action2.setToolTipText("Action 2 tooltip");
		action2.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
				getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		doubleClickAction = new Action() {
			public void run() {
				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection)selection).getFirstElement();
				showMessage("Double-click detected on "+obj.toString());
			}
		};
	}

	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
	}
	private void showMessage(String message) {
		MessageDialog.openInformation(
			viewer.getControl().getShell(),
			"VFT View",
			message);
	}
*/
	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		//viewer.getControl().setFocus();
	}
}