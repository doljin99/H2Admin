/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.hotmail.doljin99;

import com.hotmail.doljin99.loginmanager.LoginManager;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import org.h2.tools.Csv;

/**
 *
 * @author dolji
 */
public class ServerTreePane extends javax.swing.JPanel {

    private Object status;

    private ServerMen serverList;

    private Tables tables;
    private TableColumns columns;

    private List<String> codeCompletionList;

    private JTree tree;
    private final LoginManager loginManager;

    /**
     * Creates new form ServerTree
     *
     * @param serverList
     * @param codeCompletionList
     * @param loginManager
     */
    public ServerTreePane(ServerMen serverList, List<String> codeCompletionList, LoginManager loginManager) {
        this(serverList, null, codeCompletionList, loginManager);
    }

    /**
     * Creates new form ServerTree
     *
     * @param serverList
     * @param status
     * @param codeCompletionList
     * @param loginManager
     */
    public ServerTreePane(ServerMen serverList, Object status, List<String> codeCompletionList, LoginManager loginManager) {
        initComponents();
        this.serverList = serverList;
        this.status = status;
        this.codeCompletionList = codeCompletionList;
        this.loginManager = loginManager;

        init();
    }

    private void init() {
        jSplitPaneServerTree.setDividerLocation(280);

        tables = new Tables();
        columns = new TableColumns();

        makeTree();
    }

