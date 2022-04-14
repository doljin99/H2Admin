/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.hotmail.doljin99;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author dolji
 */
public class TableInfoPane extends javax.swing.JPanel {

    /**
     * Creates new form TableInfoPane
     *
     * @param table
     */
    public TableInfoPane(Table table) {
        initComponents();

        init(table);
    }

    private void init(Table table) {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        jTablePrimaryKeys.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        
        jTextFieldServerName.setText(table.getServerName());
        jTextFieldJdbcUrl.setText(table.getJdbcUrl());

        jTextFieldTableCat.setText(table.getTableCat());
        jTextFieldTableName.setText(table.getTableName());
        jTextFieldTableType.setText(table.getTableType());
        jTextFieldTypeCat.setText(table.getTypeCat());
        jTextFieldTypeSchem.setText(table.getTypeSchem());
        jTextFieldTypeName.setText(table.getTypeName());
        jTextFieldSelfReferencingColName.setText(table.getSelfReferencingColName());
        jTextFieldRefGeneration.setText(table.getRefGeneration());

        PrimaryKeys keys = table.getPrimaryKeys();
        DefaultTableModel model = (DefaultTableModel) jTablePrimaryKeys.getModel();
        model.setRowCount(0);
        if (keys == null) {
            jTextFieldPrimaryKeyName.setText("");
        } else {
            jTextFieldPrimaryKeyName.setText(keys.getPkName());
            Object[] row = new Object[2];
            for (int i = 0; i < keys.size(); i++) {
                PrimaryKey key = keys.get(i);
                row[0] = key.getKeySeq();
                row[1] = key.getColumnName();

                model.addRow(row);
            }
        }
        MyUtilities.setTableHAllHeadersAlignment(jTablePrimaryKeys, SwingConstants.CENTER);
        MyUtilities.alignColumnWidth(jTablePrimaryKeys);
        jTablePrimaryKeys.validate();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jTextFieldJdbcUrl = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextFieldServerName = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextFieldTableCat = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextFieldTableName = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextFieldTableType = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextFieldTypeCat = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jTextFieldTypeName = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jTextFieldTypeSchem = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jTextFieldSelfReferencingColName = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jTextFieldRefGeneration = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTablePrimaryKeys = new javax.swing.JTable();
        jLabel11 = new javax.swing.JLabel();
        jTextFieldPrimaryKeyName = new javax.swing.JTextField();

        setToolTipText("");

        jLabel1.setText("JDBC URL");

        jTextFieldJdbcUrl.setEditable(false);

        jLabel2.setText("서버 명");

        jTextFieldServerName.setEditable(false);

        jLabel3.setText("DB 명");

        jTextFieldTableCat.setEditable(false);

        jLabel4.setText("테이블 명");

        jTextFieldTableName.setEditable(false);

        jLabel5.setText("테이블 타입");

        jTextFieldTableType.setEditable(false);
        jTextFieldTableType.setToolTipText("table type. Typical types are \"TABLE\", \"VIEW\", \"SYSTEM TABLE\", \"GLOBAL TEMPORARY\", \"LOCAL TEMPORARY\", \"ALIAS\", \"SYNONYM\"");

        jLabel6.setText("Type Cat.");

        jTextFieldTypeCat.setEditable(false);

        jLabel7.setText("Type Schem");

        jTextFieldTypeName.setEditable(false);

        jLabel8.setText("Type Name");

        jTextFieldTypeSchem.setEditable(false);

        jLabel9.setText("selfReferencingColName");

        jTextFieldSelfReferencingColName.setEditable(false);
        jTextFieldSelfReferencingColName.setToolTipText("name of the designated \"identifier\" column of a typed table (may be null)");

        jLabel10.setText("refGeneration");

        jTextFieldRefGeneration.setEditable(false);
        jTextFieldRefGeneration.setToolTipText("specifies how values in SELF_REFERENCING_COL_NAME are created. Values are \"SYSTEM\", \"USER\", \"DERIVED\". (may be null)");

        jTablePrimaryKeys.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "순번", "컬럼명"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTablePrimaryKeys);

        jLabel11.setText("Primary Keys");

        jTextFieldPrimaryKeyName.setEditable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextFieldTypeName, javax.swing.GroupLayout.DEFAULT_SIZE, 608, Short.MAX_VALUE)
                            .addComponent(jTextFieldTypeSchem)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldSelfReferencingColName))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel5)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextFieldJdbcUrl, javax.swing.GroupLayout.DEFAULT_SIZE, 608, Short.MAX_VALUE)
                            .addComponent(jTextFieldServerName, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jTextFieldTableCat, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jTextFieldTableName, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jTextFieldTableType, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jTextFieldTypeCat, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addGap(18, 18, 18))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 608, Short.MAX_VALUE)
                            .addComponent(jTextFieldRefGeneration)
                            .addComponent(jTextFieldPrimaryKeyName))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jTextFieldJdbcUrl, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextFieldServerName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTextFieldTableCat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextFieldTableName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jTextFieldTableType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jTextFieldTypeCat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jTextFieldTypeSchem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jTextFieldTypeName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jTextFieldSelfReferencingColName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jTextFieldRefGeneration, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jTextFieldPrimaryKeyName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTablePrimaryKeys;
    private javax.swing.JTextField jTextFieldJdbcUrl;
    private javax.swing.JTextField jTextFieldPrimaryKeyName;
    private javax.swing.JTextField jTextFieldRefGeneration;
    private javax.swing.JTextField jTextFieldSelfReferencingColName;
    private javax.swing.JTextField jTextFieldServerName;
    private javax.swing.JTextField jTextFieldTableCat;
    private javax.swing.JTextField jTextFieldTableName;
    private javax.swing.JTextField jTextFieldTableType;
    private javax.swing.JTextField jTextFieldTypeCat;
    private javax.swing.JTextField jTextFieldTypeName;
    private javax.swing.JTextField jTextFieldTypeSchem;
    // End of variables declaration//GEN-END:variables
}
