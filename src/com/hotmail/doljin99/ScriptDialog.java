/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package com.hotmail.doljin99;

import com.hotmail.doljin99.simpleeditor.SimpleEditor;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author dolji
 */
public class ScriptDialog extends javax.swing.JDialog {

    private final ServerMan serverMan;
    private final DatabaseMan databaseMan;
    private final Tables tables;
    private Table selectedTable;
    private final TableColumns tableColumns;
    
    private final List<String> codeCompletionList;

    private SimpleEditor selectEditor;
    private SimpleEditor insertEditor;
    private SimpleEditor updateEditor;
    private SimpleEditor deleteEditor;
    private SimpleEditor sqlEditor;    

    ScriptDialog(Frame frame, boolean modal, ServerMan serverMan, DatabaseMan databaseMan, Tables tables, String tableName, TableColumns tableColumns, List<String> codeCompletionList) {
        super(frame, modal);
        this.serverMan = serverMan;
        this.databaseMan = databaseMan;
        this.tables = tables;
        this.tableColumns = tableColumns;
        this.codeCompletionList = codeCompletionList;

        initComponents();

        init(tableName);
    }

    private void init(String tableName) {
        jComboBoxTables.removeAllItems();
        for (int i = 0; i < tables.size(); i++) {
            Table tempTable = tables.get(i);
            jComboBoxTables.addItem(tempTable.getTableName());
        }
        jComboBoxTables.validate();

        jComboBoxTables.setSelectedItem(tableName);
        jComboBoxTables.validate();

        selectEditor = new SimpleEditor(jTextAreaStatus, codeCompletionList);
        insertEditor = new SimpleEditor(jTextAreaStatus, codeCompletionList);
        updateEditor = new SimpleEditor(jTextAreaStatus, codeCompletionList);
        deleteEditor = new SimpleEditor(jTextAreaStatus, codeCompletionList);
        sqlEditor = new SimpleEditor(jTextAreaStatus, codeCompletionList);
        setSimpleEditor(jPanelSelect, selectEditor);
        setSimpleEditor(jPanelInsert, insertEditor);
        setSimpleEditor(jPanelUpdate, updateEditor);
        setSimpleEditor(jPanelDelete, deleteEditor);
        setSimpleEditor(jPanelSql, sqlEditor);

        setSize(900, 700);
        setLocationRelativeTo(null);
    }

    private void setSimpleEditor(JPanel target, SimpleEditor editor) {
        BorderLayout layout = (BorderLayout) target.getLayout();
        Component old = layout.getLayoutComponent(BorderLayout.CENTER);
        if (old != null) {
            target.remove(old);
        }
        target.add(editor, BorderLayout.CENTER);
        target.validate();
    }

    private void makeColumnTable(Table table) {
        TableColumns selectedTableColumns = tableColumns.findTableColumns(serverMan.getServerName(), databaseMan.getDatabaseName(), table.getTableName());
        DefaultTableModel model = (DefaultTableModel) jTableColumns.getModel();
        model.setRowCount(0);
        Object[] row = new Object[model.getColumnCount()];
        for (int i = 0; i < selectedTableColumns.size(); i++) {
            TableColumn column = selectedTableColumns.get(i);
            row[0] = true;
            row[1] = column.getOrdinalPosition();
            row[2] = column.getColumnName();
            row[3] = column.getTypeName();
            row[4] = column.getColumnSize();
            int keySeq = table.getPrimaryKeys().isKey(column.getColumnName());
            row[5] = (keySeq > 0) ? String.valueOf(keySeq) : "";
            String nullable = column.getIsNullable();
            row[6] = (nullable.equalsIgnoreCase("YES"));
            String auto = column.getIsAutoincrement();
            row[7] = (auto.equalsIgnoreCase("YES"));
            row[8] = column.getColumnDef();

            model.addRow(row);
        }
        H2AUtilities.alignColumnWidth(jTableColumns);
        jTableColumns.validate();
    }

    private String makeSelectScript() {
        int rowCount = jTableColumns.getRowCount();
        if (rowCount <= 0) {
            return null;
        }
        StringBuilder script = new StringBuilder("SELECT \n");
        for (int i = 0; i < rowCount; i++) {
            if (!(boolean) jTableColumns.getValueAt(i, 0)) {
                continue;
            }
            script.append("\t");
            script.append(jTableColumns.getValueAt(i, 2));
            String as = (String) jTableColumns.getValueAt(i, 9);
            if (as != null && !as.isEmpty()) {
                script.append(" AS ").append(as);
            }
            script.append(",\n");
        }
        script.deleteCharAt(script.length() - 2);
        script.append("FROM \n\t").append(selectedTable.getTableName());

        if (jCheckBoxByKey.isSelected()) {  // key 항목으로 WHERE구를 작성이 선택 되었을때            
            String where = makeWhereByKey();
            if (where != null) {
                script.append(where);
            }
        }

        int limit = (int) jSpinnerLimit.getValue();
        if (limit > 0) {
            script.append("\nLIMIT ").append(limit);
        }

        return script.toString();
    }