    private void addTableNode(String serverName, Connection connection, DefaultMutableTreeNode databaseNode) {
        DatabaseMetaData metaData;
        ResultSet resultSet = null;
        try {
            metaData = connection.getMetaData();
            String catalog = databaseNode.toString().toUpperCase();
            String schemaPattern = "PUBLIC";
            resultSet = metaData.getTables(catalog, schemaPattern, null, null);
            while (resultSet.next()) {
                String tableName = resultSet.getString(3);
                DefaultMutableTreeNode tableNode = new DefaultMutableTreeNode(tableName);
//                tables.addTables(serverName, catalog, metaData);
                tables.addTable(serverName, catalog, tableName, metaData);
                addColumnNode(serverName, tableNode, metaData, catalog, schemaPattern, tableName);
                databaseNode.add(tableNode);
            }
//            setStatus("????????? ?????? ?????? ??????");
        } catch (SQLException ex) {
            setStatus("????????? ?????? ?????? ??? ??????: " + ex.getLocalizedMessage());
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException ex) {
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                }
            }
        }
    }

    private void addColumnNode(String serverName, DefaultMutableTreeNode node, DatabaseMetaData metaData, String catalog, String schemaPattern, String tableName) {
        ResultSet resultSet = null;
        try {
            resultSet = metaData.getColumns(catalog, schemaPattern, tableName, null);
            while (resultSet.next()) {
                String columnName = resultSet.getString(4);
                node.add(new DefaultMutableTreeNode(columnName));
            }
            resultSet.close();
            resultSet = metaData.getColumns(catalog, schemaPattern, tableName, null);
            columns.addColumns(serverName, resultSet);
        } catch (SQLException ex) {
            setStatus("column ?????? ?????? ??? ??????: " + ex.getLocalizedMessage());
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException ex) {
                }
            }
        }
    }

    class SelectionListener implements TreeSelectionListener {

        @Override
        public void valueChanged(TreeSelectionEvent e) {
            TreePath selected = e.getPath();
            if (selected == null) {
                return;
            }
            jButtonStart.setEnabled(false);
            jButtonStop.setEnabled(false);
            jButtonAddDatabase.setEnabled(false);
            jButtonAddTable.setEnabled(false);
            jButtonReadCsv.setEnabled(false);
            jButtonSql.setEnabled(false);
            int count = selected.getPathCount();
            switch (count) {
                case 2: {
                    String name = selected.getLastPathComponent().toString();
//                    setStatus("selected.name = " + name);
                    ServerMan serverMan = findServerMan(name);
                    if (serverMan != null) {
                        if (serverMan.isLocal()) {
                            if (serverMan.isRun()) {
                                jButtonStart.setEnabled(false);
                                jButtonStop.setEnabled(true);
                                jButtonAddDatabase.setEnabled(true);
                            } else {
                                jButtonStart.setEnabled(true);
                                jButtonStop.setEnabled(false);
                                jButtonAddDatabase.setEnabled(false);
                            }
                        } else {
                            jButtonStart.setEnabled(false);
                            jButtonStop.setEnabled(false);
                            jButtonAddDatabase.setEnabled(false);
                        }
                        ServerInfoPane pane = makeServerInfoPane(serverMan);
                        setInfoPane(pane);
//                        BorderLayout layout = (BorderLayout) jPanelInfo.getLayout();
//                        Component component = layout.getLayoutComponent(BorderLayout.CENTER);
//                        if (component != null) {
//                            jPanelInfo.remove(layout.getLayoutComponent(BorderLayout.CENTER));
//                        }
//                        jPanelInfo.add(pane, BorderLayout.CENTER);
//                        jPanelInfo.validate();
                    }
                    break;
                }
                case 3: {
                    Object[] paths = selected.getPath();
                    String serverName = paths[1].toString();
                    String databaseName = paths[2].toString();
                    ServerMan serverMan = findServerMan(serverName);
                    if (serverMan.isRun()) {
                        jButtonAddTable.setEnabled(true);
                    } else {
                        jButtonAddTable.setEnabled(false);
                    }

                    DatabaseInfoPane pane = new DatabaseInfoPane(serverMan, databaseName);
                    setInfoPane(pane);

                    break;
                }
                case 4: {
                    Object[] paths = selected.getPath();
                    String serverName = paths[1].toString();
                    String databaseName = paths[2].toString();
                    String tableName = paths[3].toString();
                    ServerMan serverMan = findServerMan(serverName);
                    DatabaseMan databaseMan = serverMan.findDatabaseMan(serverName, databaseName);
                    if (serverMan.isRun()) {
                        jButtonReadCsv.setEnabled(true);
                    } else {
                        jButtonReadCsv.setEnabled(false);
                    }
                    jButtonSql.setEnabled(true);   // ????????? ?????? ?????? ???????????? ???????????? ?????? ??? ?????? ??????, ?????? ????????? ???????????? ?????? ??? ??????
                    Table table = tables.findByName(serverName, databaseName, tableName);

                    TableInfoPane pane = new TableInfoPane(table);
                    setInfoPane(pane);

                    break;
                }
                case 5: {
                    Object[] paths = selected.getPath();
                    String serverName = paths[1].toString();
                    String databaseName = paths[2].toString();
                    String tableName = paths[3].toString();
                    String columnName = paths[4].toString();
                    TableColumn column = columns.findColumn(serverName, databaseName, tableName, columnName);
                    if (column == null) {
                        setStatus("column not found: " + serverName + ", " + databaseName + ", " + tableName + ", " + columnName);
                        break;
                    }

                    ColumnInfoPane pane = new ColumnInfoPane(column);
                    setInfoPane(pane);

                    break;
                }
                default:
                    break;
            }
//            setStatus("pathcount = " + count);
        }
    }

    private void setInfoPane(JPanel pane) {
        BorderLayout layout = (BorderLayout) jPanelInfo.getLayout();
        Component component = layout.getLayoutComponent(BorderLayout.CENTER);
        if (component != null) {
            jPanelInfo.remove(layout.getLayoutComponent(BorderLayout.CENTER));
        }
        jPanelInfo.add(pane, BorderLayout.CENTER);
        jPanelInfo.validate();
    }

    private ServerInfoPane makeServerInfoPane(ServerMan serverMan) {
        ServerInfoPane pane = new ServerInfoPane(serverMan);
        return pane;
    }

    ServerMan findServerMan(String name) {
        for (int i = 0; i < serverList.size(); i++) {
            ServerMan serverMan = serverList.get(i);
            if (name.equals(serverMan.getServerName())) {
                return serverMan;
            }
        }
        return null;
    }

