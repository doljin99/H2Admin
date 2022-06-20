/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.hotmail.doljin99;

import java.awt.Cursor;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import org.h2.tools.Csv;

/**
 *
 * @author dolji
 */
public class ResultSetPane extends javax.swing.JPanel {

    private final DatabaseMan databaseMan;
    private String scriptName;
    private final String script;

    /**
     * Creates new form ResultSetPane
     *
     * @param databaseMan
     * @param script
     */
    public ResultSetPane(DatabaseMan databaseMan, String script) {
        initComponents();

        this.databaseMan = databaseMan;
        this.script = script;

        init();
    }

    private void init() {
        setEncodingComboBox();
        jTextAreaScript.setText(script);
        
        queryAndDisplay(script);
    }
    
    private void queryAndDisplay(String selectScript) {        
        DefaultTableModel model = (DefaultTableModel) jTableResult.getModel();
        model.setColumnCount(0);
        model.setRowCount(0);
        
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        ResultSetMetaData metaData;
        int rowCount = 0;
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        try {
            connection = databaseMan.getConnection();
            if (connection == null) {
                setStatus("connection 획득을 실패했습니다.");
                return;
            }
            
            statement = connection.createStatement();
            resultSet = statement.executeQuery(selectScript);
            
            metaData = resultSet.getMetaData();
            
            scriptName = metaData.getCatalogName(1) + "-" + metaData.getTableName(1);
            int columnCount = metaData.getColumnCount();
            String[] headers = new String[columnCount];
            for (int i = 0; i < columnCount; i++) {
                headers[i] = metaData.getColumnLabel(i + 1);
                model.addColumn(headers[i]);
            }
            Object[] row = new Object[columnCount];
            while (resultSet.next()) {
                for (int i = 0; i < row.length; i++) {
                    row[i] = resultSet.getObject(headers[i]);
                }
                model.addRow(row);
                rowCount++;
            }
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###,##0");
            jLabelCount.setText(decimalFormat.format(rowCount));
            jLabelCount.validate();
            jToolBarGrid.validate();
        } catch (SQLException ex) {
            setStatus("init error: " + ex.getLocalizedMessage());
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException ex) {
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                }
            }
            MyUtilities.alignColumnWidth(jTableResult);
            jTableResult.validate();
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }
    
    private void setEncodingComboBox() {
        String defaultEncoding = System.getProperty("file.encoding");
        jComboBoxEncoding.addItem(defaultEncoding);
        if (!"UTF-8".equalsIgnoreCase(defaultEncoding)) {
            jComboBoxEncoding.addItem("UTF-8");
        }
        if (!"MS949".equalsIgnoreCase(defaultEncoding)) {
            jComboBoxEncoding.addItem("MS949");
        }
        if (!"KSC5601".equalsIgnoreCase(defaultEncoding)) {
            jComboBoxEncoding.addItem("KSC5601");
        }
        if (!"EUC-KR".equalsIgnoreCase(defaultEncoding)) {
            jComboBoxEncoding.addItem("EUC-KR");
        }
        jComboBoxEncoding.setSelectedItem(defaultEncoding);
    }

    void save2Csv() {
        String selectScript = jTextAreaScript.getText();
        if (selectScript == null || selectScript.isEmpty()) {
            selectScript = script;
        }
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV FILE", "csv", "csv");
        chooser.setFileFilter(filter);
        chooser.setDialogTitle("save csv: " + scriptName);
        chooser.showSaveDialog(this);
        File file = chooser.getSelectedFile();
        if (file == null) {
            setStatus("CSV 파일 저장을 취소하였습니다.");
            return;
        }
        
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        try {
            connection = databaseMan.getConnection();
            if (connection == null) {
                setStatus("connection 획득을 실패했습니다.");
                return;
            }
            
            statement = connection.createStatement();
            resultSet = statement.executeQuery(selectScript);
            Csv csv = new Csv();
            csv.write(file.getAbsolutePath(), resultSet, (String) jComboBoxEncoding.getSelectedItem());
        } catch (SQLException ex) {
            setStatus("CSV write error: " + ex.getLocalizedMessage());
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException ex) {
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                }
            }
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }

    void setStatus(String msg) {
        jTextAreaStatus.append(msg + "\n");
        jTextAreaStatus.setCaretPosition(jTextAreaStatus.getText().length() - 1);
        jTextAreaStatus.validate();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanelresult = new javax.swing.JPanel();
        jToolBarGrid = new javax.swing.JToolBar();
        jLabelCount = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jButtonRefresh = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        jButtonSaveCsv = new javax.swing.JButton();
        jComboBoxEncoding = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableResult = new javax.swing.JTable();
        jPanelScript = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextAreaScript = new javax.swing.JTextArea();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextAreaStatus = new javax.swing.JTextArea();

        setLayout(new java.awt.BorderLayout());

        jPanelresult.setLayout(new java.awt.BorderLayout());

        jToolBarGrid.setRollover(true);

        jLabelCount.setText(org.openide.util.NbBundle.getMessage(ResultSetPane.class, "ResultSetPane.jLabelCount.text")); // NOI18N
        jToolBarGrid.add(jLabelCount);

        jLabel1.setText(org.openide.util.NbBundle.getMessage(ResultSetPane.class, "ResultSetPane.jLabel1.text")); // NOI18N
        jToolBarGrid.add(jLabel1);
        jToolBarGrid.add(jSeparator1);

        jButtonRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/general/Refresh16.gif"))); // NOI18N
        jButtonRefresh.setText(org.openide.util.NbBundle.getMessage(ResultSetPane.class, "ResultSetPane.jButtonRefresh.text")); // NOI18N
        jButtonRefresh.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonRefresh.setFocusable(false);
        jButtonRefresh.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRefreshActionPerformed(evt);
            }
        });
        jToolBarGrid.add(jButtonRefresh);
        jToolBarGrid.add(jSeparator2);

        jButtonSaveCsv.setIcon(new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/general/Save16.gif"))); // NOI18N
        jButtonSaveCsv.setText(org.openide.util.NbBundle.getMessage(ResultSetPane.class, "ResultSetPane.jButtonSaveCsv.text")); // NOI18N
        jButtonSaveCsv.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonSaveCsv.setFocusable(false);
        jButtonSaveCsv.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonSaveCsv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveCsvActionPerformed(evt);
            }
        });
        jToolBarGrid.add(jButtonSaveCsv);

        jComboBoxEncoding.setMaximumSize(new java.awt.Dimension(200, 32767));
        jComboBoxEncoding.setMinimumSize(new java.awt.Dimension(200, 23));
        jComboBoxEncoding.setPreferredSize(new java.awt.Dimension(200, 23));
        jToolBarGrid.add(jComboBoxEncoding);

        jPanelresult.add(jToolBarGrid, java.awt.BorderLayout.PAGE_START);

        jTableResult.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTableResult);

        jPanelresult.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab(org.openide.util.NbBundle.getMessage(ResultSetPane.class, "ResultSetPane.jPanelresult.TabConstraints.tabTitle"), jPanelresult); // NOI18N

        jPanelScript.setLayout(new java.awt.BorderLayout());

        jTextAreaScript.setColumns(20);
        jTextAreaScript.setLineWrap(true);
        jTextAreaScript.setRows(5);
        jTextAreaScript.setTabSize(4);
        jTextAreaScript.setWrapStyleWord(true);
        jScrollPane3.setViewportView(jTextAreaScript);

        jPanelScript.add(jScrollPane3, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab(org.openide.util.NbBundle.getMessage(ResultSetPane.class, "ResultSetPane.jPanelScript.TabConstraints.tabTitle"), jPanelScript); // NOI18N

        add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        jPanel3.setLayout(new java.awt.BorderLayout());

        jTextAreaStatus.setEditable(false);
        jTextAreaStatus.setColumns(20);
        jTextAreaStatus.setLineWrap(true);
        jTextAreaStatus.setRows(5);
        jTextAreaStatus.setTabSize(4);
        jTextAreaStatus.setWrapStyleWord(true);
        jScrollPane2.setViewportView(jTextAreaStatus);

        jPanel3.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        add(jPanel3, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonSaveCsvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveCsvActionPerformed
        save2Csv();
    }//GEN-LAST:event_jButtonSaveCsvActionPerformed

    private void jButtonRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRefreshActionPerformed
        String selectScript = jTextAreaScript.getText();
        if (selectScript == null || selectScript.isEmpty()) {
            queryAndDisplay(script);
        } else {
            queryAndDisplay(selectScript);
        }
    }//GEN-LAST:event_jButtonRefreshActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonRefresh;
    private javax.swing.JButton jButtonSaveCsv;
    private javax.swing.JComboBox<String> jComboBoxEncoding;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabelCount;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanelScript;
    private javax.swing.JPanel jPanelresult;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTableResult;
    private javax.swing.JTextArea jTextAreaScript;
    private javax.swing.JTextArea jTextAreaStatus;
    private javax.swing.JToolBar jToolBarGrid;
    // End of variables declaration//GEN-END:variables

    String getScriptName() {
        return scriptName;
    }
}