    private String makeInsertScript() {
        int rowCount = jTableColumns.getRowCount();
        if (rowCount <= 0) {
            return null;
        }
        StringBuilder part1 = new StringBuilder("INSERT INTO ");
        part1.append(selectedTable.getTableName()).append(" ( \n");
        StringBuilder part2 = new StringBuilder(" VALUES ( \n");
        for (int i = 0; i < rowCount; i++) {
            if (!(boolean) jTableColumns.getValueAt(i, 0)) {
                continue;
            }
            part1.append("\t");
            part1.append(jTableColumns.getValueAt(i, 2));
            part1.append(",\n");

            part2.append("\t");
            if (jCheckBoxPrepared.isSelected()) {
                part2.append("?");
            } else {
                Object object = jTableColumns.getValueAt(i, 11);
                if (object == null) {
                    part2.append("null");
                } else {
                    part2.append("'");
                    part2.append(jTableColumns.getValueAt(i, 11));
                    part2.append("'");
                }
            }
            part2.append(",\n");
        }
        part1.deleteCharAt(part1.length() - 2);
        part2.deleteCharAt(part2.length() - 2);
        part1.append(")");
        part2.append(")");

        return part1.toString() + part2.toString();
    }

    private String makeUpdateScript() {
        int rowCount = jTableColumns.getRowCount();
        if (rowCount <= 0) {
            return null;
        }
        StringBuilder script = new StringBuilder("UPDATE ");
        script.append(selectedTable.getTableName()).append(" \n");
        script.append("SET \n");
        for (int i = 0; i < rowCount; i++) {
            if (!(boolean) jTableColumns.getValueAt(i, 0)) {
                continue;
            }
            script.append("\t");
            script.append(jTableColumns.getValueAt(i, 2));
            script.append(" = ");
            if (jCheckBoxPrepared.isSelected()) {
                script.append("?");
            } else {
                Object object = jTableColumns.getValueAt(i, 11);
                if (object == null) {
                    script.append("null");
                } else {
                    script.append("'");
                    script.append(jTableColumns.getValueAt(i, 11));
                    script.append("'");
                }
            }
            script.append(",\n");
        }
        script.deleteCharAt(script.length() - 1);
        script.deleteCharAt(script.length() - 1);

        if (jCheckBoxByKey.isSelected()) {
            String where = makeWhereByKey();
            if (where != null) {
                script.append(where);
            }
        }

        return script.toString();
    }

    private String makeDeleteScript() {
        int rowCount = jTableColumns.getRowCount();
        if (rowCount <= 0) {
            return null;
        }
        StringBuilder script = new StringBuilder("DELETE FROM ");
        script.append(selectedTable.getTableName()).append(" \n");

        if (jCheckBoxByKey.isSelected()) {  // key 항목으로 WHERE구를 작성이 선택 되었을때            
            String where = makeWhereByKey();
            if (where != null) {
                script.append(where);
            }
        }

        return script.toString();
    }

