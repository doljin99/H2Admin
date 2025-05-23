/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotmail.doljin99;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 *
 * @author dolji
 */
public class ServerTree extends JTree {

    private ServerMen serverList;

    public ServerTree(ServerMen serverList) {
        this(new DefaultMutableTreeNode("servers"), serverList);
    }

    public ServerTree(TreeNode root, ServerMen serverList) {
        super(root);
        this.serverList = serverList;

        init();
    }

    private void init() {
        setCellRenderer(new TableTreeCellRenderer());
        getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        //Listen for when the selection changes.
//        addTreeSelectionListener(new SelectionListener());
    }

    ServerMan findServerMan(String name) {
        for (int i = 0; i < serverList.getServerListSize(); i++) {
            ServerMan serverMan = serverList.getServerMan(i);
            if (name.equals(serverMan.getServerName())) {
                return serverMan;
            }
        }
        return null;
    }

    class SelectionListener implements TreeSelectionListener {

        @Override
        public void valueChanged(TreeSelectionEvent e) {
            TreePath selected = e.getPath();
            int count = selected.getPathCount();
//            System.out.println("pathcount = " + count);
        }

    }

    private class TableTreeCellRenderer extends DefaultTreeCellRenderer {

        Icon defaultLeafIcon;
        ImageIcon globalIcon;
        ImageIcon serverIcon;
        ImageIcon serverOnIcon;
        ImageIcon serverOffIcon;
        ImageIcon dbmsIcon;
        ImageIcon dbIcon;
        ImageIcon tableIcon;

        public TableTreeCellRenderer() {
            super();

            init();
        }

        private void init() {
            defaultLeafIcon = this.getDefaultLeafIcon();
            globalIcon = createImageIcon("global.png");
            serverIcon = createImageIcon("server.png");
            serverOnIcon = createImageIcon("serverOn.png");
            serverOffIcon = createImageIcon("serverOff.png");
            dbmsIcon = createImageIcon("dbms.png");
            dbIcon = createImageIcon("db.png");
            tableIcon = createImageIcon("table.png");
        }

        @Override
        public java.awt.Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            if (tree == null) {
                return this;
            }
            TreePath tp = tree.getPathForRow(row);
            if (tp == null) {
                return this;
            }
            switch (tp.getPathCount()) {
                case 1:
                    setIcon(globalIcon);
                    break;
                case 2:
//                    System.out.println("tp.toString = " + tp.getLastPathComponent().toString());
                    ServerMan serverMan = findServerMan(tp.getLastPathComponent().toString());
                    if (serverMan != null && serverMan.isRun()) {
                        setIcon(serverOnIcon);
                    } else {
                        setIcon(serverOffIcon);
                    }                    
                    break;
                case 3:
                    setIcon(dbmsIcon);
                    break;
                case 4:
                    setIcon(dbIcon);
                    break;
                case 5:
                    setIcon(tableIcon);
                    break;
            }
            return this;
        }
    }

    protected ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = ServerTree.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.out.println("Couldn't find file: " + path);
            return null;
        }
    }
}