//    DatabaseMan findDatabaseMan(String serverName, String databaseName) {
//        for (int i = 0; i < databaseList.size(); i++) {
//            DatabaseMan databaseMan = databaseList.get(i);
//            if (serverName.equalsIgnoreCase(databaseMan.getServerName()) && databaseName.equalsIgnoreCase(databaseMan.getDatabaseName())) {
//                return databaseMan;
//            }
//        }
//        return null;
//    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        jButtonStart = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        jButtonStop = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        jButtonAddDatabase = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jButtonDatabaseBackup = new javax.swing.JButton();
        jSeparator6 = new javax.swing.JToolBar.Separator();
        jButtonAddTable = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JToolBar.Separator();
        jButtonReadCsv = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        jButtonSql = new javax.swing.JButton();
        jSplitPaneServerTree = new javax.swing.JSplitPane();
        jPanelTree = new javax.swing.JPanel();
        jPanelInfo = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        jToolBar1.setRollover(true);

        jButtonStart.setIcon(new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/media/Play16.gif"))); // NOI18N
        jButtonStart.setText("Start");
        jButtonStart.setToolTipText("????????? ????????? ??????????????????.");
        jButtonStart.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonStart.setEnabled(false);
        jButtonStart.setFocusable(false);
        jButtonStart.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonStartActionPerformed(evt);
            }
        });
        jToolBar1.add(jButtonStart);
        jToolBar1.add(jSeparator2);

        jButtonStop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/media/Stop16.gif"))); // NOI18N
        jButtonStop.setText("Stop");
        jButtonStop.setToolTipText("????????? ????????? ??????????????????.");
        jButtonStop.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonStop.setEnabled(false);
        jButtonStop.setFocusable(false);
        jButtonStop.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonStopActionPerformed(evt);
            }
        });
        jToolBar1.add(jButtonStop);
        jToolBar1.add(jSeparator3);

        jButtonAddDatabase.setIcon(new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/general/Add16.gif"))); // NOI18N
        jButtonAddDatabase.setText("??????????????????");
        jButtonAddDatabase.setToolTipText("????????? ????????? ?????????????????? ?????? ????????? ????????????????????? ??????????????? ???????????????.");
        jButtonAddDatabase.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonAddDatabase.setEnabled(false);
        jButtonAddDatabase.setFocusable(false);
        jButtonAddDatabase.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonAddDatabase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddDatabaseActionPerformed(evt);
            }
        });
        jToolBar1.add(jButtonAddDatabase);
        jToolBar1.add(jSeparator1);

        jButtonDatabaseBackup.setIcon(new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/general/SaveAll16.gif"))); // NOI18N
        jButtonDatabaseBackup.setText("??????");
        jButtonDatabaseBackup.setToolTipText("????????????????????? ???????????????.");
        jButtonDatabaseBackup.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonDatabaseBackup.setFocusable(false);
        jButtonDatabaseBackup.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonDatabaseBackup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDatabaseBackupActionPerformed(evt);
            }
        });
        jToolBar1.add(jButtonDatabaseBackup);
        jToolBar1.add(jSeparator6);

        jButtonAddTable.setIcon(new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/general/Add16.gif"))); // NOI18N
        jButtonAddTable.setText("?????????");
        jButtonAddTable.setToolTipText("????????? ????????????????????? ???????????? ????????? ??? ?????? ?????? ???????????????.");
        jButtonAddTable.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonAddTable.setEnabled(false);
        jButtonAddTable.setFocusable(false);
        jButtonAddTable.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonAddTable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddTableActionPerformed(evt);
            }
        });
        jToolBar1.add(jButtonAddTable);
        jToolBar1.add(jSeparator5);

        jButtonReadCsv.setIcon(new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/general/Import16.gif"))); // NOI18N
        jButtonReadCsv.setText("CSV ??????");
        jButtonReadCsv.setToolTipText("????????? ???????????? CSV????????? ???????????? ???????????????.");
        jButtonReadCsv.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonReadCsv.setEnabled(false);
        jButtonReadCsv.setFocusable(false);
        jButtonReadCsv.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonReadCsv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReadCsvActionPerformed(evt);
            }
        });
        jToolBar1.add(jButtonReadCsv);
        jToolBar1.add(jSeparator4);

        jButtonSql.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/hotmail/doljin99/definer.png"))); // NOI18N
        jButtonSql.setText("SQL");
        jButtonSql.setToolTipText("????????? ???????????? DML ????????? ??? ??? ?????? ????????? ???????????? ?????? ?????????.");
        jButtonSql.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonSql.setEnabled(false);
        jButtonSql.setFocusable(false);
        jButtonSql.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonSql.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSqlActionPerformed(evt);
            }
        });
        jToolBar1.add(jButtonSql);

        add(jToolBar1, java.awt.BorderLayout.PAGE_START);

        jPanelTree.setLayout(new java.awt.BorderLayout());
        jSplitPaneServerTree.setLeftComponent(jPanelTree);

        jPanelInfo.setLayout(new java.awt.BorderLayout());
        jSplitPaneServerTree.setRightComponent(jPanelInfo);

        add(jSplitPaneServerTree, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonAddDatabaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddDatabaseActionPerformed
        TreePath selectionPath = tree.getSelectionPath();
        if (selectionPath == null) {
            setStatus("?????? ????????? ?????? ????????? ??????????????????.");
            return;
        }
        addDatabase();
    }//GEN-LAST:event_jButtonAddDatabaseActionPerformed

    private void addTable(TreePath selectionPath) {
        Object[] paths = selectionPath.getPath();
        String serverName = paths[1].toString();
        String databaseName = paths[2].toString();
        ServerMan serverMan = findServerMan(serverName);
        DatabaseMan databaseMan = serverMan.findDatabaseMan(serverName, databaseName);
        AddTableDialog dialog = new AddTableDialog((Frame) SwingUtilities.getWindowAncestor(this), true, serverMan, databaseMan, codeCompletionList);
        dialog.setSize(900, 600);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
        boolean changed = dialog.isChanged();
        dialog.dispose();
        if (changed) {
            makeTree();
            refreshTree();
        }
    }

    private void jButtonStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonStartActionPerformed
        TreePath selectionPath = tree.getSelectionPath();
        if (selectionPath == null) {
            setStatus("?????? ????????? ?????? ????????? ??????????????????.");
            return;
        }
        String serverName = selectionPath.getLastPathComponent().toString();
        ServerMan serverMan = findServerMan(serverName);
        if (serverMan == null) {
            setStatus("?????? ????????? ????????????: " + serverName);
            return;
        }
        try {
            boolean success = serverMan.start();
            if (success) {
                setStatus(serverName + ": " + serverMan.getPort() + " server start ??????.");
            } else {
                setStatus(serverName + ": " + serverMan.getPort() + " server start ??????: " + serverMan.getMessage());
            }
            jButtonStart.setEnabled(false);
            jButtonStop.setEnabled(true);
        } catch (SQLException ex) {
            setStatus(serverName + " server start error: " + ex.getLocalizedMessage());
        }
        tree.setSelectionRow(0);
        tree.setSelectionPath(selectionPath);
        tree.validate();
    }//GEN-LAST:event_jButtonStartActionPerformed

    private void jButtonStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonStopActionPerformed
        TreePath selectionPath = tree.getSelectionPath();
        if (selectionPath == null) {
            setStatus("?????? ????????? ?????? ????????? ??????????????????.");
            return;
        }
        String serverName = selectionPath.getLastPathComponent().toString();
        ServerMan serverMan = findServerMan(serverName);
        if (serverMan == null) {
            setStatus("?????? ????????? ????????????: " + serverName);
            return;
        }
        try {
            boolean stopped = serverMan.stop();
            setStatus(serverMan.getServerName() + " ??????: " + serverMan.getPort() + " " + serverMan.getMessage());
        } catch (SQLException ex) {
            setStatus("?????? ?????? ??? ??????: " + ex.getLocalizedMessage());
        }
        tree.setSelectionRow(0);
        tree.setSelectionPath(selectionPath);
        tree.validate();
    }//GEN-LAST:event_jButtonStopActionPerformed

    private void jButtonReadCsvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReadCsvActionPerformed
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV FILE", "csv", "csv");
        chooser.setFileFilter(filter);
        chooser.showOpenDialog(this);
        File file = chooser.getSelectedFile();
        if (file == null) {
            setStatus("CSV ?????? ?????? ??????");
            return;
        }
        TreePath selectionPath = tree.getSelectionPath();
        if (selectionPath == null) {
            setStatus("?????? ???????????? ????????? ????????? ????????? ??????????????????.");
            return;
        }
        Object[] paths = selectionPath.getPath();
        String serverName = paths[1].toString();
        String databaseName = paths[2].toString();
        String tableName = paths[3].toString();
        ServerMan serverMan = findServerMan(serverName);
        DatabaseMan databaseMan = serverMan.findDatabaseMan(serverName, databaseName);
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            Csv csv = new Csv();
            csv.setCaseSensitiveColumnNames(false);
            rs = csv.read(file.getAbsolutePath(), null, null);
            boolean save = serverMan.isRun();
            if (!serverMan.isRun()) {
                serverMan.start();
            }
            Connection connection = serverMan.getConnection(databaseName);
            ArrayList<String> columnNames = getColumnNames(rs.getMetaData());
            String insertPreparedString = makeInsertPreparedString(tableName, columnNames);
            ps = connection.prepareStatement(insertPreparedString);
            int count = 0;
            while (rs.next()) {
                for (int i = 0; i < columnNames.size(); i++) {
                    String columnName = columnNames.get(i);
                    ps.setObject(i + 1, rs.getObject(i + 1));
                }
                count += ps.executeUpdate();
            }
            if (!save) {
                serverMan.stop();
            }
            setStatus(count + " ??? ??????.");
        } catch (SQLException ex) {
            setStatus("CSV ?????? ?????? ??????: " + ex.getLocalizedMessage());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ex) {
                }
            }
        }
    }//GEN-LAST:event_jButtonReadCsvActionPerformed

    private void jButtonSqlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSqlActionPerformed
        TreePath selectionPath = tree.getSelectionPath();
        if (selectionPath == null) {
            setStatus("?????? SQL ???????????? ????????? ????????? ????????? ??????????????????.");
            return;
        }
        Object[] paths = selectionPath.getPath();
        if (paths.length < 4) {
            setStatus("?????? ?????? ????????? ???????????? ????????????. \n refresh tree ???????????? ???????????? ??? ????????? ?????? ????????? ????????? ????????????.");
            return;
        }
        String serverName = paths[1].toString();
        String databaseName = paths[2].toString();
        String tableName = paths[3].toString();
        ServerMan serverMan = serverList.findByName(serverName);
        DatabaseMan databaseMan = serverMan.findDatabaseByName(databaseName);
        Tables databaseTables = tables.getDatabaseTables(serverName, databaseName);
        for (int i = 0; i < databaseTables.size(); i++) {
            Table get = databaseTables.get(i);
//            System.out.println("table = " + get.getTableName());
        }
        Table table = tables.findByName(serverName, databaseName, tableName);
        TableColumns tableColumns = columns.findTableColumns(serverName, databaseName);
        ScriptDialog dialog = new ScriptDialog((Frame) SwingUtilities.getWindowAncestor(this), false, serverMan, databaseMan, databaseTables, tableName, tableColumns, codeCompletionList);
        dialog.setVisible(true);
    }//GEN-LAST:event_jButtonSqlActionPerformed

    private void jButtonAddTableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddTableActionPerformed
        TreePath selectionPath = tree.getSelectionPath();
        if (selectionPath == null) {
            setStatus("?????? ????????? ?????? ????????? ??????????????????.");
            return;
        }
        addTable(selectionPath);
    }//GEN-LAST:event_jButtonAddTableActionPerformed

    private void jButtonDatabaseBackupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDatabaseBackupActionPerformed
        backup();
    }//GEN-LAST:event_jButtonDatabaseBackupActionPerformed

    protected void backup() {
        TreePath selectionPath = tree.getSelectionPath();
        if (selectionPath == null || selectionPath.getPathCount() < 2) {
            setStatus("?????? ????????? ????????? ????????????????????? ??????????????????.");
            return;
        }

        Object[] paths = selectionPath.getPath();
        String serverName = paths[1].toString();
        ServerMan serverMan = findServerMan(serverName);
        if (serverMan == null) {
            setStatus("????????? ?????? ??? ????????????.: " + serverName);
            return;
        }
        if (!serverMan.isLocal()) {
            setStatus("?????? ??????????????? remote ?????? ????????????????????? ?????? ?????? ????????? ????????????.");
            return;
        }

        String databaseName;
        DatabaseMan databaseMan = null;
        if (paths.length >= 3) {
            databaseName = paths[2].toString();
            databaseMan = serverMan.findDatabaseMan(serverName, databaseName);
        }
        BackupDialog dialog = new BackupDialog((Frame) SwingUtilities.getWindowAncestor(this), true, serverMan, databaseMan);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
        if (dialog.isDone()) {
            setStatus("backup??? ?????????????????????.");
        } else {
            setStatus("backup??? ?????? ?????? ?????????????????????.");
        }
        String[] messages = dialog.getMessage().split("\n");
        dialog.dispose();
        for (int i = 0; i < messages.length; i++) {
            String message = messages[i];
            setStatus("\t" + message);
        }
    }

    private ArrayList<String> getColumnNames(ResultSetMetaData metaData) {
        ArrayList<String> columnNames = new ArrayList<>();
        try {
            int count = metaData.getColumnCount();
            for (int i = 0; i < count; i++) {
                columnNames.add(metaData.getColumnName(i + 1));
            }
        } catch (SQLException ex) {
            setStatus("????????? ???????????? ??????: " + ex.getLocalizedMessage());
        }

        return columnNames;
    }

    private String makeInsertPreparedString(String tableName, ArrayList<String> columnNames) {
        int count = columnNames.size();
        if (count <= 0) {
            return null;
        }
        StringBuilder firstPart = new StringBuilder();
        StringBuilder secondPart = new StringBuilder();
        for (int i = 0; i < count; i++) {
            String name = columnNames.get(i);
            firstPart.append(name).append(",");
            secondPart.append("?,");
        }
        firstPart.deleteCharAt(firstPart.length() - 1);
        secondPart.deleteCharAt(secondPart.length() - 1);
        String script = "INSERT INTO " + tableName + " ( " + firstPart + " ) values ( " + secondPart + " )";
        return script;
    }

