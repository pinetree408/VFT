package vft.views;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

public class VFTTree {

    public static JTree init()
    {
    	JTree tree;
    	
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
        
        return tree;

    }
}
