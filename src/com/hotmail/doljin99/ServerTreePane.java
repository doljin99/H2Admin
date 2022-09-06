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

    private ServerMen serverMen;

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
        this.serverMen = serverList;
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
//            setStatus("테이블 노드 작성 완료");
        } catch (SQLException ex) {
            setStatus("테이블 노드 작성 중 에러: " + ex.getLocalizedMessage());
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
            setStatus("column 노드 추가 중 에러: " + ex.getLocalizedMessage());
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
//                    DatabaseMan databaseMan = serverMan.findDatabaseMan(serverName, databaseName);
                    if (serverMan.isRun()) {
                        jButtonReadCsv.setEnabled(true);
                    } else {
                        jButtonReadCsv.setEnabled(false);
                    }
                    jButtonSql.setEnabled(true);   // 서버가 실행 중인 경우에만 스크립트 작성 및 실행 가능, 정지 중에는 스크립트 작성 만 가능
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
        for (int i = 0; i < serverMen.getServerListSize(); i++) {
            ServerMan serverMan = serverMen.getServerMan(i);
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
        jButtonAddUser = new javax.swing.JButton();
        jSeparator7 = new javax.swing.JToolBar.Separator();
        jButtonDatabaseBackup = new javax.swing.JButton();
        jSeparator6 = new javax.swing.JToolBar.Separator();
        jButtonScheduleBackup = new javax.swing.JButton();
        jSeparator8 = new javax.swing.JToolBar.Separator();
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
        jButtonStart.setToolTipText("선택된 서버를 실행시킵니다.");
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
        jButtonStop.setToolTipText("선택된 서버를 정지시킵니다.");
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
        jButtonAddDatabase.setText("데이터베이스");
        jButtonAddDatabase.setToolTipText("선택된 서버에 데이터베이스 또는 선택된 데이터베이스에 테이블을를 추가합니다.");
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

        jButtonAddUser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/general/Zoom16.gif"))); // NOI18N
        jButtonAddUser.setText("사용자");
        jButtonAddUser.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonAddUser.setFocusable(false);
        jButtonAddUser.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonAddUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddUserActionPerformed(evt);
            }
        });
        jToolBar1.add(jButtonAddUser);
        jToolBar1.add(jSeparator7);

        jButtonDatabaseBackup.setIcon(new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/general/SaveAll16.gif"))); // NOI18N
        jButtonDatabaseBackup.setText("백업");
        jButtonDatabaseBackup.setToolTipText("데이터베이스를 백업합니다.");
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

        jButtonScheduleBackup.setIcon(new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/general/SaveAll16.gif"))); // NOI18N
        jButtonScheduleBackup.setText("정기백업");
        jButtonScheduleBackup.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonScheduleBackup.setFocusable(false);
        jButtonScheduleBackup.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonScheduleBackup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScheduleBackupActionPerformed(evt);
            }
        });
        jToolBar1.add(jButtonScheduleBackup);
        jToolBar1.add(jSeparator8);

        jButtonAddTable.setIcon(new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/general/Add16.gif"))); // NOI18N
        jButtonAddTable.setText("테이블");
        jButtonAddTable.setToolTipText("선택된 데이터베이스에 테이블을 추가할 수 있는 창이 표시됩니다.");
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
        jButtonReadCsv.setText("CSV 읽기");
        jButtonReadCsv.setToolTipText("선택된 테이블에 CSV파일로 데이터를 입력합니다.");
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
        jButtonSql.setToolTipText("선택된 테이블에 DML 작업을 할 수 있는 도구를 제공하는 창을 엽니다.");
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
            setStatus("먼저 추가할 대상 노드를 선택하십시오.");
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
            setStatus("먼저 시작할 서버 노드를 선택하십시오.");
            return;
        }
        String serverName = selectionPath.getLastPathComponent().toString();
        ServerMan serverMan = findServerMan(serverName);
        if (serverMan == null) {
            setStatus("서버 정보가 없습니다: " + serverName);
            return;
        }
        try {
            boolean success = serverMan.start();
            if (success) {
                setStatus(serverName + ": " + serverMan.getPort() + " server start 성공.");
            } else {
                setStatus(serverName + ": " + serverMan.getPort() + " server start 실패: " + serverMan.getMessage());
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
            setStatus("먼저 중지할 서버 노드를 선택하십시오.");
            return;
        }
        String serverName = selectionPath.getLastPathComponent().toString();
        ServerMan serverMan = findServerMan(serverName);
        if (serverMan == null) {
            setStatus("서버 정보가 없습니다: " + serverName);
            return;
        }
        try {
            boolean stopped = serverMan.stop();
            setStatus(serverMan.getServerName() + " 서버: " + serverMan.getPort() + " " + serverMan.getMessage());
        } catch (SQLException ex) {
            setStatus("서버 정지 중 에러: " + ex.getLocalizedMessage());
        }
        tree.setSelectionRow(0);
        tree.setSelectionPath(selectionPath);
        tree.validate();
    }//GEN-LAST:event_jButtonStopActionPerformed

    private void jButtonReadCsvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReadCsvActionPerformed
        TreePath selectionPath = tree.getSelectionPath();
        if (selectionPath == null) {
            setStatus("먼저 데이터를 입력할 테이블 노드를 선택하십시오.");
            return;
        }
        Object[] paths = selectionPath.getPath();
        String serverName = paths[1].toString();
        String databaseName = paths[2].toString();
        String tableName = paths[3].toString();
        ServerMan serverMan = findServerMan(serverName);
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle(tableName + " 입력용 CSV 파일 열기");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV FILE", "csv", "csv");
        chooser.setFileFilter(filter);
        chooser.showOpenDialog(this);
        File file = chooser.getSelectedFile();
        if (file == null) {
            setStatus(tableName + " CSV 파일 입력 취소");
            return;
        }