//    private void addServer() {
//        AddServerDialog dialog = new AddServerDialog((Frame) SwingUtilities.getWindowAncestor(this), true);
//        dialog.setVisible(true);
//
//        ServerMan serverMan = dialog.getServerMan();
//        if (serverMan == null) {
//            System.out.println(" ?????? ????????? ?????????????????????");
//            return;
//        }
//        TreePath selectionPath = tree.getSelectionPath();
//        DefaultMutableTreeNode node = (DefaultMutableTreeNode) selectionPath.getLastPathComponent();
//        node.add(new DefaultMutableTreeNode(serverMan.getName()));
//        if (serverList.add(serverMan)) {
//            setStatus("server ??????: " + serverMan.getPort());
//        } else {
//            setStatus("server ?????? ??????: " + serverList.getMessage());
//            dialog.dispose();
//            return;
//        }
//        dialog.dispose();
//        makeTree();
//        validate();
//    }
    private void addDatabase() {
        TreePath selectionPath = tree.getSelectionPath();
        if (selectionPath == null) {
            setStatus("DB??? ????????? ?????? ????????? ??????????????????.");
            return;
        }
        String serverName = selectionPath.getLastPathComponent().toString();
        ServerMan serverMan = findServerMan(serverName);
        AddDatabaseDialog dialog = new AddDatabaseDialog((Frame) SwingUtilities.getWindowAncestor(this), true, serverMan);
        dialog.setVisible(true);
        DatabaseMan databaseMan = dialog.getDatabaseMan();
        if (databaseMan == null) {
            setStatus("database ?????? ??????.");
            return;
        }
        databaseMan.encryptFields(loginManager);
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) selectionPath.getLastPathComponent();
        node.add(new DefaultMutableTreeNode(databaseMan.getDatabaseName()));
        serverMan.addDatabase(databaseMan);
        dialog.dispose();
        refreshTree();
    }

    void refreshTree() {
        TreePath selectionPath = tree.getSelectionPath();
        tree.setSelectionRow(0);
        if (selectionPath != null) {
            tree.setSelectionPath(selectionPath);
        }
        tree.validate();
        validate();
    }

    public void makeTree() {
        TreePath selectionPath = null;
        if (tree != null) {
            selectionPath = tree.getSelectionPath();
        }
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("servers");
        if (serverList == null) {
            serverList = new ServerMen();
        }

        for (int i = 0; i < serverList.size(); i++) {
            ServerMan serverMan = serverList.get(i);
            DefaultMutableTreeNode serverNode = new DefaultMutableTreeNode(serverMan.getServerName());
            boolean save = true;
            if (serverMan.isLocal()) {
                save = serverMan.isRun();
                try {
                    serverMan.start();
                } catch (SQLException ex) {
                    setStatus("server start failed: " + ex.getLocalizedMessage());
                }
            }
            addDatabaseNode(serverNode, serverMan);
            if (serverMan.isLocal()) {
                if (!save) {
                    try {
                        serverMan.stop();
                    } catch (SQLException ex) {
                    }
                }
            }
            root.add(serverNode);
//            setStatus("sm = " + serverMan.getServerName());
        }
//        DefaultTreeModel model = new DefaultTreeModel(root);
//        model.setRoot(root);
        BorderLayout layout = (BorderLayout) jPanelTree.getLayout();
        Component component = layout.getLayoutComponent(BorderLayout.CENTER);
        if (component != null) {
            remove(layout.getLayoutComponent(BorderLayout.CENTER));
        }
        tree = null;
        tree = new ServerTree(root, serverList);
        tree.addTreeSelectionListener(new SelectionListener());
        tree.validate();
        JScrollPane scrollPane = new JScrollPane(tree);
        jPanelTree.add(scrollPane, BorderLayout.CENTER);
        jPanelTree.validate();

        if (selectionPath != null) {
            tree.setSelectionPath(selectionPath);
            tree.validate();
        }
        validate();
    }

    private void addDatabaseNode(DefaultMutableTreeNode serverNode, ServerMan serverMan) {
        DatabaseMen databaseMen = serverMan.getDatabaseMen();
        for (int i = 0; i < databaseMen.size(); i++) {
            DatabaseMan databaseMan = databaseMen.get(i);
            DefaultMutableTreeNode databaseNode = new DefaultMutableTreeNode(databaseMan.getDatabaseName());
            Connection connection = serverMan.getConnection(databaseMan.getDatabaseName());
            if (connection != null) {
                serverMan.setRun(true);
                addTableNode(serverMan.getServerName(), connection, databaseNode);
            } else {
                setStatus(serverMan.getServerName() + "????????? " + databaseMan.getDatabaseName() + " connection ??????: " + databaseMan.getMessage());
            }
            serverNode.add(databaseNode);
        }
    }

    public TreePath getSelectedPath() {
        return tree.getSelectionPath();
    }

    public void setSelectionPath(TreePath treePath) {
        if (tree == null) {
            return;
        }
        tree.setSelectionPath(treePath);
        tree.validate();
    }

    private void setStatus(String msg) {
        if (status == null) {
            System.out.println(msg);
        } else if (status instanceof JTextArea) {
            ((JTextArea) status).append(msg + "\n");
            ((JTextArea) status).setCaretPosition(((JTextArea) status).getText().length() - 1);
            ((JTextArea) status).validate();
        } else {
            System.out.println(msg);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAddDatabase;
    private javax.swing.JButton jButtonAddTable;
    private javax.swing.JButton jButtonDatabaseBackup;
    private javax.swing.JButton jButtonReadCsv;
    private javax.swing.JButton jButtonSql;
    private javax.swing.JButton jButtonStart;
    private javax.swing.JButton jButtonStop;
    private javax.swing.JPanel jPanelInfo;
    private javax.swing.JPanel jPanelTree;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JToolBar.Separator jSeparator5;
    private javax.swing.JToolBar.Separator jSeparator6;
    private javax.swing.JSplitPane jSplitPaneServerTree;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables
}