    private String makeWhereByKey() {
        int rowCount = jTableColumns.getRowCount();
        if (rowCount <= 0) {
            return null;
        }
        StringBuilder where = new StringBuilder("\nWHERE \n");
        int keyCount = 0;
        for (int i = 0; i < rowCount; i++) {
            if (hasValue(jTableColumns.getValueAt(i, 5))) { // primary key 컬럼이면
                keyCount++;
                where.append("\t");
                where.append(jTableColumns.getValueAt(i, 2));
                if (hasValue(jTableColumns.getValueAt(i, 10))) {    // 연산자 값이 있을 때
                    where.append(" ");
                    where.append(jTableColumns.getValueAt(i, 10));
                    where.append(" ");
                } else {
                    where.append(" = ");
                }
                if (jCheckBoxPrepared.isSelected()) {
                    where.append("?");
                    where.append(" AND\n");
                } else {
                    if (hasValue(jTableColumns.getValueAt(i, 11))) {
                        where.append("'");
                        where.append(jTableColumns.getValueAt(i, 11));
                        where.append("'");
                    } else {
                        where.append("''");
                    }
                    where.append(" AND\n");
                }
            }
        }
        if (keyCount <= 0) {
            return null;
        }
        int index = where.lastIndexOf(" AND\n");
        
        return where.substring(0, index);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar8 = new javax.swing.JToolBar();
        jComboBoxTables = new javax.swing.JComboBox<>();
        jSplitPane1 = new javax.swing.JSplitPane();
        jSplitPane2 = new javax.swing.JSplitPane();
        jPanel2 = new javax.swing.JPanel();
        jToolBar2 = new javax.swing.JToolBar();
        jPanel5 = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        jCheckBoxAllColumnsSelect = new javax.swing.JCheckBox();
        jSeparator12 = new javax.swing.JToolBar.Separator();
        jButtonUp = new javax.swing.JButton();
        jSeparator9 = new javax.swing.JToolBar.Separator();
        jButtonDown = new javax.swing.JButton();
        jSeparator13 = new javax.swing.JToolBar.Separator();
        jCheckBoxPrepared = new javax.swing.JCheckBox();
        jCheckBoxByKey = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableColumns = new javax.swing.JTable();
        jTabbedPaneWork = new javax.swing.JTabbedPane();
        jPanelSelect = new javax.swing.JPanel();
        jToolBar3 = new javax.swing.JToolBar();
        jButtonScriptSelect = new javax.swing.JButton();
        jSeparator10 = new javax.swing.JToolBar.Separator();
        jLabel1 = new javax.swing.JLabel();
        jSpinnerLimit = new javax.swing.JSpinner();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        jButtonExecuteQuery = new javax.swing.JButton();
        jPanelInsert = new javax.swing.JPanel();
        jToolBar4 = new javax.swing.JToolBar();
        jCheckBoxInsertMin = new javax.swing.JCheckBox();
        jCheckBoxIncludeNullable = new javax.swing.JCheckBox();
        jCheckBoxExcludeDefault = new javax.swing.JCheckBox();
        jSeparator11 = new javax.swing.JToolBar.Separator();
        jButtonScriptInsert = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        jButtonExecuteInsert = new javax.swing.JButton();
        jPanelUpdate = new javax.swing.JPanel();
        jToolBar5 = new javax.swing.JToolBar();
        jSeparator5 = new javax.swing.JToolBar.Separator();
        jButtonScriptUpdate = new javax.swing.JButton();
        jSeparator6 = new javax.swing.JToolBar.Separator();
        jButtonExecuteUpdate = new javax.swing.JButton();
        jPanelDelete = new javax.swing.JPanel();
        jToolBar6 = new javax.swing.JToolBar();
        jSeparator7 = new javax.swing.JToolBar.Separator();
        jButtonScriptDelete = new javax.swing.JButton();
        jSeparator8 = new javax.swing.JToolBar.Separator();
        jButtonExecuteDelete = new javax.swing.JButton();
        jPanelSql = new javax.swing.JPanel();
        jToolBar7 = new javax.swing.JToolBar();
        jButtonExecuteSql = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextAreaStatus = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jToolBar8.setRollover(true);

        jComboBoxTables.setMaximumSize(new java.awt.Dimension(200, 32767));
        jComboBoxTables.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxTablesItemStateChanged(evt);
            }
        });
        jToolBar8.add(jComboBoxTables);

        getContentPane().add(jToolBar8, java.awt.BorderLayout.NORTH);

        jSplitPane1.setDividerLocation(500);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jSplitPane2.setDividerLocation(200);
        jSplitPane2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jPanel2.setLayout(new java.awt.BorderLayout());

        jToolBar2.setRollover(true);
        jPanel2.add(jToolBar2, java.awt.BorderLayout.NORTH);

        jPanel5.setLayout(new java.awt.BorderLayout());

        jToolBar1.setRollover(true);

        jCheckBoxAllColumnsSelect.setSelected(true);
        jCheckBoxAllColumnsSelect.setText("모두");
        jCheckBoxAllColumnsSelect.setFocusable(false);
        jCheckBoxAllColumnsSelect.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jCheckBoxAllColumnsSelect.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxAllColumnsSelectItemStateChanged(evt);
            }
        });
        jToolBar1.add(jCheckBoxAllColumnsSelect);
        jToolBar1.add(jSeparator12);

        jButtonUp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/navigation/Up16.gif"))); // NOI18N
        jButtonUp.setText("위로");
        jButtonUp.setToolTipText("선택된 행을 위로 이동");
        jButtonUp.setFocusable(false);
        jButtonUp.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonUpActionPerformed(evt);
            }
        });
        jToolBar1.add(jButtonUp);
        jToolBar1.add(jSeparator9);

        jButtonDown.setIcon(new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/navigation/Down16.gif"))); // NOI18N
        jButtonDown.setText("아래로");
        jButtonDown.setToolTipText("선택된 행을 아래로 이동");
        jButtonDown.setFocusable(false);
        jButtonDown.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonDown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDownActionPerformed(evt);
            }
        });
        jToolBar1.add(jButtonDown);
        jToolBar1.add(jSeparator13);

        jCheckBoxPrepared.setText("prepared");
        jCheckBoxPrepared.setToolTipText("prepared 문의 형식으로 스크립트를 작성");
        jCheckBoxPrepared.setFocusable(false);
        jCheckBoxPrepared.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jCheckBoxPrepared);

        jCheckBoxByKey.setText("by key");
        jCheckBoxByKey.setToolTipText("primary key를 이용한 WHERE구문 적용");
        jCheckBoxByKey.setFocusable(false);
        jCheckBoxByKey.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jCheckBoxByKey);

        jPanel5.add(jToolBar1, java.awt.BorderLayout.NORTH);

        jTableColumns.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "선택", "순번", "컬럼명", "타입", "길이", "PKey", "null가능", "자동증가", "Default", "AS", "연산자", "값"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false, false, false, false, false, true, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTableColumns);

        jPanel5.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel2.add(jPanel5, java.awt.BorderLayout.CENTER);

        jSplitPane2.setLeftComponent(jPanel2);

        jPanelSelect.setLayout(new java.awt.BorderLayout());

        jToolBar3.setRollover(true);

        jButtonScriptSelect.setIcon(new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/general/New16.gif"))); // NOI18N
        jButtonScriptSelect.setText("스크립트작성");
        jButtonScriptSelect.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonScriptSelect.setFocusable(false);
        jButtonScriptSelect.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonScriptSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptSelectActionPerformed(evt);
            }
        });
        jToolBar3.add(jButtonScriptSelect);
        jToolBar3.add(jSeparator10);

        jLabel1.setText("Limit");
        jToolBar3.add(jLabel1);

        jSpinnerLimit.setModel(new javax.swing.SpinnerNumberModel(1000, 0, null, 100));
        jSpinnerLimit.setToolTipText("전체 건수 조회는 0");
        jSpinnerLimit.setMaximumSize(new java.awt.Dimension(80, 32767));
        jSpinnerLimit.setPreferredSize(new java.awt.Dimension(80, 24));
        jToolBar3.add(jSpinnerLimit);
        jToolBar3.add(jSeparator2);

        jButtonExecuteQuery.setIcon(new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/media/Play16.gif"))); // NOI18N
        jButtonExecuteQuery.setText("실행");
        jButtonExecuteQuery.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonExecuteQuery.setFocusable(false);
        jButtonExecuteQuery.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonExecuteQuery.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExecuteQueryActionPerformed(evt);
            }
        });
        jToolBar3.add(jButtonExecuteQuery);

        jPanelSelect.add(jToolBar3, java.awt.BorderLayout.NORTH);

        jTabbedPaneWork.addTab("Select", new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/general/Search16.gif")), jPanelSelect); // NOI18N

        jPanelInsert.setLayout(new java.awt.BorderLayout());

        jToolBar4.setRollover(true);

        jCheckBoxInsertMin.setSelected(true);
        jCheckBoxInsertMin.setText("최소");
        jCheckBoxInsertMin.setFocusable(false);
        jCheckBoxInsertMin.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jCheckBoxInsertMin.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxInsertMinItemStateChanged(evt);
            }
        });
        jToolBar4.add(jCheckBoxInsertMin);

        jCheckBoxIncludeNullable.setText("null가능 포함");
        jCheckBoxIncludeNullable.setFocusable(false);
        jCheckBoxIncludeNullable.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jCheckBoxIncludeNullable.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxIncludeNullableItemStateChanged(evt);
            }
        });
        jToolBar4.add(jCheckBoxIncludeNullable);

        jCheckBoxExcludeDefault.setText("기본값 설정 항목 제외");
        jCheckBoxExcludeDefault.setFocusable(false);
        jCheckBoxExcludeDefault.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jCheckBoxExcludeDefault.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxExcludeDefaultItemStateChanged(evt);
            }
        });
        jToolBar4.add(jCheckBoxExcludeDefault);
        jToolBar4.add(jSeparator11);

        jButtonScriptInsert.setIcon(new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/general/New16.gif"))); // NOI18N
        jButtonScriptInsert.setText("스크립트작성");
        jButtonScriptInsert.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonScriptInsert.setFocusable(false);
        jButtonScriptInsert.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonScriptInsert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptInsertActionPerformed(evt);
            }
        });
        jToolBar4.add(jButtonScriptInsert);
        jToolBar4.add(jSeparator4);

        jButtonExecuteInsert.setIcon(new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/media/Play16.gif"))); // NOI18N
        jButtonExecuteInsert.setText("실행");
        jButtonExecuteInsert.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonExecuteInsert.setFocusable(false);
        jButtonExecuteInsert.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonExecuteInsert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExecuteInsertActionPerformed(evt);
            }
        });
        jToolBar4.add(jButtonExecuteInsert);

        jPanelInsert.add(jToolBar4, java.awt.BorderLayout.NORTH);

        jTabbedPaneWork.addTab("Insert", new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/table/RowInsertAfter16.gif")), jPanelInsert); // NOI18N

        jPanelUpdate.setLayout(new java.awt.BorderLayout());

        jToolBar5.setRollover(true);
        jToolBar5.add(jSeparator5);

        jButtonScriptUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/general/New16.gif"))); // NOI18N
        jButtonScriptUpdate.setText("스크립트작성");
        jButtonScriptUpdate.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonScriptUpdate.setFocusable(false);
        jButtonScriptUpdate.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonScriptUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptUpdateActionPerformed(evt);
            }
        });
        jToolBar5.add(jButtonScriptUpdate);
        jToolBar5.add(jSeparator6);

        jButtonExecuteUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/media/Play16.gif"))); // NOI18N
        jButtonExecuteUpdate.setText("실행");
        jButtonExecuteUpdate.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonExecuteUpdate.setFocusable(false);
        jButtonExecuteUpdate.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonExecuteUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExecuteUpdateActionPerformed(evt);
            }
        });
        jToolBar5.add(jButtonExecuteUpdate);

        jPanelUpdate.add(jToolBar5, java.awt.BorderLayout.NORTH);

        jTabbedPaneWork.addTab("Update", new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/general/Edit16.gif")), jPanelUpdate); // NOI18N

        jPanelDelete.setLayout(new java.awt.BorderLayout());

        jToolBar6.setRollover(true);
        jToolBar6.add(jSeparator7);

        jButtonScriptDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/general/New16.gif"))); // NOI18N
        jButtonScriptDelete.setText("스크립트작성");
        jButtonScriptDelete.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonScriptDelete.setFocusable(false);
        jButtonScriptDelete.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonScriptDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScriptDeleteActionPerformed(evt);
            }
        });
        jToolBar6.add(jButtonScriptDelete);
        jToolBar6.add(jSeparator8);

        jButtonExecuteDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/media/Play16.gif"))); // NOI18N
        jButtonExecuteDelete.setText("실행");
        jButtonExecuteDelete.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonExecuteDelete.setFocusable(false);
        jButtonExecuteDelete.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonExecuteDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExecuteDeleteActionPerformed(evt);
            }
        });
        jToolBar6.add(jButtonExecuteDelete);

        jPanelDelete.add(jToolBar6, java.awt.BorderLayout.NORTH);

        jTabbedPaneWork.addTab("Delete", new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/table/RowDelete16.gif")), jPanelDelete); // NOI18N

        jPanelSql.setLayout(new java.awt.BorderLayout());

        jToolBar7.setRollover(true);

        jButtonExecuteSql.setIcon(new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/media/Play16.gif"))); // NOI18N
        jButtonExecuteSql.setText("실행");
        jButtonExecuteSql.setToolTipText("해당 데이터베이스에 대한 임의의 SQL 문 실행");
        jButtonExecuteSql.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonExecuteSql.setFocusable(false);
        jButtonExecuteSql.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonExecuteSql.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExecuteSqlActionPerformed(evt);
            }
        });
        jToolBar7.add(jButtonExecuteSql);

        jPanelSql.add(jToolBar7, java.awt.BorderLayout.NORTH);

        jTabbedPaneWork.addTab("SQL", new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/general/New16.gif")), jPanelSql); // NOI18N

        jSplitPane2.setRightComponent(jTabbedPaneWork);

        jSplitPane1.setTopComponent(jSplitPane2);

        jPanel1.setLayout(new java.awt.BorderLayout());

        jTextAreaStatus.setEditable(false);
        jTextAreaStatus.setColumns(20);
        jTextAreaStatus.setLineWrap(true);
        jTextAreaStatus.setRows(5);
        jTextAreaStatus.setTabSize(4);
        jTextAreaStatus.setWrapStyleWord(true);
        jScrollPane3.setViewportView(jTextAreaStatus);

        jPanel1.add(jScrollPane3, java.awt.BorderLayout.CENTER);

        jSplitPane1.setBottomComponent(jPanel1);

        getContentPane().add(jSplitPane1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonScriptSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptSelectActionPerformed
        try {
            jSpinnerLimit.commitEdit();
        } catch (ParseException ex) {
        }
        selectEditor.setText(makeSelectScript());
    }//GEN-LAST:event_jButtonScriptSelectActionPerformed

    private void jButtonUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonUpActionPerformed
        H2AUtilities.upTableRow(jTableColumns);
    }//GEN-LAST:event_jButtonUpActionPerformed

    private void jButtonDownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDownActionPerformed
        H2AUtilities.downTableRow(jTableColumns);
    }//GEN-LAST:event_jButtonDownActionPerformed

    private void jButtonExecuteQueryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExecuteQueryActionPerformed
        String script = selectEditor.getText();
        if (script == null || script.isEmpty()) {
            setStatus("실행할 스크립트가 없습니다.");
            return;
        }
        if (!serverMan.isRun()) {
            setStatus("서버가 정지 중입니다.");
            return;
        }
        Connection connection = serverMan.getConnection(databaseMan.getDatabaseName());
        if (connection == null) {
            setStatus("connection 획득을 실패했습니다.");
            return;
        }
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            resultSet = statement.executeQuery(script);

            ResultSetPane resultSetPane = new ResultSetPane(serverMan, databaseMan, script);
            ResultSetDialog dialog = new ResultSetDialog((Frame) SwingUtilities.getWindowAncestor(this), false, resultSetPane);
            dialog.setSize(900, 600);
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
        } catch (SQLException ex) {
            setStatus("Select 실행 중 에러: " + ex.getLocalizedMessage());
        } finally {
//            if (statement != null) {
//                try {
//                    statement.close();
//                } catch (SQLException ex) {
//                }
//            }
//            if (resultSet != null) {
//                try {
//                    resultSet.close();
//                } catch (SQLException ex) {
//                }
//            }
//            try {
//                connection.close();
//            } catch (SQLException ex) {
//            }
        }
    }//GEN-LAST:event_jButtonExecuteQueryActionPerformed

    private void jComboBoxTablesItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxTablesItemStateChanged
        String tableName = (String) jComboBoxTables.getSelectedItem();
        selectedTable = tables.findByName(serverMan.getServerName(), databaseMan.getDatabaseName(), tableName);
        if (selectedTable == null) {
            return;
        }
        setTitle(databaseMan.getDatabaseName() + " - " + selectedTable.getTableName());
        makeColumnTable(selectedTable);
    }//GEN-LAST:event_jComboBoxTablesItemStateChanged

    private void jCheckBoxAllColumnsSelectItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBoxAllColumnsSelectItemStateChanged
        setAllColumnsSelect(jCheckBoxAllColumnsSelect.isSelected());
    }//GEN-LAST:event_jCheckBoxAllColumnsSelectItemStateChanged

    private void jButtonExecuteInsertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExecuteInsertActionPerformed
        String script = insertEditor.getText();
        if (script == null || script.isEmpty()) {
            setStatus("실행할 Insert 문이 없습니다.");
            return;
        }

        if (!serverMan.isRun()) {
            setStatus("서버가 정지 중입니다.");
            return;
        }
        Connection connection = serverMan.getConnection(databaseMan.getDatabaseName());
        if (connection == null) {
            setStatus("connection 획득을 실패했습니다.");
            return;
        }
        Statement statement = null;
        try {
            statement = connection.createStatement();
            int result = statement.executeUpdate(script);
            setStatus("insert: " + result + " 행이 영향을 받았습니다.");
        } catch (SQLException ex) {
            setStatus("insert error: " + ex.getLocalizedMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException ex) {
            }
            try {
                connection.close();
            } catch (SQLException ex) {
            }
        }
    }//GEN-LAST:event_jButtonExecuteInsertActionPerformed

    private void jCheckBoxInsertMinItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBoxInsertMinItemStateChanged
        setInsertColumns();
    }//GEN-LAST:event_jCheckBoxInsertMinItemStateChanged

    private void jCheckBoxIncludeNullableItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBoxIncludeNullableItemStateChanged
        setInsertColumns();
    }//GEN-LAST:event_jCheckBoxIncludeNullableItemStateChanged

    private void jCheckBoxExcludeDefaultItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBoxExcludeDefaultItemStateChanged
        setInsertColumns();
    }//GEN-LAST:event_jCheckBoxExcludeDefaultItemStateChanged

    private void jButtonScriptInsertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptInsertActionPerformed
        // setInsertColumns();
        insertEditor.setText(makeInsertScript());
    }//GEN-LAST:event_jButtonScriptInsertActionPerformed

    private void jButtonExecuteUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExecuteUpdateActionPerformed
        String script = updateEditor.getText();
        if (script == null || script.isEmpty()) {
            setStatus("실행할 Update 문이 없습니다.");
            return;
        }

        String temp = script.toUpperCase();
        if (!H2AUtilities.containWord(temp, "WHERE")) {
            int result = JOptionPane.showConfirmDialog(this, "where 절이 없습니다. 계속 하시겠습니까?", "where 구문 여부 확인", JOptionPane.OK_CANCEL_OPTION);
            if (result != JOptionPane.OK_OPTION) {
                setStatus("update 실행을 취소하였습니다.");
                return;
            }
        }

        if (!serverMan.isRun()) {
            setStatus("서버가 정지 중입니다.");
            return;
        }
        Connection connection = serverMan.getConnection(databaseMan.getDatabaseName());
        if (connection == null) {
            setStatus("connection 획득을 실패했습니다.");
            return;
        }
        Statement statement = null;
        try {
            statement = connection.createStatement();
            int result = statement.executeUpdate(script);
            setStatus("update: " + result + " 행이 영향을 받았습니다.");
        } catch (SQLException ex) {
            setStatus("update error: " + ex.getLocalizedMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException ex) {
            }
            try {
                connection.close();
            } catch (SQLException ex) {
            }
        }
    }//GEN-LAST:event_jButtonExecuteUpdateActionPerformed
    
    private void jButtonScriptUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptUpdateActionPerformed
        updateEditor.setText(makeUpdateScript());
    }//GEN-LAST:event_jButtonScriptUpdateActionPerformed

    private void jButtonExecuteSqlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExecuteSqlActionPerformed
        String script = sqlEditor.getText();
        if (script == null || script.isEmpty()) {
            setStatus("실행할 SQL 문이 없습니다.");
            return;
        }

        String temp = script.toUpperCase();

        if (!serverMan.isRun()) {
            setStatus("서버가 정지 중입니다.");
            return;
        }

        if (temp.startsWith("SELECT")) {
            ResultSetPane resultSetPane = new ResultSetPane(serverMan, databaseMan, script);
            ResultSetDialog dialog = new ResultSetDialog((Frame) SwingUtilities.getWindowAncestor(this), false, resultSetPane);
            dialog.setSize(900, 600);
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
        } else {
            Connection connection = null;
            Statement statement = null;
            try {
                connection = serverMan.getConnection(databaseMan.getDatabaseName());
                statement = connection.createStatement();
                int result = statement.executeUpdate(script);
                setStatus("SQL: " + result + " 행이 영향을 받았습니다.");
            } catch (SQLException ex) {
                setStatus("SQL error: " + ex.getLocalizedMessage());
            } finally {
                try {
                    if (statement != null) {
                        statement.close();
                    }
                } catch (SQLException ex) {
                }
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException ex) {
                }
            }
        }
    }//GEN-LAST:event_jButtonExecuteSqlActionPerformed

    private void jButtonScriptDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScriptDeleteActionPerformed
        deleteEditor.setText(makeDeleteScript());
    }//GEN-LAST:event_jButtonScriptDeleteActionPerformed

    private void jButtonExecuteDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExecuteDeleteActionPerformed
        String script = deleteEditor.getText();
        if (script == null || script.isEmpty()) {
            setStatus("실행할 Delete 문이 없습니다.");
            return;
        }

        String temp = script.toUpperCase();
        if (!temp.contains(" WHERE ")) {
            int result = JOptionPane.showConfirmDialog(this, "where 절이 없습니다. 계속 하시겠습니까?", "where 구문 여부 확인", JOptionPane.OK_CANCEL_OPTION);
            if (result != JOptionPane.OK_OPTION) {
                setStatus("delete 실행을 취소하였습니다.");
                return;
            }
        }

        if (!serverMan.isRun()) {
            setStatus("서버가 정지 중입니다.");
            return;
        }
        Connection connection = serverMan.getConnection(databaseMan.getDatabaseName());
        if (connection == null) {
            setStatus("connection 획득을 실패했습니다.");
            return;
        }
        Statement statement = null;
        try {
            statement = connection.createStatement();
            int result = statement.executeUpdate(script);
            setStatus("delete: " + result + " 행이 영향을 받았습니다.");
        } catch (SQLException ex) {
            setStatus("delete error: " + ex.getLocalizedMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException ex) {
            }
            try {
                connection.close();
            } catch (SQLException ex) {
            }
        }
    }//GEN-LAST:event_jButtonExecuteDeleteActionPerformed

    private void setInsertColumns() {
        int rowCount = jTableColumns.getRowCount();
        setAllColumnsSelect(false);
        for (int i = 0; i < rowCount; i++) {
            if ((boolean) jTableColumns.getValueAt(i, 7)) { //자동 증가 항목 제외
                continue;
            }
            if (hasValue(jTableColumns.getValueAt(i, 5))) { // primary key 항목은 필수
                jTableColumns.setValueAt(true, i, 0);
                continue;
            }
            // nullable이면서 nullable 제외 설정이면 제외
            if ((boolean) jTableColumns.getValueAt(i, 6) && !jCheckBoxIncludeNullable.isSelected()) {
                continue;
            }
            // deafult 값 설정 제외가 선택되어 있고 default 값이 설정 되어 있으면 제외
            if (hasValue(jTableColumns.getValueAt(i, 8)) && jCheckBoxExcludeDefault.isSelected()) {
                continue;
            }
            jTableColumns.setValueAt(true, i, 0);
        }
        jTableColumns.validate();
    }

    private boolean hasValue(Object object) {
        if (object == null) {
            return false;
        }
        String value = object.toString();
        return (!value.isEmpty());
    }

    private void setAllColumnsSelect(boolean select) {
        int count = jTableColumns.getRowCount();
        for (int i = 0; i < count; i++) {
            jTableColumns.setValueAt(select, i, 0);
        }
        jTableColumns.validate();
    }

    private void setStatus(String msg) {
        jTextAreaStatus.append(msg + "\n");
        jTextAreaStatus.setCaretPosition(jTextAreaStatus.getText().length() - 1);
        jTextAreaStatus.validate();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonDown;
    private javax.swing.JButton jButtonExecuteDelete;
    private javax.swing.JButton jButtonExecuteInsert;
    private javax.swing.JButton jButtonExecuteQuery;
    private javax.swing.JButton jButtonExecuteSql;
    private javax.swing.JButton jButtonExecuteUpdate;
    private javax.swing.JButton jButtonScriptDelete;
    private javax.swing.JButton jButtonScriptInsert;
    private javax.swing.JButton jButtonScriptSelect;
    private javax.swing.JButton jButtonScriptUpdate;
    private javax.swing.JButton jButtonUp;
    private javax.swing.JCheckBox jCheckBoxAllColumnsSelect;
    private javax.swing.JCheckBox jCheckBoxByKey;
    private javax.swing.JCheckBox jCheckBoxExcludeDefault;
    private javax.swing.JCheckBox jCheckBoxIncludeNullable;
    private javax.swing.JCheckBox jCheckBoxInsertMin;
    private javax.swing.JCheckBox jCheckBoxPrepared;
    private javax.swing.JComboBox<String> jComboBoxTables;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanelDelete;
    private javax.swing.JPanel jPanelInsert;
    private javax.swing.JPanel jPanelSelect;
    private javax.swing.JPanel jPanelSql;
    private javax.swing.JPanel jPanelUpdate;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JToolBar.Separator jSeparator10;
    private javax.swing.JToolBar.Separator jSeparator11;
    private javax.swing.JToolBar.Separator jSeparator12;
    private javax.swing.JToolBar.Separator jSeparator13;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JToolBar.Separator jSeparator5;
    private javax.swing.JToolBar.Separator jSeparator6;
    private javax.swing.JToolBar.Separator jSeparator7;
    private javax.swing.JToolBar.Separator jSeparator8;
    private javax.swing.JToolBar.Separator jSeparator9;
    private javax.swing.JSpinner jSpinnerLimit;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JTabbedPane jTabbedPaneWork;
    private javax.swing.JTable jTableColumns;
    private javax.swing.JTextArea jTextAreaStatus;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JToolBar jToolBar3;
    private javax.swing.JToolBar jToolBar4;
    private javax.swing.JToolBar jToolBar5;
    private javax.swing.JToolBar jToolBar6;
    private javax.swing.JToolBar jToolBar7;
    private javax.swing.JToolBar jToolBar8;
    // End of variables declaration//GEN-END:variables
}