//        DatabaseMan databaseMan = serverMan.findDatabaseMan(serverName, databaseName);
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
//                    String columnName = columnNames.get(i);
                    ps.setObject(i + 1, rs.getObject(i + 1));
                }
                count += ps.executeUpdate();
            }
            if (!save) {
                serverMan.stop();
            }
            setStatus(count + " 건 입력.");
        } catch (SQLException ex) {
            setStatus("CSV 파일 읽기 실패: " + ex.getLocalizedMessage());
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
            setStatus("먼저 SQL 스크립트 작업할 테이블 노드를 선택하십시오.");
            return;
        }
        Object[] paths = selectionPath.getPath();
        if (paths.length < 4) {
            setStatus("서버 트리 정보가 정확하지 않습니다. \n refresh tree 버튼으로 새로고침 해 주시고 다시 시도해 주시기 바랍니다.");
            return;
        }
        String serverName = paths[1].toString();
        String databaseName = paths[2].toString();
        String tableName = paths[3].toString();
        ServerMan serverMan = serverMen.findByName(serverName);
        DatabaseMan databaseMan = serverMan.findDatabaseByName(databaseName);
        Tables databaseTables = tables.getDatabaseTables(serverName, databaseName);
//        for (int i = 0; i < databaseTables.size(); i++) {
//            Table get = databaseTables.get(i);
//            System.out.println("table = " + get.getTableName());
//        }
//        Table table = tables.findByName(serverName, databaseName, tableName);
        TableColumns tableColumns = columns.findTableColumns(serverName, databaseName);
        ScriptDialog dialog = new ScriptDialog((Frame) SwingUtilities.getWindowAncestor(this), false, serverMan, databaseMan, databaseTables, tableName, tableColumns, codeCompletionList);
        dialog.setVisible(true);
    }//GEN-LAST:event_jButtonSqlActionPerformed

    private void jButtonAddTableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddTableActionPerformed
        TreePath selectionPath = tree.getSelectionPath();
        if (selectionPath == null) {
            setStatus("먼저 추가할 대상 노드를 선택하십시오.");
            return;
        }
        addTable(selectionPath);
    }//GEN-LAST:event_jButtonAddTableActionPerformed

    private void jButtonDatabaseBackupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDatabaseBackupActionPerformed
        backup();
    }//GEN-LAST:event_jButtonDatabaseBackupActionPerformed

    private void jButtonAddUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddUserActionPerformed
        TreePath selectionPath = tree.getSelectionPath();
        if (selectionPath == null) {
            setStatus("사용자 추가할 할 데이터베이스 노드를 선택하십시오.");
            return;
        }
        Object[] paths = selectionPath.getPath();
        if (paths.length < 3) {
            setStatus("서버 트리 정보가 정확하지 않습니다. \n refresh tree 버튼으로 새로고침 해 주시고 다시 시도해 주시기 바랍니다.");
            return;
        }
        String serverName = paths[1].toString();
        String databaseNAme = paths[2].toString();
        ServerMan serverMan = serverMen.findByName(serverName);
        if (!serverMan.isRun()) {
            setStatus(serverName + " 서버가 정지 상태이므로 접근할 수 없읍니다. 먼저 서버를 실행 시켜 주십시오");
            return;
        }
        DatabaseMan databaseMan = serverMan.findDatabaseByName(databaseNAme);
        UsersDialog dialog = new UsersDialog((Frame) SwingUtilities.getWindowAncestor(this), true,
            serverMan, databaseMan, loginManager);
        dialog.setVisible(true);
    }//GEN-LAST:event_jButtonAddUserActionPerformed

    private void jButtonScheduleBackupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScheduleBackupActionPerformed
        TreePath selectionPath = tree.getSelectionPath();
        ServerMan serverMan = getServerMan(selectionPath);
        if (serverMan == null) {
            return;
        }
        if (!serverMan.isLocal()) {
            setStatus("현재 버전에서는 remote 서버 데이터베이스에 대한 백업 기능이 없습니다.");
            return;
        }

        DatabaseMan databaseMan = getDatabaseMan(selectionPath);
        
        BackupScheduleDialog dialog = new BackupScheduleDialog((Frame) SwingUtilities.getWindowAncestor(this), true, 
            serverMan, databaseMan, serverMen.getBackupSchedules(), 
            loginManager);        
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }//GEN-LAST:event_jButtonScheduleBackupActionPerformed

    protected void backup() {
        TreePath selectionPath = tree.getSelectionPath();
        if (selectionPath == null || selectionPath.getPathCount() < 2) {
            setStatus("먼저 백업할 서버나 데이터베이스를 선택하십시오.");
            return;
        }

        Object[] paths = selectionPath.getPath();
        String serverName = paths[1].toString();
        ServerMan serverMan = findServerMan(serverName);
        if (serverMan == null) {
            setStatus("서버를 찾을 수 없습니다.: " + serverName);
            return;
        }
        if (!serverMan.isLocal()) {
            setStatus("현재 버전에서는 remote 서버 데이터베이스에 대한 백업 기능이 없습니다.");
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
            setStatus("backup이 실행되었습니다.");
        } else {
            setStatus("backup을 취소 또는 실패하였습니다.");
        }
        String[] messages = dialog.getMessage().split("\n");
        dialog.dispose();
        for (String message : messages) {
            setStatus("\t" + message);
        }
    }

    private ServerMan getServerMan(TreePath path) {
        if (path == null || path.getPathCount() < 2) {
            setStatus("먼저 백업할 서버나 데이터베이스를 선택하십시오.");
            return null;
        }

        Object[] paths = path.getPath();
        String serverName = paths[1].toString();
        ServerMan serverMan = findServerMan(serverName);
        if (serverMan == null) {
            setStatus("서버를 찾을 수 없습니다.: " + serverName);
            return null;
        }
        return serverMan;
    }

    private DatabaseMan getDatabaseMan(TreePath path) {
        if (path == null || path.getPathCount() < 2) {
            setStatus("먼저 백업할 서버나 데이터베이스를 선택하십시오.");
            return null;
        }

        Object[] paths = path.getPath();
        String serverName = paths[1].toString();
        ServerMan serverMan = getServerMan(path);
        if (serverMan == null) {
            setStatus("서버를 찾을 수 없습니다.: " + serverName);
            return null;
        }
        String databaseName;
        DatabaseMan databaseMan = null;
        if (paths.length >= 3) {
            databaseName = paths[2].toString();
            databaseMan = serverMan.findDatabaseMan(serverName, databaseName);
        }
        return databaseMan;
    }

    private ArrayList<String> getColumnNames(ResultSetMetaData metaData) {
        ArrayList<String> columnNames = new ArrayList<>();
        try {
            int count = metaData.getColumnCount();
            for (int i = 0; i < count; i++) {
                columnNames.add(metaData.getColumnName(i + 1));
            }
        } catch (SQLException ex) {
            setStatus("컬럼명 가져오기 에러: " + ex.getLocalizedMessage());
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
//            System.out.println(" 서버 생성을 취소하였습니다");
//            return;
//        }
//        TreePath selectionPath = tree.getSelectionPath();
//        DefaultMutableTreeNode node = (DefaultMutableTreeNode) selectionPath.getLastPathComponent();
//        node.add(new DefaultMutableTreeNode(serverMan.getName()));
//        if (serverMen.add(serverMan)) {
//            setStatus("server 생성: " + serverMan.getPort());
//        } else {
//            setStatus("server 생성 실패: " + serverMen.getMessage());
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
            setStatus("DB를 추가할 서버 노드를 선택하십시오.");
            return;
        }
        String serverName = selectionPath.getLastPathComponent().toString();
        ServerMan serverMan = findServerMan(serverName);
        AddDatabaseDialog dialog = new AddDatabaseDialog((Frame) SwingUtilities.getWindowAncestor(this), true, serverMan);
        dialog.setVisible(true);
        DatabaseMan databaseMan = dialog.getDatabaseMan();
        if (databaseMan == null) {
            setStatus("database 생성 실패.");
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
        if (serverMen == null) {
            serverMen = new ServerMen();
        }

        for (int i = 0; i < serverMen.getServerListSize(); i++) {
            ServerMan serverMan = serverMen.getServerMan(i);
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
        tree = new ServerTree(root, serverMen);
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
                setStatus(serverMan.getServerName() + "서버의 " + databaseMan.getDatabaseName() + " connection 실패: " + databaseMan.getMessage());
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
    private javax.swing.JButton jButtonAddUser;
    private javax.swing.JButton jButtonDatabaseBackup;
    private javax.swing.JButton jButtonReadCsv;
    private javax.swing.JButton jButtonScheduleBackup;
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
    private javax.swing.JToolBar.Separator jSeparator7;
    private javax.swing.JToolBar.Separator jSeparator8;
    private javax.swing.JSplitPane jSplitPaneServerTree;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables
}
